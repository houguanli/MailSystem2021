package com.java.HSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HInnerTextField extends JTextField {

    HInnerTextField self;

    private void init() {
        setBorder(null);
        setBackground(null);
        setFont(new Font("black", Font.PLAIN, 16));
        setPreferredSize(new Dimension(150, 30));
        setSize(150, 30);
        self = this;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (self.getText().getBytes().length > 30) {
                    setToolTipText("max len is 30!");
                    e.consume();
                } else setToolTipText(null);
            }
        });
    }

    HInnerTextField() {
        init();
    }

    HInnerTextField(int x, int y) {
        init();
        changePreferredSize(new Dimension(x, y));
    }

    public void changePreferredSize(Dimension dimension) {//need change
        setPreferredSize(dimension);
        setSize(dimension);
    }

}
