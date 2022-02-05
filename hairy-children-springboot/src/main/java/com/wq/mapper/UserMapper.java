package com.wq.mapper;

import com.wq.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过用户名查询用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    User getUserByName (String userName);


    /**
     * 通过用户名查询用户盐
     * @param userName 用户名
     * @return 用户盐
     */
    String getUserSaltByUserName (String userName);

}
