package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.pojo.UserCollection;
import com.wq.mapper.UserCollectionMapper;
import com.wq.service.UserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service("userCollectionService")
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

    @Resource
    private UserCollectionMapper userCollectionMapper;

    @Override
    public List<UserCollection> getAllCollectionById(Long userId) {
        QueryWrapper<UserCollection> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId);
        return userCollectionMapper.selectList (wrapper);
    }
}
