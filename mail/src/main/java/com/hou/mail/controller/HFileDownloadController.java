package com.hou.mail.controller;

import com.hou.mail.response.FileRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.*;

@Controller
public class HFileDownloadController {
    private final String fromPath =
            System.getProperty("user.dir") + "\\files\\upload\\";
    private final String filePath =
            System.getProperty("user.dir") + "\\files\\download\\";
//    @ResponseBody
    @PostMapping("/download/local")
    public String downloadLocal(
            @RequestParam("fileName") String fileName,
            HttpSession httpSession){
        if(fileName != null){
            File src = new File(fromPath + fileName);
            System.out.println("download from: " + src);
            File target = new File(filePath + fileName);
            int ans = FileRes.downloadFile(src, target);
            if(ans == 0){
                httpSession.setAttribute("downloadMes", "success");
            }else {
                httpSession.setAttribute("downloadMes", "fail with code " + ans);
            }
        }
        return "success";
    }
}
