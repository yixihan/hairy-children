package com.wq.controller;

import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.message.*;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
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


    /**************adopt**************/
    @PostMapping("/readAdoptMailBox")
    public Result readAdoptMailBox (Long id) {
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

        Map<String, Object> map = new HashMap<>(16);
        map.put ("messages", messages);
        return Result.success (map);
    }

    @PostMapping("/getAdoptMessage")
    public Result getAdoptMessage (Long id) {
        AdoptMailbox message = adoptMailboxService.getMessage (id);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }

    /**************clue**************/
    @PostMapping("/readClueMailBox")
    public Result readClueMailBox (Long id) {
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

        Map<String, Object> map = new HashMap<>(16);
        map.put ("messages", messages);
        return Result.success (map);
    }

    @PostMapping("/getClueMessage")
    public Result getClueMessage (Long id) {
        ClueMailbox message = clueMailboxService.getMessage (id);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************comment**************/
    @PostMapping("/readCommentMailBox")
    public Result readCommentMailBox (Long id) {
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

        Map<String, Object> map = new HashMap<>(16);
        map.put ("messages", messages);
        return Result.success (map);
    }

    @PostMapping("/getCommentMessage")
    public Result getCommentMessage (Long id) {
        CommentMailbox message = commentMailboxService.getMessage (id);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************reply**************/
    @PostMapping("/readReplyMailBox")
    public Result readReplyMailBox (Long id) {
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

        Map<String, Object> map = new HashMap<>(16);
        map.put ("messages", messages);
        return Result.success (map);
    }

    @PostMapping("/getReplyMessage")
    public Result getReplyMessage (Long id) {
        ReplyMailbox message = replyMailboxService.getMessage (id);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************commentLike**************/
    @PostMapping("/readCommentLikeMailBox")
    public Result readCommentLikeMailBox (Long id) {
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

        Map<String, Object> map = new HashMap<>(16);
        map.put ("messages", messages);
        return Result.success (map);
    }

    @PostMapping("/getCommentLikeMessage")
    public Result getCommentLikeMessage (Long id) {
        CommentLikeMailbox message = commentLikeMailboxService.getMessage (id);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }


    /**************titleLike**************/
    @PostMapping("/readTitleLikeMailBox")
    public Result readTitleLikeMailBox (Long id) {
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

        Map<String, Object> map = new HashMap<>(16);
        map.put ("messages", messages);
        return Result.success (map);
    }

    @PostMapping("/getTitleLikeMessage")
    public Result getTitleLikeMessage (Long id) {
        TitleLikeMailbox message = titleLikeMailboxService.getMessage (id);

        Map<String, Object> map = new HashMap<>(16);
        map.put ("message", message);
        return Result.success (map);
    }

}
