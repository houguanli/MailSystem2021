package com.java.HSwing;

import javax.swing.*;
import java.awt.*;

public class HFrame extends JFrame {
    private void init() {
        getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
        String iconPath = "source\\pic\\programIcon.jpg";
        ImageIcon imageIcon = new ImageIcon(iconPath);
        setIconImage(imageIcon.getImage());
        HInnerPasswordField hPasswordField = new HInnerPasswordField();
        HPasswordField htest = new HPasswordField();
        getContentPane().add(htest);
        HTextField hTextField = new HTextField();
        getContentPane().add(hTextField);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    HFrame() {
        init();
    }

    HFrame(int x, int y) {
        init();
        this.setSize(x, y);
    }

    public static void main(String[] args) {
        new HFrame(800, 600);
    }
}
