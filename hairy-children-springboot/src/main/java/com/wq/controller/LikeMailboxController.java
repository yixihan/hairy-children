package com.wq.controller;


import com.wq.common.pojo.Result;
import com.wq.pojo.TitleLikeMailbox;
import com.wq.service.TitleLikeMailboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wq
 * @since 2022-02-13
 */
@RestController
@Slf4j
@RequestMapping("/like-mailbox")
public class LikeMailboxController {

    @Resource
    private TitleLikeMailboxService titleLikeMailboxService;

    @PostMapping("/getMessages")
    public Result getMessages (Long userId) {
        List<TitleLikeMailbox> messageList = titleLikeMailboxService.getMessages (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("messageList", messageList);
        return Result.success ("获取消息成功", map);
    }

    @PostMapping("/getMessage")
    public Result getMessage (Long id) {
        TitleLikeMailbox message = titleLikeMailboxService.getMessage (id);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success ("获取消息成功", map);
    }

    @PostMapping("/read")
    public Result read (Long id) {
        Boolean read = titleLikeMailboxService.read (id);

        return read ? Result.success () : Result.fail ();
    }


}
