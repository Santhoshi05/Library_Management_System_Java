package dao;
import java.sql.*;
import java.time.LocalDate;
public class TransactionDAO {
    public boolean issueBook(int uid, int bid) {
        try {
            Connection con = DBConnection.getConnection();

            String check = "SELECT quantity FROM books WHERE book_id=?";
            PreparedStatement ps = con.prepareStatement(check);
            ps.setInt(1, bid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int qty = rs.getInt(1);

                if (qty <= 0) {
                    System.out.println("Book Not Available");
                    return false;
                }
                String update = "UPDATE books SET quantity = quantity - 1 WHERE book_id=?";
                PreparedStatement ps2 = con.prepareStatement(update);
                ps2.setInt(1, bid);
                ps2.executeUpdate();

                String ins = "INSERT INTO transactions(user_id, book_id, issue_date, due_date, status) VALUES(?,?,?,?,?)";
                PreparedStatement ps3 = con.prepareStatement(ins);

                LocalDate issue = LocalDate.now();
                LocalDate due = issue.plusDays(1);
                ps3.setInt(1, uid);
                ps3.setInt(2, bid);
                ps3.setDate(3, Date.valueOf(issue));
                ps3.setDate(4, Date.valueOf(due));
                ps3.setString(5, "ISSUED");

                ps3.executeUpdate();

                System.out.println("Due Date: " + due);
                System.out.println("Book Issued Successfully");

                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
    public void showIssuedBooks(int uid) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT t.book_id, b.title, b.author, t.due_date " +
                         "FROM transactions t JOIN books b ON t.book_id=b.book_id " +
                         "WHERE t.user_id=? AND t.status='ISSUED'";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, uid);

            ResultSet rs = ps.executeQuery();

            System.out.println("BookID | Title | Author | Due Date");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt(1) + " | " +
                    rs.getString(2) + " | " +
                    rs.getString(3) + " | " +
                    rs.getDate(4)
                );
            }

            if (!found) {
                System.out.println("No issued books found");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean isBookIssued(int uid, int bid) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM transactions WHERE user_id=? AND book_id=? AND status='ISSUED'";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, uid);
            ps.setInt(2, bid);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
    public double returnBook(int uid, int bid) {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT due_date FROM transactions WHERE user_id=? AND book_id=? AND status='ISSUED'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, uid);
            ps.setInt(2, bid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                LocalDate due = rs.getDate(1).toLocalDate();
                LocalDate today = LocalDate.now();

                long late = today.getDayOfYear() - due.getDayOfYear();
                double fine = (late > 0) ? late * 50 : 0;

                String up = "UPDATE transactions SET status='RETURNED' WHERE user_id=? AND book_id=?";
                PreparedStatement ps2 = con.prepareStatement(up);
                ps2.setInt(1, uid);
                ps2.setInt(2, bid);
                ps2.executeUpdate();
                String updBook = "UPDATE books SET quantity = quantity + 1 WHERE book_id=?";
                PreparedStatement ps3 = con.prepareStatement(updBook);
                ps3.setInt(1, bid);
                ps3.executeUpdate();
                return fine;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }
    public void viewAllIssuedBooks() {

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                "SELECT t.transaction_id, u.name, b.title, t.issue_date, t.due_date " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.user_id " +
                "JOIN books b ON t.book_id = b.book_id " +
                "WHERE t.status='ISSUED'";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nID | USER | BOOK | ISSUE DATE | DUE DATE");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("transaction_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("title") + " | " +
                    rs.getDate("issue_date") + " | " +
                    rs.getDate("due_date")
                );
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void viewLateUsers() {

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                "SELECT u.name, b.title, t.due_date, " +
                "DATEDIFF(CURDATE(), t.due_date) AS late_days, " +
                "(DATEDIFF(CURDATE(), t.due_date) * 10) AS fine " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.user_id " +
                "JOIN books b ON t.book_id = b.book_id " +
                "WHERE t.status='ISSUED' AND CURDATE() > t.due_date";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nUSER | BOOK | DUE DATE | LATE DAYS | FINE");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(
                    rs.getString("name") + " | " +
                    rs.getString("title") + " | " +
                    rs.getDate("due_date") + " | " +
                    rs.getInt("late_days") + " | ₹" +
                    rs.getInt("fine")
                );
            }

            if (!found) {
                System.out.println("No late returns");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}