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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxlibraryassistant.database.GetConn;
import jfxlibraryassistant.settings.Preferences;
import jfxlibraryassistant.ui.check.CheckListInfo;

/**
 *
 * @author ChanMyaeOo
 */
public class IssueDAO extends GetConn{
    
    //insert issue data into issue table
    public void setIssueBook(String bookID, String memberID) throws SQLException{
        Connection conn = getConInstance();
        //String dateStr = java.time.LocalDate.now().toString();
        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
        String dateStr = df.format(now);
        String sql = "insert into lmsdb.issue (book_id, member_id, issue_date,long_date,renew_count) values (?,?,?,?,0)";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, bookID);
        pre.setString(2, memberID);
        pre.setString(3, dateStr);
        pre.setLong(4,(now.getTime()+(86400000 * Preferences.getPreferences().getnOfDaysWithoutFine())));   //+(86400000 * Preferences.getPreferences().getnOfDaysWithoutFine()))
        pre.execute();
        
        Connection conn1 = getConInstance();
        Date now1 = new Date();
        DateFormat df1 = DateFormat.getDateInstance(DateFormat.FULL);
        String dateStr1 = df1.format(now);
        String sql1 = "insert into lmsdb.check_expire (book_id, member_id, issue_date,long_date,renew_count) values (?,?,?,?,0)";
        PreparedStatement pre1 = conn1.prepareStatement(sql1);
        pre1.setString(1, bookID);
        pre1.setString(2, memberID);
        pre1.setString(3, dateStr1);
        pre1.setLong(4,(now.getTime()+(86400000 * Preferences.getPreferences().getnOfDaysWithoutFine())));   //+(86400000 * Preferences.getPreferences().getnOfDaysWithoutFine()))
        pre1.execute();
    }
    
    //check issue book is already exists or not
    public boolean checkIssueTable(String bookID) throws SQLException{
        Connection conn = getConInstance();
        String sql = "select count(*) as count from lmsdb.issue where book_id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, bookID);
        ResultSet result = pre.executeQuery();
        boolean check = true;
        if(result.next()){
            int count = result.getInt("count");
            if(count == 0)
                check = false;
        }
        return check;
    }
    
    //check again for member in issue book (purpose is user error handling)
    public boolean checkIssueTable1(String memberID) throws SQLException{
        Connection conn = getConInstance();
        String sql = "select count(*) as count from lmsdb.issue where member_id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, memberID);
        ResultSet result = pre.executeQuery();
        boolean check = true;
        if(result.next()){
            int count = result.getInt("count");
            if(count == 0)
                check = false;
        }
        return check;
    }
    
   
    
    //get data from issue table to show in table view
    public ObservableList<IssueList> getIssueList() throws SQLException{
        Connection conn = getConInstance();
        ObservableList<IssueList> issueList = FXCollections.observableArrayList();
        String sql = "select * from lmsdb.issue";
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(sql);
        while(result.next()){
            String bookID = result.getString("book_id");
            String memberID = result.getString("member_id");
            String issueDate = result.getString("issue_date");
            int renewCount = result.getInt("renew_count");
            
            IssueList list = new IssueList(bookID, memberID, issueDate, renewCount);
            issueList.add(list);
        }
        return issueList;
    }
    
    //to show data in check table
    public ObservableList<CheckListInfo> getIssueListForCheck() throws SQLException{
        Connection conn = getConInstance();
        ObservableList<CheckListInfo> checkIssueList = FXCollections.observableArrayList();
        String sql = "select * from lmsdb.issue";
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(sql);
        while(result.next()){
            String bookID = result.getString("book_id");
            String memberID = result.getString("member_id");
            String issueDate = result.getString("issue_date");
            int renewCount = result.getInt("renew_count");
            
            
            CheckListInfo cls = new CheckListInfo(memberID,bookID,issueDate);
            checkIssueList.add(cls);
        }
        return checkIssueList;
    }
    
    //get long_date for checking issue book date  to return
     public ObservableList<CheckListInfo> getLongDateForCheck() throws SQLException{
        Connection conn = getConInstance();
        ObservableList<CheckListInfo> checkList = FXCollections.observableArrayList();
        Date now = new Date();
        long nowLong = now.getTime();
       //ArrayList<Long> dateList = new ArrayList<>();
        //String sql = "select * from lmsdb.issue where long_date < ?";
        String sql = "select * from lmsdb.issue, lmsdb.check_expire where lmsdb.issue.long_date < lmsdb.check_expire.expire_date";
        //PreparedStatement pre = conn.prepareStatement(sql);
        Statement state = conn.createStatement();
        //pre.setLong(1, checkDate);
        ResultSet result = state.executeQuery(sql);
        while(result.next()){
            String bookID = result.getString("book_id");
            String memberID = result.getString("member_id");
            String issueDate = result.getString("issue_date");
            int renewCount = result.getInt("renew_count");
            
            //for fine per day
            Long long_date = result.getLong("long_date");
            long calLong = nowLong - long_date;
            
            //for fine per day second edit
            long calculateExpDate = (calLong/ 86400000)* Preferences.getPreferences().getFinePerDay();
            
            
            CheckListInfo checkInfo = new CheckListInfo(memberID,bookID,issueDate,calculateExpDate);         //added calLong parameter for fine per 
            //dateList.add(long_date);
            checkList.add(checkInfo);
        }
        return checkList;
    }
     
    //add current date to database
     public void setCurDate() throws SQLException{
         Connection conn = getConInstance();
         Date now = new Date();
         long curDate = now.getTime();
         String sql = "insert into lmsdb.check_expire (expire_date) values (?)";
         PreparedStatement pre = conn.prepareStatement(sql);
         pre.setLong(1, curDate);
         pre.execute();
         
     }
     
     //delete current date from database
     public void removeCurDate() throws SQLException{
         Connection conn = getConInstance();
         //Date now = new Date();
        // long curDate = now.getTime();
         //String sql = "insert into lmsdb.check_expire (expire_date) values (0)";
         String sql = "UPDATE lmsdb.check_expire SET expire_date=NULL WHERE expire_date is not NULL";
         PreparedStatement pre = conn.prepareStatement(sql);
         //Statement state = conn.createStatement();
         //pre.setLong(1, 0);
         pre.execute();
         //state.execute(sql);
     }

//     public ArrayList<Long> getExpireDate() throws SQLException{
//         Connection conn = getConInstance();
//         ArrayList<Long> dateList = new ArrayList<>();
//         List<Long> list = getLongDateForCheck();
//         for(Long tempList : list){
//              long return_date = tempList + (86400000 * Preferences.getPreferences().getnOfDaysWithoutFine());
//              Date now = new Date();
//              long nowLong = now.getTime();
//              
//              long tempLongVal = nowLong - return_date;
//              
//         
//              
//              String sql = "select * from lmsdb.issue where tempLongVal > list";
//              Statement state = conn.createStatement();
//              ResultSet result = state.executeQuery(sql);
//              while(result.next()){
//                String bookID = result.getString("book_id");
//                String memberID = result.getString("member_id");
//                String issueDate = result.getString("issue_date");
//                int renewCount = result.getInt("renew_count");
//                Long long_date = result.getLong("long_date");
//            
//            
//            dateList.add(long_date);
//        }
//              
//         }
//         
//         return dateList;
//     }
     
    
    
    //search issue book for submission
    public IssueInfo getIssueInfo(String bookID) throws SQLException{
        Connection conn = getConInstance();
        String sql = "select * from lmsdb.issue where book_id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, bookID);
        ResultSet result = pre.executeQuery();
        IssueInfo issueInfo = null;
        while(result.next()){
            String idForBook = result.getString("book_id");
            String idForMember = result.getString("member_id");
            String issueDate = result.getString("issue_date");
            int renewCount = result.getInt("renew_count");
            issueInfo = new IssueInfo(idForBook,idForMember,issueDate,renewCount);
        }
        return issueInfo;
    }
    
    //update renew count value
    public void updateRenewCount(String id) throws SQLException{
        Connection conn = getConInstance();
        String sql = "update lmsdb.issue set renew_count = renew_count+1 where book_id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1,id);
        pre.execute();
    }
    
    //delete issue information from issue table when submission
    public void deleteIssueInfo(String id) throws SQLException{
        Connection conn = getConInstance();
        String sql = "delete from lmsdb.issue where book_id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, id);
        pre.execute();
    }
}
