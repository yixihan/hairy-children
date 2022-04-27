package com.wq.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.*;
import com.wq.service.message.CommentLikeMailboxService;
import com.wq.service.message.CommentMailboxService;
import com.wq.service.message.ReplyMailboxService;
import com.wq.service.redis.RedisService;
import com.wq.util.PageUtils;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentRootService commentRootService;

    @Resource
    private CommentReplyService commentReplyService;

    @Resource
    private TitleService titleService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisService redisService;

    @Resource
    private CommentLikeMailboxService commentLikeMailboxService;

    @Resource
    private CommentMailboxService commentMailboxService;

    @Resource
    private ReplyMailboxService replyMailboxService;

    /**
     * comment_like:userId:rootId
     */
    private static final String USER_COMMENT_LIKE_KEY = "comment_like:%s:%s";

    @PostMapping("/addRootComment")
    public Result addRootComment(@RequestBody CommentRoot commentRoot) {

        // 设置 用户 id, gmtCreate
        commentRoot.setGmtCreate (new Date ());
        commentRoot.setUserId (ShiroUtils.getUserId ());

        Boolean add = commentRootService.addRootComment (commentRoot);

        QueryWrapper<CommentRoot> wrapper = new QueryWrapper<> ();
        Map<String, Object> columns = new HashMap<> (16);
        columns.put ("answer_id", commentRoot.getAnswerId ());
        columns.put ("user_id", commentRoot.getUserId ());
        columns.put ("content", commentRoot.getContent ());
        columns.put ("gmt_create", commentRoot.getGmtCreate ());

        wrapper.allEq (true, columns, true);
        CommentRoot one = commentRootService.getOne (wrapper);

        Title title = titleService.getById (commentRoot.getAnswerId ());

        CommentMailbox mailbox = new CommentMailbox ();
        mailbox.setRootId (one.getRootId ());
        mailbox.setTitleId (one.getAnswerId ());
        mailbox.setCommentContent (one.getContent ());
        mailbox.setSendUserId (one.getUserId ());
        mailbox.setReceiveUserId (title.getUserId ());


        // 给被评论者发送信息
        sendCommentRootMailBox (mailbox);
        Map<String, Object> map = new HashMap<> (16);
        setUserInfo (one);
        map.put ("rootComment", one);

        return add ? Result.success ("评论成功", map) : Result.fail ("评论失败");
    }


    @PostMapping("/addSonComment/{titleId}")
    public Result addSonComment(@PathVariable Long titleId, @RequestBody CommentReply commentReply) {

        commentReply.setGmtCreate (new Date ());
        Boolean add = commentRootService.addSonComment (commentReply, titleId);

        QueryWrapper<CommentReply> wrapper = new QueryWrapper<> ();
        Map<String, Object> columns = new HashMap<> (16);
        columns.put ("root_id", commentReply.getRootId ());
        columns.put ("user_id", commentReply.getUserId ());
        columns.put ("content", commentReply.getContent ());
        columns.put ("gmt_create", commentReply.getGmtCreate ());
        wrapper.allEq (true, columns, true);

        if (commentReply.getReplyCommentId () != null) {
            wrapper.eq ("reply_comment_id", commentReply.getReplyCommentId ());
        }
        CommentReply one = commentReplyService.getOne (wrapper);

        Title title = titleService.getById (titleId);

        ReplyMailbox mailbox = new ReplyMailbox ();
        mailbox.setTitleId (titleId);
        mailbox.setRootId (one.getRootId ());
        mailbox.setReplyId (one.getReplyId ());
        mailbox.setReplyContent (one.getContent ());
        mailbox.setSendUserId (one.getUserId ());
        mailbox.setReceiveUserId (title.getUserId ());
        // 给被评论者发送信息
        sendCommentReplyMailBox (mailbox);
        Map<String, Object> map = new HashMap<> (16);
        setUserInfo (one);
        map.put ("rootComment", one);

        return add ? Result.success ("评论成功", map) : Result.fail ("评论失败");
    }

    @PostMapping("/removeRootComment")
    public Result removeRootComment(@RequestBody Map<String, Object> params) {

        long rootId = Long.parseLong (String.valueOf (params.get ("rootId")));
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));
        Integer replyCount = commentRootService.removeRootComment (rootId);

        // 更新回复数
        updateCommentTitleCount (titleId, -(replyCount + 1));

        return Result.success ("删除评论成功");

    }

    @PostMapping("/removeSonComment")
    public Result removeSonComment(@RequestBody Map<String, Object> params) {

        long replyId = Long.parseLong (String.valueOf (params.get ("replyId")));
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        CommentReply commentReply = commentReplyService.getById (replyId);
        Integer count = commentRootService.removeSonComment (replyId);

        // 更新回复数
        updateCommentRootCount (commentReply.getRootId (), -count);
        updateCommentTitleCount (titleId, -count);

        return Result.success ("删除评论成功");
    }

    @PostMapping("/getAllTitleComment")
    public Result getAllTitleComment(@RequestBody Map<String, Object> params) {
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", titleId)) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        List<CommentRoot> commentRootList = commentRootService.getAll (titleId);

        if (commentRootList != null) {
            for (CommentRoot commentRoot : commentRootList) {
                for (CommentReply commentReply : commentRoot.getCommentReplyList ()) {
                    setUserInfo (commentReply);
                }
                setUserInfo (commentRoot);
            }
        }
        PageUtils commentPage = new PageUtils (commentRootList, commentRootList == null ? 0 : commentRootList.size (), 5, 0);

        HashMap<String, Object> map = new HashMap<> (8);
        map.put ("page", commentPage);
        return Result.success (map);
    }

    @PostMapping("/getAllUserComment")
    public Result getAllUserComment(@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", userId)) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        List<UserComments> userCommentList = commentRootService.getUserComments (userId);

        for (UserComments userComments : userCommentList) {
            setUserInfo (userComments);
            setTitleInfo (userComments);
        }

        PageUtils commentPage = new PageUtils (userCommentList, userCommentList.size (), 5, 0);

        HashMap<String, Object> map = new HashMap<> (8);
        map.put ("page", commentPage);
        return Result.success (map);
    }

    @PostMapping("/likeComment")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result likeComment(@RequestBody Map<String, Object> params) {

        Long rootId = JSONObject.parseObject (String.valueOf (params.get ("rootId")), Long.class);
        Long userId = JSONObject.parseObject (String.valueOf (params.get ("userId")), Long.class);

        CommentRoot commentRoot = commentRootService.getById (rootId);

        if (commentRoot == null) {
            log.error ("没有该评论");
            throw new RuntimeException ("没有该评论");
        }

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", userId)) <= 0) {
            log.error ("没有该用户");
            throw new RuntimeException ("没有该用户");
        }

        // 点赞
        String key = String.format (USER_COMMENT_LIKE_KEY, userId, rootId);
        Boolean bit = redisService.getBit (key, userId);
        if (bit) {
            log.warn ("已点赞, 请勿重复点赞");
            return Result.fail (555, "已点赞, 请勿重复点赞");
        }
        Boolean setBit = redisService.setBit (key, userId, true);
        if (setBit) {
            log.error ("点赞失败");
            throw new RuntimeException ("点赞失败");
        }

        commentRoot.setLikeCount (Math.toIntExact (redisService.getLikeValueAll (key)));
        boolean update = commentRootService.updateById (commentRoot);
        commentRootService.updateRedis(userId, commentRoot.getAnswerId ());

        if (!update) {
            log.error ("评论更新失败");
            throw new RuntimeException ("点赞失败");
        }

        // 发送消息
        CommentLikeMailbox commentLikeMailbox = new CommentLikeMailbox ();
        commentLikeMailbox.setTitleId (commentRoot.getAnswerId ());
        commentLikeMailbox.setRootId (rootId);
        commentLikeMailbox.setSendUserId (userId);
        commentLikeMailbox.setReceiveUserId (commentRoot.getUserId ());
        commentLikeMailbox.setGmtCreate (new Date ());
        boolean save = commentLikeMailboxService.save (commentLikeMailbox);

        if (!save) {
            log.error ("消息持久化失败");
            throw new RuntimeException ("点赞失败");
        }
        QueryWrapper<CommentLikeMailbox> wrapper = new QueryWrapper<> ();
        Map<String, Object> columns = new HashMap<>(16);
        columns.put ("title_id", commentLikeMailbox.getTitleId ());
        columns.put ("send_user_id", commentLikeMailbox.getSendUserId ());
        columns.put ("receive_user_id", commentLikeMailbox.getReceiveUserId ());
        columns.put ("gmt_create", commentLikeMailbox.getGmtCreate ());
        wrapper.allEq (true, columns, true);

        CommentLikeMailbox one = commentLikeMailboxService.getOne (wrapper);

        if (one == null) {
            log.error ("无此消息");
            throw new RuntimeException ("点赞失败");
        }

        commentLikeMailboxService.sendMailbox (one);

        return Result.success ("点赞成功");
    }

    /**
     * 发送父评论 消息
     *
     * @param mailbox
     */
    @Async
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendCommentRootMailBox(CommentMailbox mailbox) {

        // 添加文章回复数
        boolean titleCount = updateCommentTitleCount (mailbox.getTitleId (), 1);

        if (!titleCount) {
            log.warn ("文章回复添加失败");
            throw new RuntimeException ("文章回复添加失败");
        }

        boolean save = commentMailboxService.save (mailbox);

        if (!save) {
            log.warn ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }


        commentMailboxService.sendMailbox (mailbox);
    }

    /**
     * 发送子评论 消息
     *
     * @param mailbox
     */
    @Async
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendCommentReplyMailBox(ReplyMailbox mailbox) {
        // 添加文章回复数
        boolean titleCount = updateCommentTitleCount (mailbox.getTitleId (), 1);

        if (!titleCount) {
            log.warn ("文章回复添加失败");
            throw new RuntimeException ("文章回复添加失败");
        }

        // 添加评论回复数
        boolean rootCount = updateCommentRootCount (mailbox.getRootId (), 1);

        if (!rootCount) {
            log.warn ("评论回复添加失败");
            throw new RuntimeException ("评论回复添加失败");
        }

        boolean save = replyMailboxService.save (mailbox);

        if (!save) {
            log.warn ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }

        replyMailboxService.sendMailbox (mailbox);
    }


    /**
     * 添加文章回复数
     */
    private boolean updateCommentTitleCount(Long titleId, Integer count) {
        Title title = titleService.getById (titleId);
        title.setCommentCount (title.getCommentCount () + count);
        return titleService.updateById (title);
    }

    /**
     * 添加父评论回复数
     */
    private boolean updateCommentRootCount(Long rootId, Integer count) {
        CommentRoot commentRoot = commentRootService.getById (rootId);
        commentRoot.setReplyCount (commentRoot.getReplyCount () + count);
        return commentRootService.updateById (commentRoot);
    }

    private void setUserInfo (CommentRoot commentRoot) {
        UserInfo info = userInfoService.getUserInfoById (commentRoot.getUserId ());
        User user = userService.getById (commentRoot.getUserId ());

        commentRoot.setUserAvatar (info.getUserAvatar ());
        commentRoot.setUserName (user.getUserName ());
    }

    private void setUserInfo (CommentReply commentReply) {
        UserInfo info = userInfoService.getUserInfoById (commentReply.getUserId ());
        User user = userService.getById (commentReply.getUserId ());

        commentReply.setUserAvatar (info.getUserAvatar ());
        commentReply.setUserName (user.getUserName ());

        if (commentReply.getReplyUserId () != null) {
            UserInfo replyInfo = userInfoService.getUserInfoById (commentReply.getReplyUserId ());
            User replyUser = userService.getById (commentReply.getReplyUserId ());

            commentReply.setReplyUserAvatar (replyInfo.getUserAvatar ());
            commentReply.setReplyUserName (replyUser.getUserName ());
        }
    }

    private void setUserInfo (UserComments userComments) {

        UserInfo info = userInfoService.getUserInfoById (userComments.getUserId ());
        User user = userService.getById (userComments.getUserId ());

        userComments.setUserAvatar (info.getUserAvatar ());
        userComments.setUserName (user.getUserName ());

        if (userComments.getRootId () != null) {
            CommentRoot commentRoot = commentRootService.getById (userComments.getRootId ());
            UserInfo rootInfo = userInfoService.getUserInfoById (commentRoot.getUserId ());
            User rootUser = userService.getById (commentRoot.getUserId ());

            userComments.setRootUserId (commentRoot.getUserId ());
            userComments.setRootUserName (rootUser.getUserName ());
            userComments.setRootUserAvatar (rootInfo.getUserAvatar ());
        }

    }

    private void setTitleInfo (UserComments userComments) {
        Title title = titleService.getById (userComments.getAnswerId ());

        userComments.setTitleAuthorId (title.getUserId ());
        userComments.setTitleName (title.getTitleName ());
        userComments.setTitleImg (title.getTitleImg ());
    }




}
