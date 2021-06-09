package com.java.HSwing;

import javax.swing.*;
import java.awt.*;

public class HTextField extends JPanel {
    HInnerTextField input = new HInnerTextField();

    private void initInput() {
        JPanel emp = new JPanel(), emp1 = new JPanel(), emp2 = new JPanel();
        emp.setPreferredSize(new Dimension(10, 0));
        emp1.setPreferredSize(new Dimension(10, 1));
        emp2.setPreferredSize(new Dimension(10, 2));
        emp1.setBackground(null);
        emp2.setBackground(null);
        add(emp, BorderLayout.WEST);
        add(emp1, BorderLayout.NORTH);
        add(emp2, BorderLayout.SOUTH);
        add(input, BorderLayout.CENTER);
    }

    //    private void initHidePane(){
//        hidePane.setToolTipText("max length is 20!");
//        add(hidePane);
//
//    }
    private void init() {
//        System.out.println(input.getWidth() + " " + input.getHeight());
        setPreferredSize(new Dimension(input.getWidth() + 20, input.getHeight() + 3));
        setPreferredSize(new Dimension(300, 33));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setLayout(new BorderLayout());
        setBorder(new HLineBorder(new Color(92, 187, 255), 1, true));
        initInput();


    }

    HTextField() {
        init();
    }

    HTextField(int x, int y) {
        init();
        changePreferredSize(new Dimension(x, y));
    }

    public void changePreferredSize(Dimension dimension) {//need change
        input.setPreferredSize(new Dimension(dimension.width - 10, dimension.height - 2));
        setPreferredSize(dimension);
    }

    public String getText() {
        return input.getText();
    }
}
