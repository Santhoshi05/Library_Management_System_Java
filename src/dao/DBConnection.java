package dao;
import java.sql.*;
public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC",
                "root",
                "Mahi@143"
            );
        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }
        return con;
    }
}