package service;
import dao.BookDAO;
import dao.TransactionDAO;
import java.util.Scanner;
import exception.*;
public class UserService {
    TransactionDAO dao = new TransactionDAO();
    BookDAO bookDAO = new BookDAO();
    Scanner sc = new Scanner(System.in);
    public void issue(int uid, int bid) throws BookNotFoundException {
        try {
            if (!bookDAO.isBookExists(bid)) {
            	throw new BookNotFoundException("Book Not Found");
            }
            if (!bookDAO.isBookAvailable(bid)) {
            	throw new BookNotFoundException("Book out of stock");
            }
            dao.issueBook(uid, bid);

        } catch(BookNotFoundException e) {
        	System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void returnBook(int uid) throws PaymentException {
        try {
            System.out.println("\nYOUR ISSUED BOOKS:");
            dao.showIssuedBooks(uid);

            System.out.print("\nEnter Book ID to Return: ");
            int bid = sc.nextInt();

            if (!dao.isBookIssued(uid, bid)) {
                System.out.println("This book is not issued by you");
                return;
            }
            double fine = dao.returnBook(uid, bid);
            System.out.println("Fine Amount: ₹" + fine);
            double remaining = fine;
            while (remaining > 0) {
                System.out.print("Enter Payment: ");
                double pay = sc.nextDouble();
                if (pay <= 0) {
                	throw new PaymentException("Invalid payment amount");
                }
                remaining -= pay;
                if (remaining > 0) {
                    System.out.println("Remaining: ₹" + remaining);
                }
                if (remaining < 0) {
                    System.out.println("Refund: ₹" + Math.abs(remaining));
                }
            }
            System.out.println("Payment Completed");
            System.out.println("Book Returned Successfully");

        } catch(PaymentException e) {
        	System.out.println(e.getMessage());
        	
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void viewIssuedBooks(int uid) {
        try {
            System.out.println("\nYOUR ISSUED BOOKS:");
            dao.showIssuedBooks(uid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}