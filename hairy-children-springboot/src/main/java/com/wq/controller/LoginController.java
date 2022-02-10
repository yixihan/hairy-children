package com.wq.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.common.service.CodeService;
import com.wq.pojo.User;
import com.wq.service.UserService;
import com.wq.util.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-02-07-15:50
 */
@RestController
@Slf4j
@RequestMapping("/v")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private CodeService codeService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过 用户名 密码 登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    @ApiOperation(value = "通过 用户名 密码 登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataTypeClass = String.class, required = true)
    })
    @PostMapping("/login")
    public Result loginByUserName(String userName, String password) {

        // 查询数据库
        User user = userService.getUserByName (userName);

        if (user == null) {
            log.warn ("认证失败 : 用户名不存在 ~");
            return Result.fail ("认证失败 : 用户名不存在 ~");
        }

        // 加密用户输入的密码
        String md5Password = new Md5Hash (password, user.getUserSalt (), 1024).toHex ();

        // 将加密后的密码存入 user
        user.setUserPassword (md5Password);

        // 登录
        try {
            Map<String, Object> token = userService.login (user);
            return Result.success (200, "登录成功 !", token);
        } catch (UnknownAccountException e) {
            // 用户不存在
            log.warn ("认证失败 : 用户名不存在 ~");
            return Result.fail ("认证失败 : 用户名不存在 ~");
        } catch (IncorrectCredentialsException e) {
            //密码错误
            log.warn ("认证失败 : 密码错误 ~");
            return Result.fail ("认证失败 : 密码错误 ~");
        }
    }

    /**
     * 通过 邮箱 登录
     *
     * @param email 邮箱
     */
    @ApiOperation(value = "通过 邮箱 登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", dataTypeClass = String.class, required = true),
    })
    @PostMapping("/loginByEmail")
    public Result loginByEmail(String email) {

        // 查询数据库
        QueryWrapper<User> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_email", email);
        User user = userService.getOne (wrapper);

        // 登录
        try {
            Map<String, Object> token = userService.login (user);
            return Result.success (200, "登录成功 !", token);
        } catch (UnknownAccountException e) {
            // 用户不存在
            return Result.fail ("认证失败 : 用户名不存在 ~");
        } catch (IncorrectCredentialsException e) {
            //密码错误
            return Result.fail ("认证失败 : 密码错误 ~");
        }
    }

    /**
     * 通过 电话 登录
     *
     * @param phone 电话
     */
    @ApiOperation(value = "通过 电话 登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", dataTypeClass = String.class, required = true),
    })
    @PostMapping("/loginByPhone")
    public Result loginByPhone(String phone) {

        // 查询数据库
        QueryWrapper<User> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_phone", phone);
        User user = userService.getOne (wrapper);

        // 登录
        try {
            Map<String, Object> token = userService.login (user);
            return Result.success (200, "登录成功 !", token);
        } catch (UnknownAccountException e) {
            // 用户不存在
            return Result.fail ("认证失败 : 用户名不存在 ~");
        } catch (IncorrectCredentialsException e) {
            //密码错误
            return Result.fail ("认证失败 : 密码错误 ~");
        }
    }

    /**
     * 注册
     *
     * @param user user
     */
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result register(User user) {

        boolean name = codeService.verifyUserName (user.getUserName ());

        if (! name) {
            return Result.fail ("该用户名已被注册");
        }

        if (! StringUtils.isEmpty (user.getUserEmail ())) {
            boolean email = codeService.verifyUserEmail (user.getUserEmail ());

            if (! email) {
                return Result.fail ("该邮箱已被注册");
            }
        }

        if (! StringUtils.isEmpty (user.getUserPhone ())) {
            boolean phone = codeService.verifyUserPhone (user.getUserPhone ());

            if (! phone) {
                return Result.fail ("该电话已被注册");
            }
        }

        Boolean register = userService.register (user);

        return register ? Result.success ("注册 成功 !") : Result.fail ("注册 失败 !");
    }

    /**
     * 通过 邮箱 重置 密码
     *
     * @param email    邮箱
     * @param password 密码
     */
    @ApiOperation(value = "通过 邮箱 重置 密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataTypeClass = String.class, required = true),
    })
    @PostMapping("/resetPasswordByEmail")
    public Result resetPasswordByEmail(String email, String password) {

        // 查询数据库
        QueryWrapper<User> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_email", email);
        User user = userService.getOne (wrapper);

        // 重置密码
        Boolean reset = userService.resetPassword (user.getUserId (), password);
        return reset ? Result.success ("密码重置成功, 请重新登录") : Result.fail ("密码重置失败, 请稍后再试");
    }

    /**
     * 通过 电话 重置 密码
     *
     * @param phone    电话
     * @param password 密码
     */
    @ApiOperation(value = "通过 电话 重置 密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "电话", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataTypeClass = String.class, required = true),
    })
    @PostMapping("/resetPasswordByPhone")
    public Result resetPasswordByPhone(String phone, String password) {

        // 查询数据库
        QueryWrapper<User> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_phone", phone);
        User user = userService.getOne (wrapper);

        // 重置密码
        Boolean reset = userService.resetPassword (user.getUserId (), password);
        return reset ? Result.success ("密码重置成功, 请重新登录") : Result.fail ("密码重置失败, 请稍后再试");
    }


}
