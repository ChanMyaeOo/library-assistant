/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author ChanMyaeOo
 */
public class LibraryAssistantUtil {
    private static final String IMAGE_LOC = "/resources/icon1.png";
    public static void setStageImage(Stage stage){
        stage.getIcons().add(new Image(IMAGE_LOC));
    }
}
