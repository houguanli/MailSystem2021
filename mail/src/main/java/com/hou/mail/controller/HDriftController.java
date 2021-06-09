package com.hou.mail.controller;

import com.hou.mail.bean.Drift;
import com.hou.mail.bean.FileMes;
import com.hou.mail.bean.Mail;
import com.hou.mail.bean.User;
import com.hou.mail.response.DriftRes;
import com.hou.mail.response.FileRes;
import com.hou.mail.response.MailRes;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Scanner;

@Controller
public class HDriftController {
    private static ArrayList<Mail> arr = null;
    private static ArrayList<String> filePath = null, fileName = null, message;


    @RequestMapping("/drifts.html")
    public String mainToDrift(){
        return "drifts";
    }

    @RequestMapping("/get-drift")
    public String getDrift(HttpSession httpSession){
        Drift get = DriftRes.getRandomDrift();
        if(get == null){
            System.out.println("err at get random drift!");
            return "main";
        }else {
            Mail toShow = MailRes.getMailByID(get.getRealID());
            if(toShow == null){
                System.out.println("ERR AT Get mail with id " + get.getRealID());
                return "main";
            }else {
                User from = UserRes.getUserByID(toShow.getId());
                String nick = "";
                if(from != null) nick = from.getNickname();
                nick = nick.equals("") ? "no nickname" : nick;
                httpSession.setAttribute("nickname_drift", nick);
                httpSession.setAttribute("drift", toShow);
                messageHelper tmp = new messageHelper(toShow.getMessage());
                httpSession.setAttribute("messages", message);
                httpSession.setAttribute("links", fileName);
                return "drifts";
            }
        }
    }
    @RequestMapping("/show-drift")
    public String showDrift(HttpSession httpSession){
        return "showMail";
    }

    static class messageHelper {
        public messageHelper(String originalMessage) {
            message = new ArrayList<>();
            int ignore = 0;
            Scanner scanner = new Scanner(originalMessage);
            while (scanner.hasNext()) {
                String tmp = scanner.nextLine();
                if (tmp.equals("MESSAGE_EOF\n") || tmp.equals("MESSAGE_EOF")) break;
                message.add(tmp);
            }
            while (scanner.hasNext()) {
                String tmp = scanner.nextLine();
                FileMes res = FileRes.getFileMesByHash(tmp);
                System.out.println("HASH: " + tmp);
                ignore++;
                if (res == null) System.out.println(ignore + "UNKNOWN FILE WITH HASH : " + tmp);
                else {
                    filePath.add(res.getPath());
                    fileName.add(res.getName());
                }
            }
        }
    }
}
