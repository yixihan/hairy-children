package com.wq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wq.common.PhotoProperties;
import com.wq.mapper.TitleLikeMailboxMapper;
import com.wq.mapper.TitleMapper;
import com.wq.pojo.Title;
import com.wq.service.TitleService;
import com.wq.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wq
 * @since 2022-02-05
 */
@Service("TitleService")
public class TitleServiceImpl extends ServiceImpl<TitleMapper, Title> implements TitleService {

    @Resource
    private PhotoProperties photoProperties;

    @Resource
    private TitleMapper titleMapper;

    @Resource
    private TitleLikeMailboxMapper titleLikeMailboxMapper;

    @Override
    public Boolean createTitle(Title title) {

        // 获取文件路径 以用户 id 作为目录
        String fileName = title.getUserId ()  + "-" + UUID.randomUUID ().toString ();
        File titlePath = new File (photoProperties.getTitlePaths () + "/" + fileName);

        // 生成图片目录
        String imageName = String.format (FileUtils.TITLE_DIR, title.getTitleName () + "-" + System.currentTimeMillis ());

        // 创建图片上传路径 src/resources/static/photoDir/title/userId/titleName-时间戳.assets
        File imagePath = new File (photoProperties.getPaths () + "/" + titlePath + "/" + imageName);
        FileUtils.isFileExists (imagePath);

        title.setTitleDir (titlePath.getPath () + "/" + imageName);
        return titleMapper.insert (title) == 1;
    }

    @Override
    public String uploadTitleImg(Long titleId, MultipartFile file) {

        // 校验传入的文件
        FileUtils.isEmpty (file);

        // 获取图片路径
        Title title = titleMapper.selectById (titleId);
        File filePath = new File (title.getTitleDir ());

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
    public List<Title> getTitleByUserId(Long userId) {
        QueryWrapper<Title> wrapper = new QueryWrapper<> ();
        wrapper.eq ("user_id", userId);
        return titleMapper.selectList (wrapper);
    }

}
