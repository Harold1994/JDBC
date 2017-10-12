package JDBC;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class ExecuteSQL {
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

    public void executeSql(String sql) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement())
        {
            Boolean hasResult = stmt.execute(sql);
            if(hasResult){
                try(
                        ResultSet rs = stmt.getResultSet())
                {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int ColumnCount = rsmd.getColumnCount();
                    while(rs.next()){
                        for(int i = 0; i< ColumnCount; i++)
                            System.out.println(rs.getString(i + 1) + "\t");
                        System.out.println("\n");
                    }
                }
            }
            else{
                System.out.println("affected " + stmt.getUpdateCount() + "records");
            }

        }

    }

    public static void main(String[] args) throws Exception {
        ExecuteSQL es = new ExecuteSQL();
        es.initPrarm("mysql.ini");
        System.out.println("delete table");
        es.executeSql("drop table if exists jdbc_test");

        System.out.println("create new table");
        es.executeSql("create table jdbc_test "
        + "( jdbc_id int auto_increment primary key,"
        + "jdbc_name varchar(255),"
        + "jdbc_desc text);");

        System.out.println("insert into tabel");
        es.executeSql("insert into jdbc_test values(1,\"wang\",\"women teacher\");");
    }

}
