package view;
import java.util.*;
import dao.*;
import service.*;
import exception.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthDAO auth = new AuthDAO();
        AdminService admin = new AdminService();
        UserService user = new UserService();
        BookDAO bookDAO = new BookDAO();
        while (true) {
            System.out.println("\n WELCOME TO LIBRARY MANAGEMENT SYSTEM ");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            if (choice == 3) {
                System.out.println("THANKK YOUU");
                break;
            }
            String roleSelected = "";
            if (choice == 1) roleSelected = "ADMIN";
            else if (choice == 2) roleSelected = "USER";
            else {
                System.out.println("Invalid Choice");
                continue;
            }
            System.out.print("Username: ");
            String username = sc.next();
            System.out.print("Password: ");
            String password = sc.next();
            String role = auth.login(username, password);
            if (role == null || !role.equals(roleSelected)) {
                System.out.println("Invalid Login or Wrong Role Selected");
                continue;
            }
            int loggedUserId = auth.getUserId(username);
            if (loggedUserId == -1 && role.equals("USER")) {
                System.out.println("User ID not found");
                continue;
            }
            System.out.println("Welcome " + role);
            if (role.equals("ADMIN")) {
                while (true) {
                    System.out.println("\nWELCOME TO ADMIN PANEL");
                    System.out.println("1. Add User");
                    System.out.println("2. View Users");
                    System.out.println("3. Add Book");
                    System.out.println("4. Update Book");
                    System.out.println("5. Delete Book");
                    System.out.println("6. Search Book");
                    System.out.println("7. View Issued Books");
                    System.out.println("8. View Late Returns");
                    System.out.println("9. View Books");
                    System.out.println("10, Logout");
                    System.out.print("Enter your choice: ");
                    int ch = sc.nextInt();
                    switch (ch) {
                        case 1:
                            sc.nextLine();
                            System.out.print("Name: ");
                            String name = sc.nextLine();

                            System.out.print("Email: ");
                            String email = sc.nextLine();

                            System.out.print("Username: ");
                            String u = sc.next();

                            System.out.print("Password: ");
                            String p = sc.next();
                            admin.addUser(name, email, u, p);
                            break;
                        case 2:
                            admin.viewUsers();
                            break;
                        case 3:
                            sc.nextLine();
                            System.out.print("Title: ");
                            String title = sc.nextLine();
                            System.out.print("Author: ");
                            String author = sc.nextLine();
                            System.out.print("Quantity: ");
                            int qty = sc.nextInt();
                            admin.addBook(title, author, qty);
                            break;
                        case 4:
                            try {
                                bookDAO.showAllBooks();
                                System.out.print("Book ID: ");
                                int id = sc.nextInt();
                                sc.nextLine();
                                System.out.print("New Title: ");
                                String newTitle = sc.nextLine();
                                System.out.print("New Author: ");
                                String newAuthor = sc.nextLine();
                                System.out.print("New Quantity: ");
                                int newQty = sc.nextInt();
                                bookDAO.updateBook(id, newTitle, newAuthor, newQty);
                            } catch (BookNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            } catch (Exception e) {
                                System.out.println("Invalid Input");
                            }
                            break;
                        case 5:
                            try {
                                bookDAO.showAllBooks();
                                System.out.print("\nEnter Book ID to delete: ");
                                int delId = sc.nextInt();
                                System.out.print("Are you sure? (y/n): ");
                                char confirm = sc.next().charAt(0);
                                if (confirm == 'y' || confirm == 'Y') {
                                    bookDAO.deleteBook(delId);
                                } else {
                                    System.out.println("Delete cancelled");
                                }
                            } catch (BookNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            } catch (Exception e) {
                                System.out.println("Invalid input");
                            }
                            break;
                        case 6:
                            sc.nextLine();
                            System.out.print("Search: ");
                            String key = sc.nextLine();
                            admin.search(key);
                            break;
                        case 7:
                        	admin.viewIssuedBooks();
                        	break;
                        case 8:
                        	admin.lateReturns();
                        	break;
                        case 9:
                        	admin.viewBooks();
                        	break;
                        case 10:
                            System.out.println("Admin Logged Out");
                            break;
                        default:
                            System.out.println("Invalid Choice");
                    }
                    if (ch == 10) break;
                }
            }
            else if (role.equals("USER")) {

                while (true) {
                    System.out.println("\nWELCOME TO USER PANEL");
                    System.out.println("1. Issue Book");
                    System.out.println("2. Return Book");
                    System.out.println("3. Search Book");
                    System.out.println("4. Show Issued books");
                    System.out.println("5. Logout");
                    System.out.print("Enter your choice: ");
                    int ch = sc.nextInt();
                    switch (ch) {
                        case 1:
                        	 try {
                        	        bookDAO.showAllBooks();
                        	        System.out.print("Enter Book ID: ");
                        	        int bid = sc.nextInt();
                        	        user.issue(loggedUserId, bid);
                        	    } catch (BookNotFoundException e) {
                        	        System.out.println(e.getMessage());
                        	    } catch (Exception e) {
                        	        System.out.println("Invalid Input");
                        	        sc.nextLine();
                        	    }
                        	    break;
                        case 2:
                        	try {
                            user.returnBook(loggedUserId);
                        	} catch(PaymentException e) {
                        		System.out.println(e.getMessage());
                        	}
                        	catch(Exception e) {
                        		System.out.println(e.getMessage());
                        	}
                            break;
                        case 3:
                            sc.nextLine();
                            System.out.print("Search: ");
                            String keyword = sc.nextLine();
                            bookDAO.search(keyword);
                            break;
                        case 4:
                        	user.viewIssuedBooks(loggedUserId);
                        	break;
                        case 5:
                            System.out.println("User Logged Out");
                            break;
                        default:
                            System.out.println("Invalid Choice");
                    }
                    if (ch == 5) break;
                }
            }
        }
        sc.close();
    }
}