package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.UserCollection;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
public interface UserCollectionService extends IService<UserCollection> {

    /**
     * 通过 id 获取该用户的所有收藏夹
     * @param userId 用户 id
     * @return 收藏夹, 以 List 方式返回
     */
    List<UserCollection> getAllCollectionById (Long userId);

}
