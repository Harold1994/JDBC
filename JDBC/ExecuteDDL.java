package JDBC;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ExecuteDDL {
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
    }
/*
    public void createTable(String sql) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement())
        {
            stmt.executeUpdate(sql);
        }
    }
    */
    public int insertData(String sql) throws Exception{
        Class.forName(driver);
        try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement())
        {
            return stmt.executeUpdate(sql);
        }
    }

    public static void main(String[] args) throws Exception {
        ExecuteDDL ed = new ExecuteDDL();
        ed.initPrarm("mysql.ini");
//        ed.createTable("create table jdbc_test "
//        + "( jdbc_id int auto_increment primary key,"
//        + "jdbc_name varchar(255),"
//        + "jdbc_desc text);");
        int result = ed.insertData("insert into jdbc_test(jdbc_name) values(\"wang\",);");


        System.out.println(result);
    }
}
