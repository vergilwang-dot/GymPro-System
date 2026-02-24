package com.gym.dao;

import com.gym.model.Course;
import com.gym.util.DbHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    // 查詢所有課程
    public List<Course> findAll() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (Connection conn = DbHelper.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                    rs.getInt("id"), rs.getString("course_name"), rs.getString("teacher"),
                    rs.getInt("price"), rs.getInt("max_capacity"), rs.getInt("current_capacity"),
                    rs.getString("course_day"), rs.getString("course_time"), rs.getString("img_path")
                ));
            }
        }
        return list;
    }

    // 管理員：新增課程
    public void addCourse(String name, String teacher, int price, int maxCap, String day, String time, String imgPath) throws SQLException {
        String sql = "INSERT INTO course (course_name, teacher, price, max_capacity, course_day, course_time, img_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, teacher); ps.setInt(3, price);
            ps.setInt(4, maxCap); ps.setString(5, day); ps.setString(6, time); ps.setString(7, imgPath);
            ps.executeUpdate();
        }
    }

    // 管理員：刪除課程
    public void deleteCourse(int id) throws SQLException {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection conn = DbHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}