package com.java.HSwing;

import javax.swing.border.LineBorder;
import java.awt.*;

public class HLineBorder extends LineBorder {
    private int changVal(int st, int val) {
        return st >= val ? st - val : 0;
    }

    public HLineBorder(Color color) {
        super(color);
    }

    public HLineBorder(Color color, int thickness, boolean roundCorners) {
        super(color, thickness, roundCorners);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color oldColor = g.getColor();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(renderingHints);
        g2.setColor(lineColor);
        for (int i = 0; i < thickness; i++) {
            Color temp = new Color(changVal(lineColor.getRed(), i), changVal(lineColor.getGreen(), i), changVal(lineColor.getBlue(), i));
//            System.out.println(temp);
            g2.setColor(temp);
            g2.setStroke(new BasicStroke(3));
            if (!roundedCorners) {
                g2.drawRect(x + i, y + i, width - i - i - 1, height - i - i - 1);
            } else {
                g2.drawRoundRect(x + i, y + i, width - i - i - 1, height - i - i - 1, 20, 20);
            }
        }
        g2.setColor(oldColor);
//        super.paintBorder(c, g, x, y, width, height);
    }
}