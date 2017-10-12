package JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

public class QueryExecutor {
    JFrame jf = new JFrame("查询执行器");
    private JScrollPane scrollPane;
    private JButton execBn = new JButton(" 查询");
    private JTextField sqlField = new JTextField(45);
    private static Connection conn;
    private static Statement stmt;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("mysql.ini"));
            String driver = props.getProperty("driver");
            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String pass = props.getProperty("pass");

            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(){
        JPanel top = new JPanel();
        top.add(new JLabel("输入查询语句: "));
        top.add(sqlField);
        top.add(execBn);
        execBn.addActionListener(new ExceListener());
        sqlField.addActionListener(new ExceListener());
        jf.add(top, BorderLayout.NORTH);
        jf.setSize(680,400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
    class ExceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            if(scrollPane != null){
                jf.remove(scrollPane);
            }
            try(ResultSet rs = stmt.executeQuery(sqlField.getText())) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Vector<String> columnName = new Vector<>();
                Vector<Vector<String>> data = new Vector<>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    columnName.add(rsmd.getColumnName(i + 1));
                }
                while (rs.next()) {
                    Vector<String> v = new Vector<>();
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        v.add(rs.getString(i + 1));
                    }
                    data.add(v);
                }
                JTable table = new JTable(data, columnName);
                scrollPane = new JScrollPane(table);
                jf.add(scrollPane);
                jf.validate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


    }
    public static void main(String[] args) {
        new QueryExecutor().init();
    }

}
