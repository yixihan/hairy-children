package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.PhotoProperties;
import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.AdoptService;
import com.wq.service.TitleService;
import com.wq.service.UserInfoService;
import com.wq.service.UserService;
import com.wq.service.message.AdoptMailboxService;
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
@RequestMapping("/adopt")
public class AdoptController {

    @Resource
    private AdoptService adoptService;

    @Resource
    private AdoptMailboxService adoptMailboxService;

    @Resource
    private TitleService titleService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PhotoProperties photoProperties;

    @PostMapping("/creatAdopt")
    public Result createAdopt(@RequestBody Adopt adopt) {

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", adopt.getUserId ())) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", adopt.getTitleId ())) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        if (!adoptService.isExists (adopt.getTitleId (), adopt.getUserId ())) {
            return Result.fail ("请勿重复申请领养贴");
        }

        Boolean create = adoptService.createAdopt (adopt);

        if (create) {
            Map<String, Object> map = new HashMap<> (16);
            map.put ("adoptId", adopt.getAdoptId ());
            return Result.success ("创建成功", map);
        }

        return Result.fail ("创建失败");
    }

    @PostMapping("/updateAdopt")
    public Result updateAdopt(@RequestBody Adopt adopt) {
        boolean update = adoptService.updateById (adopt);
        adopt = adoptService.getById (adopt.getAdoptId ());

        Title title = titleService.getById (adopt.getTitleId ());

        AdoptMailbox mailbox = new AdoptMailbox ();
        mailbox.setAdoptId (adopt.getAdoptId ());
        mailbox.setTitleId (adopt.getTitleId ());
        mailbox.setSendUserId (adopt.getUserId ());
        mailbox.setReceiveUserId (title.getUserId ());

        sendAdoptMailBox (mailbox);

        return update ? Result.success ("更新成功") : Result.fail ("更新失败");
    }

    @PostMapping("/deleteAdopt")
    public Result deleteAdopt(@RequestBody Map<String, Object> params) {
        long adoptId = Long.parseLong (String.valueOf (params.get ("adoptId")));

        boolean remove = adoptService.removeById (adoptId);

        return remove ? Result.success ("删除成功") : Result.fail ("删除失败");
    }

    @PostMapping("/updateImg/{adoptId}")
    public Result updateImg(@PathVariable Long adoptId, @RequestParam("imgs") MultipartFile[] imgs) {
        StringBuilder adoptImgs = new StringBuilder ();
        List<String> imgList = new ArrayList<> ();

        for (MultipartFile img : imgs) {
            String adoptImg = adoptService.uploadImg (adoptId, img);
            imgList.add (adoptImg);
            adoptImgs.append (adoptImg).append ("::");
        }

        Adopt adopt = adoptService.getById (adoptId);
        adopt.setImgsDir (adopt.getImgsDir () + adoptImgs.toString ());
        adoptService.updateById (adopt);
        Map<String, Object> map= new HashMap<> (16);
        map.put ("imgList", imgList);

        return Result.success ("图片上传成功", map);
    }

    @PostMapping("/deleteImg")
    public Result deleteImg(@RequestBody Map<String, Object> params) {
        long adoptId = Long.parseLong (String.valueOf (params.get ("adoptId")));
        String imgUrl = String.valueOf (params.get ("imgUrl"));

        if (adoptService.count (new QueryWrapper<Adopt> ().eq ("adopt_id", adoptId)) <= 0) {
            return Result.fail (555, "没有该领养申请");
        }

        Adopt adopt = adoptService.getById (adoptId);

        String imgsDir = adopt.getImgsDir ();
        adopt.setImgsDir (imgsDir.replaceAll (imgUrl + "::", ""));
        boolean update = adoptService.updateById (adopt);

        return update ? Result.success ("删除成功") : Result.fail ("删除失败");
    }

    @PostMapping("/getAdopt")
    public Result getAdopt(@RequestBody Map<String, Object> params) {
        long adoptId = Long.parseLong (String.valueOf (params.get ("adoptId")));
        Adopt adopt = adoptService.getById (adoptId);

        if (!StringUtils.isEmpty (adopt.getImgsDir ())) {
            adopt.setImgs (adopt.getImgsDir ().split ("::"));
        }
        setUserInfo (adopt);
        setTitleInfo (adopt);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("adopt", adopt);
        return Result.success ("获取成功", map);
    }

    @PostMapping("/success")
    public Result finish(@RequestBody Map<String, Object> params) {
        long adoptId = Long.parseLong (String.valueOf (params.get ("adoptId")));
        boolean isSuccess = Boolean.parseBoolean (String.valueOf (params.get ("isSuccess")));
        Adopt adopt = adoptService.getById (adoptId);

        if (adopt.getIsSuccess () == 1) {
            return Result.fail (555, "请勿重复同意申请");
        }

        adopt.setIsSuccess (isSuccess ? 1 : 2);
        boolean update1 = adoptService.updateById (adopt);
        return update1 ? Result.success ("更新成功") : Result.fail ("更新失败");

    }

    @PostMapping("/getAllUserAdopts")
    public Result getAllUserAdopts(@RequestBody Map<String, Object> params) {
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", userId)) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        List<Adopt> adoptList = adoptService.getAdoptsByUserId (userId);

        for (Adopt adopt : adoptList) {
            if (!StringUtils.isEmpty (adopt.getImgsDir ())) {
                adopt.setImgs (adopt.getImgsDir ().split ("::"));
            }
            setUserInfo (adopt);
            setTitleInfo (adopt);
        }

        PageUtils adoptPage = new PageUtils (adoptList, adoptList.size (), 5, 0);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("page", adoptPage);
        return Result.success ("获取成功", map);
    }

    @PostMapping("/getAllTitleAdopts")
    public Result getAllTitleAdopts(@RequestBody Map<String, Object> params) {
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", titleId)) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        List<Adopt> adoptList = adoptService.getAllAdoptsByTitleId (titleId);

        for (Adopt adopt : adoptList) {
            if (!StringUtils.isEmpty (adopt.getImgsDir ())) {
                adopt.setImgs (adopt.getImgsDir ().split ("::"));
            }
            setUserInfo (adopt);
            setTitleInfo (adopt);

        }

        PageUtils adoptPage = new PageUtils (adoptList, adoptList.size (), 5, 0);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("page", adoptPage);
        return Result.success ("获取成功", map);
    }

    /**
     * 发送消息 - 领养申请
     *
     * @param mailbox
     */
    @Async
    @Transactional(rollbackFor = RuntimeException.class)
    public void sendAdoptMailBox(AdoptMailbox mailbox) {
        boolean save = adoptMailboxService.save (mailbox);

        if (!save) {
            log.warn ("消息持久化失败");
            throw new RuntimeException ("消息持久化失败");
        }

        adoptMailboxService.sendMailbox (mailbox);
    }

    private void setUserInfo(Adopt adopt) {
        UserInfo info = userInfoService.getUserInfoById (adopt.getUserId ());
        User user = userService.getById (adopt.getUserId ());

        adopt.setUserAvatar (info.getUserAvatar ());
        adopt.setUserName (user.getUserName ());
    }

    private void setTitleInfo(Adopt adopt) {
        Title title = titleService.getById (adopt.getTitleId ());
        User author = userService.getById (title.getUserId ());

        adopt.setTitleAuthorId (title.getUserId ());
        adopt.setTitleAuthorName (author.getUserName ());
        adopt.setTitleName (title.getTitleName ());
    }
}
