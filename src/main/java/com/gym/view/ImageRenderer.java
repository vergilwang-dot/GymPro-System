package com.gym.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

public class ImageRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ImageIcon) {
            JLabel label = new JLabel((ImageIcon) value);
            label.setHorizontalAlignment(JLabel.CENTER);
            // 如果被選中，保持背景色
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(table.getSelectionBackground());
            }
            return label;
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
