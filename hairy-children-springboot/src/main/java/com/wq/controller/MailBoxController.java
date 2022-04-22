package com.wq.controller;

import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.UserInfoService;
import com.wq.service.UserService;
import com.wq.service.message.*;
import com.wq.util.PageUtils;
import com.wq.util.shiro.ShiroUtils;
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
    public Result getAdoptMailboxCount () {
        Integer count = adoptMailboxService.getMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadAdoptMailBoxCount")
    public Result getUnReadAdoptMailBoxCount () {
        Integer count = adoptMailboxService.getUnreadMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getAdoptMessages")
    public Result getAdoptMessages () {
        List<AdoptMailbox> messages = adoptMailboxService.getMessages (ShiroUtils.getUserId ());

        for (AdoptMailbox message : messages) {
            setUserInfo (message);
        }

        PageUtils messagesPage = new PageUtils (messages, messages.size (), 10, 0);

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
    public Result getClueMailboxCount () {
        Integer count = clueMailboxService.getMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadClueMailBoxCount")
    public Result getUnReadClueMailBoxCount () {
        Integer count = clueMailboxService.getUnreadMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getClueMessages")
    public Result getClueMessages () {
        List<ClueMailbox> messages = clueMailboxService.getMessages (ShiroUtils.getUserId ());

        for (ClueMailbox message : messages) {
            setUserInfo (message);
        }

        PageUtils messagesPage = new PageUtils (messages, messages.size (), 10, 0);

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
    public Result getCommentMailboxCount () {
        Integer count = commentMailboxService.getMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadCommentMailBoxCount")
    public Result getUnReadCommentMailBoxCount () {
        Integer count = commentMailboxService.getUnreadMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getCommentMessages")
    public Result getCommentMessages () {
        List<CommentMailbox> messages = commentMailboxService.getMessages (ShiroUtils.getUserId ());

        for (CommentMailbox message : messages) {
            setUserInfo (message);
        }
        PageUtils messagesPage = new PageUtils (messages, messages.size (), 10, 0);

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
    public Result getReplyMailboxCount () {
        Integer count = replyMailboxService.getMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadReplyMailBoxCount")
    public Result getUnReadReplyMailBoxCount () {
        Integer count = replyMailboxService.getUnreadMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getReplyMessages")
    public Result getReplyMessages () {
        List<ReplyMailbox> messages = replyMailboxService.getMessages (ShiroUtils.getUserId ());
        for (ReplyMailbox message : messages) {
            setUserInfo (message);
        }
        PageUtils messagesPage = new PageUtils (messages, messages.size (), 10, 0);

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
    public Result getCommentLikeMailboxCount () {
        Integer count = commentLikeMailboxService.getMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadCommentLikeMailBoxCount")
    public Result getUnReadCommentLikeMailBoxCount () {
        Integer count = commentLikeMailboxService.getUnreadMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getCommentLikeMessages")
    public Result getCommentLikeMessages () {
        List<CommentLikeMailbox> messages = commentLikeMailboxService.getMessages (ShiroUtils.getUserId ());

        for (CommentLikeMailbox message : messages) {
            setUserInfo (message);
        }

        PageUtils messagesPage = new PageUtils (messages, messages.size (), 10, 0);

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
    public Result getTitleLikeMailboxCount () {
        Integer count = titleLikeMailboxService.getMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getUnReadTitleLikeMailBoxCount")
    public Result getUnReadTitleLikeMailBoxCount () {
        Integer count = titleLikeMailboxService.getUnreadMailBoxCount (ShiroUtils.getUserId ());

        Map<String, Object> map = new HashMap<>(16);
        map.put ("count", count);
        return Result.success (map);
    }

    @PostMapping("/getTitleLikeMessages")
    public Result getTitleLikeMessages () {
        List<TitleLikeMailbox> messages = titleLikeMailboxService.getMessages (ShiroUtils.getUserId ());
        for (TitleLikeMailbox message : messages) {
            setUserInfo (message);
        }
        PageUtils messagesPage = new PageUtils (messages, messages.size (), 10, 0);

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
