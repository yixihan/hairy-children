package com.wq.service.impl;

import com.wq.pojo.Adopt;
import com.wq.mapper.AdoptMapper;
import com.wq.service.AdoptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service
public class AdoptServiceImpl extends ServiceImpl<AdoptMapper, Adopt> implements AdoptService {

    @Override
    public Boolean createAdopt(Adopt adopt) {
        return null;
    }

    @Override
    public Boolean updateAdopt(Adopt adopt) {
        return null;
    }

    @Override
    public Boolean deleteAdopt(Long adoptId) {
        return null;
    }

    @Override
    public Adopt getAdoptByAdoptId(Long adoptId) {
        return null;
    }

    @Override
    public List<Adopt> getAllAdoptsByTitleId(Long titleId) {
        return null;
    }
}
