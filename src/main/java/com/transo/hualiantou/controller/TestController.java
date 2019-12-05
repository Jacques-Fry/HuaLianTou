package com.transo.hualiantou.controller;

import com.transo.hualiantou.pojo.ProjectManage;
import com.transo.hualiantou.utils.IPaddress;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author PC20
 * @create 2019/8/13
 */
@Controller
@Api(tags="测试")
public class TestController {

   /* @GetMapping("asd")
    @ApiOperation(value = "获取用户",notes = "返回用户和认证信息",response = User.class)
    @ApiImplicitParam(name = "name",value = "姓名",required = true,dataType = "string",paramType = "json")
    public JSONResult get(String name){
        User user =new User();
        JSONResult jsonResult=new JSONResult(user);
        return jsonResult;
    }
*/



    @GetMapping("getip")
    @ApiOperation("获取服务器端ip")
    @ResponseBody
    public String get1(HttpServletRequest request) throws Exception {

        return IPaddress.getIpAddress(request);
    }


    /*@RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String ream(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
        rq.getRequestDispatcher("/doc.html").forward(rq,rp);

        return "/doc.html";
    }*/

}
