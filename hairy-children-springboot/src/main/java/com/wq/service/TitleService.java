package com.wq.service;

import com.wq.pojo.Title;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

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
     * 更新文章
     * @param title 文章
     * @return true : 成功 | false : 失败
     */
    Boolean updateTitle (Title title);

    /**
     * 上传文章图片
     * @param titleId 文章 id
     * @param file 图片
     * @return 图片路径
     */
    String uploadTitleImg (Long titleId, MultipartFile file);

    /**
     * 删除文章
     * @param titleId 文章 id
     * @return true : 成功 | false : 失败
     */
    Boolean deleteTitle (Long titleId);

    /**
     * 通过文章 id 获取文章
     * @param titleId 文章 id
     * @return 文章
     */
    Title getTitleById (Long titleId);


}
