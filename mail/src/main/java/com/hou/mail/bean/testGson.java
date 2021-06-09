package com.hou.mail.bean;

import com.google.gson.Gson;

public class testGson {


    public static void main(String[] args) {
        int anInt = 1;
        String getData = "data";
        String data = new Gson().toJson(new test(anInt, getData));
        test tes = new Gson().fromJson(data, test.class);
        System.out.println(tes.t1 + tes.r1);
    }
}
class test{
    int t1;
    String r1;

    public int getT1() {
        return t1;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public test(int t1, String r1) {
        this.t1 = t1;
        this.r1 = r1;
    }
}
