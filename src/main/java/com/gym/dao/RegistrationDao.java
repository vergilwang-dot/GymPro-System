package com.gym.dao;

import com.gym.util.DbHelper;
import java.sql.*;
import java.util.Vector;

public class RegistrationDao {

    // 1. 選課功能
    public void register(int mid, int cid) throws Exception {
        Connection conn = DbHelper.getConnection();
        conn.setAutoCommit(false); // 開啟事務
        try {
            // A. 鎖定課程資料 (行鎖)
            String lockSql = "SELECT current_capacity, max_capacity, course_day, course_time, price FROM course WHERE id = ? FOR UPDATE";
            PreparedStatement ps1 = conn.prepareStatement(lockSql);
            ps1.setInt(1, cid);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) throw new Exception("課程不存在");
            
            if (rs.getInt("current_capacity") >= rs.getInt("max_capacity")) {
                throw new Exception("名額已滿，無法報名！");
            }
            
            String day = rs.getString("course_day");
            String time = rs.getString("course_time");
            int price = rs.getInt("price");

            // B. 衝堂檢查
            String conflictSql = "SELECT COUNT(*) FROM registration r JOIN course c ON r.course_id = c.id WHERE r.member_id = ? AND c.course_day = ? AND c.course_time = ?";
            PreparedStatement ps2 = conn.prepareStatement(conflictSql);
            ps2.setInt(1, mid); ps2.setString(2, day); ps2.setString(3, time);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next() && rs2.getInt(1) > 0) throw new Exception("此時段您已有其他課程！");

            // C. 扣款
            String deductSql = "UPDATE member SET balance = balance - ? WHERE id = ? AND balance >= ?";
            PreparedStatement ps3 = conn.prepareStatement(deductSql);
            ps3.setInt(1, price); ps3.setInt(2, mid); ps3.setInt(3, price);
            if (ps3.executeUpdate() == 0) throw new Exception("餘額不足！");

            // D. 寫入選課紀錄
            String regSql = "INSERT INTO registration (member_id, course_id) VALUES (?, ?)";
            PreparedStatement ps4 = conn.prepareStatement(regSql);
            ps4.setInt(1, mid); ps4.setInt(2, cid); 
            ps4.executeUpdate();
            
            // E. 更新課程人數 (+1)
            String capSql = "UPDATE course SET current_capacity = current_capacity + 1 WHERE id = ?";
            PreparedStatement ps5 = conn.prepareStatement(capSql);
            ps5.setInt(1, cid); ps5.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    // 2. 退選功能 (請確認這裡是否為 - 1)
    public void cancel(int regId, int memberId) throws Exception {
        Connection conn = DbHelper.getConnection();
        conn.setAutoCommit(false);
        try {
            // A. 查詢課程資訊
            String findSql = "SELECT c.price, c.id FROM registration r JOIN course c ON r.course_id = c.id WHERE r.id = ?";
            PreparedStatement ps1 = conn.prepareStatement(findSql);
            ps1.setInt(1, regId);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) throw new Exception("找不到該選課紀錄");
            
            int price = rs.getInt("price");
            int courseId = rs.getInt("id");

            // B. 退費
            String refundSql = "UPDATE member SET balance = balance + ? WHERE id = ?";
            PreparedStatement ps2 = conn.prepareStatement(refundSql);
            ps2.setInt(1, price); ps2.setInt(2, memberId);
            ps2.executeUpdate();

            // C. 刪除紀錄
            String delSql = "DELETE FROM registration WHERE id = ?";
            PreparedStatement ps3 = conn.prepareStatement(delSql);
            ps3.setInt(1, regId);
            ps3.executeUpdate();

            // D. 更新課程人數 (這裡必須是減 1)
            // ★★★ 關鍵修正點 ★★★
            String capSql = "UPDATE course SET current_capacity = current_capacity - 1 WHERE id = ?";
            PreparedStatement ps4 = conn.prepareStatement(capSql);
            ps4.setInt(1, courseId);
            ps4.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }
    
    // 3. 取得我的課表
    public Vector<Vector<Object>> getMySchedule(int mid) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT r.id, c.course_name, c.course_day, c.course_time, r.reg_date FROM registration r JOIN course c ON r.course_id = c.id WHERE r.member_id = ?";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mid); ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt(1)); row.add(rs.getString(2)); row.add(rs.getString(3)); 
                row.add(rs.getString(4)); row.add(rs.getString(5));
                data.add(row);
            }
        }
        return data;
    }
}