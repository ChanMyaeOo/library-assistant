/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import jfxlibraryassistant.alert_maker.AlertMaker;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author ChanMyaeOo
 */
public class Preferences {
    private int nOfDaysWithoutFine,finePerDay;
    private String username,password;
    public static final String CONFIG_FILE = "config.txt";
    
    public Preferences(){
        nOfDaysWithoutFine = 14;
        finePerDay = 200;
        username = "admin";
        setPassword("admin");
    }

    public int getnOfDaysWithoutFine() {
        return nOfDaysWithoutFine;
    }

    public void setnOfDaysWithoutFine(int nOfDaysWithoutFine) {
        this.nOfDaysWithoutFine = nOfDaysWithoutFine;
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
        if(password.length() < 16){
            this.password = DigestUtils.shaHex(password);
        }else{
            this.password = password;
        }
        
    }
    
    public static void initConfig(){
        Preferences preference = new Preferences();
        Gson gson = new Gson();
        FileWriter writer = null;
        try {
     
             writer = new FileWriter(CONFIG_FILE);
             gson.toJson(preference,writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                writer.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
       
    }
    
    public static Preferences getPreferences(){
        Gson gson = new Gson();
        Preferences preference = new Preferences();
        try {
            preference = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preference;
    }
    
    public static void writePreferenceToFile(Preferences preference){
       
        Gson gson = new Gson();
        FileWriter writer = null;
        try {
            writer = new FileWriter(CONFIG_FILE);
             gson.toJson(preference,writer);
             AlertMaker.informationAlert("Setting updated.", "Message");
             
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
             AlertMaker.errorAlert("Fail to update setting.", "Message");
        }finally{
            try {
                writer.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
       
    }
}
