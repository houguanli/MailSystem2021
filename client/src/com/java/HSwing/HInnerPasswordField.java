package com.java.HSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HInnerPasswordField extends JPasswordField {

    HInnerPasswordField self;

    private void init() {
        setBorder(null);
        setBackground(null);
        setFont(new Font("black", Font.BOLD, 16));
        setEchoChar('*');
        setPreferredSize(new Dimension(150, 30));
        setSize(150, 30);
        self = this;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (self.getPassword().length > 30) {
                    setToolTipText("max len is 30!");

                    e.consume();
                } else setToolTipText(null);
            }
        });
    }

    HInnerPasswordField() {
        init();
    }

    HInnerPasswordField(int x, int y) {
        init();
        changePreferredSize(new Dimension(x, y));
    }

    public void changePreferredSize(Dimension dimension) {//need change
        setSize(dimension);
        setPreferredSize(dimension);
    }
}
