package com.wq.controller;


import com.wq.common.pojo.Result;
import com.wq.common.service.CodeService;
import com.wq.pojo.User;
import com.wq.pojo.UserInfo;
import com.wq.service.UserInfoService;
import com.wq.service.UserService;
import com.wq.util.VerifyUtils;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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
@Slf4j
@RequestMapping("/user")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private CodeService codeService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/bindEmail")
    public Result bindEmail(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String email = String.valueOf (params.get ("email"));
        log.info ("email : " + email);
        // 判断邮箱格式是否正确
        if (!VerifyUtils.isEmail (email)) {
            return Result.fail (555, "邮箱格式错误");
        }

        // 判断该邮箱是否已被绑定
        if (!codeService.verifyUserEmail (email)) {
            return Result.fail (555, "该邮箱已被绑定");
        }

        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否已绑定邮箱
        if (!StringUtils.isEmpty (user.getUserEmail ())) {
            return Result.fail (555, "请先解绑邮箱后再进行绑定操作");
        }

        // 绑定邮箱
        user.setUserEmail (email);
        boolean update = userService.updateById (user);

        return update ? Result.success ("绑定成功 !") : Result.fail ("绑定失败 !");
    }

    @PostMapping("/unboundEmail")
    public Result unboundEmail() {
        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否未绑定邮箱
        if (StringUtils.isEmpty (user.getUserEmail ())) {
            return Result.fail (555, "请先绑定邮箱后再进行解绑操作");
        }

        // 解绑邮箱
        user.setUserEmail ("");
        boolean update = userService.updateById (user);

        return update ? Result.success ("解绑成功 !") : Result.fail ("解绑失败 !");
    }

    @PostMapping("/bindPhone")
    public Result bindPhone(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String phone = String.valueOf (params.get ("phone"));
        log.info ("phone : " + phone);

        // 判断电话格式是否正确
        if (!VerifyUtils.isModel (phone)) {
            return Result.fail (555, "电话格式错误");
        }

        // 判断该电话是否已被绑定
        if (!codeService.verifyUserPhone (phone)) {
            return Result.fail ("该电话已被绑定");
        }

        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否已绑定电话
        if (!StringUtils.isEmpty (user.getUserPhone ())) {
            return Result.fail ("请先解绑电话后再进行绑定操作");
        }

        // 绑定电话
        user.setUserPhone (phone);
        boolean update = userService.updateById (user);

        return update ? Result.success ("绑定成功 !") : Result.fail ("绑定失败 !");
    }

    @PostMapping("/unboundPhone")
    public Result unboundPhone() {
        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否未绑定电话
        if (StringUtils.isEmpty (user.getUserPhone ())) {
            return Result.fail (555, "请先绑定电话后再进行解绑操作");
        }

        // 解绑电话
        user.setUserPhone ("");
        boolean update = userService.updateById (user);

        return update ? Result.success ("解绑成功 !") : Result.fail ("解绑失败 !");
    }

    @PostMapping("/getUserInfo")
    public Result getUserInfo(@RequestBody Map<String, Object> params) {

        long userId = Long.parseLong (String.valueOf (params.get ("userId")));


        User user = userService.getById (userId);
        if (user == null) {
            return Result.fail (555, "无此用户信息");
        }
        UserInfo info = userInfoService.getUserInfoById (userId);

        Map<String, Object> userInfo = new HashMap<> (16);
        userInfo.put ("userId", user.getUserId ());
        userInfo.put ("userName", user.getUserName ());
        userInfo.put ("userAvatar", info.getUserAvatar ());
        userInfo.put ("userAutograph", info.getUserAutograph ());
        userInfo.put ("userPetCond", info.getUserPetCond ());
        if (ShiroUtils.getUserId () == userId) {
            userInfo.put ("userPhone", user.getUserPhone ());
            userInfo.put ("userEmail", user.getUserEmail ());
            userInfo.put ("userRealName", info.getUserRealName ());
            userInfo.put ("userIdentityCard", info.getUserIdentityCard ());
            userInfo.put ("userAddress", info.getUserAddress ());
            userInfo.put ("userGender", info.getUserGender ());
            userInfo.put ("userBirth", info.getUserBirth ());
            userInfo.put ("birthShow", info.getBirthShow ());
            userInfo.put ("addressShow", info.getAddressShow ());
            userInfo.put ("genderShow", info.getGenderShow ());
        } else {
            if (info.getAddressShow () == 1) {
                userInfo.put ("userAddress", info.getUserAddress ());
            }
            if (info.getGenderShow () == 1) {
                userInfo.put ("userGender", info.getUserGender ());
            }
            if (info.getBirthShow () == 1) {
                userInfo.put ("userBirth", info.getUserBirth ());
            }
        }


        return Result.success ("用户信息获取成功", userInfo);
    }

}
