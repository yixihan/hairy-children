package com.wq.config.shiro.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义 shiro 接口 JwtToken, 替换原本 shiro 的token
 * @author : yixihan
 * @create : 2022-02-05-12:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken implements AuthenticationToken {

    private String token;


    /**
     * 返回 JwtToken
     * @return JwtToken
     */
    @Override
    public Object getPrincipal() {
        return token;
    }


    /**
     * 返回 JwtToken
     * @return JwtToken
     */
    @Override
    public Object getCredentials() {
        return token;
    }
}
