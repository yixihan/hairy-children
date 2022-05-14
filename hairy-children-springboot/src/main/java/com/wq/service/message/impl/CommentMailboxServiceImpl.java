package com.wq.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.mapper.CommentMailboxMapper;
import com.wq.pojo.CommentMailbox;
import com.wq.service.message.CommentMailboxService;
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
public class CommentMailboxServiceImpl extends ServiceImpl<CommentMailboxMapper, CommentMailbox> implements CommentMailboxService {

    @Resource
    private CommentMailboxMapper commentMailboxMapper;

    @Resource
    private RedisService redisService;

    /**
     * comment_root_message:receiveId
     */
    public static final String COMMENT_KEY = " comment_root_message:%s";


    @Override
    public void sendMailbox(CommentMailbox commentMailbox) {

        String key = String.format (COMMENT_KEY, commentMailbox.getReceiveUserId ());

        redisService.setBit (key, commentMailbox.getReceiveUserId (), true);
    }

    @Override
    public Boolean read(Long id) {
        CommentMailbox commentMailbox = commentMailboxMapper.selectById (id);
        commentMailbox.setIsRead (1);

        return commentMailboxMapper.updateById (commentMailbox) == 1;
    }

    @Override
    public Integer getMailBoxCount(Long userId) {
        QueryWrapper<CommentMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return commentMailboxMapper.selectCount (wrapper);
    }

    /**
     * 获取用户未读消息数 -- 评论回复 - 父评论
     *
     * @param userId 用户 id
     * @return 未读消息数
     */
    @Override
    public Integer getUnreadMailBoxCount(Long userId) {
        QueryWrapper<CommentMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId)
                .eq ("is_read", 0);
        return commentMailboxMapper.selectCount (wrapper);
    }

    @Override
    public List<CommentMailbox> getMessages(Long userId) {
        QueryWrapper<CommentMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId).orderByDesc("gmt_create");;
        return commentMailboxMapper.selectList (wrapper);
    }

    @Override
    public CommentMailbox getMessage(Long id) {
        return commentMailboxMapper.selectById (id);
    }
}
