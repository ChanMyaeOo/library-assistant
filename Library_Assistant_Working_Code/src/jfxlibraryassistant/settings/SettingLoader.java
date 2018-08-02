/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.settings;

import jfxlibraryassistant.main.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxlibraryassistant.database.Database;

/**
 *
 * @author ChanMyaeOo
 */
public class SettingLoader extends Application {
    
    
        
    
    @Override
    public void start(Stage stage) throws IOException{
        try {
            Database db = Database.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(SettingLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent root = FXMLLoader.load(getClass().getResource("/jfxlibraryassistant/settings/settings.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
       
       //Preferences.initConfig();
       
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
