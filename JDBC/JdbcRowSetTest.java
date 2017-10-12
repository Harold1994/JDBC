package JDBC;

import com.sun.rowset.JdbcRowSetImpl;

import javax.sql.rowset.JdbcRowSet;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JdbcRowSetTest {
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

    public void update(String sql) throws Exception{
        Class.forName(driver);
        try(
                Connection conn = DriverManager.getConnection(url,user,pass);
                JdbcRowSet jdbcRs = new JdbcRowSetImpl(conn))
        {
            jdbcRs .setCommand(sql);
            jdbcRs.execute();
            jdbcRs.afterLast();

            while(jdbcRs.previous()){
                System.out.println(jdbcRs.getString(1) + "\t"
                +jdbcRs.getString(2)+"\t" +
                        jdbcRs.getString(3));
                if(jdbcRs.getInt("sid") == 4){
                    jdbcRs.updateString("name","huazai");
                    jdbcRs.updateRow();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JdbcRowSetTest jt = new JdbcRowSetTest();
        jt.initPrarm("mysql.ini");
        jt.update("select * from studentinfo");
    }
}
