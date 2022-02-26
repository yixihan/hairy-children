package com.wq.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.ClueMailbox;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-13
 */
public interface ClueMailboxService extends IService<ClueMailbox> {


    /**
     * 发送线索之后发送信息
     *
     * @param clueMailbox
     */
    void sendMailbox (ClueMailbox clueMailbox);

    /**
     * 阅读消息, is_read : 0 => 1
     * @param id 主键 id
     * @return true : 操作成功 | false : 操作失败
     */
    Boolean read (Long id);

    /**
     * 获取用户未读消息数 -- 线索
     * @param userId 用户 id
     * @return 未读消息数
     */
    Integer getMailBoxCount (Long userId);

    /**
     * 获取用户所有消息 -- 线索
     * @param userId 用户 id
     * @return 用户所有消息
     */
    List<ClueMailbox> getMessages (Long userId);

    /**
     * 获取指定消息 -- 线索
     * @param id 主键 id
     * @return 消息主体
     */
    ClueMailbox getMessage (Long id);
}
