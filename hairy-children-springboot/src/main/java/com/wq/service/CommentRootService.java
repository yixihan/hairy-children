package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.CommentReply;
import com.wq.pojo.CommentRoot;
import com.wq.pojo.UserComments;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
public interface CommentRootService extends IService<CommentRoot> {

    /**
     * 获取指定 文章 id 下的所有评论并存入 Redis
     * @param titleId 文章 id
     * @return 父评论集合 List形式
     */
    List<CommentRoot> getAll (Long titleId);


    /**
     * 添加子评论
     * @param commentReply commentReply
     * @param titleId 文章 id
     * @return true : 成功 | false : 失败
     */
    Boolean addSonComment (CommentReply commentReply, Long titleId);

    /**
     * 添加父评论
     * @param commentRoot comment
     * @return true : 成功 | false : 失败
     */
    Boolean addRootComment (CommentRoot commentRoot);

    /**
     * 获取指定 父评论 id 的所有子评论
     * @param rootId 父评论 id
     * @return 指定 文章 id 下的指定 父评论 id 的所有子评论
     */
    List<CommentReply> getSonComments(Long rootId);

    /**
     * 获取用户所有的评论
     * @param userId 用户 id
     * @return 用户所有的评论
     */
    List<UserComments> getUserComments (Long userId);

    /**
     * 获取用户所有的 父评论
     * @param userId 用户 id
     * @return 用户所有的 父评论
     */
    List<UserComments> getAllUserRootComments(Long userId);

    /**
     * 获取用户所有的 子评论
     * @param userId 用户 id
     * @return 用户所有的 子评论
     */
    List<UserComments> getAllUserSonComments(Long userId);

    /**
     * 给父评论 点赞
     * @param rootId 父评论 id
     * @return true : 成功 | false : 失败
     */
    Boolean updateRootCommentLikeCount (Long rootId);
}
