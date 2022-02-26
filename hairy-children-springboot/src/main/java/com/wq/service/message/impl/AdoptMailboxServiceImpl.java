package com.wq.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.mapper.AdoptMailboxMapper;
import com.wq.pojo.AdoptMailbox;
import com.wq.service.message.AdoptMailboxService;
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
public class AdoptMailboxServiceImpl extends ServiceImpl<AdoptMailboxMapper, AdoptMailbox> implements AdoptMailboxService {

    @Resource
    private AdoptMailboxMapper adoptMailboxMapper;

    @Resource
    private RedisService redisService;

    /**
     * adopt_message:receiveId
     */
    private static final String ADOPT_KEY = "adopt_message:%s";

    @Override
    public void sendMailbox(AdoptMailbox adoptMailbox) {
        String key = String.format (ADOPT_KEY, adoptMailbox.getReceiveUserId ());
        redisService.setBit (key, adoptMailbox.getReceiveUserId (), true);
    }

    @Override
    public Boolean read(Long id) {
        AdoptMailbox adoptMailbox = adoptMailboxMapper.selectById (id);
        adoptMailbox.setIsRead (1);

        return adoptMailboxMapper.updateById (adoptMailbox) == 1;
    }

    @Override
    public Integer getMailBoxCount(Long userId) {
        QueryWrapper<AdoptMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return adoptMailboxMapper.selectCount (wrapper);
    }

    @Override
    public List<AdoptMailbox> getMessages(Long userId) {
        QueryWrapper<AdoptMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return adoptMailboxMapper.selectList (wrapper);
    }

    @Override
    public AdoptMailbox getMessage(Long id) {
        return adoptMailboxMapper.selectById (id);
    }
}
