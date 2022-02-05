package com.wq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wq.pojo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 通过用户 id 获取 用户信息
     * @param userId 用户 id
     * @return 用户信息
     */
    UserInfo getUserInfoById (Long userId);

    /**
     * 更新用户信息
     * @param userInfo 用户信息
     * @return true : 成功 | false : 失败
     */
    Boolean updateUserInfo (UserInfo userInfo);

    /**
     * 上传用户头像
     * @param userId 用户 id
     * @param file 头像
     * @return 头像存储 url
     */
    String updateUserAvatar (Long userId, MultipartFile file);

    /**
     * 通过 用户 id 删除用户信息
     * @param userId 用户 id
     * @return true : 成功 | false : 失败
     */
    Boolean deleteUserInfoById (Long userId);


}
