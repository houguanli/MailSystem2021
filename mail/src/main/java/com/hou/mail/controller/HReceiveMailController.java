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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Scanner;

@Controller
public class HReceiveMailController {
    private static ArrayList<Mail> arr = null;
    private static ArrayList<String> filePath = null, fileName = null;
    private static ArrayList<dataHelper> mailList;

    /**
     * TODO:: update the status of the mail
     *
     * @param httpSession: add new element for httpSession
     * @return :the result of the function page, need to rebuild after
     */
    @RequestMapping("/receiveEmail.html")
    public String MainToReceive(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        arr = MailRes.getReceiveMail(user.getId());
//        httpSession.setAttribute("mails", arr);
        if (arr == null) {
            System.out.println("UN HANDEL ERR! ON CAN NOT GET EMAILS!");
            return "receiveEmail";
        }
//        StringBuilder toDiv = new StringBuilder();
        mailList = new ArrayList<>();
        int rea = 0, unr = 0, len = arr.size(), tmp, i = 0;
        for (com.hou.mail.bean.Mail mail : arr) {
            tmp = mail.getStatus();
            if (tmp == 1) rea++;
            else if (tmp == 2) unr++;
            User from = UserRes.getUserByID(mail.getFromId());
            String icon_type = mail.getStatus() == 2 ? "unread" : "open",
                    tit = mail.getTitle(), nic = from == null ? "unknown user" : from.getNickname();
            mailList.add(new dataHelper(icon_type, nic, tit, mail.getId()));
        }
        httpSession.setAttribute("received", rea);
        httpSession.setAttribute("beenRead", unr);
        httpSession.setAttribute("mailList", mailList);
//        httpSession.setAttribute("toDiv", toDiv.toString());
        return "receiveEmail";
    }

    @PostMapping("/showMail.html")
    public String show(@RequestParam("index") String string
            , HttpSession httpSession) {
        try {
            System.out.println("RUN POST FOR " + string);
            Mail get = arr.get(Integer.parseInt(string));
            System.out.println(get);
            int res = MailRes.updateMailChangeMailToReadByID(get.getId());
            System.out.println("update mailID: " + get.getId() + " to been read from sent with result: " + res);
            get.setStatus(3);
            httpSession.setAttribute("title", get.getTitle());
            filePath = new ArrayList<>();
            fileName = new ArrayList<>();
            messageHelper helper = new messageHelper(get.getMessage());
            httpSession.setAttribute("messages", helper.message);
            httpSession.setAttribute("links", fileName);
//            System.out.println(get);
//            return "main";
        } catch (IndexOutOfBoundsException e) {
            System.out.println("OUT LIMIT!");
            return "main";
        }
        return "showMail";
    }

    @PostMapping("/del-receive")
    public String onDelMail(@RequestParam("index") String string
            , HttpSession httpSession) {
        try {
            int ind = Integer.parseInt(string);
            System.out.println("RUN POST FOR " + string);
            Mail get = arr.get(ind);
            System.out.println(get);
            int res = MailRes.updateMailChangeMailToDeletedByID(get.getId());
            System.out.println("update mailID: " + get.getId() + " to been deleted from sent with result: " + res);
            get.setStatus(3);
            httpSession.setAttribute("title", get.getTitle());
            arr.remove(ind);
            mailList.remove(ind);
            httpSession.setAttribute("mailList", mailList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("OUT LIMIT!");
            return "main";
        }
        return "receiveEmail";
    }

    @PostMapping("showMail-download")
    @ResponseBody
    public String downloadFile(@RequestParam("index") String index) {
        try {
            int in = Integer.parseInt(index);
            File src = new File(filePath.get(in));
            return FileRes.downloadFileInDefaultPath(fileName.get(in), src) + "";
        } catch (IndexOutOfBoundsException e) {
            System.out.println("handle error on file index " + index);
            e.printStackTrace();
            return "download fail!";
        }
    }

    static class messageHelper {
        public ArrayList<String> message;

        public messageHelper(String originalMessage) {
            message = new ArrayList<>();
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
                if (res == null) System.out.println("UNKNOWN FILE WITH HASH : " + tmp);
                else {
                    filePath.add(res.getPath());
                    fileName.add(res.getName());
                }
            }
        }
    }

    static class dataHelper {
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int id;

        public String getIcon_type() {
            return icon_type;
        }

        public void setIcon_type(String icon_type) {
            this.icon_type = icon_type;
        }

        public String getFrom_name() {
            return from_name;
        }

        public void setFrom_name(String from_name) {
            this.from_name = from_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String icon_type;
        public String from_name;

        public dataHelper(String icon_type, String from_name, String title, int id) {
            this.icon_type = icon_type;
            this.from_name = from_name;
            this.title = title;
        }

        public String title;
    }
}
