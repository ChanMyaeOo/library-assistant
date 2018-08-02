/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author ChanMyaeOo
 */
public class GetConn {
    Connection conn;
    public Connection getConInstance() throws SQLException{
        conn = Database.getInstance().getConn();
        return conn;
    }
}
