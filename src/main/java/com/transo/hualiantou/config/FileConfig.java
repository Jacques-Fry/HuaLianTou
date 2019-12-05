package com.transo.hualiantou.config;

/**
 * @author Jack_YD
 * @create 2019/8/21 9:58
 */

import com.sun.corba.se.impl.orbutil.threadpool.WorkQueueImpl;
import com.transo.hualiantou.utils.pathUtils;
import org.apache.commons.io.EndianUtils;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FileConfig extends WebMvcConfigurerAdapter {

    //将jar文件下的对应静态资源文件路径对应到磁盘的路径(根据个人的情况修改"file:static/"的static的值)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").
                addResourceLocations("classpath:/static/","file:"+new pathUtils().getPath()+"/static/");
    }




}
