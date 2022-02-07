package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.Adopt;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
public interface AdoptService extends IService<Adopt> {

    /**
     * 创建申请贴
     * @param adopt adopt
     * @return true : 成功 | false : 失败
     */
    Boolean createAdopt (Adopt adopt);


    /**
     * 根据用户 id 获取用户发布的申请贴
     * @param userId 用户 id
     * @return 线索贴内容
     */
    List<Adopt> getAdoptsByUserId (Long userId);

    /**
     * 根据文章 id 获取该文章下所有申请贴
     * @param titleId 文章 id
     * @return 该文章下所有申请贴
     */
    List<Adopt> getAllAdoptsByTitleId (Long titleId);
}
