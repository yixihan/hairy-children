package com.wq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wq.pojo.CommentReply;
import com.wq.pojo.CommentRoot;
import com.wq.pojo.UserComments;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Mapper
public interface CommentRootMapper extends BaseMapper<CommentRoot> {

    /**
     * 获取指定 文章 id 下的所有父评论
     * @param titleId 文章 id
     * @return 指定 文章 id 下的所有父评论
     */
    List<CommentRoot> getAllRootCommentsByTitleId (Long titleId);

    /**
     * 获取指定 父评论 id 的所有子评论
     * @param rootId 父评论 id
     * @return 指定 父评论 id 的所有子评论
     */
    List<CommentReply> getSonComments(Long rootId);

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
}
