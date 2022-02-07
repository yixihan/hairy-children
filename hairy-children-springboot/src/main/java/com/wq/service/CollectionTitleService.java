package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.CollectionTitle;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
public interface CollectionTitleService extends IService<CollectionTitle> {


    /**
     * 通过收藏夹 id 获取该收藏夹所有文章
     * @param userCollectionId 收藏夹 id
     * @return 该收藏夹下所有的文章, 以 List 集合形式返回
     */
    List<CollectionTitle> getAllCollectionTitle (Long userCollectionId);

}
