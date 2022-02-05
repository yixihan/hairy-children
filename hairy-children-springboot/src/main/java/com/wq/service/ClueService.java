package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.Clue;

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
     * 更新线索贴
     * @param clue clue
     * @return true : 成功 | false : 失败
     */
    Boolean updateClue (Clue clue);

    /**
     * 删除线索贴
     * @param clueId 线索贴 id
     * @return true : 成功 | false : 失败
     */
    Boolean deleteClue (Long clueId);

    /**
     * 根据线索贴 id 获取线索贴内容
     * @param clueId 线索贴 id
     * @return 线索贴内容
     */
    Clue getClueByClueId (Long clueId);

    /**
     * 根据文章 id 获取该文章下所有线索贴
     * @param titleId 文章 id
     * @return 该文章下所有线索贴
     */
    List<Clue> getAllCluesByTitleId (Long titleId);
}
