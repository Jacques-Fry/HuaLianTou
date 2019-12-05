package com.transo.hualiantou.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 花落泪知雨
 * @create 2019/8/20
 */
public class FileUtils {

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName){
        return getUUID() + FileUtils.getSuffix(fileOriginName);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    /**
     *
     * @param file 文件
     * @param realPath   文件存放路径
     * @return
     */
    public static boolean upload(MultipartFile file, String realPath){


        //使用原文件名
        // String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
