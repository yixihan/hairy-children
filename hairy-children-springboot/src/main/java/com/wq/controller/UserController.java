package com.wq.controller;


import com.wq.common.pojo.Result;
import com.wq.common.service.CodeService;
import com.wq.pojo.User;
import com.wq.pojo.UserInfo;
import com.wq.service.UserInfoService;
import com.wq.service.UserService;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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
@RequestMapping("/user")
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
    public Result bindEmail (String email) {
        // 判断该邮箱是否已被绑定
        if (! codeService.verifyUserEmail (email)) {
            return Result.fail ("该邮箱已被绑定");
        }

        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否已绑定邮箱
        if (! StringUtils.isEmpty (user.getUserEmail ())) {
            return Result.fail ("请先解绑邮箱后再进行绑定操作");
        }

        // 绑定邮箱
        user.setUserEmail (email);
        boolean update = userService.updateById (user);

        return update ? Result.success ("绑定成功 !") : Result.fail ("绑定失败 !");
    }

    @PostMapping("/unboundEmail")
    public Result unboundEmail () {
        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否未绑定邮箱
        if (StringUtils.isEmpty (user.getUserEmail ())) {
            return Result.fail ("请先绑定邮箱后再进行解绑操作");
        }

        // 解绑邮箱
        user.setUserEmail ("");
        boolean update = userService.updateById (user);

        return update ? Result.success ("解绑成功 !") : Result.fail ("解绑失败 !");
    }

    @PostMapping("/bindPhone")
    public Result bindPhone (String phone) {
        // 判断该电话是否已被绑定
        if (! codeService.verifyUserPhone (phone)) {
            return Result.fail ("该电话已被绑定");
        }

        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否已绑定电话
        if (! StringUtils.isEmpty (user.getUserPhone ())) {
            return Result.fail ("请先解绑电话后再进行绑定操作");
        }

        // 绑定电话
        user.setUserPhone (phone);
        boolean update = userService.updateById (user);

        return update ? Result.success ("绑定成功 !") : Result.fail ("绑定失败 !");
    }

    @PostMapping("/unboundPhone")
    public Result unboundPhone () {
        User user = userService.getById (ShiroUtils.getUserId ());

        // 判断该用户是否未绑定电话
        if (StringUtils.isEmpty (user.getUserPhone ())) {
            return Result.fail ("请先绑定电话后再进行解绑操作");
        }

        // 解绑电话
        user.setUserPhone ("");
        boolean update = userService.updateById (user);

        return update ? Result.success ("解绑成功 !") : Result.fail ("解绑失败 !");
    }

    @PostMapping("/getUserInfo")
    public Result getUserInfo () {
        Long userId = ShiroUtils.getUserId ();

        User user = userService.getById (userId);
        UserInfo info = userInfoService.getUserInfoById (userId);

        Map<String, Object> userInfo = new HashMap<>(16);
        userInfo.put ("userId", user.getUserId ());
        userInfo.put ("userName", user.getUserName ());
        userInfo.put ("userPhone", user.getUserPhone ());
        userInfo.put ("userEmail", user.getUserEmail ());
        userInfo.put ("userRealName", info.getUserRealName ());
        userInfo.put ("userIdentityCard", info.getUserIdentityCard ());
        userInfo.put ("userAddress", info.getUserAddress ());
        userInfo.put ("AddressShow", info.getAddressShow ());
        userInfo.put ("userGender", info.getUserGender ());
        userInfo.put ("GenderShow", info.getGenderShow ());
        userInfo.put ("userBirth", info.getUserBirth ());
        userInfo.put ("BirthShow", info.getBirthShow ());
        userInfo.put ("userAvatar", info.getUserAvatar ());
        userInfo.put ("userAutograph", info.getUserAutograph ());
        userInfo.put ("userPetCond", info.getUserPetCond ());

        return Result.success ("用户信息获取成功", userInfo);
    }

    @PostMapping("/logout")
    public Result logout(String token) {
        Subject subject = ShiroUtils.getSubject ();

        subject.logout ();

        return Result.success ();
    }
}
