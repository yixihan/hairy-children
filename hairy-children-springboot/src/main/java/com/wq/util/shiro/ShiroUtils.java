package com.wq.util.shiro;

import com.wq.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @author : yixihan
 * @create : 2022-02-05-12:22
 */
public class ShiroUtils {

    /**
     * 返回当前登录用户
     * @return user
     */
    public static User getUser () {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }


    /**
     * 获取当前登录 subject
     * @return subject
     */
    public static Subject getSubject () {
        return SecurityUtils.getSubject();
    }


    /**
     * 返回当前登录用户的 id
     * @return id
     */
    public static Long getUserId () {
        return getUser().getUserId();
    }
}
