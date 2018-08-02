/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChanMyaeOo
 */
public class Database {
    private static Database db;
     private Connection conn;
    
    public Database() throws SQLException{
        connect();
        createDb();
        createBookTable();
        createMemberTable();
        createIssueTable();
        createCheckTable();
        createExpireDate();
        
    }
    public static Database getInstance() throws SQLException{
        if(db == null){
            db = new Database();
        }
        return db;
    }
   
    
    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver found");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
        }
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","");
            System.out.println("Connected");
        } catch (SQLException ex) {
            System.out.println("Not connected");
        }
    }
    
    //create database
    public void createDb() throws SQLException{
        String sql = "create database if not exists lmsdb";
        Statement state = conn.createStatement();
        state.execute(sql);
    }
    
    //create book table
    public void createBookTable() throws SQLException{
        String sql = "create table if not exists lmsdb.books (id varchar(255) primary key, title varchar(255), author varchar(255), publisher varchar(255), is_available boolean default true)";
        Statement state = conn.createStatement();
        state.execute(sql);
    }
    
    //create member table
    public void createMemberTable() throws SQLException{
        String sql = "create table if not exists lmsdb.members (id varchar(255) primary key,name varchar(255),mobile varchar(255),email varchar(255))";
        Statement state = conn.createStatement();
        state.execute(sql);
    }
    
    //create issue table
    public void createIssueTable() throws SQLException{
        String sql = "create table if not exists lmsdb.issue (book_id varchar(255), member_id varchar(255), issue_date varchar(255),long_date LONGTEXT,renew_count int, foreign key(book_id) references lmsdb.books(id), foreign key(member_id) references lmsdb.members(id))";
        Statement state = conn.createStatement();
        state.execute(sql);
    }   
    
    //create check table
    public void createCheckTable() throws SQLException{
        String sql = "create table if not exists lmsdb.check (member_id varchar(255), over_limited_time varchar(255), fine_to_pay int, foreign key(member_id) references lmsdb.members(id))";
        Statement state = conn.createStatement();
        state.execute(sql);
    }
    
    //create check expire date table
    public void createExpireDate() throws SQLException{
        String sql = "create table if not exists lmsdb.check_expire (expire_date LONGTEXT,book_id varchar(255), member_id varchar(255), issue_date varchar(255),long_date LONGTEXT,renew_count int, foreign key(book_id) references lmsdb.books(id), foreign key(member_id) references lmsdb.members(id))";
        Statement state = conn.createStatement();
        state.execute(sql);
    }
   
    
    public void disconnect(){
        if(conn != null){
            try {
                conn.close();
                System.out.println("Closed");
            } catch (SQLException ex) {
                System.out.println("Cannot close");
            }
        }
    }
    
    public Connection getConn(){
        return conn;
    }
}
