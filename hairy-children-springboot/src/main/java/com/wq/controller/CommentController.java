package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.CommentLikeMailboxService;
import com.wq.service.CommentRootService;
import com.wq.service.redis.RedisService;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
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
    private RedisService redisService;

    @Resource
    private CommentLikeMailboxService commentLikeMailboxService;

    private static final String USER_COMMENT_LIKE_KEY = "comment_like:%s:%s";

    @PostMapping("/addRootComment")
    public Result addRootComment (CommentRoot commentRoot) {

        // 设置 用户 id
        commentRoot.setUserId (ShiroUtils.getUserId ());

        Boolean add = commentRootService.addRootComment (commentRoot);

        return add ? Result.success ("评论成功") : Result.fail ("评论失败");
    }

    @PostMapping("/addSonComment")
    public Result addSonComment (CommentReply commentReply, Long titleId) {
        // 设置 用户 id
        commentReply.setUserId (ShiroUtils.getUserId ());

        Boolean add = commentRootService.addSonComment (commentReply, titleId);

        return add ? Result.success ("评论成功") : Result.fail ("评论失败");
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

}
