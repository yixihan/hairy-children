package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.PhotoProperties;
import com.wq.common.pojo.Result;
import com.wq.pojo.Title;
import com.wq.service.TitleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
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
@RequestMapping("/title")
public class TitleController {

    @Resource
    private TitleService titleService;

    @Resource
    private PhotoProperties photoProperties;

    @PostMapping("/createTitle")
    public Result createTitle (Title title) {

        Boolean creat = titleService.createTitle (title);

        if (creat) {
            QueryWrapper<Title> wrapper = new QueryWrapper<> ();
            wrapper.eq ("user_id", title.getUserId ())
                    .eq ("title_type", title.getTitleType ())
                    .eq ("title_name", title.getTitleName ());

            Title one = titleService.getOne (wrapper);

            Map<String, Object> map = new HashMap<> (16);
            map.put("titleId", one.getTitleId ());
            return Result.success("创建成功", map);
        }

        return Result.fail ("创建失败, 请重新尝试");
    }

    @PostMapping("/updateTitle")
    public Result updateTitle (Title title) {
        boolean update = titleService.updateById (title);

        return update ? Result.success("更新文章成功") : Result.fail("更新文章失败");
    }

    @PostMapping("/deleteTitle")
    public Result deleteTitle (Long titleId) {
        boolean remove = titleService.removeById (titleId);

        return remove ? Result.success("删除文章成功") : Result.fail("删除文章失败");
    }

    @PostMapping("/updateImg")
    public Result updateImg (Long titleId, @Param("file") MultipartFile img) {
        String titleImg = titleService.uploadTitleImg (titleId, img);

        Map<String, Object> map = new HashMap<>(16);
        map.put("titleImg", photoProperties.getUrlPaths () + titleImg);
        return Result.success(map);
    }

    @PostMapping("/getTitle")
    public Result getMd (Long titleId) {

        Title title = titleService.getById (titleId);

        Map<String, Object> map = new HashMap<>(16);
        map.put("title", title);
        return Result.success("获取文章成功", map);
    }

    @PostMapping("/finish")
    public Result finish (Long titleId) {
        Title title = titleService.getById (titleId);
        title.setIsFinish (2);
        boolean update = titleService.updateById (title);

        return update ? Result.success ("更新成功") : Result.fail ("更新失败");
    }

    @PostMapping("/getAllUserTitles")
    public Result getAllUserTitles (Long userId) {
        List<Title> titleList = titleService.getTitleByUserId (userId);

        Map<String, Object> map = new HashMap<>(16);
        map.put("titleList", titleList);
        return Result.success("获取文章成功", map);
    }

    @PostMapping("/selectTitle")
    public Result selectTitle (String titleName) {
        QueryWrapper<Title> wrapper = new QueryWrapper<> ();
        wrapper.like ("title_name", titleName);

        Map<String, Object> map = titleService.getMap (wrapper);

        return Result.success (map);
    }

    /**
     * 查询文章
     * @param titleType 文章类型, 1 : 领养, 2 : 寻宠
     * @param timeLimit 实现限制, 1 : 今日, 2 : 本周, 3 : 半年内, 4 : 不限
     * @param city 城市, 为空 ("") 则是不限
     * @param isFinish 完成状态, 1 : 未完成, 2 : 完成, 3 : 不限
     * @param time 是否按时间排序
     * @param like 是否按点赞排序
     * @param reply 是否按回复数排序
     * @param collection 是否按收藏数排序
     */
    @PostMapping("/getAllTitles")
    public Result getAllTitles (Integer titleType, Integer timeLimit, String city, Integer isFinish,
                                Boolean time, Boolean like, Boolean reply, Boolean collection ) {

        QueryWrapper<Title> wrapper = new QueryWrapper<> ();
        // 文章类型限制
        wrapper.eq ("title_type", titleType);

        // 城市限制
        if (! "".equals (city)) {
            wrapper.eq ("user_address", city);
        }

        // 完成状态限制
        if (isFinish != 3) {
            wrapper.eq ("is_finish", isFinish);
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

        // 排序
        titleList.sort ((o1, o2) -> {
            if (like) {
                return Integer.compare (o1.getLikeCount (), o2.getLikeCount ());
            } else if (time) {
                return Long.compare (o1.getGmtCreate ().getTime (), o2.getGmtCreate ().getTime ());
            } else if (reply) {
                return Integer.compare (o1.getCommentCount (), o2.getCommentCount ());
            } else if (collection) {
                return Integer.compare (o1.getCollectionCount (), o2.getCollectionCount ());
            } else {
                int val1 = o1.getLikeCount () + o1.getCommentCount () + o1.getCollectionCount ();
                int val2 = o2.getLikeCount () + o2.getCommentCount () + o2.getCollectionCount ();
                return Integer.compare (val1, val2);
            }
        });

        Map<String, Object> map = new HashMap<>(16);
        map.put ("titleList", titleList);
        return Result.success (map);
    }
}
