package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.config.shiro.jwt.JwtToken;
import com.wq.mapper.UserCollectionMapper;
import com.wq.mapper.UserInfoMapper;
import com.wq.mapper.UserMapper;
import com.wq.pojo.User;
import com.wq.pojo.UserCollection;
import com.wq.pojo.UserInfo;
import com.wq.service.UserService;
import com.wq.util.shiro.JwtUtils;
import com.wq.util.shiro.SaltUtils;
import com.wq.util.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service("UserService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserCollectionMapper userCollectionMapper;
    
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean register(User user) {

        // 获取随机盐
        String salt = SaltUtils.getSalt(10);

        // 加密用户的密码
        String md5Password = new Md5Hash (user.getUserPassword(), salt, 1024).toHex();
        user.setUserPassword(md5Password);
        user.setUserSalt(salt);

        // 保存到数据库中
        int insertUser = userMapper.insert(user);

        // 如果返回的修改行数小于 1, 代表插入失败, 返回 false
        if (insertUser == 0) {
            log.error ("用户注册失败 --- user 表插入失败",
                    new RuntimeException ("用户注册失败 --- user 表插入失败"));
        }

        // 获取用户 id
        User one = userMapper.getUserByName(user.getUserName());

        // 为用户创建用户信息表
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId (one.getUserId());
        int insertInfo = userInfoMapper.insert(userInfo);

        // 为用户创建默认收藏夹表
        UserCollection userCollection = new UserCollection ();
        userCollection.setUserId (one.getUserId ());
        userCollection.setCollectionName ("默认收藏夹");
        int insertCollection = userCollectionMapper.insert (userCollection);

        // 如果插入失败
        if (insertInfo == 0) {
            log.error ("用户注册失败 --- user_info 表插入失败",
                    new RuntimeException ("用户注册失败 --- user_info 表插入失败"));
        } else if (insertCollection == 0){
            log.error ("用户注册失败 --- user_collection 表插入失败",
                    new RuntimeException ("用户注册失败 --- user_collection 表插入失败"));
        }

        return true;
    }

    @Override
    public Map<String, Object> login(User user) throws AuthenticationException {

        // 生成 JwtToken
        Map<String, String> payload = new HashMap<> (16);
        payload.put("userId", String.valueOf(user.getUserId()));
        payload.put("userName", user.getUserName());
        String token = JwtUtils.getJwtToken(user.getUserPassword(), payload);

        // token 转为 JwtToken
        JwtToken jwtToken = new JwtToken(token);

        // 获取 subject
        Subject subject = ShiroUtils.getSubject();

        // 登录, 这里可能出现的异常采用抛出, 方便返回错误信息
        subject.login(jwtToken);

        // 登录成功
        Map<String, Object> map = new HashMap<> (16);
        map.put ("token", jwtToken.getToken ());
        map.put ("userId", user.getUserId ());
        return map;
    }

    @Override
    public Boolean resetPassword(Long userId, String newPassword) {
        User user = userMapper.selectById(userId);

        // 获取随机盐
        String salt = SaltUtils.getSalt(10);

        // 加密用户的密码
        String md5Password = new Md5Hash(newPassword, salt, 1024).toHex();

        user.setUserPassword(md5Password);
        user.setUserSalt(salt);

        // 更新数据库
        int update = userMapper.updateById(user);

        return update >= 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean cancellationUser(Long userId) {

        // 删除 user 表
        int deleteUser = userMapper.deleteById (userId);

        // 删除 user_info 表
        int deleteInfo = userInfoMapper.delete (
                new QueryWrapper<UserInfo> ().eq ("user_id", userId));

        // 删除 user_collection 表
        int deleteCollection = userCollectionMapper.delete (
                new QueryWrapper<UserCollection> ().eq ("user_id", userId));

        if (deleteUser == 0) {
            log.error ("删除 user 表失败", new RuntimeException ("删除 user 表失败"));
        } else if (deleteInfo == 0) {
            log.error ("删除 user_info 表失败", new RuntimeException ("删除 user_info 表失败"));
        } else if (deleteCollection == 0) {
            log.error ("删除 user_collection 表失败", new RuntimeException ("删除 user_collection 表失败"));
        }
        return true;
    }

    @Override
    public User getUserByName(String userName) {
        return userMapper.getUserByName (userName);
    }

}
