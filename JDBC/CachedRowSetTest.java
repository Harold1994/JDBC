package JDBC;

import javax.jws.soap.SOAPBinding;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.plaf.nimbus.State;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class CachedRowSetTest {
    private static String driver;
    private static String url;
    private static String user;
    private static String pass;

    public void initPrarm(String paramFile) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(paramFile));
        driver = props.getProperty("driver");
        url = props.getProperty("url");
        user = props.getProperty("user");
        pass = props.getProperty("pass");
    }
    
    public CachedRowSet query(String sql) throws Exception{
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url,user,pass);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet cachedRs = factory.createCachedRowSet();
        cachedRs.populate(rs);
        rs.close();
        stmt.close();
        conn.close();
        return cachedRs;
    }

    public static void main(String[] args) throws Exception {
        CachedRowSetTest ct = new CachedRowSetTest();
        ct.initPrarm("mysql.ini");
        CachedRowSet rs = ct.query("select * from studentinfo");
        rs.afterLast();
        while(rs.previous()){
            System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t"
                    + rs.getInt(3) + "\t" + rs.getString(4) + "\t");
            if(rs.getInt("sid") == 5){
                rs.updateString("name","xiaohei");
                rs.updateRow();
            }
        }
        Connection conn = DriverManager.getConnection(url,user,pass);
        conn.setAutoCommit(false);
        rs.acceptChanges(conn);

    }
}
