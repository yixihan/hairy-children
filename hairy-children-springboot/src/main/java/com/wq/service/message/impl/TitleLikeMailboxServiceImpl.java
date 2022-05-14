package com.wq.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.mapper.TitleLikeMailboxMapper;
import com.wq.pojo.TitleLikeMailbox;
import com.wq.service.message.TitleLikeMailboxService;
import com.wq.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TitleLikeMailboxServiceImpl extends ServiceImpl<TitleLikeMailboxMapper, TitleLikeMailbox> implements TitleLikeMailboxService {

    @Resource
    private TitleLikeMailboxMapper titleLikeMailboxMapper;

    @Resource
    private RedisService redisService;

    /**
     * title_like_message:receiveUserId
     */
    public static final String USER_KEY = "title_like_message:%s";


    @Override
    public void sendMailbox(TitleLikeMailbox titleLikeMailbox) {
        String key = String.format (USER_KEY, titleLikeMailbox.getReceiveUserId ());
        redisService.setBit (key, titleLikeMailbox.getReceiveUserId (), true);

    }

    @Override
    public Boolean read(Long id) {
        TitleLikeMailbox titleLikeMailbox = titleLikeMailboxMapper.selectById (id);
        titleLikeMailbox.setIsRead (1);

        String key = String.format (USER_KEY, titleLikeMailbox.getReceiveUserId ());
        redisService.setBit (key, titleLikeMailbox.getReceiveUserId (), false);

        return titleLikeMailboxMapper.updateById (titleLikeMailbox) == 1;
    }

    @Override
    public Integer getMailBoxCount(Long userId) {
        QueryWrapper<TitleLikeMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId);
        return titleLikeMailboxMapper.selectCount (wrapper);
    }

    @Override
    public Integer getUnreadMailBoxCount(Long userId) {
        QueryWrapper<TitleLikeMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId)
                .eq ("is_read", 0);
        return titleLikeMailboxMapper.selectCount (wrapper);
    }

    @Override
    public List<TitleLikeMailbox> getMessages(Long userId) {
        QueryWrapper<TitleLikeMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("receive_user_id", userId).orderByDesc("gmt_create");;
        return titleLikeMailboxMapper.selectList (wrapper);
    }

    @Override
    public TitleLikeMailbox getMessage(Long id) {
        return titleLikeMailboxMapper.selectById (id);
    }


}
