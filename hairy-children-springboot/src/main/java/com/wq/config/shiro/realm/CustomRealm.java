package com.wq.config.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.config.shiro.jwt.JwtToken;
import com.wq.pojo.User;
import com.wq.service.impl.UserServiceImpl;
import com.wq.util.SpringUtils;
import com.wq.util.shiro.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author : yixihan
 * @create : 2022-02-05-12:21
 */
public class CustomRealm extends AuthorizingRealm {

    /**
     * 将 token 转为 自定义的 JwtToken
     * @param token shiro 提供的 token
     * @return 自定义的 JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 权限校验, 本后端不提供
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }


    /**
     * 身份认证
     * @param token JwtToken
     * @return 用户认证信息
     * @throws AuthenticationException 认证失败返回异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 获取 JwtToken
        String jwtToken = String.valueOf(token.getCredentials());

        // 从 JwtToken 中获取 userName
        String userName = JwtUtils.getUserName(jwtToken);

        // 根据用户名, 从数据库中查询用户信息 (查询出多用户会抛出异常)
        User user = getUserService().getOne(new QueryWrapper<User> ().eq("user_name", userName), true);

        // 用户不存在
        if (user == null) {
            throw new UnknownAccountException();
        }

        // token 过期
        if (JwtUtils.isExpired(jwtToken)) {
            throw new ExpiredCredentialsException ();
        }


        // 密码错误
        if (! JwtUtils.verifyToken(jwtToken,user.getUserPassword())) {
            throw new IncorrectCredentialsException ();
        }

        /*
        user : principal
        jwtToken : credentials
         */
        return new SimpleAuthenticationInfo (user, jwtToken, this.getName());
    }


    private UserServiceImpl getUserService () {
        return SpringUtils.getBean("UserService");
    }
}
