package com.hou.mail.controller;

import com.hou.mail.component.StorageService;
import com.hou.mail.response.FileRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Controller
public class HFileUploadController {
//    @Value("${filePath}")
    private final String filePath =
            System.getProperty("user.dir") + "\\files\\upload\\";

    //    @Autowired
//    public HFileUploadController(StorageService storageService){
//        this.storageService = storageService;
//    }
//    @ResponseBody
    @PostMapping("/suc-upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        int ans = -1;
        String res = "success";
        System.out.println(filePath);
        try {
            ans = FileRes.uploadFile(file.getBytes(), filePath, file.getOriginalFilename());
            System.out.println(file.getOriginalFilename());
        } catch (Exception e) {
            System.out.println("Fail to load");
            e.printStackTrace();
//            return "fail";
        }
        if(ans == 0){
            System.out.println("ok!");
            request.setAttribute("loadMes", res);
        }else {
            request.setAttribute("loadMes", "fail");
        }
        return "success";
    }
}
