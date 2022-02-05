package com.wq.service.impl;

import com.wq.pojo.Title;
import com.wq.mapper.TitleMapper;
import com.wq.service.TitleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service
public class TitleServiceImpl extends ServiceImpl<TitleMapper, Title> implements TitleService {

    @Override
    public Boolean createTitle(Title title) {
        return null;
    }

    @Override
    public Boolean updateTitle(Title title) {
        return null;
    }

    @Override
    public String uploadTitleImg(Long titleId, MultipartFile file) {
        return null;
    }

    @Override
    public Boolean deleteTitle(Long titleId) {
        return null;
    }

    @Override
    public Title getTitleById(Long titleId) {
        return null;
    }
}
