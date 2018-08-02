/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.settings1;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class Settings1Controller implements Initializable {

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
        initDefauntValues();
     
    }    

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        int nOfDays = Integer.parseInt(nOfDaysWithoutFine.getText());
        int fine = Integer.parseInt(finePerDay.getText());
        String uname = username.getText();
        String pass = password.getText();
        
        Preferences1 pref1 = Preferences1.getPreferences();
        pref1.setnOfDays(nOfDays);
        pref1.setFinePerDay(fine);
        pref1.setUsername(uname);
        pref1.setPassword(pass);
        Preferences1.actualFileWrite(pref1);
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        ((Stage)cancelBtn.getScene().getWindow()).close();
    }
    
    public void initDefauntValues(){
        Preferences1 pref = Preferences1.getPreferences();
        
        nOfDaysWithoutFine.setText(String.valueOf(pref.getnOfDays()));
        finePerDay.setText(String.valueOf(pref.getFinePerDay()));
        username.setText(pref.getUsername());
        password.setText(pref.getPassword());
    }
    
}
