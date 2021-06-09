package com;

import com.hou.response.*;

public class Main {
    public static void main(String[] args) {
        int res = RegisterRes.register("test2", "hello");
//        int res = LoginRes.login("test1", "hello");
        if (res == 0) System.out.println("OK");
        else System.out.println("fail with error code " + res);
        res = RegisterRes.register("user1@mail.com", "123456");
        if (res == 0) System.out.println("OK");
        else System.out.println("fail with error code " + res);
    }
}
