package com.gym.util;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {
    // 檢查密碼規則：6位以上 + 包含一個大寫字母
    public static boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[A-Z].*");
    }

    // 加密
    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    // 驗證
    public static boolean checkPassword(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
}