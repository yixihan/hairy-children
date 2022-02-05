package com.wq.service.impl;

import com.wq.pojo.Clue;
import com.wq.mapper.ClueMapper;
import com.wq.service.ClueService;
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
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements ClueService {

    @Override
    public Boolean createClue(Clue clue) {
        return null;
    }

    @Override
    public Boolean updateClue(Clue clue) {
        return null;
    }

    @Override
    public Boolean deleteClue(Long clueId) {
        return null;
    }

    @Override
    public Clue getClueByClueId(Long clueId) {
        return null;
    }

    @Override
    public List<Clue> getAllCluesByTitleId(Long titleId) {
        return null;
    }
}
