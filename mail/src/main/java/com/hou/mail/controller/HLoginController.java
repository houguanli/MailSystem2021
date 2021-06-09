package com.hou.mail.controller;

import com.google.gson.Gson;
import com.hou.mail.response.LoginRes;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class HLoginController {
    @PostMapping(value = "/user-login")
//    @RequestMapping(value = "/user-login", method = RequestMethod.POST)
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Map<String, Object> stringObjectMap,
                        HttpSession httpSession){
        int ans = LoginRes.login(email, password);
        if(ans == 0){
            //success prevent the post submit twice
            httpSession.setAttribute("userMes", UserRes.getNicknameByEmail(email));
            httpSession.setAttribute("tmp_page_type", "main");
//            httpSession.setAttribute("user", new Gson().toJson(UserRes.getUserByEmail(email)));
            httpSession.setAttribute("user", UserRes.getUserByEmail(email));
            return "main";
        }else {
            stringObjectMap.put("errorMes","用户名/密码错误");
            return "login";
//            return "redirect:/fail-log";
        }
    }
}
