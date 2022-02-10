package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.PhotoProperties;
import com.wq.common.pojo.Result;
import com.wq.pojo.Clue;
import com.wq.pojo.Title;
import com.wq.service.ClueService;
import com.wq.service.TitleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private PhotoProperties photoProperties;

    @PostMapping("/creatClue")
    public Result createClue (Clue clue) {

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
    public Result updateClue (Clue clue) {
        boolean update = clueService.updateById (clue);

        return update ? Result.success("更新成功") : Result.fail("更新失败");
    }

    @PostMapping("/deleteClue")
    public Result deleteClue (Long clueId) {
        boolean remove = clueService.removeById (clueId);

        return remove ? Result.success("删除成功") : Result.fail("删除失败");
    }

    @PostMapping("/updateImg")
    public Result updateImg (Long clueId, @Param("file") MultipartFile[] imgs) {
        StringBuilder clueImgs = new StringBuilder(imgs.length * 20);

        for (MultipartFile img : imgs) {
            String clueImg = clueService.uploadImg (clueId, img);
            clueImgs.append (photoProperties.getUrlPaths ()).append (clueImg).append ("-");
        }
        clueImgs.deleteCharAt (clueImgs.length () - 1);

        Clue clue = clueService.getById (clueId);
        clue.setImgsDir (clueImgs.toString ());
        clueService.updateById (clue);

        return Result.success("图片上传成功");
    }

    @PostMapping("/getClue")
    public Result getMd (Long clueId) {

        Clue clue = clueService.getById (clueId);

        Map<String, Object> map = new HashMap<>(16);
        map.put("clue", clue);
        return Result.success("获取成功", map);
    }

    @PostMapping("/success")
    public Result finish (Long clueId, Long titleId) {
        Title title = titleService.getById (titleId);

        if (title.getIsFinish () == 1) {
            return Result.fail ("请勿重复同意申请");
        }

        Clue clue = clueService.getById (clueId);
        clue.setIsSuccess (1);
        title.setIsFinish (1);
        boolean update1 = clueService.updateById (clue);
        boolean update2 = titleService.updateById (title);

        return update1 && update2 ? Result.success ("更新成功") : Result.fail ("更新失败");
    }

    @PostMapping("/getAllUserClues")
    public Result getAllUserClues (Long userId) {
        List<Clue> clueList = clueService.getCluesByUserId (userId);

        Map<String, Object> map = new HashMap<> (16);
        map.put("clueList", clueList);
        return Result.success("获取成功", map);
    }

    @PostMapping("/getAllTitleClues")
    public Result getAllTitleClues (Long titleId) {
        List<Clue> clueList = clueService.getCluesByTitleId (titleId);

        Map<String, Object> map = new HashMap<>(16);
        map.put("clueList", clueList);
        return Result.success("获取成功", map);
    }

}
