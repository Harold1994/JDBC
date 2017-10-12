package JDBC;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

public class PreparedStatementTest {
    private String driver;
    private String url;
    private String user;
    private String pass;

    public void initPrarm(String paramFile) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(paramFile));
        driver = props.getProperty("driver");
        url = props.getProperty("url");
        user = props.getProperty("user");
        pass = props.getProperty("pass");
        Class.forName(driver);
    }

    public void insertUseStatement() throws Exception{
        long start = System.currentTimeMillis();
        try(
                Connection conn = DriverManager.getConnection(url,user,pass);
                Statement stmt = conn.createStatement()
                ){
            for(int i = 1; i< 100;i ++){
                stmt.executeUpdate("insert into jdbc_test values(" + i +  ",\"name" + i + "\",\"desc" + i+ "\")");
            }
            System.out.println("Statement uses:" + (System.currentTimeMillis() - start));
        }
    }

    public void insertUserPreapare() throws Exception{
        long start = System.currentTimeMillis();
        try(
                Connection conn = DriverManager.getConnection(url,user,pass);
                PreparedStatement pstmt = conn.prepareStatement("insert into jdbc_test values(?,?,?)")
        ){
            for(int i = 100; i< 200;i ++){
                pstmt.setObject(1,i);
                pstmt.setString(2,"name" + i);
                pstmt.setString(3,"desc" + i);
                pstmt.executeUpdate();
            }
            System.out.println("Statement uses:" + (System.currentTimeMillis() - start));
        }
    }

    public static void main(String[] args) throws Exception {
        PreparedStatementTest pt = new PreparedStatementTest();
        pt.initPrarm("mysql.ini");
//        pt.insertUseStatement();
        pt.insertUserPreapare();
    }

}
