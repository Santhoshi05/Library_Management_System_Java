package dao;
import exception.*;
import java.sql.*;

public class BookDAO {
    public void addBook(String title, String author, int qty) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO books(title, author, quantity) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, qty);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book Added Successfully");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateBook(int bookId, String title, String author, int qty) throws BookNotFoundException{
        try {
            Connection con = DBConnection.getConnection();

            String checkSql = "SELECT * FROM books WHERE book_id=?";
            PreparedStatement check = con.prepareStatement(checkSql);
            check.setInt(1, bookId);

            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
            	 throw new BookNotFoundException("Book ID not found");
            }

            String sql = "UPDATE books SET title=?, author=?, quantity=? WHERE book_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, qty);
            ps.setInt(4, bookId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book Updated Successfully");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteBook(int bookId) throws BookNotFoundException {
        try {
            Connection con = DBConnection.getConnection();

            String checkSql = "SELECT * FROM books WHERE book_id=?";
            PreparedStatement check = con.prepareStatement(checkSql);
            check.setInt(1, bookId);

            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
            	 throw new BookNotFoundException("Book ID not found");
            }

            String sql = "DELETE FROM books WHERE book_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book Deleted Successfully");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void search(String key) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + key + "%");
            ps.setString(2, "%" + key + "%");

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("book_id") + " | " +
                    rs.getString("title") + " | " +
                    rs.getString("author") + " | Qty: " +
                    rs.getInt("quantity")
                );
            }

            if (!found) {
                System.out.println("No books found");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void showAllBooks() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM books";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\nID | TITLE | AUTHOR | QTY");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("book_id") + " | " +
                    rs.getString("title") + " | " +
                    rs.getString("author") + " | " +
                    rs.getInt("quantity")
                );
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean isBookExists(int bookId) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT 1 FROM books WHERE book_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, bookId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean isBookAvailable(int bookId) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT quantity FROM books WHERE book_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, bookId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantity") > 0;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}