package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wq.pojo.UserInfo;
import com.wq.mapper.UserInfoMapper;
import com.wq.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserInfoById(Long userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId);
        return userInfoMapper.selectOne (wrapper);
    }

    @Override
    public Boolean updateUserInfo(UserInfo userInfo) {
        return userInfoMapper.updateById (userInfo) > 0;
    }

    @Override
    public String updateUserAvatar(Long userId, MultipartFile file) {

        return null;
    }

    @Override
    public Boolean deleteUserInfoById(Long userId) {
        return null;
    }
}
