package com.wq.service.impl;

import com.wq.pojo.UserCollection;
import com.wq.mapper.UserCollectionMapper;
import com.wq.service.UserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

    @Override
    public Boolean createCollection(UserCollection collection) {
        return null;
    }

    @Override
    public Boolean updateCollection(UserCollection collection) {
        return null;
    }

    @Override
    public Boolean deleteCollection(Long collectionId) {
        return null;
    }

    @Override
    public List<UserCollection> getAllCollectionById(Long userId) {
        return null;
    }
}
