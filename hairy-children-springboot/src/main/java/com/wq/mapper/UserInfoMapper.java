package com.wq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wq.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
