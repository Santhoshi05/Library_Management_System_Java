package dao;

import java.sql.*;

public class UserDAO {

    public void addUser(String name, String email, String username, String password) {

        try {
            Connection con = DBConnection.getConnection();

            String sql1 = "INSERT INTO users(name,email) VALUES(?,?)";
            PreparedStatement ps1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);

            ps1.setString(1, name);
            ps1.setString(2, email);
            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();
            int userId = 0;

            if (rs.next()) userId = rs.getInt(1);

            String sql2 = "INSERT INTO login(user_id,username,password,role) VALUES(?,?,?,'USER')";
            PreparedStatement ps2 = con.prepareStatement(sql2);

            ps2.setInt(1, userId);
            ps2.setString(2, username);
            ps2.setString(3, password);
            ps2.executeUpdate();

            System.out.println("User Added! ID: " + userId);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void viewUsers() {
        try {
            Connection con = DBConnection.getConnection();

            ResultSet rs = con.prepareStatement("SELECT * FROM users").executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}