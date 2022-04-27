package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.PhotoProperties;
import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.ClueService;
import com.wq.service.TitleService;
import com.wq.service.UserInfoService;
import com.wq.service.UserService;
import com.wq.service.message.ClueMailboxService;
import com.wq.util.PageUtils;
import com.wq.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@RestController
@Slf4j
@RequestMapping("/clue")
public class ClueController {

    @Resource
    private ClueService clueService;

    @Resource
    private TitleService titleService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ClueMailboxService clueMailboxService;

    @Resource
    private PhotoProperties photoProperties;

    @PostMapping("/creatClue")
    public Result createClue(@RequestBody Clue clue) {

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", clue.getUserId ())) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", clue.getTitleId ())) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        if (!clueService.isExists (clue.getTitleId (), clue.getUserId ())) {
            return Result.fail ("请勿重复创建线索贴");
        }

        clue.setGmtCreate (new Date ());
        Boolean create = clueService.createClue (clue);

        if (create) {
            QueryWrapper<Clue> wrapper = new QueryWrapper<> ();
            Map<String, Object> columns = new HashMap<> (16);
            columns.put ("user_id", clue.getUserId ());
            columns.put ("title_id", clue.getTitleId ());
            columns.put ("gmt_create", clue.getGmtCreate ());
            wrapper.allEq (true, columns, true).select ("clue_id");

            clue = clueService.getOne (wrapper);

            Map<String, Object> map = new HashMap<> (16);
            map.put ("clueId", clue.getClueId ());
            return Result.success ("创建成功", map);
        }

        return Result.fail ("创建失败");
    }

    @PostMapping("/updateClue")
    public Result updateClue(@RequestBody Clue clue) {
        boolean update = clueService.updateById (clue);
        clue = clueService.getById (clue.getClueId ());

        Title title = titleService.getById (clue.getTitleId ());

        ClueMailbox mailbox = new ClueMailbox ();
        mailbox.setClueId (clue.getClueId ());
        mailbox.setTitleId (clue.getTitleId ());
        mailbox.setSendUserId (clue.getUserId ());
        mailbox.setReceiveUserId (title.getUserId ());

        sendClueMailBox (mailbox);

        return update ? Result.success ("更新成功") : Result.fail ("更新失败");
    }

    @PostMapping("/deleteClue")
    public Result deleteClue(@RequestBody Map<String, Object> params) {
        long clueId = Long.parseLong (String.valueOf (params.get ("clueId")));
        boolean remove = clueService.removeById (clueId);

        return remove ? Result.success ("删除成功") : Result.fail ("删除失败");
    }

    @PostMapping("/updateImg/{clueId}")
    public Result updateImg(@PathVariable Long clueId, @RequestParam("imgs") MultipartFile[] imgs) {
        StringBuilder clueImgs = new StringBuilder ();
        List<String> imgList = new ArrayList<>();

        for (MultipartFile img : imgs) {
            String clueImg = clueService.uploadImg (clueId, img);
            imgList.add (clueImg);
            clueImgs.append (clueImg).append ("::");
        }


        Clue clue = clueService.getById (clueId);
        clue.setImgsDir (clue.getImgsDir () + clueImgs.toString ());
        clueService.updateById (clue);
        Map<String, Object> map= new HashMap<> (16);
        map.put ("imgList", imgList);

        return Result.success ("图片上传成功", map);
    }

    @PostMapping("/deleteImg")
    public Result deleteImg(@RequestBody Map<String, Object> params) {
        long clueId = Long.parseLong (String.valueOf (params.get ("clueId")));
        String imgUrl = String.valueOf (params.get ("imgUrl"));

        if (clueService.count (new QueryWrapper<Clue> ().eq ("clue_id", clueId)) <= 0) {
            return Result.fail (555, "没有该领养申请");
        }

        Clue clue = clueService.getById (clueId);

        String imgsDir = clue.getImgsDir ();
        clue.setImgsDir (imgsDir.replaceAll (imgUrl + "::", ""));
        boolean update = clueService.updateById (clue);

        return update ? Result.success ("图片删除成功") : Result.fail ("图片删除失败");
    }

    @PostMapping("/getClue")
    public Result getClue(@RequestBody Map<String, Object> params) {
        long clueId = Long.parseLong (String.valueOf (params.get ("clueId")));
        Clue clue = clueService.getById (clueId);
        if (!StringUtils.isEmpty (clue.getImgsDir ())) {
            clue.setImgs (clue.getImgsDir ().split ("::"));
        }
        setUserInfo (clue);
        setTitleInfo (clue);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("clue", clue);
        return Result.success ("获取成功", map);
    }

    @PostMapping("/success")
    public Result finish(@RequestBody Map<String, Object> params) {
        long clueId = Long.parseLong (String.valueOf (params.get ("clueId")));
        Clue clue = clueService.getById (clueId);
        Title title = titleService.getById (clue.getTitleId ());

        if (clue.getIsSuccess () == 1) {
            return Result.fail (555, "请勿重复同意线索");
        }

        clue.setIsSuccess (1);
        boolean update1 = clueService.updateById (clue);

        return update1 ? Result.success ("更新成功") : Result.fail ("更新失败");
    }

    @PostMapping("/getAllUserClues")
    public Result getAllUserClues(@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", userId)) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        List<Clue> clueList = clueService.getCluesByUserId (userId);

        for (Clue clue : clueList) {
            if (!StringUtils.isEmpty (clue.getImgsDir ())) {
                clue.setImgs (clue.getImgsDir ().split ("::"));
            }
            setUserInfo (clue);
            setTitleInfo (clue);
        }

        PageUtils cluePage = new PageUtils (clueList, clueList.size (), 5, 0);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("page", cluePage);
        return Result.success ("获取成功", map);
    }

    @PostMapping("/getAllTitleClues")
    public Result getAllTitleClues(@RequestBody Map<String, Object> params) {
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", titleId)) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        List<Clue> clueList = clueService.getCluesByTitleId (titleId);

        for (Clue clue : clueList) {
            if (!StringUtils.isEmpty (clue.getImgsDir ())) {
                clue.setImgs (clue.getImgsDir ().split ("::"));
            }
            setUserInfo (clue);
            setTitleInfo (clue);
        }

        PageUtils cluePage = new PageUtils (clueList, clueList.size (), 5, 0);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("page", cluePage);
        return Result.success ("获取成功", map);
    }

    @Async
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendClueMailBox(ClueMailbox mailbox) {
        boolean save = clueMailboxService.save (mailbox);

        if (!save) {
            log.warn ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }

        clueMailboxService.sendMailbox (mailbox);
    }


    private void setUserInfo(Clue clue) {
        UserInfo info = userInfoService.getUserInfoById (clue.getUserId ());
        User user = userService.getById (clue.getUserId ());

        clue.setUserAvatar (info.getUserAvatar ());
        clue.setUserName (user.getUserName ());
    }

    private void setTitleInfo (Clue clue) {
        Title title = titleService.getById (clue.getTitleId ());
        User author = userService.getById (title.getUserId ());

        clue.setTitleAuthorId (title.getUserId ());
        clue.setTitleAuthorName (author.getUserName ());
        clue.setTitleName (title.getTitleName ());
    }
}
