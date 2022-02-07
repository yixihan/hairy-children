package com.wq.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author : yixihan
 * @create : 2022-02-07-14:07
 */
public class FileUtils {

    public static final String IMG_NAME = "%s.png";

    public static final String TITLE_DIR = "%s.assets";


    public static void uploadFile (MultipartFile file, String fileName, File filePath) throws IOException {

        // 校验文件路径
        isFileExists(filePath);

        // 拼接文件路径
        String fileUrl = filePath + "/" + fileName;

        System.out.println("fileUrl = " + fileUrl);

        // 上传文件
        file.transferTo(Paths.get(fileUrl));
    }


    public static void isFileExists (File file) {

        // 校验是否已有文件夹, 如果没有则创建
        if (! file.exists()) {

            // 使用 mkdirs()
            boolean mkdir = file.mkdirs();

            // 判断文件是否创建成功
            if (! mkdir) {
                throw new RuntimeException("创建文件失败");
            }
        }
    }

    public static void isEmpty (MultipartFile file) {
        if (file == null || file.isEmpty ()) {
            throw new RuntimeException("传入的文件为空");
        }
    }
}
