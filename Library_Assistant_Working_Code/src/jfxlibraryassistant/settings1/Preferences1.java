/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.settings1;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author ChanMyaeOo
 */
public class Preferences1 {
    private int nOfDays,finePerDay;
    private String username,password;
    public static final String CONFIG_FILE = "config1.txt";
    
    public Preferences1(){
        nOfDays = 14;
        finePerDay = 100;
        username = "admin1";
        setPassword("admin1");
    }

    public int getnOfDays() {
        return nOfDays;
    }

    public void setnOfDays(int nOfDays) {
        this.nOfDays = nOfDays;
    }

    public int getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(int finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = DigestUtils.shaHex(password);
    }
    
    public static void initConfig1(){
        
        Preferences1 pref1 = new Preferences1();
        Gson gson = new Gson();
        FileWriter writer = null;
        
        try {
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(pref1,writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences1.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                writer.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
       
    }
    
    public static Preferences1 getPreferences(){
        Preferences1 pref1 = new Preferences1();
        Gson gson = new Gson();
        try {
            pref1 = gson.fromJson(new FileReader(CONFIG_FILE), Preferences1.class);
        } catch (FileNotFoundException ex) {
            initConfig1();
            Logger.getLogger(Preferences1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pref1;
    }
    
    public static void actualFileWrite(Preferences1 pref){
      // Preferences1 pref1 = new Preferences1();
        Gson gson = new Gson();
        FileWriter writer = null;
        try {
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(pref,writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences1.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                writer.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
        
    }
}
