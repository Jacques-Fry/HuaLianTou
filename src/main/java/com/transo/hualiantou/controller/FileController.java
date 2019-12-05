package com.transo.hualiantou.controller;

import com.alibaba.fastjson.JSON;
import com.transo.hualiantou.ReturnsStyle.JSONResult;
import com.transo.hualiantou.utils.FileUtils;
import com.transo.hualiantou.token.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 花落泪知雨
 * @create 2019/8/20
 */
@CrossOrigin(origins = "*")
@RestController
@Api(tags = "文件模块")
public class FileController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;//redis服务

    @Value("${jar.imageFilePath}")
    private String imageFilePath;

    @Value("${jar.imageFileUrl}")
    private String imageFileUrl;

    @Value("${jar.surveyFilePath}")
    private String surveyFilePath;

    @Value("${jar.surveyFileUrl}")
    private String surveyFileUrl;

    /**
     *
     * @param file 上传的文件
     * @return
     */
    @PostMapping("/fileUpload")
    @ApiOperation(value = "上传文件")
    public JSONResult upload(@RequestParam("file")MultipartFile file , @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        // 获得文件名字
        String fileName=file.getOriginalFilename();
        // 生成新的文件名
        String newName =  FileUtils.getFileName(fileName);
        if(FileUtils.upload(file, imageFilePath + "/" +newName)){
            //上传成功
            String UrlJSON="{\"url\":\""+imageFileUrl+newName+"\"}";
            return new JSONResult(JSON.parse(UrlJSON));
        }else{
            //上传失败
            return new JSONResult("上传失败,服务器异常,请重试!",400);
        }
    }



    /**
     *
     * @param file 上传的文件
     * @return
     */
    @PostMapping("/fileUpload/survey")
    @ApiOperation(value = "上传尽职调查的文件")
    public JSONResult uploadSurvey(@RequestParam("file")MultipartFile file , @RequestHeader String token, HttpServletResponse rp, HttpServletRequest rq){
        //验证token
        if(!JWTUtils.verifyToken(token,rq,redisTemplate)){
            rp.setStatus(4000);
            return new JSONResult("token过期或无效!",4000);
        }

        // 获得文件名字
        String fileName=file.getOriginalFilename();
        // 生成新的文件名
        String newName =  FileUtils.getFileName(fileName);
        if(FileUtils.upload(file, surveyFilePath + "/" +newName)){
            //上传成功
            String UrlJSON="{\"url\":\""+surveyFileUrl+newName+"\"}";
            return new JSONResult(JSON.parse(UrlJSON));
        }else{
            //上传失败
            return new JSONResult("上传失败,服务器异常,请重试!",400);
        }
    }



}
