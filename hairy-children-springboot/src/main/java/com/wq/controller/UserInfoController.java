package com.wq.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.common.pojo.Result;
import com.wq.pojo.UserInfo;
import com.wq.service.UserInfoService;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("/user-info")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Value ("${customer.appCode}")
    private String appCode;

    @PostMapping("/updateUserInfo")
    public Result updateUserInfo (UserInfo userInfo) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", ShiroUtils.getUserId ());
        boolean update = userInfoService.update (userInfo, wrapper);

        return update ? Result.success ("更新用户信息成功 !") : Result.fail ("更新用户信息失败 !");
    }

    @PostMapping("/uploadAvatar")
    public Result uploadAvatar (@RequestParam("avatar") MultipartFile avatar) {
        Long userId = ShiroUtils.getUserId();

        // 上传头像
        String avatarUrl = userInfoService.updateUserAvatar(userId, avatar);

        // 将图形路径存储到数据库
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserAvatar(avatarUrl);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", ShiroUtils.getUserId ());

        boolean b = userInfoService.update (userInfo, wrapper);

        return b ? Result.success() : Result.fail();
    }

    @PostMapping("/authentication")
    public Result authenticate(String realName, String identityCard) {
        String url = "https://puhui.shumaidata.com/id_card/check/puhui";

        Map<String, String> params = new HashMap<>(16);
        params.put("idcard", identityCard);
        params.put("name", realName);

        try {
            url = url + buildRequestUrl(params);
            OkHttpClient client = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url(url).addHeader("Authorization", "APPCODE " + appCode).build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body ();
            JSONObject jsonObject = JSONObject.parseObject (body.string ());

            if ((int) jsonObject.get ("code") == 200) {
                JSONObject data = jsonObject.getJSONObject ("data");
                int result = (int) data.get ("result");
                if (result == 1) {
                    return Result.fail ("身份信息不一致, 请正确输入");
                } else if (result == 2) {
                    return Result.fail ("无该公民身份记录, 请正确输入");
                }
                // 更新用户地址
                UserInfo userInfo = userInfoService.getUserInfoById (ShiroUtils.getUserId ());
                userInfo.setUserAddress (String.valueOf (data.get ("address")));

                // 更新用户生日
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                Date parse = format.parse(String.valueOf (data.get ("birthday")));
                userInfo.setUserBirth (parse);

                // 更新用户性别
                userInfo.setUserGender (String.valueOf (data.get ("sex")));

                // 更新用户真实姓名
                userInfo.setUserRealName (realName);

                // 更新用户身份证号
                userInfo.setUserIdentityCard (identityCard);
                boolean update = userInfoService.updateById (userInfo);

                return update ? Result.success ("实名认证成功 !") : Result.fail ("实名认证失败");
            }

            return Result.fail ("实名认证失败");
        } catch (IOException | ParseException e) {
            e.printStackTrace ();
            return Result.fail ("实名认证失败");
        }
    }

    private String buildRequestUrl(Map<String, String> params) {
        StringBuilder url = new StringBuilder("?");
        for (String key : params.keySet ()) {
            url.append (key).append ("=").append (params.get (key)).append ("&");
        }
        return url.substring(0, url.length() - 1);
    }
}
