package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.common.PhotoProperties;
import com.wq.mapper.UserInfoMapper;
import com.wq.pojo.UserInfo;
import com.wq.service.UserInfoService;
import com.wq.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Slf4j
@Service("UserInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private PhotoProperties photoProperties;

    @Override
    public UserInfo getUserInfoById(Long userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId);
        return userInfoMapper.selectOne (wrapper);
    }

    @Override
    public String updateUserAvatar(Long userId, MultipartFile file) {

        // 文件校验
        FileUtils.isEmpty (file);

        // 生成文件名, 以用户 id 为文件名
        String fileName = String.format (FileUtils.IMG_NAME, "userAvatar-" + userId + "-" + System.currentTimeMillis());

        // 生成文件路径
        File filePath = new File (photoProperties.getPaths () + "/" + photoProperties.getAvatarPaths ());
        try {
            // 上传文件
            FileUtils.uploadFile (file, fileName, filePath);

            return photoProperties.getAvatarPaths () + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace ();
            log.error ("文件上传失败", e.getCause ());
            return null;
        }

    }

    @Override
    public Boolean deleteUserInfoById(Long userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId);
        return userInfoMapper.delete (wrapper) == 1;
    }
}
