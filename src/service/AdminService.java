package service;

import dao.*;
public class AdminService {
    UserDAO u = new UserDAO();
    BookDAO b = new BookDAO();
    TransactionDAO t = new TransactionDAO();
    public void addUser(String n,String e,String u1,String p){
        u.addUser(n,e,u1,p);
    }
    public void addBook(String t,String a,int q){
        b.addBook(t,a,q);
    }
    public void search(String k){
        b.search(k);
    }
    public void viewUsers(){
        u.viewUsers();
    }
    public void viewIssuedBooks() {
    	t.viewAllIssuedBooks();
    }
    public void lateReturns() {
    	t.viewLateUsers();
    }
    public void viewBooks() {
    	b.showAllBooks();
    }
}