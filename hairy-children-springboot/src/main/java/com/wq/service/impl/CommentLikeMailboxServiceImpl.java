package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.mapper.CommentLikeMailboxMapper;
import com.wq.pojo.CommentLikeMailbox;
import com.wq.service.CommentLikeMailboxService;
import com.wq.service.redis.RedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-13
 */
@Service
public class CommentLikeMailboxServiceImpl extends ServiceImpl<CommentLikeMailboxMapper, CommentLikeMailbox> implements CommentLikeMailboxService {

    @Resource
    private CommentLikeMailboxMapper commentLikeMailboxMapper;

    @Resource
    private RedisService redisService;

    /**
     * user_message:receiveUserId
     */
    public static final String USER_KEY = "user_message:%s";

    @Override
    public void sendMailbox(CommentLikeMailbox commentLikeMailbox) {
        String key = String.format (USER_KEY, commentLikeMailbox.getReceiveUserId ());
        redisService.setBit (key, commentLikeMailbox.getReceiveUserId (), true);
    }

    @Override
    public Boolean read(Long id) {
        CommentLikeMailbox commentLikeMailbox = commentLikeMailboxMapper.selectById (id);
        commentLikeMailbox.setIsRead (1);

        return commentLikeMailboxMapper.updateById (commentLikeMailbox) == 1;
    }

    @Override
    public Integer getMailBoxCount(Long userId) {
        QueryWrapper<CommentLikeMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return commentLikeMailboxMapper.selectCount (wrapper);
    }

    @Override
    public List<CommentLikeMailbox> getMessages(Long userId) {
        QueryWrapper<CommentLikeMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return commentLikeMailboxMapper.selectList (wrapper);
    }

    @Override
    public CommentLikeMailbox getMessage(Long id) {
        return commentLikeMailboxMapper.selectById (id);
    }
}
