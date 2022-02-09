package com.wq.controller;


import com.wq.pojo.CommentReply;
import com.wq.pojo.CommentRoot;
import com.wq.common.pojo.Result;
import com.wq.pojo.UserComments;
import com.wq.service.CommentRootService;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
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
    public Result likeComment (Long rootId) {
        Boolean update = commentRootService.updateRootCommentLikeCount (rootId);

        return update ? Result.success ("点赞成功") : Result.fail ("点赞失败");
    }

}
