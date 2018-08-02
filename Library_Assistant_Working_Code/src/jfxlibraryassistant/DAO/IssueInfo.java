/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.DAO;

/**
 *
 * @author ChanMyaeOo
 */
public class IssueInfo {
    private String bookID,memberID,issueDate;
    private int renewCount;

    public IssueInfo(String bookID, String memberID, String issueDate, int renewCount) {
        this.bookID = bookID;
        this.memberID = memberID;
        this.issueDate = issueDate;
        this.renewCount = renewCount;
    }

    public String getBookID() {
        return bookID;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public int getRenewCount() {
        return renewCount;
    }
    
}
