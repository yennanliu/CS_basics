package com.atguigu.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Date:2021/7/5
 * Author:ybc
 * Description:
 */
@Controller
public class HelloController {

    //  通过@RequestMapping注解，可以通过请求路径匹配要处理的具体的请求
    //  /表示的当前工程的上下文路径
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/target")
    public String toTarget(){
        return "target";
    }

}
