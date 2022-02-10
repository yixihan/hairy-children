package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.Adopt;
import org.springframework.web.multipart.MultipartFile;

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
     * 上传图片
     * @param adoptId 申请贴 id
     * @param file 图片
     * @return 图片路径
     */
    String uploadImg (Long adoptId, MultipartFile file);


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

    /**
     * 校验该用户是否可以发申请, 同一用户在同一文章只能同时存在一个未审核的领养申请
     * @param titleId 文章 id
     * @param userId 用户 id
     * @return true : 可以 | false : 不可以
     */
    Boolean isExists (Long titleId, Long userId);
}
