package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.User;
import org.apache.shiro.authc.AuthenticationException;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
public interface UserService extends IService<User> {

    /**
     * 注册用户
     *
     * @param user 用户信息
     * @return true : 成功 | false : 失败
     */
    Boolean register (User user);

    /**
     * 登录
     *
     * @param user 用户登录信息
     * @return JwtToken
     * @throws AuthenticationException 用户身份认证异常
     */
    Map<String, Object> login (User user) throws AuthenticationException;

    /**
     * 重置密码
     * @param userId 用户 id
     * @param newPassword 新密码
     * @return true : 成功 | false : 失败
     */
    Boolean resetPassword (Long userId, String newPassword);

    /**
     * 注销用户
     * @param userId 用户 id
     * @return true : 成功 | false : 失败
     */
    Boolean cancellationUser (Long userId);

    /**
     * 通过用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    User getUserByName (String userName);
}
