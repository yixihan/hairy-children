package com.wq.service.impl;

import com.wq.pojo.CollectionTitle;
import com.wq.mapper.CollectionTitleMapper;
import com.wq.service.CollectionTitleService;
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
public class CollectionTitleServiceImpl extends ServiceImpl<CollectionTitleMapper, CollectionTitle> implements CollectionTitleService {

    @Override
    public Boolean createCollectionTitle(CollectionTitle collectionTitle) {
        return null;
    }

    @Override
    public Boolean deleteCollectionTitle(Long id) {
        return null;
    }

    @Override
    public List<CollectionTitle> getAllCollectionTitle(Long userCollectionId) {
        return null;
    }

    @Override
    public Integer getTitleCount(Long userCollectionId) {
        return null;
    }
}
