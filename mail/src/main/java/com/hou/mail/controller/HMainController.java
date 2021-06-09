package com.hou.mail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HMainController {
//    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "Hello sp boot!";
    }
    //THIS SHOW STH
    @RequestMapping("/success")
    public String success(){
        return "success";
    }
    @RequestMapping("/")
    public String index(){
        return "login";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
    @RequestMapping("/settings.html")
    public String MainToSetting(){
        return "settings";
    }

}

