package com.gym.controller;

import com.gym.view.*;
import com.gym.model.*;
import com.gym.dao.*;
import com.gym.util.*;
import org.apache.poi.xssf.usermodel.*;
import javax.swing.*;
import java.awt.Image;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController {
    private MainFrame view;
    private Member user;
    private RegistrationDao rDao = new RegistrationDao();
    private MemberDao mDao = new MemberDao();
    private CourseDao cDao = new CourseDao();

    public MainController(MainFrame v, Member u) {
        this.view = v; this.user = u;
        // 權限控管：非管理員隱藏後台 (從後面開始刪，避免索引跑掉)
        // Tab 4: 會員管理, Tab 3: 課程管理
        if(!"ADMIN".equalsIgnoreCase(u.getRole())) {
            view.tabs.remove(4); 
            view.tabs.remove(3); 
        }
        
        init(); 
        refresh();
        new javax.swing.Timer(1000, e -> view.lblTime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()))).start();
    }

    private void init() {
        view.txtName.setText(user.getUsername());
        view.txtPhone.setText(user.getPhone());
        view.txtMail.setText(user.getEmail());

        // 1. 選課
        view.btnReg.addActionListener(e -> {
            int r = view.courseTable.getSelectedRow();
            if(r != -1) {
                try {
                    int cid = (int)view.courseModel.getValueAt(r, 0);
                    rDao.register(user.getId(), cid);
                    Toast.show("選課成功！已發送通知信", view.getLocation(), view.getSize());
                    // Email 功能 (可選)
                    // new Thread(() -> EmailUtil.sendEmail(user.getEmail(), "選課成功", "您已成功預約課程！")).start();
                    user = mDao.findById(user.getId()); 
                    refresh();
                } catch(Exception ex) { JOptionPane.showMessageDialog(view, ex.getMessage()); }
            }
        });

        // 2. 退選
        view.btnCancel.addActionListener(e -> {
            int r = view.myTable.getSelectedRow();
            if(r != -1) {
                try {
                    int regId = (int)view.myModel.getValueAt(r, 0);
                    rDao.cancel(regId, user.getId()); 
                    Toast.show("退選成功！費用已退還", view.getLocation(), view.getSize());
                    user = mDao.findById(user.getId());
                    refresh();
                } catch(Exception ex) { JOptionPane.showMessageDialog(view, "退選失敗"); }
            }
        });

        // 3. 儲值
        view.btnDeposit.addActionListener(e -> {
            try {
                int amt = Integer.parseInt(view.txtDepAmt.getText());
                mDao.deposit(user.getId(), amt);
                user = mDao.findById(user.getId());
                refresh();
                Toast.show("儲值成功！", view.getLocation(), view.getSize());
                view.txtDepAmt.setText("");
            } catch(Exception ex) { JOptionPane.showMessageDialog(view, "金額格式錯誤"); }
        });

        // 4. 個人資料修改
        view.btnSave.addActionListener(e -> {
            try {
                mDao.updateMember(user.getId(), view.txtName.getText(), view.txtPhone.getText(), view.txtMail.getText());
                user = mDao.findById(user.getId());
                JOptionPane.showMessageDialog(view, "修改成功");
            } catch(Exception ex) { ex.printStackTrace(); }
        });

        // 5. 匯出 Excel
        view.btnExp.addActionListener(e -> exportExcel());

        // ======================================
        // ★ 課程管理 (Admin)
        // ======================================
        
        // 點選表格回填資料 (Data Binding)
        view.adminTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = view.adminTable.getSelectedRow();
                if (r != -1) {
                    view.adName.setText(view.adminModel.getValueAt(r, 1).toString());
                    view.adTeach.setText(view.adminModel.getValueAt(r, 2).toString());
                    view.adPrice.setText(view.adminModel.getValueAt(r, 3).toString());
                    
                    String capStr = view.adminModel.getValueAt(r, 4).toString(); // "11/20"
                    if(capStr.contains("/")) view.adCap.setText(capStr.split("/")[1]);
                    else view.adCap.setText(capStr);

                    view.adDay.setText(view.adminModel.getValueAt(r, 5).toString());
                    view.adTime.setText(view.adminModel.getValueAt(r, 6).toString());
                    view.adImg.setText(view.adminModel.getValueAt(r, 7).toString());
                }
            }
        });

        view.btnAddC.addActionListener(e -> {
            try {
                cDao.addCourse(view.adName.getText(), view.adTeach.getText(), Integer.parseInt(view.adPrice.getText()), 
                               Integer.parseInt(view.adCap.getText()), view.adDay.getText(), view.adTime.getText(), view.adImg.getText());
                refresh();
                JOptionPane.showMessageDialog(view, "新增成功");
            } catch(Exception ex) { JOptionPane.showMessageDialog(view, "輸入錯誤"); }
        });

        view.btnDelC.addActionListener(e -> {
            int r = view.adminTable.getSelectedRow();
            if(r != -1) {
                try {
                    cDao.deleteCourse((int)view.adminModel.getValueAt(r, 0));
                    refresh();
                } catch(Exception ex) { ex.printStackTrace(); }
            } else {
                JOptionPane.showMessageDialog(view, "請先選擇課程");
            }
        });

        // ======================================
        // ★ 會員管理 (Admin)
        // ======================================

        // 點選表格回填資料
        view.memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = view.memberTable.getSelectedRow();
                if (r != -1) {
                    view.memId.setText(view.memberModel.getValueAt(r, 0).toString());
                    view.memName.setText(view.memberModel.getValueAt(r, 1).toString());
                    view.memEmail.setText(view.memberModel.getValueAt(r, 2).toString());
                    view.memPhone.setText(view.memberModel.getValueAt(r, 3).toString());
                    view.memBalance.setText(view.memberModel.getValueAt(r, 4).toString());
                    view.memRole.setText(view.memberModel.getValueAt(r, 5).toString());
                }
            }
        });

        // 更新會員
        view.btnUpdMem.addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.memId.getText());
                int bal = Integer.parseInt(view.memBalance.getText());
                mDao.updateMemberByAdmin(id, view.memEmail.getText(), view.memPhone.getText(), bal, view.memRole.getText());
                refresh();
                JOptionPane.showMessageDialog(view, "會員資料已更新");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(view, "請先選擇會員或檢查格式");
            }
        });

        // 刪除會員
        view.btnDelMem.addActionListener(e -> {
            int r = view.memberTable.getSelectedRow();
            if(r != -1) {
                int opt = JOptionPane.showConfirmDialog(view, "確定刪除此會員？\n這將一併刪除他的選課紀錄！", "警告", JOptionPane.YES_NO_OPTION);
                if(opt == JOptionPane.YES_OPTION) {
                    try {
                        int mid = (int)view.memberModel.getValueAt(r, 0);
                        mDao.deleteMember(mid);
                        refresh();
                        JOptionPane.showMessageDialog(view, "會員已刪除");
                    } catch(Exception ex) {
                        JOptionPane.showMessageDialog(view, "刪除失敗: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view, "請先選擇會員");
            }
        });
    }

    public void refresh() {
        try {
            view.lblInfo.setText("使用者: " + user.getUsername() + " | 餘額: $" + user.getBalance());

            // 清空所有表格
            view.courseModel.setRowCount(0);
            view.adminModel.setRowCount(0);
            view.memberModel.setRowCount(0);
            
            Map<String, Integer> chartData = new HashMap<>();
            
            // 載入課程
            for(Course c : cDao.findAll()) {
                ImageIcon icon = null;
                try {
                    String path = "src/main/resources/img/" + c.getImgPath();
                    File f = new File(path);
                    if(f.exists()) {
                        Image img = new ImageIcon(path).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(img);
                    }
                } catch(Exception e) {}
                
                // 剩餘名額
                int remaining = c.getMaxCapacity() - c.getCurrentCapacity();

                // 選課表
                view.courseModel.addRow(new Object[]{
                    c.getId(), c.getCourseName(), c.getTeacher(), c.getPrice(), 
                    "剩餘: " + remaining, c.getCourseDay() + " " + c.getCourseTime(), icon
                });
                
                // 後台課程表
                view.adminModel.addRow(new Object[]{
                    c.getId(), c.getCourseName(), c.getTeacher(), c.getPrice(), 
                    c.getCurrentCapacity() + "/" + c.getMaxCapacity(),
                    c.getCourseDay(), c.getCourseTime(), c.getImgPath()     
                });

                chartData.put(c.getCourseName(), c.getCurrentCapacity());
            }

            view.updateChart(chartData);

            // 載入我的課表
            view.myModel.setRowCount(0);
            for(Vector<Object> v : rDao.getMySchedule(user.getId())) {
                view.myModel.addRow(v);
            }

            // ★ 載入會員列表 (僅限管理員)
            if("ADMIN".equalsIgnoreCase(user.getRole())) {
                for(Member m : mDao.findAll()) {
                    view.memberModel.addRow(new Object[]{
                        m.getId(), m.getUsername(), m.getEmail(),
                        m.getPhone(), m.getBalance(), m.getRole()
                    });
                }
            }

        } catch(Exception e) { 
            e.printStackTrace(); 
        }
    }

    private void exportExcel() {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("我的課表");
            
            XSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            
            XSSFFont headerFont = wb.createFont();
            headerFont.setFontName("微軟正黑體"); 
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);

            XSSFCellStyle dataStyle = wb.createCellStyle();
            dataStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER); 
            
            XSSFFont dataFont = wb.createFont();
            dataFont.setFontName("微軟正黑體");
            dataStyle.setFont(dataFont);

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(20); 
            
            for (int j = 0; j < view.myTable.getColumnCount(); j++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(j);
                cell.setCellValue(view.myTable.getColumnName(j));
                cell.setCellStyle(headerStyle);
            }

            sheet.createFreezePane(0, 1);

            for (int i = 0; i < view.myTable.getRowCount(); i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1); 
                row.setHeightInPoints(18); 
                
                for (int j = 0; j < view.myTable.getColumnCount(); j++) {
                    org.apache.poi.ss.usermodel.Cell cell = row.createCell(j);
                    Object val = view.myTable.getValueAt(i, j);
                    
                    if (val instanceof Integer) {
                        cell.setCellValue((Integer) val);
                    } else if (val instanceof Double) {
                        cell.setCellValue((Double) val);
                    } else {
                        cell.setCellValue(val != null ? val.toString() : "");
                    }
                    cell.setCellStyle(dataStyle);
                }
            }

            for (int j = 0; j < view.myTable.getColumnCount(); j++) {
                sheet.autoSizeColumn(j);
                int currentWidth = sheet.getColumnWidth(j);
                sheet.setColumnWidth(j, (int)(currentWidth * 1.2)); 
            }

            String filePath = System.getProperty("user.home") + "/Desktop/GymReport_Professional.xlsx";
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                wb.write(out);
                JOptionPane.showMessageDialog(view, "匯出成功！\n檔案已儲存至: " + filePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "匯出失敗: " + e.getMessage());
        }
    }
}