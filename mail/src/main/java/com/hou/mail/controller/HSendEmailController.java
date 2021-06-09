package com.hou.mail.controller;

import com.hou.mail.bean.Drift;
import com.hou.mail.bean.Mail;
import com.hou.mail.bean.User;
import com.hou.mail.response.DriftRes;
import com.hou.mail.response.FileRes;
//import com.hou.mail.response.uploadInfo;
import com.hou.mail.response.MailRes;
import com.hou.mail.response.UserRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

@Controller
public class HSendEmailController {
    static private final ArrayList<String> hashBase = new ArrayList<String >();
    @RequestMapping("/sendEmail.html")
    public String MainToSend(
                             HttpSession httpSession) {
//        System.out.println(httpSession.getAttribute("FUCK"));
        httpSession.setAttribute("type", 0);
        httpSession.setAttribute("toAdd", "");
        httpSession.setAttribute("data", new dataHelper());
        return "sendEmail";
    }

    /**
     *to make mail be complete when upload
     */
    @PostMapping("/sending-mail")
    public String sending(@RequestParam("address") String address,
                          @RequestParam("title") String title,
                          @RequestParam("message") String message,
                          @RequestParam("type") String type,
                          @RequestParam("hashList") String fileHash,
                          HttpSession httpSession) {
        User from = (User)httpSession.getAttribute("user");
        int t = Integer.parseInt(type);
//       change_type('1')">私人邮件</li>
//       change_type('2')">群发邮件</li>
//       change_type('3')">漂流瓶</li>
        if(t == 1){//personal email
            User target = UserRes.getUserByEmail(address);
            if(from.getEmail().equals(address)){
                System.out.println("CANNOT SEND EMAIL TO YOURSELF!");
                return "main";
            } else if(target == null){//need to trun a fault page after
                System.out.println("Email " + address + "NOT find!");
                return "main";
            }else{
                int toID = target.getId();
                Mail mail = new Mail(from.getId(),toID, 2, -1, message + "\nMESSAGE_EOF\n" + fileHash,  title, 1);
                int res = MailRes.insertNewMail(mail);
                if(res != 0) {
                    System.out.println("catch fault on inserting new mail!");
                    return "main";
                }else{
                    System.out.println(mail);
                }
                return "main";
            }

        }else if(t == 2){//group email
            Scanner scanner = new Scanner(address); scanner.useDelimiter(";");
            while (scanner.hasNext()){
                String add = scanner.next();
                System.out.println("CHECK ADD " + add);
                User target = UserRes.getUserByEmail(add);
                if(from.getEmail().equals(add)){
                    System.out.println("CANNOT SEND EMAIL TO YOURSELF!");
                } else if(target == null){//need to trun a fault page after
                    System.out.println("this Email " + address + "NOT find!");
                }else{
                    int toID = target.getId();
                    Mail mail = new Mail(from.getId(),toID, 2, -1, message + "\nMESSAGE_EOF\n" + fileHash,  title, 2);
                    int res = MailRes.insertNewMail(mail);
                    if(res != 0) {
                        System.out.println("catch fault on inserting new mail!");
                    }else{
                        System.out.println("add " + add + "send success! ");
                    }
                }
            }
            return "main";
        }else if(t == 3){//a drift no need to has to id(toID = 0
            Mail mail = new Mail(from.getId(),0, 2, -1, message + "\nMESSAGE_EOF\n" + fileHash,  title, 3);
            int res = MailRes.insertNewMail(mail);
            int insID = DriftRes.getNewestDriftID();
            if(res != 0 || insID <= 0) {
                System.out.println("catch fault on inserting new mail!");
            }else{
                System.out.println("send mail success! " +
                        "\n + trying to send drift");
                int ans = DriftRes.insertNewDrift(new Drift(-1, insID));
                if(ans != 0) System.out.println("ERR ON SENDING DRIFT");
            }
        }else {//a draft also need to id,but may not content it
            int toID;
            User target = UserRes.getUserByEmail(address);
            if(from.getEmail().equals(address) || target == null){
                toID = 0;
//                System.out.println("CANNOT SEND EMAIL TO YOURSELF!");
//                return "main";
            } else{//need to trun a fault page after
                toID = target.getId();
            }
            Mail mail = new Mail(from.getId(),toID, 1, -1, message + "\nMESSAGE_EOF\n" + fileHash,  title, -1);
            int res = MailRes.insertNewMail(mail);
            if(res != 0) {
                System.out.println("catch fault on inserting new draft!");
            }else{
                System.out.println("send success! ");
            }
        }
        return "main";
    }

    /**
     * @return : 1: client err 0 ok -1 upload fail -2 hash fail
     */
    @PostMapping("/sendEmail-upload")
    @ResponseBody
    public FileRes.uploadInfo upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("NO SUCH FILE!");
            return null;
        } else {
//            FileRes.uploadInfo
            var res = FileRes.uploadMultiPartFile(file);
            if(res == null) {
                System.out.println("UPLOAD FAILED");
                return null;
            }
            if(res.hash != null) hashBase.add(res.hash);
            return res;
        }

    }
    static class dataHelper{
        public int getStatus() {
            return status;
        }

        public int getType() {
            return type;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setType(int type) {
            this.type = type;
        }

        private int status = 0, type = 0;
        public void debug(){
            System.out.println(status + " " + type);
        }
    }
//    static class ShanQingFriendsHelper{
//        public Voice check(GroupFriend someone){
//            if(someone.equals(new GroupFriend("brother jie"))) return new Voice("No! brother jie\n");
//            else if(someone.equals(new GroupFriend("やじゅうせんぱい"))) return new Voice("heng heng heng aaaaaaa\n");
//            else if(someone.equals(new GroupFriend("cold water"))) return new Voice("let me see see!\n");
//            else return new Voice("GO! fencing with me!\n");
//        }
//        public void f_ckShanQing(GroupFriend someone) throws NullPointerException{
//            while (someone.hasMore()) someone.doWith(this);
//            this.out.println("just this?");
//        }
//    }
}
