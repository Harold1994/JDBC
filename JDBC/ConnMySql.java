package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnMySql {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/home", "user", "lh1994114");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from goods"))
        {
            while(rs.next()){
                System.out.println(rs.getString(1) + "\t"
                        + rs.getString(2) +"\t"+rs.getInt(3) + "\t"
                        + rs.getFloat(4));
            }
        }
    }
}
