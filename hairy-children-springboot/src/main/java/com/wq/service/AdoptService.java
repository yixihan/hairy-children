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
     * 更新申请贴
     * @param adopt adopt
     * @return true : 成功 | false : 失败
     */
    Boolean updateAdopt (Adopt adopt);

    /**
     * 删除申请贴
     * @param adoptId 申请贴 id
     * @return true : 成功 | false : 失败
     */
    Boolean deleteAdopt (Long adoptId);

    /**
     * 根据申请贴 id 获取申请贴内容
     * @param adoptId 申请贴 id
     * @return 申请贴内容
     */
    Adopt getAdoptByAdoptId (Long adoptId);

    /**
     * 根据文章 id 获取该文章下所有申请贴
     * @param titleId 文章 id
     * @return 该文章下所有申请贴
     */
    List<Adopt> getAllAdoptsByTitleId (Long titleId);
}
