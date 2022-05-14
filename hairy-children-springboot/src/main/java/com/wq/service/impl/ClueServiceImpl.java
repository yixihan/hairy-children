package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.common.PhotoProperties;
import com.wq.mapper.ClueMapper;
import com.wq.pojo.Clue;
import com.wq.service.ClueService;
import com.wq.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service("ClueService")
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements ClueService {

    @Resource
    private PhotoProperties photoProperties;

    @Resource
    private ClueMapper clueMapper;

    @Override
    public Boolean createClue(Clue clue) {
        // 获取文件路径 以文章id - 用户id 作为目录
        String fileName = clue.getTitleId () + "-" + clue.getUserId ().toString ();
        File cluePath = new File (photoProperties.getCluePaths () + "/" + fileName);

        // 生成图片目录
        String imageName = String.format (FileUtils.TITLE_DIR, System.currentTimeMillis ());

        // 创建图片上传路径 src/resources/static/photoDir/clue/titleId-userId/时间戳.assets
        File imagePath = new File (photoProperties.getPaths () + "/" + cluePath + "/" + imageName);
        FileUtils.isFileExists (imagePath);

        clue.setClueDir (cluePath.getPath () + "/" + imageName);
        return clueMapper.insert (clue) == 1;
    }

    @Override
    public String uploadImg(Long clueId, MultipartFile file) {
        // 校验传入的文件
        FileUtils.isEmpty (file);

        // 获取图片路径
        Clue clue = clueMapper.selectById (clueId);
        File filePath = new File (clue.getClueDir ());

        // 生成文件名
        String fileName = String.format (FileUtils.IMG_NAME, System.currentTimeMillis ());

        try {
            FileUtils.uploadFile (file, fileName, new File (photoProperties.getPaths () + "/" + filePath));
            return filePath + "/" + fileName;

        } catch (IOException e) {
            e.printStackTrace ();
            log.error ("文件上传失败", e.getCause ());
            return null;
        }
    }

    @Override
    public List<Clue> getCluesByUserId(Long userId) {
        QueryWrapper<Clue> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId).notIn ("is_success", -1).orderByDesc("gmt_create");
        return clueMapper.selectList (wrapper);
    }

    @Override
    public List<Clue> getCluesByTitleId(Long titleId) {
        QueryWrapper<Clue> wrapper = new QueryWrapper<> ();
        wrapper.eq ("title_id", titleId).notIn ("is_success", -1).orderByDesc("is_success", "gmt_create");
        return clueMapper.selectList (wrapper);
    }

    @Override
    public Boolean isExists(Long titleId, Long userId) {
        QueryWrapper<Clue> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId)
                .eq ("title_id", titleId);
        Integer count = clueMapper.selectCount (wrapper);

        return count == 0;
    }
}
