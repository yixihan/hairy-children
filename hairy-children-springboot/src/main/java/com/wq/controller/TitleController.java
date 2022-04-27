package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.PhotoProperties;
import com.wq.common.pojo.Result;
import com.wq.pojo.Title;
import com.wq.pojo.TitleLikeMailbox;
import com.wq.pojo.User;
import com.wq.pojo.UserInfo;
import com.wq.service.TitleService;
import com.wq.service.UserInfoService;
import com.wq.service.UserService;
import com.wq.service.message.TitleLikeMailboxService;
import com.wq.service.redis.RedisService;
import com.wq.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Slf4j
@RestController
@RequestMapping("/title")
public class TitleController {

    @Resource
    private TitleService titleService;

    @Resource
    private TitleLikeMailboxService titleLikeMailboxService;

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private PhotoProperties photoProperties;

    /**
     * title_like:userId:titleId
     */
    private static final String USER_TITLE_LIKE_KEY = "title_like:%s:%s";

    @PostMapping("/createTitle")
    public Result createTitle(@RequestBody Title title) {

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", title.getUserId ())) <= 0) {
            return Result.fail (555, "没有此用户");
        }

        title.setGmtCreate (new Date ());
        Boolean creat = titleService.createTitle (title);

        if (creat) {
            QueryWrapper<Title> wrapper = new QueryWrapper<> ();
            Map<String, Object> columns = new HashMap<> (16);
            columns.put ("user_id", title.getUserId ());
            columns.put ("title_type", title.getTitleType ());
            columns.put ("title_name", title.getTitleName ());
            columns.put ("gmt_create", title.getGmtCreate ());
            wrapper.allEq (true, columns, true).select ("title_id");

            Title one = titleService.getOne (wrapper);

            Map<String, Object> map = new HashMap<> (16);
            map.put ("titleId", one.getTitleId ());
            return Result.success ("创建成功", map);
        }

        return Result.fail ("创建失败, 请重新尝试");
    }

    @PostMapping("/uploadImg/{titleId}")
    public Result uploadImg (@RequestParam("img") MultipartFile img, @PathVariable Long titleId) {
        String titleImg = titleService.uploadTitleImg (titleId, img);
        Title title = titleService.getById (titleId);
        title.setTitleImg (titleImg);
        boolean update = titleService.updateById (title);
        return update ? Result.success ("文章预览图设置成功") : Result.fail ("文章预览图设置失败");
    }

    @PostMapping("/updateTitle")
    public Result updateTitle(@RequestBody Title title) {
        boolean update = titleService.updateById (title);

        return update ? Result.success ("更新文章成功") : Result.fail ("更新文章失败");
    }

    @PostMapping("/deleteTitle")
    public Result deleteTitle(@RequestBody Map<String, Object> params) {

        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        boolean remove = titleService.removeById (titleId);

        return remove ? Result.success ("删除文章成功") : Result.fail ("删除文章失败");
    }

    @PostMapping("/updateImg/{titleId}")
    public Result updateImg(@RequestParam("img") MultipartFile img, @PathVariable Long titleId) {
        String titleImg = titleService.uploadTitleImg (titleId, img);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("imgDir", titleImg);
        return Result.success (map);

    }

    @PostMapping("/getTitle")
    public Result getTitle(@RequestBody Map<String, Object> params) {
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        Title title = titleService.getById (titleId);
        setUserInfo (title);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("title", title);
        return Result.success ("获取文章成功", map);
    }

    @PostMapping("/finish")
    public Result finish(@RequestBody Map<String, Object> params) {
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", titleId)) <= 0) {
            return Result.fail (555, "没有该文章");
        }

        Title title = titleService.getById (titleId);
        title.setIsFinish (1);
        boolean update = titleService.updateById (title);

        return update ? Result.success ("更新成功") : Result.fail ("更新失败");
    }

    @PostMapping("/getAllUserTitles")
    public Result getAllUserTitles(@RequestBody Map<String, Object> params) {

        long userId = Long.parseLong (String.valueOf (params.get ("userId")));

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", userId)) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        List<Title> titleList = titleService.getTitleByUserId (userId);
        for (Title title : titleList) {
            setUserInfo (title);
        }
        PageUtils titlePage = new PageUtils (titleList, titleList.size (), 5, 0);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("page", titlePage);
        return Result.success ("获取文章成功", map);
    }

    @PostMapping("/selectTitle")
    public Result selectTitle(@RequestBody Map<String, Object> params) {

        String titleName = String.valueOf (params.get ("titleName"));

        QueryWrapper<Title> wrapper = new QueryWrapper<> ();
        wrapper.like ("title_name", titleName);

        List<Title> titleList = titleService.list (wrapper);
        for (Title title : titleList) {
            setUserInfo (title);
        }
        PageUtils titlePage = new PageUtils (titleList, titleList.size (), 5, 0);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("page", titlePage);

        return Result.success (map);
    }

    @PostMapping("/getTitleType")
    public Result getTitleType(@RequestBody Map<String, Object> params) {
        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));

        QueryWrapper<Title> wrapper = new QueryWrapper<> ();
        wrapper.eq ("title_id", titleId).select ("title_type");
        Title title = titleService.getOne (wrapper);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("titleType", title.getTitleType ());

        return Result.success (map);
    }

    /**
     * 查询文章
     * <p>
     * titleType  文章类型, 1 : 领养, 2 : 寻宠
     * <p>
     * timeLimit  时间限制, 1 : 今日, 2 : 本周, 3 : 半年内, 4 : 不限
     * <p>
     * city       城市, 为空 ("") 则是不限
     * <p>
     * isFinish   完成状态, 1 : 未完成, 2 : 完成, 3 : 不限
     * <p>
     * time       是否按时间排序
     * <p>
     * like       是否按点赞排序
     * <p>
     * reply      是否按回复数排序
     * <p>
     * collection 是否按收藏数排序
     */
    @PostMapping("/getAllTitles")
    public Result getAllTitles(@RequestBody Map<String, Object> params) {

        String titleName = String.valueOf (params.get ("titleName"));
        int titleType = Integer.parseInt (String.valueOf (params.get ("titleType")));
        int timeLimit = Integer.parseInt (String.valueOf (params.get ("timeLimit")));
        String city = String.valueOf (params.get ("city"));
        int isFinish = Integer.parseInt (String.valueOf (params.get ("isFinish")));

        boolean time = Boolean.parseBoolean (String.valueOf (params.get ("time")));
        boolean like = Boolean.parseBoolean (String.valueOf (params.get ("like")));
        boolean reply = Boolean.parseBoolean (String.valueOf (params.get ("reply")));
        boolean collection = Boolean.parseBoolean (String.valueOf (params.get ("collection")));

        QueryWrapper<Title> wrapper = new QueryWrapper<> ();
        // 文章类型限制
        wrapper.eq ("title_type", titleType).like ("title_name", titleName);

        // 城市限制
        if (!"".equals (city)) {
            wrapper.like ("user_address", city);
        }

        // 完成状态限制
        if (isFinish != 3) {
            wrapper.eq ("is_finish", isFinish).notIn ("is_finish", -1);
        } else {
            wrapper.notIn ("is_finish", -1);
        }

        // 时间限制
        if (timeLimit != 4) {

            LocalDate now = LocalDate.now ();

            if (timeLimit == 1) {
                LocalDate yesterday = now.minusDays (1);
                wrapper.between ("gmt_create", yesterday, now);
            } else if (timeLimit == 2) {
                LocalDate lastWeek = now.minusWeeks (1);
                wrapper.between ("gmt_create", lastWeek, now);
            } else if (timeLimit == 3) {
                LocalDate sixMonth = now.minusMonths (6);
                wrapper.between ("gmt_create", sixMonth, now);
            } else {
                log.warn ("timeLimit 参数错误");
                return Result.fail ("timeLimit 参数错误");
            }
        }

        List<Title> titleList = titleService.list (wrapper);

        for (Title title : titleList) {
            setUserInfo (title);
        }

        // 排序
        titleList.sort ((o1, o2) -> {
            if (like) {
                return Integer.compare (o2.getLikeCount (), o1.getLikeCount ());
            } else if (time) {
                return Long.compare (o2.getGmtCreate ().getTime (), o1.getGmtCreate ().getTime ());
            } else if (reply) {
                return Integer.compare (o2.getCommentCount (), o1.getCommentCount ());
            } else if (collection) {
                return Integer.compare (o2.getCollectionCount (), o1.getCollectionCount ());
            } else {
                int val1 = o1.getLikeCount () + o1.getCommentCount () + o1.getCollectionCount ();
                int val2 = o2.getLikeCount () + o2.getCommentCount () + o2.getCollectionCount ();
                return Integer.compare (val2, val1);
            }
        });

        PageUtils titlePage = new PageUtils (titleList, titleList.size (), 5, 0);
        Map<String, Object> map = new HashMap<> (16);
        map.put ("page", titlePage);
        return Result.success (map);
    }

    @PostMapping("/like")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result like(@RequestBody Map<String, Object> params) {

        long titleId = Long.parseLong (String.valueOf (params.get ("titleId")));
        long userId = Long.parseLong (String.valueOf (params.get ("userId")));

        Title title = titleService.getById (titleId);

        if (title == null) {
            log.error ("无此文章");
            throw new RuntimeException ("无此文章");
        }

        if (userService.count (new QueryWrapper<User> ().eq ("user_id", userId)) <= 0) {
            return Result.fail (555, "没有该用户");
        }

        // 点赞
        String key = String.format (USER_TITLE_LIKE_KEY, title.getUserId (), titleId);
        Boolean bit = redisService.getBit (key, userId);
        if (bit) {
            log.warn ("已点赞, 请勿重复点赞");
            return Result.fail (555, "已点赞, 请勿重复点赞");
        }
        Boolean setBit = redisService.setBit (key, userId, true);
        if (setBit) {
            log.error ("点赞失败");
            throw new RuntimeException ("点赞失败");
        }

        title.setLikeCount (Math.toIntExact (redisService.getLikeValueAll (key)));
        boolean update = titleService.updateById (title);

        if (!update) {
            log.error ("文章更新失败");
            throw new RuntimeException ("点赞失败");
        }

        // 发送消息
        TitleLikeMailbox titleLikeMailbox = new TitleLikeMailbox ();
        titleLikeMailbox.setTitleId (titleId);
        titleLikeMailbox.setSendUserId (userId);
        titleLikeMailbox.setReceiveUserId (title.getUserId ());

        boolean save = titleLikeMailboxService.save (titleLikeMailbox);

        if (!save) {
            log.error ("消息持久化失败");
            throw new RuntimeException ("点赞失败");
        }
        QueryWrapper<TitleLikeMailbox> wrapper = new QueryWrapper<> ();
        wrapper.eq ("title_id", titleLikeMailbox.getTitleId ()).eq ("send_user_id", titleLikeMailbox.getSendUserId ()).like ("receive_user_id", titleLikeMailbox.getReceiveUserId ());

        TitleLikeMailbox one = titleLikeMailboxService.getOne (wrapper);

        if (one == null) {
            log.error ("无此消息");
            throw new RuntimeException ("点赞失败");
        }

        titleLikeMailboxService.sendMailbox (one);

        return Result.success ("点赞成功");
    }

    private void setUserInfo (Title title) {
        UserInfo info = userInfoService.getUserInfoById (title.getUserId ());
        User user = userService.getById (title.getUserId ());

        title.setUserAvatar (info.getUserAvatar ());
        title.setUserName (user.getUserName ());
    }
}
