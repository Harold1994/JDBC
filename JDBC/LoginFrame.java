package JDBC;

import com.sun.org.apache.xpath.internal.SourceTree;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class LoginFrame {
    private final String PROP_FILE = "mysql.ini";
    private String driver;
    private String url;
    private String user;
    private String pass;
    
    private JFrame jf = new JFrame(" LOGIN ");
    private JTextField userField = new JTextField(20);
    private JTextField passField = new JTextField(20);
    private JButton loginBUtton = new JButton("登录");
    
    public void init() throws Exception{
        Properties connProp = new Properties();
        connProp.load(new FileInputStream(PROP_FILE));
        driver = connProp.getProperty("driver");
        url = connProp.getProperty("url");
        user = connProp.getProperty("user");
        pass = connProp.getProperty("pass");

        Class.forName(driver);

        loginBUtton.addActionListener(e ->{
            if(validate(userField.getText(),passField.getText())){
                JOptionPane.showMessageDialog(jf,"success login");
            }else
                JOptionPane.showMessageDialog(jf,"failed");
        });

        jf.add(userField, BorderLayout.NORTH);
        jf.add(passField);
        jf.add(loginBUtton,BorderLayout.SOUTH);
        jf.pack();
        jf.setVisible(true);
    }

    private boolean validate(String userName, String passWord) {
        String sql = "select * from  jdbc_test where jdbc_name='" +userName +"' and jdbc_id='" + passWord + "'";
        System.out.println(sql);
//        try(
//            Connection conn = DriverManager.getConnection(url, user, pass);
//            Statement stmt = conn.createStatement();
//            ResultSet re = stmt.executeQuery(sql))
//        {
//            if(re.next()){
//                return true;
//            }
//        }
        try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement psstmt = conn.prepareStatement("select * from  jdbc_test where jdbc_name=? and jdbc_id =?")
                ){
            psstmt.setString(1,userName);
            psstmt.setString(2,passWord);
            try( ResultSet rs = psstmt.executeQuery()){
                if(rs.next()){
                return true;
            }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        new LoginFrame().init();
    }
}
