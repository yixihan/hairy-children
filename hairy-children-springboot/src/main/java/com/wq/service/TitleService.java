package com.wq.service;

import com.wq.pojo.Title;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface TitleService extends IService<Title> {

    /**
     * 创建文章, 以 titleType 区分是领养贴还是寻宠贴
     * @param title 文章
     * @return true : 成功 | false : 失败
     */
    Boolean createTitle (Title title);

    /**
     * 上传文章图片
     * @param titleId 文章 id
     * @param file 图片
     * @return 图片路径
     */
    String uploadTitleImg (Long titleId, MultipartFile file);


    /**
     * 通过用户 id 获取文章
     * @param userId 用户 id
     * @return 文章
     */
    List<Title> getTitleByUserId (Long userId);


}
