package com.hou.mail.controller;

import com.hou.mail.bean.FileMes;
import com.hou.mail.bean.Mail;
import com.hou.mail.bean.User;
import com.hou.mail.response.FileRes;
import com.hou.mail.response.MailRes;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

@Controller
public class HSentController {
    private static ArrayList<dataHelper> sent;
    private static ArrayList<String> filePath, fileName;
    @RequestMapping("/sent.html")
    public String MainToSent(HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        sent = new ArrayList<>();
        ArrayList<Mail> sentList = MailRes.getSentMail(user.getId());
        if (sentList == null) {
            System.out.println("UN HANDEL ERR! ON CAN NOT GET EMAILS!");
            return "main";
        }
//        StringBuilder toDiv = new StringBuilder();
//        int rea = 0, unr = 0, len = sentList.size(), tmp, i = 0;
        for (Mail mail : sentList) {
            User from = UserRes.getUserByID(mail.getToId());
            String tit = mail.getTitle(), mailAdd = from == null ? "unknown user" : from.getEmail();
            sent.add(new dataHelper(tit, mailAdd, mail.getId()));
        }
        httpSession.setAttribute("sent", sent);

        return "sent";
    }
     private static class dataHelper{
         public dataHelper(String title, String tarAdd) {
             this.title = title;
             this.tarAdd = tarAdd;
         }

         public String title;
         public String tarAdd;

         public dataHelper(String title, String tarAdd, int id) {
             this.title = title;
             this.tarAdd = tarAdd;
             this.id = id;
         }

         public int id;
    }

    @PostMapping("/showSent.html")
    public String toSentMailPage(
            @RequestParam("index") String index,
            HttpSession httpSession){
        System.out.println("ON LOADING SENT MAIL PAGE!!!!");
        Mail mail = MailRes.getMailByID(sent.get(Integer.parseInt(index)).id);
        httpSession.setAttribute("tmpSent", mail);
//        System.out.println("get Showing page with index");
        try {
//            System.out.println("RUN POST FOR " + string);
            Mail get = MailRes.getMailByID(sent.get(Integer.parseInt(index)).id);
            if(get == null) {
                System.out.println("CATCH NULL MAIL ID !");
                throw new NullPointerException();
            }
            httpSession.setAttribute("title", get.getTitle());
            filePath = new ArrayList<>(); fileName = new ArrayList<>();
            messageHelper helper = new messageHelper(get.getMessage());
            httpSession.setAttribute("messages", helper.message);
            httpSession.setAttribute("links", fileName);
//            System.out.println(get);
//            return "main";
        }catch (IndexOutOfBoundsException e){
            System.out.println("OUT LIMIT!");
            return "main";
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return "showSent";
    }

    @PostMapping("/del-sent")
    public String delSentMail(
            @RequestParam("index") String index,
            HttpSession httpSession){
        System.out.println("ON LOADING SENT MAIL PAGE!!!!" + index);

        System.out.println("get Showing page with index");
        return "sent";
    }

    static class messageHelper{
        public ArrayList<String> message;
        public messageHelper(String originalMessage){
            message = new ArrayList<>();
            Scanner scanner = new Scanner(originalMessage);

            while (scanner.hasNext()) {
                String tmp = scanner.nextLine();
                if(tmp.equals("MESSAGE_EOF\n") || tmp.equals("MESSAGE_EOF")) break;
                message.add(tmp);
            }
            while (scanner.hasNext()) {
                String tmp = scanner.nextLine();
                FileMes res = FileRes.getFileMesByHash(tmp);
                System.out.println("HASH: " + tmp);
                if(res == null) System.out.println("UNKNOWN FILE WITH HASH : " + tmp);
                else {
                    filePath.add(res.getPath());
                    fileName.add(res.getName());
                }
            }
        }
    }
}

