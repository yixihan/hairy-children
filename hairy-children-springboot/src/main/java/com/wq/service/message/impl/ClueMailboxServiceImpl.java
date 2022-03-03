package com.wq.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.mapper.ClueMailboxMapper;
import com.wq.pojo.ClueMailbox;
import com.wq.service.message.ClueMailboxService;
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
public class ClueMailboxServiceImpl extends ServiceImpl<ClueMailboxMapper, ClueMailbox> implements ClueMailboxService {

    @Resource
    private ClueMailboxMapper clueMailboxMapper;

    @Resource
    private RedisService redisService;

    /**
     * clue_message:receiveId
     */
    private static final String CLUE_KEY = "clue_message:%s";

    @Override
    public void sendMailbox(ClueMailbox clueMailbox) {
        String key = String.format (CLUE_KEY, clueMailbox.getReceiveUserId ());
        redisService.setBit (key, clueMailbox.getReceiveUserId (), true);
    }

    @Override
    public Boolean read(Long id) {
        ClueMailbox clueMailbox = clueMailboxMapper.selectById (id);
        clueMailbox.setIsRead (1);

        return clueMailboxMapper.updateById (clueMailbox) == 1;
    }

    @Override
    public Integer getMailBoxCount(Long userId) {
        QueryWrapper<ClueMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return clueMailboxMapper.selectCount (wrapper);
    }

    /**
     * 获取用户未读消息数 -- 线索
     *
     * @param userId 用户 id
     * @return 未读消息数
     */
    @Override
    public Integer getUnreadMailBoxCount(Long userId) {
        QueryWrapper<ClueMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId)
                .eq ("is_read", 0);
        return clueMailboxMapper.selectCount (wrapper);
    }

    @Override
    public List<ClueMailbox> getMessages(Long userId) {
        QueryWrapper<ClueMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return clueMailboxMapper.selectList (wrapper);
    }

    @Override
    public ClueMailbox getMessage(Long id) {
        return clueMailboxMapper.selectById (id);
    }
}
