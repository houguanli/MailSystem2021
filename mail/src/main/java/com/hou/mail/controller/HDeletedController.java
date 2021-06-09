package com.hou.mail.controller;

import com.hou.mail.bean.Mail;
import com.hou.mail.bean.User;
import com.hou.mail.response.MailRes;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class HDeletedController {
    /**
     * self : from id in mail is himself
     *
     */
    private static ArrayList<Mail> self_deleted, to_deleted;
    @RequestMapping("/deleted.html")
    public String mainToDeleted(HttpSession httpSession){
        System.out.println("RUN MAIN TO DELETED ");
        User user = (User) httpSession.getAttribute("user");
//        self_deleted = MailRes.getDeletedMailByUserID(user.getId());
        to_deleted = MailRes.getDeletedMailByToID(user.getId());
        if (to_deleted == null) {
            System.out.println("UN HANDEL ERR! ON CAN NOT GET EMAILS!");
            return "main";
        }
//        httpSession.setAttribute("self_deleted", self_deleted);
        httpSession.setAttribute("to_deleted", to_deleted);
        return "deleted";
    }
}
