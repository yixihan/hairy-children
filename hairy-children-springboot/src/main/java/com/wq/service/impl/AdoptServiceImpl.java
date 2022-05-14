package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.common.PhotoProperties;
import com.wq.mapper.AdoptMapper;
import com.wq.pojo.Adopt;
import com.wq.service.AdoptService;
import com.wq.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service("AdoptService")
public class AdoptServiceImpl extends ServiceImpl<AdoptMapper, Adopt> implements AdoptService {

    @Resource
    private AdoptMapper adoptMapper;

    @Resource
    private PhotoProperties photoProperties;

    @Override
    public Boolean createAdopt(Adopt adopt) {
        // 获取文件路径 以文章id - 用户id - UUID 作为目录
        String fileName = adopt.getTitleId() + "-" + adopt.getUserId().toString();
        File adoptPath = new File(photoProperties.getAdoptPaths() + "/" + fileName);

        // 生成图片目录
        String imageName = String.format(FileUtils.TITLE_DIR, System.currentTimeMillis());

        // 创建图片上传路径 src/resources/static/photoDir/adopt/titleId-userId/时间戳.assets
        File imagePath = new File(photoProperties.getPaths() + "/" + adoptPath + "/" + imageName);
        FileUtils.isFileExists(imagePath);

        adopt.setAdoptDir(adoptPath.getPath() + "/" + imageName);
        return adoptMapper.insert(adopt) == 1;
    }

    @Override
    public String uploadImg(Long adoptId, MultipartFile file) {
        // 校验传入的文件
        FileUtils.isEmpty(file);

        // 获取图片路径
        Adopt adopt = adoptMapper.selectById(adoptId);
        File filePath = new File(adopt.getAdoptDir());

        // 生成文件名
        String fileName = String.format(FileUtils.IMG_NAME, System.currentTimeMillis());

        try {
            FileUtils.uploadFile(file, fileName, new File(photoProperties.getPaths() + "/" + filePath));
            return filePath + "/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败", e.getCause());
            return null;
        }
    }

    @Override
    public List<Adopt> getAdoptsByUserId(Long userId) {
        QueryWrapper<Adopt> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).notIn("is_success", -1).orderByDesc("gmt_create");
        return adoptMapper.selectList(wrapper);
    }

    @Override
    public List<Adopt> getAllAdoptsByTitleId(Long titleId) {
        QueryWrapper<Adopt> wrapper = new QueryWrapper<>();
        wrapper.eq("title_id", titleId).notIn("is_success", -1)
                .last("order by is_success = 1 and gmt_create desc");
        return adoptMapper.selectList(wrapper);
    }

    @Override
    public Boolean isExists(Long titleId, Long userId) {
        QueryWrapper<Adopt> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("title_id", titleId);
        Integer count = adoptMapper.selectCount(wrapper);

        return count == 0;
    }
}
