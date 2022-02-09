package com.wq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.pojo.CollectionTitle;
import com.wq.pojo.UserCollection;
import com.wq.service.CollectionTitleService;
import com.wq.service.UserCollectionService;
import com.wq.util.shiro.ShiroUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
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

    @PostMapping("/createFavorites")
    public Result createFavorites (UserCollection userCollection) {
        userCollection.setUserId (ShiroUtils.getUserId ());
        boolean save = userCollectionService.save (userCollection);

        return save ? Result.success ("创建收藏夹成功 !") : Result.fail ("创建收藏夹失败");
    }

    @PostMapping("/updateFavorites")
    public Result updateFavorites (UserCollection userCollection) {
        boolean update = userCollectionService.updateById (userCollection);

        return update ? Result.success ("修改收藏夹成功 !") : Result.fail ("修改收藏夹失败");
    }

    @PostMapping("/deleteFavorites")
    public Result deleteFavorites (Long userCollectionId) {
        boolean remove = userCollectionService.removeById (userCollectionId);

        return remove ? Result.success ("删除收藏夹成功 !") : Result.fail ("删除收藏夹失败");
    }

    @PostMapping("/getAllFavorites")
    public Result getAllFavorites () {
        List<UserCollection> userCollectionList = userCollectionService.
                getAllCollectionById (ShiroUtils.getUserId ());

        HashMap<String, Object> map = new HashMap<> (16);
        map.put ("userCollectionList", userCollectionList);
        return Result.success (map);
    }

    @PostMapping("/addCollection")
    public Result addCollection (CollectionTitle collectionTitle) {
        Long collectionId = collectionTitle.getCollectionId ();
        Long titleId = collectionTitle.getTitleId ();

        QueryWrapper<CollectionTitle> wrapper = new QueryWrapper<> ();
        wrapper.eq ("collection_id", collectionId)
                .eq ("title_id", titleId);
        CollectionTitle one = collectionTitleService.getOne (wrapper);

        if (one != null) {
            return Result.fail ("该文章已被收藏");
        }

        UserCollection userCollection = userCollectionService.getById (collectionId);
        userCollection.setCollectionCount (userCollection.getCollectionCount () + 1);
        boolean update = userCollectionService.updateById (userCollection);
        boolean save = collectionTitleService.save (collectionTitle);

        return save && update? Result.success ("收藏成功 !") : Result.fail ("收藏失败");
    }

    @PostMapping("/deleteCollection")
    public Result deleteCollection (Long collectionTitleId) {
        CollectionTitle collectionTitle = collectionTitleService.getById (collectionTitleId);

        if (collectionTitle == null) {
            return Result.fail ("取消收藏失败, 改文章未被收藏");
        }

        UserCollection userCollection = userCollectionService.getById (collectionTitle.getCollectionId ());
        userCollection.setCollectionCount (userCollection.getCollectionCount () - 1);
        boolean update = userCollectionService.updateById (userCollection);
        boolean remove = collectionTitleService.removeById (collectionTitleId);

        return update && remove ? Result.success ("取消收藏成功 !") : Result.fail ("取消收藏失败");
    }

    @PostMapping("/getAllCollections")
    public Result getAllCollections (Long userCollectionId) {
        List<CollectionTitle> collectionTitleList = collectionTitleService.
                getAllCollectionTitle (userCollectionId);

        HashMap<String, Object> map = new HashMap<> (16);
        map.put ("collectionTitleList", collectionTitleList);
        return Result.success (map);
    }

}
