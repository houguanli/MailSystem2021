package com.hou;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

    }

    public static void mmain(String[] args) throws EOFException {
        System.out.println("服务器运行...");
        Thread thread = new Thread(() -> {//这是lambda表达式，写在线程方法中非常方便
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                Socket socket = serverSocket.accept();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String iString = inputStream.readUTF();//读取信息要放在最前面，以读取客户端发来的信息
                    System.out.println(iString);
                    String aString = bufferedReader.readLine();
                    if (aString.equals("bye")) break;
                    //写入输出流
                    outputStream.writeUTF(aString);
                    //将输出流刷新
                    outputStream.flush();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("服务器停止");
        });
        thread.start();
    }
}

