package com.wq.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.mapper.ReplyMailboxMapper;
import com.wq.pojo.ReplyMailbox;
import com.wq.service.message.ReplyMailboxService;
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
public class ReplyMailboxServiceImpl extends ServiceImpl<ReplyMailboxMapper, ReplyMailbox> implements ReplyMailboxService {

    @Resource
    private ReplyMailboxMapper replyMailboxMapper;

    @Resource
    private RedisService redisService;

    /**
     * comment_reply_message:receiveId
     */
    public static final String REPLY_KEY = " comment_reply_message:%s";


    @Override
    public void sendMailbox(ReplyMailbox replyMailbox) {

        String key = String.format (REPLY_KEY, replyMailbox.getReceiveUserId ());

        redisService.setBit (key, replyMailbox.getReceiveUserId (), true);
    }

    @Override
    public Boolean read(Long id) {
        ReplyMailbox replyMailbox = replyMailboxMapper.selectById (id);
        replyMailbox.setIsRead (1);

        return replyMailboxMapper.updateById (replyMailbox) == 1;
    }

    @Override
    public Integer getMailBoxCount(Long userId) {
        QueryWrapper<ReplyMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return replyMailboxMapper.selectCount (wrapper);
    }

    /**
     * 获取用户未读消息数 -- 评论回复 - 子评论
     *
     * @param userId 用户 id
     * @return 未读消息数
     */
    @Override
    public Integer getUnreadMailBoxCount(Long userId) {
        QueryWrapper<ReplyMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId)
                .eq ("is_read", 0);
        return replyMailboxMapper.selectCount (wrapper);
    }

    @Override
    public List<ReplyMailbox> getMessages(Long userId) {
        QueryWrapper<ReplyMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return replyMailboxMapper.selectList (wrapper);
    }

    @Override
    public ReplyMailbox getMessage(Long id) {
        return replyMailboxMapper.selectById (id);
    }
}
