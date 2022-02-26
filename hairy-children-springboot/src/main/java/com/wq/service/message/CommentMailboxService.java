package com.wq.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.CommentMailbox;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-13
 */
public interface CommentMailboxService extends IService<CommentMailbox> {


    /**
     * 评论之后发送信息 - 父评论
     *
     * @param commentMailbox
     */
    void sendMailbox (CommentMailbox commentMailbox);

    /**
     * 阅读消息, is_read : 0 => 1
     * @param id 主键 id
     * @return true : 操作成功 | false : 操作失败
     */
    Boolean read (Long id);

    /**
     * 获取用户未读消息数 -- 评论回复 - 父评论
     * @param userId 用户 id
     * @return 未读消息数
     */
    Integer getMailBoxCount (Long userId);

    /**
     * 获取用户所有消息 -- 评论回复 - 父评论
     * @param userId 用户 id
     * @return 用户所有消息
     */
    List<CommentMailbox> getMessages (Long userId);

    /**
     * 获取指定消息 -- 评论回复 - 父评论
     * @param id 主键 id
     * @return 消息主体
     */
    CommentMailbox getMessage (Long id);
}
