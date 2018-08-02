/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.about;
import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class AboutController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
   
    @FXML
    private JFXButton githubBtn;
    
    private static final String GITHUB = "https://github.com/ChanMyaeOo";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
     private void loadWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
            handleWebpageLoadException(url);
        }
    }
     
      private void handleWebpageLoadException(String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        Stage stage = new Stage();
        Scene scene = new Scene(new StackPane(browser));
        stage.setScene(scene);
        stage.setTitle("Genuine Coder");
        stage.show();
    }

    @FXML
    private void loadGmailPage(ActionEvent event) {
    }

    @FXML
    private void loadFacebookPage(ActionEvent event) {
    }

    @FXML
    private void loadGithubPage(ActionEvent event) {
        loadWebpage(GITHUB);
    }
    
}
