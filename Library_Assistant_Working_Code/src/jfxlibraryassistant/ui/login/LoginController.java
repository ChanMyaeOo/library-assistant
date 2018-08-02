/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxlibraryassistant.DAO.IssueDAO;
import jfxlibraryassistant.main.MainController;
import jfxlibraryassistant.settings.Preferences;
import jfxlibraryassistant.util.LibraryAssistantUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class LoginController implements Initializable {


    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

     Preferences preference;
     IssueDAO issueDAO;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preferences.getPreferences();
        issueDAO = new IssueDAO();
    }    

    @FXML
    private void handleSaveButtonAction(ActionEvent event) throws SQLException {
        
        String uname = username.getText();
        String pass = DigestUtils.shaHex(password.getText());
        
        if(uname.equals(preference.getUsername()) && pass.equals(preference.getPassword())){
            ((Stage)username.getScene().getWindow()).close();
            loadWindow();
            //issueDAO.setCurDate();
        }else{
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
        }
    }
    
    void loadWindow() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/jfxlibraryassistant/main/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageImage(stage);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }
    
}
