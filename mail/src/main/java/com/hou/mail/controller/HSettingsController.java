package com.hou.mail.controller;

import com.google.gson.Gson;
import com.hou.mail.bean.User;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class HSettingsController {
    @PostMapping("/settings-change")
    public String birthday(
            @RequestParam("nickname") String nickname,
            @RequestParam("birthday") String birthday,
            HttpSession httpSession
    ){
        System.out.println("get birthday " + birthday );
        User user = (User) httpSession.getAttribute("user");
        user.setBirthday(birthday);
        user.setNickname(nickname);
        httpSession.setAttribute("user", user);
        int res = UserRes.updateUser(user);
        System.out.println("Upgraded ! user with status "  + res);
        return "settings";
    }
}
