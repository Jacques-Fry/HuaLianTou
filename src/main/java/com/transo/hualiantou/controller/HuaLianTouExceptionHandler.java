package com.transo.hualiantou.controller;

import com.transo.hualiantou.ReturnsStyle.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jack_YD
 * @create 2019/8/27 16:07
 */
@CrossOrigin(origins = "*")
@ControllerAdvice
public class HuaLianTouExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public JSONResult exception(MethodArgumentNotValidException me) {

        BindingResult bindingResult = me.getBindingResult();
        return new JSONResult(bindingResult);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public JSONResult exception(Exception e, HttpServletResponse rs) {
        e.printStackTrace();

        return new JSONResult("程序错误!", 500);
    }


 /*   @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView noHandlerFoundHandler(Exception e) {
        e.printStackTrace();
        return new ModelAndView("/doc.html");
    }*/

}
