/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.check;

/**
 *
 * @author ChanMyaeOo
 */
public class CheckListInfo {
    private String memberID;
    private String issueDate;
    static Long finePerDay;
    private String bookID;
    private long checkDate;
    public CheckListInfo(String memberID,String bookID,String issueDate){
        this.memberID = memberID;
        this.bookID = bookID;
        this.issueDate = issueDate;
    }
     public CheckListInfo(String memberID,String bookID,String issueDate,long checkDate){
        this.memberID = memberID;
        this.bookID = bookID;
        this.issueDate = issueDate;
        this.checkDate = checkDate;
    }
    
    public CheckListInfo(){
        
    }
    public void setFinePerDay(Long finePerDay){
        this.finePerDay = finePerDay;
    }
    public String getMemberID() {
        return memberID;
    }
    
    public String getIssueDate(){
        return issueDate;
    }
    
    public static Long getFinePerDay(){
        return finePerDay;
    }
    
    public String getBookID(){
        return bookID;
    }

    public long getCheckDate() {
        return checkDate;
    }
}
