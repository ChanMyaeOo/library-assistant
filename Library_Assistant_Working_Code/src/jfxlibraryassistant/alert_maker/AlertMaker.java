/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.alert_maker;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import jfxlibraryassistant.util.LibraryAssistantUtil;

/**
 *
 * @author ChanMyaeOo
 */
public class AlertMaker {
    public static void informationAlert(String content,String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.show();
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        LibraryAssistantUtil.setStageImage(stage);
    }
    
    public static void errorAlert(String content, String title){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.show();
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        LibraryAssistantUtil.setStageImage(stage);
    }
    
    public static void warningAlert(String content, String title){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.show();
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        LibraryAssistantUtil.setStageImage(stage);
    }
}
