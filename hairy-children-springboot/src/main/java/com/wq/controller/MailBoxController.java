package com.wq.controller;

import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.UserInfoService;
import com.wq.service.UserService;
import com.wq.service.message.*;
import com.wq.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-02-26-18:06
 */
@RestController
@Slf4j
@RequestMapping("/mailbox")
public class MailBoxController {

    @Resource
    private AdoptMailboxService adoptMailboxService;

    @Resource
    private ClueMailboxService clueMailboxService;

    @Resource
    private CommentMailboxService commentMailboxService;

    @Resource
    private ReplyMailboxService replyMailboxService;

    @Resource
    private CommentLikeMailboxService commentLikeMailboxService;

    @Resource
    private TitleLikeMailboxService titleLikeMailboxService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;


    /**************adopt**************/
    @PostMapping("/readAdoptMailBox")
    public Result readAdoptMailBox (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        Boolean read = adoptMailboxService.read (id);

        return read ? Result.success () : Result.fail ();
    }

    @PostMapping("/getAdoptMailBoxCount")
    public Result getAdoptMailboxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = adoptMailboxService.getMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadAdoptMailBoxCount")
    public Result getUnReadAdoptMailBoxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = adoptMailboxService.getUnreadMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getAdoptMessages")
    public Result getAdoptMessages (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        List<AdoptMailbox> messages = adoptMailboxService.getMessages (userId);

        for (AdoptMailbox message : messages) {
            setUserInfo (message);
        }

        PageUtils messagesPage = new PageUtils (messages, messages.size (), 5, 0);

            Map<String, Object> map = new HashMap<>(16);
        map.put ("page", messagesPage);
        return Result.success (map);
    }

    @PostMapping("/getAdoptMessage")
    public Result getAdoptMessage (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        AdoptMailbox message = adoptMailboxService.getMessage (id);
        setUserInfo (message);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }

    /**************clue**************/
    @PostMapping("/readClueMailBox")
    public Result readClueMailBox (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        Boolean read = clueMailboxService.read (id);

        return read ? Result.success () : Result.fail ();
    }

    @PostMapping("/getClueMailBoxCount")
    public Result getClueMailboxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = clueMailboxService.getMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadClueMailBoxCount")
    public Result getUnReadClueMailBoxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = clueMailboxService.getUnreadMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getClueMessages")
    public Result getClueMessages (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        List<ClueMailbox> messages = clueMailboxService.getMessages (userId);

        for (ClueMailbox message : messages) {
            setUserInfo (message);
        }

        PageUtils messagesPage = new PageUtils (messages, messages.size (), 5, 0);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("page", messagesPage);
        return Result.success (map);
    }

    @PostMapping("/getClueMessage")
    public Result getClueMessage (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        ClueMailbox message = clueMailboxService.getMessage (id);
        setUserInfo (message);
        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************comment**************/
    @PostMapping("/readCommentMailBox")
    public Result readCommentMailBox (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        Boolean read = commentMailboxService.read (id);

        return read ? Result.success () : Result.fail ();
    }

    @PostMapping("/getCommentMailBoxCount")
    public Result getCommentMailboxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = commentMailboxService.getMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadCommentMailBoxCount")
    public Result getUnReadCommentMailBoxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = commentMailboxService.getUnreadMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getCommentMessages")
    public Result getCommentMessages (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        List<CommentMailbox> messages = commentMailboxService.getMessages (userId);

        for (CommentMailbox message : messages) {
            setUserInfo (message);
        }
        PageUtils messagesPage = new PageUtils (messages, messages.size (), 5, 0);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("page", messagesPage);
        return Result.success (map);
    }

    @PostMapping("/getCommentMessage")
    public Result getCommentMessage (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        CommentMailbox message = commentMailboxService.getMessage (id);
        setUserInfo (message);
        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************reply**************/
    @PostMapping("/readReplyMailBox")
    public Result readReplyMailBox (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        Boolean read = replyMailboxService.read (id);

        return read ? Result.success () : Result.fail ();
    }

    @PostMapping("/getReplyMailBoxCount")
    public Result getReplyMailboxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = replyMailboxService.getMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadReplyMailBoxCount")
    public Result getUnReadReplyMailBoxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = replyMailboxService.getUnreadMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getReplyMessages")
    public Result getReplyMessages (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        List<ReplyMailbox> messages = replyMailboxService.getMessages (userId);
        for (ReplyMailbox message : messages) {
            setUserInfo (message);
        }
        PageUtils messagesPage = new PageUtils (messages, messages.size (), 5, 0);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("page", messagesPage);
        return Result.success (map);
    }

    @PostMapping("/getReplyMessage")
    public Result getReplyMessage (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        ReplyMailbox message = replyMailboxService.getMessage (id);
        setUserInfo (message);
        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************commentLike**************/
    @PostMapping("/readCommentLikeMailBox")
    public Result readCommentLikeMailBox (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        Boolean read = commentLikeMailboxService.read (id);

        return read ? Result.success () : Result.fail ();
    }

    @PostMapping("/getCommentLikeMailBoxCount")
    public Result getCommentLikeMailboxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = commentLikeMailboxService.getMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadCommentLikeMailBoxCount")
    public Result getUnReadCommentLikeMailBoxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = commentLikeMailboxService.getUnreadMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getCommentLikeMessages")
    public Result getCommentLikeMessages (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        List<CommentLikeMailbox> messages = commentLikeMailboxService.getMessages (userId);

        for (CommentLikeMailbox message : messages) {
            setUserInfo (message);
        }

        PageUtils messagesPage = new PageUtils (messages, messages.size (), 5, 0);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("page", messagesPage);
        return Result.success (map);
    }

    @PostMapping("/getCommentLikeMessage")
    public Result getCommentLikeMessage (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        CommentLikeMailbox message = commentLikeMailboxService.getMessage (id);
        setUserInfo (message);
        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************titleLike**************/
    @PostMapping("/readTitleLikeMailBox")
    public Result readTitleLikeMailBox (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        Boolean read = titleLikeMailboxService.read (id);

        return read ? Result.success () : Result.fail ();
    }

    @PostMapping("/getTitleLikeMailBoxCount")
    public Result getTitleLikeMailboxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = titleLikeMailboxService.getMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadTitleLikeMailBoxCount")
    public Result getUnReadTitleLikeMailBoxCount (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        Integer count = titleLikeMailboxService.getUnreadMailBoxCount (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getTitleLikeMessages")
    public Result getTitleLikeMessages (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));
        List<TitleLikeMailbox> messages = titleLikeMailboxService.getMessages (userId);
        for (TitleLikeMailbox message : messages) {
            setUserInfo (message);
        }
        PageUtils messagesPage = new PageUtils (messages, messages.size (), 5, 0);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("page", messagesPage);
        return Result.success (map);
    }

    @PostMapping("/getTitleLikeMessage")
    public Result getTitleLikeMessage (@RequestBody Map<String, Object> params) {
        long id = Long.parseLong (String.valueOf (params.get ("id")));

        TitleLikeMailbox message = titleLikeMailboxService.getMessage (id);
        setUserInfo (message);
        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    private void setUserInfo (AdoptMailbox message) {
        UserInfo sendInfo = userInfoService.getUserInfoById (message.getSendUserId ());
        User sendUser = userService.getById (message.getSendUserId ());
        UserInfo receiveInfo = userInfoService.getUserInfoById (message.getReceiveUserId ());
        User receiveUser = userService.getById (message.getReceiveUserId ());

        message.setSendUserAvatar (sendInfo.getUserAvatar ());
        message.setSendUserName (sendUser.getUserName ());
        message.setReceiveUserAvatar (receiveInfo.getUserAvatar ());
        message.setReceiveUserName (receiveUser.getUserName ());
    }

    private void setUserInfo (ClueMailbox message) {
        UserInfo sendInfo = userInfoService.getUserInfoById (message.getSendUserId ());
        User sendUser = userService.getById (message.getSendUserId ());
        UserInfo receiveInfo = userInfoService.getUserInfoById (message.getReceiveUserId ());
        User receiveUser = userService.getById (message.getReceiveUserId ());

        message.setSendUserAvatar (sendInfo.getUserAvatar ());
        message.setSendUserName (sendUser.getUserName ());
        message.setReceiveUserAvatar (receiveInfo.getUserAvatar ());
        message.setReceiveUserName (receiveUser.getUserName ());
    }

    private void setUserInfo (CommentMailbox message) {
        UserInfo sendInfo = userInfoService.getUserInfoById (message.getSendUserId ());
        User sendUser = userService.getById (message.getSendUserId ());
        UserInfo receiveInfo = userInfoService.getUserInfoById (message.getReceiveUserId ());
        User receiveUser = userService.getById (message.getReceiveUserId ());

        message.setSendUserAvatar (sendInfo.getUserAvatar ());
        message.setSendUserName (sendUser.getUserName ());
        message.setReceiveUserAvatar (receiveInfo.getUserAvatar ());
        message.setReceiveUserName (receiveUser.getUserName ());
    }

    private void setUserInfo (ReplyMailbox message) {
        UserInfo sendInfo = userInfoService.getUserInfoById (message.getSendUserId ());
        User sendUser = userService.getById (message.getSendUserId ());
        UserInfo receiveInfo = userInfoService.getUserInfoById (message.getReceiveUserId ());
        User receiveUser = userService.getById (message.getReceiveUserId ());

        message.setSendUserAvatar (sendInfo.getUserAvatar ());
        message.setSendUserName (sendUser.getUserName ());
        message.setReceiveUserAvatar (receiveInfo.getUserAvatar ());
        message.setReceiveUserName (receiveUser.getUserName ());
    }

    private void setUserInfo (CommentLikeMailbox message) {
        UserInfo sendInfo = userInfoService.getUserInfoById (message.getSendUserId ());
        User sendUser = userService.getById (message.getSendUserId ());
        UserInfo receiveInfo = userInfoService.getUserInfoById (message.getReceiveUserId ());
        User receiveUser = userService.getById (message.getReceiveUserId ());

        message.setSendUserAvatar (sendInfo.getUserAvatar ());
        message.setSendUserName (sendUser.getUserName ());
        message.setReceiveUserAvatar (receiveInfo.getUserAvatar ());
        message.setReceiveUserName (receiveUser.getUserName ());
    }

    private void setUserInfo (TitleLikeMailbox message) {
        UserInfo sendInfo = userInfoService.getUserInfoById (message.getSendUserId ());
        User sendUser = userService.getById (message.getSendUserId ());
        UserInfo receiveInfo = userInfoService.getUserInfoById (message.getReceiveUserId ());
        User receiveUser = userService.getById (message.getReceiveUserId ());

        message.setSendUserAvatar (sendInfo.getUserAvatar ());
        message.setSendUserName (sendUser.getUserName ());
        message.setReceiveUserAvatar (receiveInfo.getUserAvatar ());
        message.setReceiveUserName (receiveUser.getUserName ());
    }
}
