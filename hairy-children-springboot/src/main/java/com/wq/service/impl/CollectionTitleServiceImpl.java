package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.pojo.CollectionTitle;
import com.wq.mapper.CollectionTitleMapper;
import com.wq.service.CollectionTitleService;
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
@Service("CollectionTitleService")
public class CollectionTitleServiceImpl extends ServiceImpl<CollectionTitleMapper, CollectionTitle> implements CollectionTitleService {

    @Resource
    private CollectionTitleMapper collectionTitleMapper;

    @Override
    public List<CollectionTitle> getAllCollectionTitle(Long userCollectionId) {
        QueryWrapper<CollectionTitle> wrapper = new QueryWrapper<> ();
        wrapper.eq ("collection_id", userCollectionId);
        return collectionTitleMapper.selectList (wrapper);
    }

}
