/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import jfxlibraryassistant.alert_maker.AlertMaker;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXTextField nOfDaysWithoutFine;
    @FXML
    private JFXTextField finePerDay;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
    }    

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        
        String nOfDaysForCheck = nOfDaysWithoutFine.getText();
        String finePerDayForCheck = finePerDay.getText();
        String uname = username.getText();
        String pass = password.getText();
        
         
        if(nOfDaysForCheck.isEmpty() || finePerDayForCheck.isEmpty() || uname.isEmpty() || pass.isEmpty()){
            AlertMaker.errorAlert("Please fill in all text fields.", "Oops!");
            return;
        }
        
        
//        int nOfDays = Integer.parseInt(nOfDaysWithoutFine.getText());
//        int perDay = Integer.parseInt(finePerDay.getText());
//        String uname = username.getText();
//        String pass = password.getText();

        int nOfDays = Integer.parseInt(nOfDaysForCheck);
        int perDay = Integer.parseInt(finePerDayForCheck);
        
       
            
        
        
        Preferences preference = Preferences.getPreferences();
        preference.setnOfDaysWithoutFine(nOfDays);
        preference.setFinePerDay(perDay);
        preference.setUsername(uname);
        preference.setPassword(pass);
        Preferences.writePreferenceToFile(preference);
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
       ((Stage) cancelBtn.getScene().getWindow()).close();
        System.out.println(Preferences.getPreferences().getFinePerDay());
    }

    private void initDefaultValues() {
       Preferences preference = Preferences.getPreferences();
       nOfDaysWithoutFine.setText(String.valueOf(preference.getnOfDaysWithoutFine()));
       finePerDay.setText(String.valueOf(preference.getFinePerDay()));
       username.setText(String.valueOf(preference.getUsername()));
       password.setText(String.valueOf(preference.getPassword()));
    }
    
}
