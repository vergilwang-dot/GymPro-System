package com.gym.app;

import com.formdev.flatlaf.FlatLightLaf;
import com.gym.controller.MainController;
import com.gym.dao.MemberDao;
import com.gym.model.Member;
import com.gym.util.SecurityUtil;
import com.gym.view.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

public class MainApp {
    // 建立 DAO 實例供登入註冊使用
    private static MemberDao memberDao = new MemberDao();

    public static void main(String[] args) {
        // 1. 初始化 UI 美化 (FlatLaf)
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 10); // 按鈕圓角
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            System.err.println("FlatLaf 初始化失敗");
        }

        // 2. 啟動登入畫面
        SwingUtilities.invokeLater(MainApp::showLoginDialog);
    }

    // 顯示登入/註冊對話框
    private static void showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        
        panel.add(new JLabel("帳號 (Username):"));
        panel.add(txtUser);
        panel.add(new JLabel("密碼 (Password):"));
        panel.add(txtPass);

        Object[] options = {"登入", "註冊新會員", "離開"};
        
        // 彈出對話框
        int result = JOptionPane.showOptionDialog(null, panel, "GymPro 系統登入",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result == 0) { // 點擊「登入」
            handleLogin(txtUser.getText(), new String(txtPass.getPassword()));
        } else if (result == 1) { // 點擊「註冊」
            handleRegister();
        } else {
            System.exit(0); // 離開
        }
    }

    // 處理登入邏輯
    private static void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "請輸入帳號與密碼！");
            showLoginDialog();
            return;
        }

        try {
            // 1. 從資料庫找人
            Member user = memberDao.findByUsername(username);

            // 2. 驗證密碼 (使用 BCrypt Check)
            if (user != null && SecurityUtil.checkPassword(password, user.getPassword())) {
                
                // (選用) 可以在這裡抓取 IP 並寫入 login_logs
            	try {
                    // 取得本機 IP (例如 192.168.x.x)
                    String ip = InetAddress.getLocalHost().getHostAddress();
                    // 呼叫 DAO 寫入資料庫
                    memberDao.logLogin(user.getId(), ip);
                    System.out.println("已記錄登入 IP: " + ip);
                } catch (Exception ex) {
                    System.err.println("IP 記錄失敗 (不影響登入): " + ex.getMessage());
                }

                // 3. 登入成功 -> 啟動主程式
                MainFrame view = new MainFrame();
                new MainController(view, user); // Controller 會負責初始化資料
                view.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "帳號或密碼錯誤！", "登入失敗", JOptionPane.ERROR_MESSAGE);
                showLoginDialog(); // 重新顯示登入框
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "系統錯誤: " + e.getMessage());
        }
    }

    // 處理註冊邏輯
    private static void handleRegister() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JTextField txtEmail = new JTextField();
        JTextField txtPhone = new JTextField();

        panel.add(new JLabel("設定帳號:")); 
        panel.add(txtUser);
        panel.add(new JLabel("設定密碼 (需含大寫且>6位):")); 
        panel.add(txtPass);
        panel.add(new JLabel("Email:")); 
        panel.add(txtEmail);
        panel.add(new JLabel("電話:")); 
        panel.add(txtPhone);

        int res = JOptionPane.showConfirmDialog(null, panel, "會員註冊", JOptionPane.OK_CANCEL_OPTION);
        
        if (res == JOptionPane.OK_OPTION) {
            String rawPwd = new String(txtPass.getPassword());
            String username = txtUser.getText();

            // 1. 檢查輸入
            if (username.isEmpty() || rawPwd.isEmpty()) {
                JOptionPane.showMessageDialog(null, "帳號密碼不得為空！");
                handleRegister();
                return;
            }

            // 2. 檢查密碼強度 (使用 SecurityUtil)
            if (!SecurityUtil.isValidPassword(rawPwd)) {
                JOptionPane.showMessageDialog(null, "密碼強度不足！\n請確保長度大於 6 且包含至少一個大寫字母。", "格式錯誤", JOptionPane.WARNING_MESSAGE);
                handleRegister(); // 讓使用者重填
                return;
            }

            try {
                // 3. 密碼加密 (Hash)
                String hashedPwd = SecurityUtil.hashPassword(rawPwd);

                // 4. 建立物件 (預設餘額 0, 角色 USER)
                // Member 建構子順序: id, username, password, email, phone, balance, role
                Member newMember = new Member(null, username, hashedPwd, txtEmail.getText(), txtPhone.getText(), 0, "USER");
                
                // 5. 寫入資料庫
                memberDao.addMember(newMember);
                
                JOptionPane.showMessageDialog(null, "註冊成功！請重新登入。");
                showLoginDialog(); // 回到登入畫面

            } catch (Exception e) {
                // 通常是帳號重複 (SQL IntegrityConstraintViolationException)
                JOptionPane.showMessageDialog(null, "註冊失敗 (帳號可能已存在): " + e.getMessage());
                handleRegister();
            }
        } else {
            showLoginDialog(); // 取消註冊則回到登入頁
        }
    }
}