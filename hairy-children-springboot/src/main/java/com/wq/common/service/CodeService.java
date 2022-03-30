package com.wq.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.wq.mapper.UserMapper;
import com.wq.pojo.User;
import com.wq.util.VerifyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author : yixihan
 * @create : 2022-02-09-9:10
 */
@Service("CodeService")
@Validated
public class CodeService {

    private static final String CODE_KEY_NAME = "%s_CODE";

    private static final Integer MAX_CODE = 99999;

    private static final Integer MIN_CODE = 10000;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Value ("${customer.appCode}")
    private String appCode;

    @Value ("${customer.phone.secretId}")
    private String secretId;

    @Value ("${customer.phone.secretKey}")
    private String secretKey;

    @Value ("${customer.phone.smsSdkAppId}")
    private String smsSdkAppId;

    @Value ("${customer.phone.templateId}")
    private String templateId;

    @Value ("${customer.phone.signName}")
    private String signName;

    /**
     * 向指定邮箱发送验证码, 10 分钟有效期
     * @param email 邮箱
     */
    public boolean sendCodeByEmail(@Email String email) {

        // 生成 keyName
        String keyName = String.format(CODE_KEY_NAME, email);

        try {
            // 生成验证码
            int code = getCode(keyName);

            // 创建一个复杂的文件
            MimeMessage mailMessage = mailSender.createMimeMessage();

            // 组装邮件
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage,true,"utf-8");

            helper.setSubject("你好");
            helper.setText("<h1 style='color:red'>验证码为" + code + "</h1>" +
                    "<p>有效期为 10 分钟,.请尽快填写, 打死也不要告诉别人哦</p>",true);

            // 收件人
            helper.setTo(email);
            // 发件人
            helper.setFrom("3113788997@qq.com");

            // 发送
            mailSender.send(mailMessage);

        } catch (MessagingException e) {
            return false;
        }

        return true;
    }


    /**
     * 向指定电话发送验证码, 10 分钟有效期
     * @param phone 电话
     */
    public boolean sendCodeByPhone(String phone) {

        // 校验
        if (! VerifyUtils.isModel (phone)) {
            return false;
        }

        // 生成 keyName
        String keyName = String.format(CODE_KEY_NAME, phone);

        // 生成验证码
        Integer code = getCode(keyName);

        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId(smsSdkAppId);
            req.setSignName(signName);
            req.setTemplateId(templateId);

            String[] templateParamSet1 = {String.valueOf (code)};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
            return true;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return false;
        }
    }


    /**
     * 校验验证码是否正确
     * @param phoneOrEmail 电话 | 邮箱
     * @param code code
     */
    public boolean verifyCode(String phoneOrEmail, String code) {

        // 校验
        if (! VerifyUtils.isEmail (phoneOrEmail) && ! VerifyUtils.isModel (phoneOrEmail)) {
            return false;
        }

        // 生成 key
        String key = String.format (CODE_KEY_NAME, phoneOrEmail);

        // 校验验证码是否已经过期
        Long expire = stringRedisTemplate.getExpire(key);
        if (expire == null || expire < 0L) {
            return false;
        }

        return code.equals(stringRedisTemplate.opsForValue().get(key));
    }


    /**
     * 校验用户名是否已被注册
     * @param userName 用户名
     */
    public boolean verifyUserName(String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_name", userName);

        // 获取 user_name 为 userName 的记录数
        Integer count = userMapper.selectCount(wrapper);

        return count == 0;
    }


    /**
     * 校验邮箱是否已被绑定
     * @param email 邮箱
     */
    public boolean verifyUserEmail(@Email String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_email", email);

        // 获取 user_email 为 email 的记录数
        Integer count = userMapper.selectCount(wrapper);

        return count == 0;
    }


    /**
     * 校验电话是否已被绑定
     * @param phone 电话
     */
    public boolean verifyUserPhone(String phone) {

        // 校验
        if (! VerifyUtils.isModel (phone)) {
            return false;
        }

        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_phone", phone);

        // 获取 user_phone 为 phone 的记录数
        Integer count = userMapper.selectCount(wrapper);

        return count == 0;
    }


    /**
     * 生成验证码, 设置为 10 分钟有效期
     * 保存到 redis 数据库中
     * @param keyName email_ | phone_
     */
    public Integer getCode (String keyName) {
        Random random = new Random();

        // 生成验证码
        int code = random.nextInt((MAX_CODE - MIN_CODE + 1) + MIN_CODE);

        // 存入 redis
        stringRedisTemplate.opsForValue().set(keyName, String.valueOf(code));

        // 设置过期时间
        stringRedisTemplate.expire(keyName, 10, TimeUnit.MINUTES);

        return code;

    }

}
