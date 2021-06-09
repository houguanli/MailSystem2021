package com.hou.mail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HManagerController {
    //find all of the users message
    @RequestMapping("/manager/userInfoList")
    public String emp(){
        return "manager/userInfoList";
    }
}
