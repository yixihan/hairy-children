package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.Clue;
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
public interface ClueService extends IService<Clue> {

    /**
     * 创建线索贴
     * @param clue clue
     * @return true : 成功 | false : 失败
     */
    Boolean createClue (Clue clue);

    /**
     * 上传图片
     * @param clueId 申请贴 id
     * @param file 图片
     * @return 图片路径
     */
    String uploadImg (Long clueId, MultipartFile file);


    /**
     * 根据用户 id 获取用户发布的线索贴
     * @param userId 用户 id
     * @return 线索贴内容
     */
    List<Clue> getCluesByUserId (Long userId);

    /**
     * 根据文章 id 获取该文章下所有线索贴
     * @param titleId 文章 id
     * @return 该文章下所有线索贴
     */
    List<Clue> getCluesByTitleId (Long titleId);

    /**
     * 校验该用户是否可以发申请, 同一用户在同一文章只能同时存在一个未审核的线索
     * @param titleId 文章 id
     * @param userId 用户 id
     * @return true : 可以 | false : 不可以
     */
    Boolean isExists (Long titleId, Long userId);
}
