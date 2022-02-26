package com.wq.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.TitleLikeMailbox;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-13
 */
public interface TitleLikeMailboxService extends IService<TitleLikeMailbox> {

    /**
     * 点赞之后发送信息
     * @param titleLikeMailbox
     */
    void sendMailbox (TitleLikeMailbox titleLikeMailbox);

    /**
     * 阅读消息, is_read : 0 => 1
     * @param id 主键 id
     * @return true : 操作成功 | false : 操作失败
     */
    Boolean read (Long id);

    /**
     * 获取用户未读消息数 -- 文章点赞
     * @param userId 用户 id
     * @return 未读消息数
     */
    Integer getMailBoxCount (Long userId);

    /**
     * 获取用户所有消息 -- 文章点赞
     * @param userId 用户 id
     * @return 用户所有消息
     */
    List<TitleLikeMailbox> getMessages (Long userId);

    /**
     * 获取指定消息 -- 文章点赞
     * @param id 主键 id
     * @return 消息主体
     */
    TitleLikeMailbox getMessage (Long id);

}
