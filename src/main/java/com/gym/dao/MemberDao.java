package com.gym.dao;

import com.gym.model.Member;
import com.gym.util.DbHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {

    // 1. 根據 ID 查詢
    public Member findById(int id) throws SQLException {
        String sql = "SELECT * FROM member WHERE id = ?";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Member(
                    rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("email"), rs.getString("phone"), rs.getInt("balance"), rs.getString("role")
                );
            }
        }
        return null;
    }

    // 2. 根據帳號查詢 (登入用)
    public Member findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM member WHERE username = ?";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Member(
                    rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("email"), rs.getString("phone"), rs.getInt("balance"), rs.getString("role")
                );
            }
        }
        return null;
    }

    // 3. 新增會員 (註冊用)
    public void addMember(Member m) throws SQLException {
        String sql = "INSERT INTO member (username, password, email, phone, balance, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getUsername());
            ps.setString(2, m.getPassword());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            ps.setInt(5, m.getBalance());
            ps.setString(6, m.getRole());
            ps.executeUpdate();
        }
    }

    // 4. 會員修改個人資料
    public void updateMember(int id, String name, String phone, String email) throws SQLException {
        String sql = "UPDATE member SET username = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    // 5. 儲值功能
    public void deposit(int id, int amount) throws SQLException {
        String sql = "UPDATE member SET balance = balance + ? WHERE id = ?";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // 6. 記錄登入日誌
    public void logLogin(int memberId, String ip) {
        String sql = "INSERT INTO login_logs (member_id, login_time, ip_address) VALUES (?, NOW(), ?)";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setString(2, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // ★ 以下為本次新增的管理員功能 ★
    // ==========================================

    // 7. 查詢所有會員 (後台列表用)
    public List<Member> findAll() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM member";
        try (Connection conn = DbHelper.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Member(
                    rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("email"), rs.getString("phone"), rs.getInt("balance"), rs.getString("role")
                ));
            }
        }
        return list;
    }

    // 8. 管理員修改會員 (可改餘額與權限)
    public void updateMemberByAdmin(int id, String email, String phone, int balance, String role) throws SQLException {
        String sql = "UPDATE member SET email=?, phone=?, balance=?, role=? WHERE id=?";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, phone);
            ps.setInt(3, balance);
            ps.setString(4, role);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    // 9. 刪除會員 (交易模式：先刪選課 -> 再刪日誌 -> 最後刪人)
    public void deleteMember(int id) throws SQLException {
        Connection conn = DbHelper.getConnection();
        try {
            conn.setAutoCommit(false); // 開啟交易

            // 刪除選課紀錄
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM registration WHERE member_id = ?")) {
                ps.setInt(1, id); ps.executeUpdate();
            }
            
            // 刪除登入紀錄 (若無此表可註解掉)
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM login_logs WHERE member_id = ?")) {
                ps.setInt(1, id); ps.executeUpdate();
            }

            // 刪除會員本體
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM member WHERE id = ?")) {
                ps.setInt(1, id); ps.executeUpdate();
            }

            conn.commit(); // 提交
        } catch (SQLException e) {
            conn.rollback(); // 失敗則回滾
            throw e;
        } finally {
            conn.close();
        }
    }
}