package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.common.PhotoProperties;
import com.wq.mapper.AdoptMapper;
import com.wq.pojo.Adopt;
import com.wq.service.AdoptService;
import com.wq.util.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service("adoptService")
public class AdoptServiceImpl extends ServiceImpl<AdoptMapper, Adopt> implements AdoptService {

    @Resource
    private AdoptMapper adoptMapper;

    @Resource
    private PhotoProperties photoProperties;

    @Override
    public Boolean createAdopt(Adopt adopt) {
        // 获取文件路径 以文章id - 用户id 作为目录
        String fileName = adopt.getTitleId () + "-" + adopt.getUserId ();
        File adoptPath = new File (photoProperties.getAdoptPaths () + "/" + fileName);

        // 生成图片目录
        String imageName = String.format (FileUtils.TITLE_DIR, System.currentTimeMillis ());

        // 创建图片上传路径 src/resources/static/photoDir/adopt/titleId-userId/时间戳.assets
        File imagePath = new File (photoProperties.getPaths () + "/" + adoptPath + "/" + imageName);
        FileUtils.isFileExists (imagePath);

        adopt.setAdoptDir (imagePath.toString ());
        return adoptMapper.insert (adopt) == 1;
    }

    @Override
    public List<Adopt> getAdoptsByUserId(Long userId) {
        QueryWrapper<Adopt> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId);
        return adoptMapper.selectList (wrapper);
    }

    @Override
    public List<Adopt> getAllAdoptsByTitleId(Long titleId) {
        QueryWrapper<Adopt> wrapper = new QueryWrapper<> ();
        wrapper.eq ("title_id", titleId);
        return adoptMapper.selectList (wrapper);
    }
}
