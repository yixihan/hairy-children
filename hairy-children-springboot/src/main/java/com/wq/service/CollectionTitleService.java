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
     * 收藏文章
     * @param collectionTitle collectionTitle
     * @return true : 成功 | false : 失败
     */
    Boolean createCollectionTitle (CollectionTitle collectionTitle);

    /**
     * 取消收藏文章
     * @param id id
     * @return true : 成功 | false : 失败
     */
    Boolean deleteCollectionTitle (Long id);

    /**
     * 通过收藏夹 id 获取该收藏夹所有文章
     * @param userCollectionId 收藏夹 id
     * @return 该收藏夹下所有的文章, 以 List 集合形式返回
     */
    List<CollectionTitle> getAllCollectionTitle (Long userCollectionId);

    /**
     * 获取该收藏夹内文章数量
     * @param userCollectionId 收藏夹 id
     * @return 该收藏夹内文章数量
     */
    Integer getTitleCount (Long userCollectionId);
}
