package JDBC;

import com.sun.rowset.JdbcRowSetImpl;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;

import javax.sql.RowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class RowSetFactoryTest {
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

    public void update(String sql) throws Exception {
        Class.forName(driver);
        //Connection conn = DriverManager.getConnection(url, user, pass);
        RowSetFactory factory = RowSetProvider.newFactory();
        try (
                JdbcRowSet jdbcRs = factory.createJdbcRowSet())
        {
            jdbcRs.setUrl(url);
            jdbcRs.setPassword(pass);
            jdbcRs.setUsername(user);
            jdbcRs.setCommand(sql);
            jdbcRs.execute();

        }
    }
        public static void main(String[] args) throws Exception {
            RowSetFactoryTest jt = new RowSetFactoryTest();
            jt.initPrarm("mysql.ini");
            jt.update("select * from studentinfo");
        }
}
