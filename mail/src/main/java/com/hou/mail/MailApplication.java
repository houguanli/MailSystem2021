package com.hou.mail;

import com.hou.mail.response.RegisterRes;
import com.hou.mail.response.UserRes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailApplication {
/*
                         -----           ------
                      .' .-.  \       .'  .-.  \
                     : /'$$|  |      |  .@"$\  |
                    | |$u$$| |       | |$$,$$| |
                    | `:$$:'   ----  | :$$$$$: | -----、
                 :    `"--'             `"--'         |
                :##.       ==   ==                    |
                |##:                             ###  |
                |#'     `.          .'          `###'/
                 \        ‘‘'…………'’’               /
                  \_                            ./
                  /`-__________________________/
                      大大呱保佑，代码不要出bug
 */
    public static void main(String[] args) {
//        System.out.println("HelloWorld!");
        SpringApplication.run(MailApplication.class, args);
//        autoReg();
    }

    static void autoReg(){
        String pass = "123456", userHead = "test", old = "www.test";
        for (int i = 0; i < 50; i++){
            String user = i + "@HEmail.com";
            System.out.println("On updating user mail set old " + old + user + " to " + userHead + user);
            int res = UserRes.updateUserMail(old + user, userHead + user);
//            int res = RegisterRes.register(user, pass);
            System.out.println(res == 0 ? "REG SUCCESS! " : ("FALSE WITH status " + res));
        }
    }

    static void autoMail(){

    }

    static void autoRel(){
        
    }
}
