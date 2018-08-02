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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxlibraryassistant.database.Database;
import jfxlibraryassistant.database.GetConn;

/**
 *
 * @author ChanMyaeOo
 */
public class MemberDAO extends GetConn{
    
    
    //save member information into database
    public void saveMember(Member member) throws SQLException{
        Connection conn = Database.getInstance().getConn();
        String sql = "insert into lmsdb.members (id,name,mobile,email) values (?,?,?,?)";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, member.getId());
        pre.setString(2, member.getName());
        pre.setString(3, member.getMobile());
        pre.setString(4, member.getEmail());
        pre.execute();
    }
    
    //get member information from database to show into tableview
    public ObservableList<Member> getMembers() throws SQLException{
        Connection conn = getConInstance();
        
        ObservableList<Member> memberList = FXCollections.observableArrayList();
        String sql = "select * from lmsdb.members";
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(sql);
        while(result.next()){
            String id = result.getString("id");
            String name = result.getString("name");
            String mobile = result.getString("mobile");
            String email = result.getString("email");
            
            Member member = new Member(id,name,mobile,email);
            memberList.add(member);
        }
        return memberList;
    }
    
    //get member to show member information (at main window) => for issue book status
    public Member getMember(String memberID) throws SQLException{
        Connection conn = getConInstance();
        String sql = "select * from lmsdb.members where id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, memberID);
        ResultSet result = pre.executeQuery();
        Member member = null;
        while(result.next()){
            String id = result.getString("id");
            String name = result.getString("name");
            String mobile = result.getString("mobile");
            String email = result.getString("email");
            
            member = new Member(id,name,mobile,email);
        }
        return member;
    }
    
    //for deletion member list
    public void deleteMember(String id) throws SQLException{
        Connection conn = getConInstance();
        String sql = "delete from lmsdb.members where id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, id);
        pre.execute();
    }
    
    //update for member edition 
    public boolean updateForEditMember(Member member) throws SQLException{
        Connection conn = getConInstance();
        String sql = "update lmsdb.members set name=?, mobile=?, email=? where id=?";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, member.getName());
        pre.setString(2, member.getMobile());
        pre.setString(3, member.getEmail());
        pre.setString(4, member.getId());
        int res = pre.executeUpdate();
        if(res > 0){
            return true;
        }else{
            return false;
        }
    }
}
