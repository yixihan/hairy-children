package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.CommentReplyService;
import com.wq.service.CommentRootService;
import com.wq.service.TitleService;
import com.wq.service.message.CommentLikeMailboxService;
import com.wq.service.message.CommentMailboxService;
import com.wq.service.message.ReplyMailboxService;
import com.wq.service.redis.RedisService;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
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
    public Result addRootComment (CommentRoot commentRoot) {

        // 设置 用户 id
        commentRoot.setUserId (ShiroUtils.getUserId ());

        Boolean add = commentRootService.addRootComment (commentRoot);

        QueryWrapper<CommentRoot> wrapper = new QueryWrapper<> ();
        wrapper.eq ("answer_id", commentRoot.getAnswerId ())
                .eq ("user_id", commentRoot.getUserId ())
                .eq ("content", commentRoot.getContent ());
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

        return add ? Result.success ("评论成功") : Result.fail ("评论失败");
    }

    @PostMapping("/addSonComment")
    public Result addSonComment (CommentReply commentReply, Long titleId) {
        // 设置 用户 id
        commentReply.setUserId (ShiroUtils.getUserId ());

        Boolean add = commentRootService.addSonComment (commentReply, titleId);

        QueryWrapper<CommentReply> wrapper = new QueryWrapper<> ();
        wrapper.eq ("root_id", commentReply.getRootId ())
                .eq ("user_id", commentReply.getUserId ())
                .eq ("content", commentReply.getContent ());

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

        return add ? Result.success ("评论成功") : Result.fail ("评论失败");
    }

    @PostMapping ("/removeRootComment")
    public Result removeRootComment (Long rootId, Long titleId) {
        Integer replyCount = commentRootService.removeRootComment (rootId);

        // 更新回复数
        updateCommentRootCount (rootId, -replyCount);
        updateCommentTitleCount (titleId, - (replyCount + 1));

        return Result.success ("删除评论成功");

    }

    @PostMapping ("/removeSonComment")
    public Result removeSonComment (Long replyId, Long titleId) {
        CommentReply commentReply = commentReplyService.getById (replyId);
        Integer count = commentRootService.removeSonComment (replyId);

        // 更新回复数
        updateCommentRootCount (commentReply.getRootId (), -count);
        updateCommentTitleCount (titleId, - count);

        return Result.success ("删除评论成功");
    }

    @PostMapping("/getAllTitleComment")
    public Result getAllTitleComment (Long titleId) {
        List<CommentRoot> commentRootList = commentRootService.getAll (titleId);

        HashMap<String, Object> map = new HashMap<> (8);
        map.put ("commentRootList", commentRootList);
        return Result.success (map);
    }

    @PostMapping("/getAllUserComment")
    public Result getAllUserComment (Long userId) {
        List<UserComments> userCommentList = commentRootService.getUserComments (userId);
        HashMap<String, Object> map = new HashMap<> (8);
        map.put ("userCommentList", userCommentList);
        return Result.success (map);
    }

    @PostMapping("/likeComment")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result likeComment (Long rootId, Long userId) {
        CommentRoot commentRoot = commentRootService.getById (rootId);

        if (commentRoot == null) {
            log.error ("无此评论");
            throw new RuntimeException ("无此评论");
        }

        // 点赞
        String key = String.format (USER_COMMENT_LIKE_KEY, commentRoot.getUserId (), rootId);
        Boolean bit = redisService.getBit (key, userId);
        if (bit) {
            log.warn ("已点赞, 请勿重复点赞");
            return Result.success ("已点赞, 请勿重复点赞");
        }
        Boolean setBit = redisService.setBit (key, userId, true);
        if (setBit) {
            log.error ("点赞失败");
            throw new RuntimeException ("点赞失败");
        }

        commentRoot.setLikeCount (Math.toIntExact (redisService.getLikeValueAll (key)));
        boolean update = commentRootService.updateById (commentRoot);

        if (! update) {
            log.error ("评论更新失败");
            throw new RuntimeException ("评论更新失败");
        }

        // 发送消息
        CommentLikeMailbox commentLikeMailbox = new CommentLikeMailbox ();
        commentLikeMailbox.setTitleId (commentRoot.getAnswerId ());
        commentLikeMailbox.setRootId (rootId);
        commentLikeMailbox.setSendUserId (userId);
        commentLikeMailbox.setReceiveUserId (commentRoot.getUserId ());

        boolean save = commentLikeMailboxService.save (commentLikeMailbox);

        if (! save) {
            log.error ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }
        QueryWrapper<CommentLikeMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("title_id", commentLikeMailbox.getTitleId ())
                .eq ("send_user_id", commentLikeMailbox.getSendUserId ())
                .like ("receive_user_id", commentLikeMailbox.getReceiveUserId ());

        CommentLikeMailbox one = commentLikeMailboxService.getOne (wrapper);

        if (one == null) {
            log.error ("无此消息");
            throw new RuntimeException ("无此消息");
        }

        commentLikeMailboxService.sendMailbox (one);

        return Result.success ("发送成功");
    }

    /**
     * 发送父评论 消息
     * @param mailbox
     */
    @Async
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendCommentRootMailBox(CommentMailbox mailbox) {

        // 添加文章回复数
        boolean titleCount = updateCommentTitleCount (mailbox.getTitleId (), 1);

        if (! titleCount) {
            log.warn ("文章回复添加失败");
            throw new RuntimeException ("文章回复添加失败");
        }

        boolean save = commentMailboxService.save (mailbox);

        if (! save) {
            log.warn ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }


        commentMailboxService.sendMailbox (mailbox);
    }

    /**
     * 发送子评论 消息
     * @param mailbox
     */
    @Async
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendCommentReplyMailBox(ReplyMailbox mailbox) {
        // 添加文章回复数
        boolean titleCount = updateCommentTitleCount (mailbox.getTitleId (), 1);

        if (! titleCount) {
            log.warn ("文章回复添加失败");
            throw new RuntimeException ("文章回复添加失败");
        }

        // 添加评论回复数
        boolean rootCount = updateCommentRootCount (mailbox.getRootId (), 1);

        if (! rootCount) {
            log.warn ("评论回复添加失败");
            throw new RuntimeException ("评论回复添加失败");
        }

        boolean save = replyMailboxService.save (mailbox);

        if (! save) {
            log.warn ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }

        replyMailboxService.sendMailbox (mailbox);
    }


    /**
     * 添加文章回复数
     */
    private boolean updateCommentTitleCount (Long titleId, Integer count) {
        Title title = titleService.getById (titleId);
        title.setCommentCount (title.getCommentCount () + count);
        return titleService.updateById (title);
    }

    /**
     * 添加父评论回复数
     */
    private boolean updateCommentRootCount (Long rootId, Integer count) {
        CommentRoot commentRoot = commentRootService.getById (rootId);
        commentRoot.setReplyCount (commentRoot.getReplyCount () + count);
        return commentRootService.updateById (commentRoot);
    }



}
