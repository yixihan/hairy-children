package com.wq.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.common.service.CodeService;
import com.wq.pojo.User;
import com.wq.service.UserService;
import com.wq.util.StringUtils;
import com.wq.util.VerifyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * <p>
     * 参数
     * <ul>userName 用户名</ul>
     * <ul>password 密码</ul>
     */
    @PostMapping("/login")
    public Result loginByUserName(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String userName = String.valueOf (params.get ("userName"));
        String password = String.valueOf (params.get ("password"));
        log.info ("userName : " + userName + ", password : " + password);

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
     * <p>
     * 参数
     * <ul>email 邮箱</ul>
     */
    @PostMapping("/loginByEmail")
    public Result loginByEmail(@RequestBody Map<String, Object> params) {
        // 从 json 中提取参数
        String email = String.valueOf (params.get ("email"));
        log.info ("email : " + email);
        if (!VerifyUtils.isEmail (email)) {
            return Result.fail (555, "邮箱格式错误");
        }

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
     * <p>
     * phone 电话
     */
    @PostMapping("/loginByPhone")
    public Result loginByPhone(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String phone = String.valueOf (params.get ("phone"));
        log.info ("phone : " + phone);
        if (!VerifyUtils.isModel (phone)) {
            return Result.fail (555, "电话格式错误");
        }

        // 查询数据库
        QueryWrapper<User> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_phone", phone);
        User user = userService.getOne (wrapper);
        log.info ("user : " + user);

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
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        log.info ("user : " + user);

        boolean name = codeService.verifyUserName (user.getUserName ());

        if (!name) {
            return Result.fail (555, "该用户名已被注册");
        }

        if (!StringUtils.isEmpty (user.getUserEmail ())) {

            if (!VerifyUtils.isEmail (user.getUserEmail ())) {
                return Result.fail (555, "邮箱格式错误");
            }

            if (!codeService.verifyUserEmail (user.getUserEmail ())) {
                return Result.fail (555, "该邮箱已被注册");
            }
        }

        if (!StringUtils.isEmpty (user.getUserPhone ())) {

            if (!VerifyUtils.isModel (user.getUserPhone ())) {
                return Result.fail (555, "电话格式错误");
            }

            if (!codeService.verifyUserPhone (user.getUserPhone ())) {
                return Result.fail (555, "该电话已被注册");
            }
        }

        Boolean register = userService.register (user);

        return register ? Result.success ("注册 成功 !") : Result.fail ("注册 失败 !");
    }

    /**
     * 通过 邮箱 重置 密码
     * <p>
     * email    邮箱
     * <p>
     * password 密码
     */
    @PostMapping("/resetPasswordByEmail")
    public Result resetPasswordByEmail(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String email = String.valueOf (params.get ("email"));
        String password = String.valueOf (params.get ("password"));
        log.info ("email : " + email + ", password : " + password);

        if (!VerifyUtils.isEmail (email)) {
            return Result.fail (555, "邮箱格式错误");
        }

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
     * <p>
     * phone    电话
     * <p>
     * password 密码
     */
    @PostMapping("/resetPasswordByPhone")
    public Result resetPasswordByPhone(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String phone = String.valueOf (params.get ("phone"));
        String password = String.valueOf (params.get ("password"));
        log.info ("phone : " + phone + ", password : " + password);

        if (!VerifyUtils.isModel (phone)) {
            return Result.fail (555, "电话格式错误");
        }

        // 查询数据库
        QueryWrapper<User> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_phone", phone);
        User user = userService.getOne (wrapper);

        // 重置密码
        Boolean reset = userService.resetPassword (user.getUserId (), password);
        return reset ? Result.success ("密码重置成功, 请重新登录") : Result.fail ("密码重置失败, 请稍后再试");
    }


}
