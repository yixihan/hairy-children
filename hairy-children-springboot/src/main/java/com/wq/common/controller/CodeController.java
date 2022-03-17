package com.wq.common.controller;

import com.wq.common.pojo.Result;
import com.wq.common.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-02-09-9:13
 */
@RestController
@Slf4j
@RequestMapping("/code")
public class CodeController {
    
    @Resource
    private CodeService codeService;

    @PostMapping("/sendCodeByEmail")
    public Result sendCodeByEmail (@RequestBody String email) {

        boolean b = codeService.sendCodeByEmail(email);

        return b ? Result.success("验证码发送成功, 请注意查收") : Result.fail("验证码发送失败");
    }


    @PostMapping("/sendCodeByPhone")
    public Result sendCodeByPhone (@RequestBody String phone) {

        boolean b = codeService.sendCodeByPhone(phone);

        return b ? Result.success("验证码发送成功, 请注意查收") : Result.fail("验证码发送失败");
    }


    @PostMapping("/verifyPhoneCode")
    public Result verifyPhoneCode (@RequestBody Map<String, Object> params) {

        String phone = String.valueOf (params.get ("phone"));
        String code = String.valueOf (params.get ("code"));
        log.info ("phone : " + phone + ", code : " + code);

        boolean b = codeService.verifyCode(phone, code);

        return b ? Result.success("验证码校验正确") : Result.fail("验证码错误或验证码已过期");
    }


    @PostMapping("/verifyEmailCode")
    public Result verifyEmailCode (@RequestBody Map<String, Object> params) {

        String email = String.valueOf (params.get ("email"));
        String code = String.valueOf (params.get ("code"));
        log.info ("email : " + email + ", code : " + code);
        boolean b = codeService.verifyCode(email, code);

        return b ? Result.success("验证码校验正确") : Result.fail("验证码错误或验证码已过期");
    }

    @PostMapping("/verifyUserName")
    public Result verifyUserName (@RequestBody String userName) {


        boolean b = codeService.verifyUserName(userName);

        return b ? Result.success("该用户名还未注册") : Result.fail("该用户名已被注册");
    }


    @PostMapping("/verifyUserEmail")
    public Result verifyUserEmail (@RequestBody String email) {

        boolean b = codeService.verifyUserEmail(email);

        return b ? Result.success("该邮箱未被绑定") : Result.fail("该邮箱已被绑定");
    }


    @PostMapping("/verifyUserPhone")
    public Result verifyUserPhone (@RequestBody String phone) {

        boolean b = codeService.verifyUserPhone(phone);

        return b ? Result.success("该电话未被绑定") : Result.fail("该电话已被绑定");
    }
}
