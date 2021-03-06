package com.transo.hualiantou.utils;

/**
 * @author 花落泪知雨
 * @create 2019/9/3
 */
public class pathUtils {

    /**
     * 获取jar包所在位置
     * @return
     */
    public String getPath()
    {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(System.getProperty("os.name").contains("dows"))
        {
            path = path.substring(1,path.length());
        }
        if(path.contains("jar"))
        {
            path = path.substring(0,path.lastIndexOf("."));
            return path.substring(0,path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }
}
