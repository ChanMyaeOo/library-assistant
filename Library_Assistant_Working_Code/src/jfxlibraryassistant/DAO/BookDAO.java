/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxlibraryassistant.alert_maker.AlertMaker;
import jfxlibraryassistant.database.Database;
import jfxlibraryassistant.database.GetConn;

/**
 *
 * @author ChanMyaeOo
 */
public class BookDAO extends GetConn {

    //save book information into database
    public void addBook(Book book) throws SQLException {
        Connection conn = Database.getInstance().getConn();
        String sql = "insert into lmsdb.books (id,title,author,publisher) values (?,?,?,?)";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, book.getId());
        pre.setString(2, book.getTitle());
        pre.setString(3, book.getAuthor());
        pre.setString(4, book.getPublisher());
        pre.execute();
    }

    //get book information from database to show into tableview
    public ObservableList<Book> getBooks() throws SQLException {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        Connection conn = getConInstance();
        String sql = "select * from lmsdb.books";
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(sql);

        while (result.next()) {
            String bookID = result.getString("id");
            String title = result.getString("title");
            String author = result.getString("author");
            String publisher = result.getString("publisher");
            boolean is_available = result.getBoolean("is_available");

            //isAvail = is_available ? "Available" : "Not available";
            String isAvail = "";
            if (is_available == true) {
                isAvail = "Available";
            } else {
                isAvail = "Not available";
            }

            Book book = new Book(bookID, title, author, publisher, isAvail);
            bookList.add(book);
        }
        return bookList;
    }

    //get book to show in bookInfo session (at main window and to look book information)
    public Book getBook(String bookId) throws SQLException {
        Connection conn = getConInstance();
        String sql = "select * from lmsdb.books where id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, bookId);
        ResultSet result = pre.executeQuery();
        Book book = null;
        while (result.next()) {
            String id = result.getString("id");
            String title = result.getString("title");
            String author = result.getString("author");
            String publisher = result.getString("publisher");
            boolean isAval = result.getBoolean("is_available");
            String isAvailable = isAval ? "Available" : "Not Available";
            book = new Book(id, title, author, publisher, isAvailable);
        }
        return book;
    }

    //update available value when book is issued
    public void updateAvailable(String id, boolean bolValue) throws SQLException {
        Connection conn = getConInstance();
        String sql = "update lmsdb.books set is_available=? where id=?";
        PreparedStatement pre = conn.prepareStatement(sql);

        pre.setBoolean(1, bolValue);
        pre.setString(2, id);
        pre.execute();
    }
    
     //updating for edit book
    public boolean updateEditBook(Book book) throws SQLException{
        Connection conn = getConInstance();
        String sql = "update lmsdb.books set title=?, author=?, publisher=? where id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, book.getTitle());
        pre.setString(2,book.getAuthor());
        pre.setString(3,book.getPublisher());
        pre.setString(4, book.getId());
        int res = pre.executeUpdate();
        if(res>0){
            return true;
        }else
            return false;
    }

    //to delete book from books table
    public void deleteBook(Book book) throws SQLException {
        Connection conn = getConInstance();
        String sql = "delete from lmsdb.books where id=?";
        PreparedStatement pre;
        pre = conn.prepareStatement(sql);
        pre.setString(1, book.getId());
        pre.execute();
    }
}
