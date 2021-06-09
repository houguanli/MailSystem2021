package com.hou.mail.controller;

import com.hou.mail.response.RegisterRes;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class HRegisterController {
    @PostMapping(value = "/user-register")
    public String register(@RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("repeat") String repeat,
                           Map<String, Object> stringObjectMap,
                           HttpSession httpSession) {
        if(!repeat.equals(password)){
            System.out.println("INPUT ERR");
            stringObjectMap.put("errorMes", "两次输入的密码不一致");
            return "register";
        }else if(repeat.equals("")){
            stringObjectMap.put("errorMes", "输入的密码不能为空");
            return "register";
        } else {
            System.out.println("INPUT OK");
            int ans = RegisterRes.register(email, password);
            if (ans == 0) {//注册后自动登录
                System.out.println("REG ING ________________");
                httpSession.setAttribute("userMes", UserRes.getNicknameByEmail(email));
                httpSession.setAttribute("user", UserRes.getUserByEmail(email));
                return "main";
            } else if (ans == 2) {
                stringObjectMap.put("errorMes", "用户已存在");
            } else if (ans == 1) {
                stringObjectMap.put("errorMes", "SQL tooMany update");
            } else stringObjectMap.put("errorMes", "SQL ERROR");
            return "register";
        }
    }
}
