package com.gym.view;

import javax.swing.*;
import java.awt.*;

public class Toast extends JDialog {
    public static void show(String message, Point ownerLoc, Dimension ownerSize) {
        JDialog d = new JDialog();
        d.setUndecorated(true);
        d.setLayout(new GridBagLayout());
        d.setBackground(new Color(0, 0, 0, 170)); // 半透明黑色背景
        
        JLabel l = new JLabel(message);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
        d.add(l);
        d.pack();
        
        // 計算顯示位置 (視窗中下方)
        int x = ownerLoc.x + (ownerSize.width - d.getWidth()) / 2;
        int y = ownerLoc.y + ownerSize.height - 100;
        d.setLocation(x, y);
        
        d.setAlwaysOnTop(true);
        d.setVisible(true);
        
        // 2秒後自動消失
        new Timer(2000, e -> d.dispose()).start();
    }
}