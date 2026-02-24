package com.gym.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.util.Map;

public class MainFrame extends JFrame {
    public JTabbedPane tabs = new JTabbedPane();
    
    // 表格群
    public JTable courseTable = new JTable(); // 選課
    public JTable myTable = new JTable();     // 我的課表
    public JTable adminTable = new JTable();  // 課程管理
    public JTable memberTable = new JTable(); // ★ 會員管理
    
    public DefaultTableModel courseModel, myModel, adminModel, memberModel;
    
    // 按鈕群
    public JButton btnReg = new JButton("確認選課"), btnExp = new JButton("匯出Excel"), btnSave = new JButton("儲存個人資料");
    public JButton btnDeposit = new JButton("線上儲值"), btnCancel = new JButton("退選 (退費)");
    public JButton btnAddC = new JButton("新增課程"), btnDelC = new JButton("刪除課程");
    public JButton btnUpdMem = new JButton("更新會員"), btnDelMem = new JButton("刪除會員"); // ★ 新增按鈕

    // 輸入框群 (課程管理)
    public JTextField txtName = new JTextField(10), txtPhone = new JTextField(10), txtMail = new JTextField(15), txtDepAmt = new JTextField(5);
    public JTextField adName = new JTextField(8), adTeach = new JTextField(8), adPrice = new JTextField(5), adCap = new JTextField(3);
    public JTextField adDay = new JTextField(5), adTime = new JTextField(5), adImg = new JTextField(8);

    // ★ 輸入框群 (會員管理)
    public JTextField memId = new JTextField(3); 
    public JTextField memName = new JTextField(8);
    public JTextField memEmail = new JTextField(10);
    public JTextField memPhone = new JTextField(10);
    public JTextField memBalance = new JTextField(5);
    public JTextField memRole = new JTextField(5);

    public JLabel lblInfo = new JLabel(" "), lblTime = new JLabel(" ");
    public JPanel chartPanelContainer = new JPanel(new BorderLayout());

    public MainFrame() {
        setTitle("GymPro 終極智慧管理系統"); setSize(1200, 800); setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null);

        // 頂部狀態列
        JPanel top = new JPanel(new BorderLayout());
        top.add(lblInfo, BorderLayout.WEST); top.add(lblTime, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // 初始化表格
        courseModel = new DefaultTableModel(new String[]{"ID","課程","教練","價格","名額","時段","圖片"}, 0) {
            public Class<?> getColumnClass(int column) { return (column == 6) ? ImageIcon.class : Object.class; }
        };
        courseTable.setModel(courseModel);
        courseTable.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());

        myModel = new DefaultTableModel(new String[]{"編號","課程","星期","時間","日期"}, 0);
        myTable.setModel(myModel);

        adminModel = new DefaultTableModel(new String[]{"ID", "課程", "教練", "價格", "名額", "星期", "時段", "圖片路徑"}, 0);
        adminTable.setModel(adminModel);

        // ★ 初始化會員表格
        memberModel = new DefaultTableModel(new String[]{"ID", "帳號", "Email", "電話", "餘額", "權限"}, 0);
        memberTable.setModel(memberModel);
        memId.setEditable(false); // ID 禁止修改

        // 分頁組裝
        tabs.addTab("選購課程", createP(courseTable, btnReg));
        tabs.addTab("我的課表", createP(myTable, btnCancel, btnExp));
        tabs.addTab("數據分析", chartPanelContainer);
        tabs.addTab("課程管理", createAdminP());
        tabs.addTab("會員管理", createMemberP()); // ★ 新增分頁
        tabs.addTab("個人中心", createProfileP());
        add(tabs);

        style(this);
    }

    private JPanel createP(JTable t, JButton... btns) {
        JPanel p = new JPanel(new BorderLayout()); p.add(new JScrollPane(t));
        JPanel s = new JPanel(); for(JButton b : btns) s.add(b); p.add(s, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createAdminP() {
        JPanel p = new JPanel(new BorderLayout()); 
        p.add(new JScrollPane(adminTable));
        JPanel s = new JPanel(); 
        s.add(new JLabel("課程:")); s.add(adName); s.add(new JLabel("教練:")); s.add(adTeach);
        s.add(new JLabel("價:")); s.add(adPrice); s.add(new JLabel("額:")); s.add(adCap);
        s.add(new JLabel("日:")); s.add(adDay); s.add(new JLabel("時:")); s.add(adTime);
        s.add(new JLabel("圖:")); s.add(adImg);
        s.add(btnAddC); s.add(btnDelC);
        p.add(s, BorderLayout.SOUTH);
        return p;
    }

    // ★ 建立會員管理面板
    private JPanel createMemberP() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(memberTable));
        JPanel s = new JPanel();
        s.add(new JLabel("ID:")); s.add(memId);
        s.add(new JLabel("帳號:")); s.add(memName);
        s.add(new JLabel("Mail:")); s.add(memEmail);
        s.add(new JLabel("電話:")); s.add(memPhone);
        s.add(new JLabel("餘額:")); s.add(memBalance);
        s.add(new JLabel("權限:")); s.add(memRole);
        s.add(btnUpdMem); s.add(btnDelMem);
        p.add(s, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createProfileP() {
        JPanel p = new JPanel(new GridBagLayout()); GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(10,10,10,10);
        g.gridx=0; g.gridy=0; p.add(new JLabel("姓名:"), g); g.gridx=1; p.add(txtName, g);
        g.gridx=0; g.gridy=1; p.add(new JLabel("電話:"), g); g.gridx=1; p.add(txtPhone, g);
        g.gridx=0; g.gridy=2; p.add(new JLabel("Email:"), g); g.gridx=1; p.add(txtMail, g);
        g.gridx=0; g.gridy=3; p.add(new JLabel("儲值:"), g); 
        JPanel dep = new JPanel(); dep.add(txtDepAmt); dep.add(btnDeposit);
        g.gridx=1; p.add(dep, g);
        g.gridx=1; g.gridy=4; p.add(btnSave, g);
        return p;
    }

    public void updateChart(Map<String, Integer> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        data.forEach(dataset::setValue);
        
        JFreeChart chart = ChartFactory.createPieChart("熱門課程分佈", dataset, true, true, false);
        Font font = new Font("Microsoft JhengHei", Font.BOLD, 14);
        Font titleFont = new Font("Microsoft JhengHei", Font.BOLD, 20);
        
        if (chart.getTitle() != null) chart.getTitle().setFont(titleFont);
        if (chart.getLegend() != null) chart.getLegend().setItemFont(font);
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(font);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}人"));

        chartPanelContainer.removeAll();
        chartPanelContainer.add(new ChartPanel(chart));
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }

    private void style(Component c) {
        c.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        if (c instanceof JTable) ((JTable)c).setRowHeight(50);
        if (c instanceof Container) for (Component child : ((Container)c).getComponents()) style(child);
    }
}