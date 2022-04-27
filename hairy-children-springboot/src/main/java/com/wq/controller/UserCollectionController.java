package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.pojo.*;
import com.wq.service.*;
import com.wq.util.PageUtils;
import com.wq.util.shiro.ShiroUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@RestController
@RequestMapping("/collection")
public class UserCollectionController {

    @Resource
    private UserCollectionService userCollectionService;

    @Resource
    private CollectionTitleService collectionTitleService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private TitleService titleService;

    @PostMapping("/createFavorites")
    public Result createFavorites(@RequestBody UserCollection userCollection) {
        userCollection.setUserId (ShiroUtils.getUserId ());
        boolean save = userCollectionService.save (userCollection);

        return save ? Result.success ("创建收藏夹成功 !") : Result.fail ("创建收藏夹失败");
    }

    @PostMapping("/updateFavorites")
    public Result updateFavorites(@RequestBody UserCollection userCollection) {
        boolean update = userCollectionService.updateById (userCollection);

        return update ? Result.success ("修改收藏夹成功 !") : Result.fail ("修改收藏夹失败");
    }

    @PostMapping("/deleteFavorites")
    public Result deleteFavorites(@RequestBody Map<String, Object> params) {
        long userCollectionId = Long.parseLong (String.valueOf (params.get ("userCollectionId")));

        boolean remove = userCollectionService.removeById (userCollectionId);

        return remove ? Result.success ("删除收藏夹成功 !") : Result.fail ("删除收藏夹失败");
    }

    @PostMapping("/getAllFavorites")
    public Result getAllFavorites(@RequestBody Map<String, Object> params) {

        long userId = Long.parseLong (String.valueOf (params.get ("userId")));

        List<UserCollection> userCollectionList = userCollectionService.getAllCollectionById (userId);

        PageUtils userCollectionPage = new PageUtils (userCollectionList, userCollectionList.size (), 5, 0);

        HashMap<String, Object> map = new HashMap<> (16);
        map.put ("page", userCollectionPage);
        return Result.success (map);
    }

    @PostMapping("/addCollection")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result addCollection(@RequestBody CollectionTitle collectionTitle) {
        Long collectionId = collectionTitle.getCollectionId ();
        Long titleId = collectionTitle.getTitleId ();

        if (titleService.count (new QueryWrapper<Title> ().eq ("title_id", titleId)) <= 0) {
            return Result.fail (555, "没有此文章");
        }

        QueryWrapper<CollectionTitle> wrapper = new QueryWrapper<> ();
        wrapper.eq ("collection_id", collectionId).eq ("title_id", titleId);
        CollectionTitle one = collectionTitleService.getOne (wrapper);

        if (one != null) {
            return Result.fail (555, "该文章已被收藏");
        }

        UserCollection userCollection = userCollectionService.getById (collectionId);
        userCollection.setCollectionCount (userCollection.getCollectionCount () + 1);
        Title title = titleService.getById (titleId);
        title.setCollectionCount (title.getCollectionCount () + 1);
        boolean update1 = userCollectionService.updateById (userCollection);
        boolean save = collectionTitleService.save (collectionTitle);
        boolean update2 = titleService.updateById (title);

        return save && update1 && update2 ? Result.success ("收藏成功 !") : Result.fail ("收藏失败");
    }

    @PostMapping("/deleteCollection")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result deleteCollection(@RequestBody Map<String, Object> params) {
        long collectionTitleId = Long.parseLong (String.valueOf (params.get ("collectionTitleId")));

        CollectionTitle collectionTitle = collectionTitleService.getById (collectionTitleId);

        if (collectionTitle == null) {
            return Result.fail (555, "取消收藏失败, 改文章未被收藏");
        }

        UserCollection userCollection = userCollectionService.getById (collectionTitle.getCollectionId ());
        userCollection.setCollectionCount (userCollection.getCollectionCount () > 0 ? userCollection.getCollectionCount () - 1 : 0);
        Title title = titleService.getById (collectionTitle.getTitleId ());
        title.setCollectionCount (title.getCollectionCount () > 0 ? title.getCollectionCount () - 1 : 0);
        boolean update1 = userCollectionService.updateById (userCollection);
        boolean remove = collectionTitleService.removeById (collectionTitleId);
        boolean update2 = titleService.updateById (title);

        return update1 && update2 && remove ? Result.success ("取消收藏成功 !") : Result.fail ("取消收藏失败");
    }

    @PostMapping("/getAllCollections")
    public Result getAllCollections(@RequestBody Map<String, Object> params) {
        long userCollectionId = Long.parseLong (String.valueOf (params.get ("userCollectionId")));

        List<CollectionTitle> collectionTitleList = collectionTitleService.
                getAllCollectionTitle (userCollectionId);

        List<Title> listList = new ArrayList<>();

        for (CollectionTitle collectionTitle : collectionTitleList) {
            Title title = titleService.getById (collectionTitle.getTitleId ());
            setUserInfo (title);
            listList.add(title);
        }

        PageUtils titlePage = new PageUtils (listList, listList.size (), 5, 0);

        HashMap<String, Object> map = new HashMap<> (16);
        map.put ("page", titlePage);
        return Result.success (map);
    }

    private void setUserInfo (Title title) {
        UserInfo info = userInfoService.getUserInfoById (title.getUserId ());
        User user = userService.getById (title.getUserId ());

        title.setUserAvatar (info.getUserAvatar ());
        title.setUserName (user.getUserName ());
    }

}
