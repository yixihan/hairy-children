package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.PhotoProperties;
import com.wq.common.pojo.Result;
import com.wq.pojo.Clue;
import com.wq.pojo.ClueMailbox;
import com.wq.pojo.Title;
import com.wq.pojo.User;
import com.wq.service.ClueService;
import com.wq.service.TitleService;
import com.wq.service.UserService;
import com.wq.service.message.ClueMailboxService;
import com.wq.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private ClueMailboxService clueMailboxService;

    @Resource
    private PhotoProperties photoProperties;

    @PostMapping("/creatClue")
    public Result createClue (@RequestBody Clue clue) {

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", clue.getUserId ())) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", clue.getTitleId ())) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        if (! clueService.isExists (clue.getTitleId (), clue.getUserId ())) {
            return Result.fail ("请勿重复创建线索贴");
        }
        Boolean create = clueService.createClue (clue);

        if (create) {
            QueryWrapper<Clue> wrapper = new QueryWrapper<> ();
            wrapper.eq ("user_id", clue.getUserId ())
                    .eq ("title_id", clue.getTitleId ());

            Clue one = clueService.getOne (wrapper);

            Map<String, Object> map = new HashMap<> (16);
            map.put("clueId", one.getClueId ());
            return Result.success("创建成功", map);
        }

        return Result.fail ("创建失败");
    }

    @PostMapping("/updateClue")
    public Result updateClue (@RequestBody Clue clue) {
        boolean update = clueService.updateById (clue);
        clue = clueService.getById (clue.getClueId ());

        Title title = titleService.getById (clue.getTitleId ());

        ClueMailbox mailbox = new ClueMailbox ();
        mailbox.setClueId (clue.getClueId ());
        mailbox.setTitleId (clue.getTitleId ());
        mailbox.setSendUserId (clue.getUserId ());
        mailbox.setReceiveUserId (title.getUserId ());

        sendClueMailBox (mailbox);

        return update ? Result.success("更新成功") : Result.fail("更新失败");
    }

    @PostMapping("/deleteClue")
    public Result deleteClue (@RequestBody Map<String, Object> params) {
        long clueId = Long.parseLong (String.valueOf (params.get ("clueId")));
        boolean remove = clueService.removeById (clueId);

        return remove ? Result.success("删除成功") : Result.fail("删除失败");
    }

    @PostMapping("/updateImg/{clueId}")
    public Result updateImg (@PathVariable Long clueId, @RequestParam("imgs") MultipartFile[] imgs) {
        StringBuilder clueImgs = new StringBuilder();

        for (MultipartFile img : imgs) {
            String clueImg = clueService.uploadImg (clueId, img);
            clueImgs.append (clueImg).append ("::");
        }
        clueImgs.deleteCharAt (clueImgs.length () - 1);

        Clue clue = clueService.getById (clueId);
        clue.setImgsDir (clueImgs.toString ());
        clueService.updateById (clue);

        return Result.success("图片上传成功");
    }

    @PostMapping("/getClue")
    public Result getClue (@RequestBody Map<String, Object> params) {
        long clueId = Long.parseLong (String.valueOf (params.get ("clueId")));
        Clue clue = clueService.getById (clueId);
        clue.setImgs (clue.getImgsDir ().split ("::"));

        Map<String, Object> map = new HashMap<>(16);
        map.put("clue", clue);
        return Result.success("获取成功", map);
    }

    @PostMapping("/success")
    public Result finish (@RequestBody Map<String, Object> params) {
        long clueId = Long.parseLong (String.valueOf (params.get ("clueId")));
        Clue clue = clueService.getById (clueId);
        Title title = titleService.getById (clue.getTitleId ());

        if (clue.getIsSuccess () == 1) {
            return Result.fail (555, "请勿重复同意线索");
        }

        clue.setIsSuccess (1);
        title.setIsFinish (1);
        boolean update1 = clueService.updateById (clue);
        boolean update2 = titleService.updateById (title);

        return update1 && update2 ? Result.success ("更新成功") : Result.fail ("更新失败");
    }

    @PostMapping("/getAllUserClues")
    public Result getAllUserClues (@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", userId)) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        List<Clue> clueList = clueService.getCluesByUserId (userId);
        PageUtils cluePage = new PageUtils (clueList, clueList.size (), 10, 0);

        Map<String, Object> map = new HashMap<> (16);
        map.put("page", cluePage);
        return Result.success("获取成功", map);
    }

    @PostMapping("/getAllTitleClues")
    public Result getAllTitleClues (@RequestBody Map<String, Object> params) {
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", titleId)) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        List<Clue> clueList = clueService.getCluesByTitleId (titleId);
        PageUtils cluePage = new PageUtils (clueList, clueList.size (), 10, 0);

        Map<String, Object> map = new HashMap<>(16);
        map.put("page", cluePage);
        return Result.success("获取成功", map);
    }

    @Async
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendClueMailBox(ClueMailbox mailbox) {
        boolean save = clueMailboxService.save (mailbox);

        if (! save) {
            log.warn ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }

        clueMailboxService.sendMailbox (mailbox);
    }

}
