package dao;

import java.sql.*;

public class AuthDAO {
    public String login(String username, String password) {

        String role = null;

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT role FROM login WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
            }

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }

        return role;
    }
    public int getUserId(String username) {

        int userId = -1;

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT user_id FROM login WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("user_id");
            }

        } catch (Exception e) {
            System.out.println("UserID Error: " + e.getMessage());
        }

        return userId;
    }
}