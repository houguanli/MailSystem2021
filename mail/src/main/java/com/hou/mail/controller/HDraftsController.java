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
import java.util.Scanner;

@Controller
public class HDraftsController {
    private static ArrayList<dataHelper> myDraftsInfo;
    private static ArrayList<String> hashList, textList;
//    private static ArrayList<String> draftTarget;//maybe nothing
    @RequestMapping("/drafts.html")
    public String MainToDrafts(HttpSession httpSession){
        hashList = new ArrayList<>(); textList = new ArrayList<>();
        myDraftsInfo = new ArrayList<>();
        User user = (User)httpSession.getAttribute("user");
        ArrayList<Mail> myDrafts = MailRes.getDraftMail(user.getId());
        httpSession.setAttribute("drafts", myDrafts);
//        ArrayList<String> draftTarget = new ArrayList<>();
        for (Mail myDraft : myDrafts) {
            int i = myDraft.getToId();
            User tar = UserRes.getUserByID(i);
            dataHelper draftTarget = new dataHelper();
            draftTarget.target = (tar == null ? "" : tar.getEmail());
            draftTarget.mail = myDraft;
            myDraftsInfo.add(draftTarget);
        }
        httpSession.setAttribute("drafts", myDraftsInfo);
        return "drafts";
    }

    /**
     * TODO: reset the message, add in httpSession
     */
    @PostMapping("/draft-to-send")
    public String DraftToSendMail(HttpSession httpSession,
    @RequestParam("index") String index){
        dataHelper tmp = myDraftsInfo.get(Integer.parseInt(index));
        httpSession.setAttribute("tmpDraft", tmp);
        Scanner scanner = new Scanner(tmp.mail.getMessage());
        StringBuilder showMes = new StringBuilder();
//            String perMes = "";
//            System.out.println();
        while (scanner.hasNext()) {
            String t = scanner.nextLine();
            if(t.equals("MESSAGE_EOF\n") || t.equals("MESSAGE_EOF")) break;
            textList.add(t);
            showMes.append("\n").append(t);
        }
        while (scanner.hasNext()) {
            String t = scanner.nextLine();
            hashList.add(t);
        }
        tmp.mail.setMessage("");
        httpSession.setAttribute("hashList", hashList);
        httpSession.setAttribute("textList", textList);
        System.out.println("turn to send mail");
        return "draft_edit";
    }
    static class dataHelper{
        public Mail mail; public String target;

    }

    @PostMapping("/update-draft")
    public String sending(@RequestParam("address") String address,
                          @RequestParam("title") String title,
                          @RequestParam("message") String message,
                          @RequestParam("type") String type,
                          @RequestParam("hashList") String fileHash,
                          HttpSession httpSession) {
        User from = (User)httpSession.getAttribute("user");
        dataHelper tmp = (dataHelper)httpSession.getAttribute("tmpDraft");
        int id = tmp.mail.getId();
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
                Mail mail = new Mail(from.getId(),toID, 2, id, message + "\nMESSAGE_EOF\n" + fileHash,  title, 1);
                int res = MailRes.updateOldEmail(mail);
                if(res != 0) {
                    System.out.println("catch fault on updating new mail!");
                    return "main";
                }else{
                    System.out.println(mail);
                }
                return "main";
            }

        }else if(t == 2){
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
                    Mail mail = new Mail(from.getId(),toID, 2, id, message + "\nMESSAGE_EOF\n" + fileHash,  title, 2);
                    int res = MailRes.updateOldEmail(mail);
                    if(res != 0) {
                        System.out.println("catch fault on inserting new mail!");
                    }else{
                        System.out.println("add " + add + "send success! ");
                    }
                }
            }
            return "main";
        }else if(t == 3){//a drift no need to has to id(toID = 0
            Mail mail = new Mail(from.getId(),0, 2, id, message + "\nMESSAGE_EOF\n" + fileHash,  title, 3);
            int res = MailRes.updateOldEmail(mail);
            if(res != 0) {
                System.out.println("catch fault on inserting new mail!");
            }else{
                System.out.println("send success! ");
            }
        }else {//a draft may doesn not need to id,but may content it
            int toID;
            User target = UserRes.getUserByEmail(address);
            if(from.getEmail().equals(address) || target == null){
                toID = 0;
//                System.out.println("CANNOT SEND EMAIL TO YOURSELF!");
//                return "main";
            } else{//need to trun a fault page after
                toID = target.getId();
            }
            Mail mail = new Mail(from.getId(),toID, 1, id, message + "\nMESSAGE_EOF\n" + fileHash,  title, -1);
            int res = MailRes.updateOldEmail(mail);
            if(res != 0) {
                System.out.println("catch fault on inserting new draft!");
            }else{
                System.out.println("send success! ");
            }
        }
        return "main";
    }

}
