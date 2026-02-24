package com.gym.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageGen {

    public static void main(String[] args) {
        // 定義要生成的圖片檔名與對應顯示的文字
        String[][] data = {
            {"yoga_hatha.jpg", "Hatha Yoga", "#FF9A9E", "#FECFEF"},    // 溫暖粉色系
            {"tabata_hiit.jpg", "TABATA", "#a18cd1", "#fbc2eb"},       // 紫色系
            {"weight_basic.jpg", "Weight Training", "#84fab0", "#8fd3f4"}, // 青綠色系
            {"pilates_core.jpg", "Pilates Core", "#ff9a9e", "#fecfef"}, // 重複色系保持協調
            {"zumba_dance.jpg", "Zumba Dance", "#fccb90", "#d57eeb"},   // 活力橘紫
            {"boxing_muay.jpg", "Boxing", "#e0c3fc", "#8ec5fc"},        // 藍紫色
            {"trx_train.jpg", "TRX Training", "#43e97b", "#38f9d7"},    // 鮮綠色
            {"meditation.jpg", "Meditation", "#a8edea", "#fed6e3"}      // 平靜藍粉
        };

        try {
            // 1. 確保資料夾存在
            String projectPath = System.getProperty("user.dir"); // 抓取專案根目錄
            File dir = new File(projectPath + "/src/main/resources/img");
            if (!dir.exists()) {
                dir.mkdirs();
                System.out.println("建立資料夾: " + dir.getAbsolutePath());
            }

            // 2. 迴圈生成圖片
            for (String[] item : data) {
                String filename = item[0];
                String text = item[1];
                Color color1 = Color.decode(item[2]); // 漸層起始色
                Color color2 = Color.decode(item[3]); // 漸層結束色

                generateImage(dir, filename, text, color1, color2);
            }
            
            System.out.println("✅ 所有圖片生成完畢！請在 Eclipse 專案上按 F5 (Refresh)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateImage(File dir, String filename, String text, Color c1, Color c2) throws IOException {
        int width = 400;  // 圖片寬度
        int height = 300; // 圖片高度

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 開啟抗鋸齒 (讓文字變平滑)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. 繪製漸層背景
        GradientPaint gp = new GradientPaint(0, 0, c1, width, height, c2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // 2. 繪製半透明遮罩 (讓文字更清楚)
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.fillOval(-50, -50, 300, 300); // 裝飾圓圈

        // 3. 繪製文字 (置中)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
        
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        
        // 文字陰影
        g2d.setColor(new Color(0,0,0, 60));
        g2d.drawString(text, x+2, y+2);
        
        // 文字本體
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
        
        // 4. 畫一個外框
        g2d.setStroke(new BasicStroke(8));
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.drawRect(0, 0, width, height);

        g2d.dispose();

        // 存檔
        File file = new File(dir, filename);
        ImageIO.write(image, "jpg", file);
        System.out.println("已產生: " + file.getName());
    }
}