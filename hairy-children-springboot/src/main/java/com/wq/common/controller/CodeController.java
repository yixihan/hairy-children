package com.wq.common.controller;

import com.wq.common.pojo.Result;
import com.wq.common.service.CodeService;
import com.wq.util.VerifyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : yixihan
 * @create : 2022-02-09-9:13
 */
@RestController
@Slf4j
@Validated
@RequestMapping("/code")
public class CodeController {

    @Resource
    private CodeService codeService;

    @PostMapping("/sendCodeByEmail")
    public Result sendCodeByEmail(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String email = String.valueOf (params.get ("email"));
        if (!VerifyUtils.isEmail (email)) {
            return Result.fail (555, "邮箱格式错误");
        }

        boolean b = codeService.sendCodeByEmail (email);

        return b ? Result.success ("验证码发送成功, 请注意查收") : Result.fail ("验证码发送失败");
    }


    @PostMapping("/sendCodeByPhone")
    public Result sendCodeByPhone(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String phone = String.valueOf (params.get ("phone"));
        if (!VerifyUtils.isModel (phone)) {
            return Result.fail (555, "电话格式错误");
        }

        boolean b = codeService.sendCodeByPhone (phone);

        return b ? Result.success ("验证码发送成功, 请注意查收") : Result.fail ("验证码发送失败");
    }


    @PostMapping("/verifyPhoneCode")
    public Result verifyPhoneCode(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String phone = String.valueOf (params.get ("phone"));
        String code = String.valueOf (params.get ("code"));
        log.info ("phone : " + phone + ", code : " + code);
        if (!VerifyUtils.isModel (phone)) {
            return Result.fail (555, "电话格式错误");
        }

        boolean b = codeService.verifyCode (phone, code);
        Map<String, Object> map = new HashMap<> (16);
        map.put ("verify", b);

        return Result.success (map);
    }


    @PostMapping("/verifyEmailCode")
    public Result verifyEmailCode(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String email = String.valueOf (params.get ("email"));
        String code = String.valueOf (params.get ("code"));
        log.info ("email : " + email + ", code : " + code);
        if (!VerifyUtils.isEmail (email)) {
            return Result.fail (555, "邮箱格式错误");
        }

        boolean b = codeService.verifyCode (email, code);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("verify", b);

        return Result.success (map);
    }

    @PostMapping("/verifyUserName")
    public Result verifyUserName(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String userName = String.valueOf (params.get ("userName"));

        boolean b = codeService.verifyUserName (userName);
        Map<String, Object> map = new HashMap<> (16);
        map.put ("verify", b);

        return Result.success (map);
    }


    @PostMapping("/verifyUserEmail")
    public Result verifyUserEmail(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String email = String.valueOf (params.get ("email"));
        if (!VerifyUtils.isEmail (email)) {
            return Result.fail (555, "邮箱格式错误");
        }

        boolean b = codeService.verifyUserEmail (email);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("verify", b);

        return Result.success (map);
    }


    @PostMapping("/verifyUserPhone")
    public Result verifyUserPhone(@RequestBody Map<String, Object> params) {

        // 从 json 中提取参数
        String phone = String.valueOf (params.get ("phone"));
        if (!VerifyUtils.isModel (phone)) {
            return Result.fail (555, "电话格式错误");
        }

        boolean b = codeService.verifyUserPhone (phone);

        Map<String, Object> map = new HashMap<> (16);
        map.put ("verify", b);

        return Result.success (map);
    }
}
