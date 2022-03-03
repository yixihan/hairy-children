package com.wq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.mapper.CommentReplyMapper;
import com.wq.mapper.CommentRootMapper;
import com.wq.mapper.TitleMapper;
import com.wq.pojo.CommentReply;
import com.wq.pojo.CommentRoot;
import com.wq.pojo.UserComments;
import com.wq.service.CommentRootService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Slf4j
@Service("CommentRootService")
public class CommentRootServiceImpl extends ServiceImpl<CommentRootMapper, CommentRoot> implements CommentRootService {

    
    @Resource
    private CommentRootMapper commentRootMapper;

    @Resource
    private CommentReplyMapper commentReplyMapper;

    @Resource
    private TitleMapper titleMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 用户评论 key
     */
    public static final String USER_COMMENT = "USER_%s:myComments";

    /**
     * 文章评论 key
     */
    public static final String TITLE_COMMENT = "TITLE_%s:titleComments";
    
    @Override
    public List<CommentRoot> getAll(Long titleId) {
        // 生成 redis key
        String titleCommentKey = String.format(TITLE_COMMENT, titleId);

        // 从 redis 中获取评论内容
        return (List<CommentRoot>) redisTemplate.opsForValue ().get (titleCommentKey);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addSonComment(CommentReply commentReply, Long titleId) {

        // 如果回复的 子评论 id 为空
        if (commentReply.getReplyCommentId() != null) {

            // 根据回复子评论 id 查询 被回复人信息
            CommentReply replyComment = commentReplyMapper.selectById (commentReply.getReplyCommentId ());

            // 回复的子评论 用户 id
            commentReply.setReplyUserId(replyComment.getUserId());
        }

        // 存入数据库
        boolean insert = commentReplyMapper.insert (commentReply) == 1;

        // 异步方法更新 redis 内的评论内容
        updateTitleComment (titleId);
        updateUserComment (commentReply.getUserId ());

        return insert;
    }

    @Override
    public Boolean addRootComment(CommentRoot commentRoot) {

        // 存入数据库
        boolean insert = commentRootMapper.insert (commentRoot) == 1;

        // 异步方法更新 redis 内的评论内容
        updateTitleComment (commentRoot.getAnswerId ());
        updateUserComment (commentRoot.getUserId ());

        return insert;
    }

    @Override
    public List<CommentReply> getSonComments(Long rootId) {
        List<CommentReply> sonComments = commentRootMapper.getSonComments (rootId);
        return sonComments;
    }

    @Override
    public List<UserComments> getUserComments(Long userId) {
        // 生成 redis key
        String userCommentKey = String.format(USER_COMMENT, userId);

        // 从 redis 中获取内容
        return (List<UserComments>) redisTemplate.opsForValue ().get (userCommentKey);
    }

    @Override
    public List<UserComments> getAllUserRootComments(Long userId) {
        return commentRootMapper.getAllUserRootComments (userId);
    }

    @Override
    public List<UserComments> getAllUserSonComments(Long userId) {
        return commentRootMapper.getAllUserSonComments (userId);
    }


    @Async
    public void updateTitleComment (Long titleId) {
        // 生成 redis key
        String titleCommentKey = String.format(TITLE_COMMENT, titleId);

        // 获取文章的所有父评论
        List<CommentRoot> rootCommentList = commentRootMapper.getAllRootCommentsByTitleId (titleId);


        // 获取所有父评论的子评论
        for (CommentRoot commentRoot : rootCommentList) {
            commentRoot.setCommentReplyList (getSonComments (commentRoot.getRootId ()));
        }


        redisTemplate.opsForValue ().set (titleCommentKey, rootCommentList);

    }


    @Async
    public void updateUserComment (Long userId) {
        // 生成 redis key
        String userCommentKey = String.format(USER_COMMENT, userId);

        List<UserComments> userCommentsList = new ArrayList<>();

        // 获取 父评论
        List<UserComments> allUserRootComments = getAllUserRootComments (userId);
        log.info ("allUserRootComments : " + allUserRootComments);
        userCommentsList.addAll (allUserRootComments);


        // 获取 子评论
        List<UserComments> allUserSonComments = getAllUserSonComments (userId);
        log.info ("allUserSonComments : " + allUserSonComments);
        userCommentsList.addAll (allUserSonComments);

        // 按时间先后排序
        userCommentsList.sort ((o1, o2) -> - o1.getGmtCreate ().compareTo (o2.getGmtCreate ()));

        redisTemplate.opsForValue ().set (userCommentKey, userCommentsList);

    }

}
