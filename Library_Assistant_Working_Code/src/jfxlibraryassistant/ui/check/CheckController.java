/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.check;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import jfxlibraryassistant.DAO.Book;
import jfxlibraryassistant.DAO.IssueDAO;
import jfxlibraryassistant.main.MainController;
import jfxlibraryassistant.settings.Preferences;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class CheckController implements Initializable {

    @FXML
    private TableView<CheckListInfo> checkTableView;
    @FXML
    private TableColumn<CheckListInfo, String> memberIDCol;
    @FXML
    private TableColumn<CheckListInfo, String> limitedDateCol;
    @FXML
    private TableColumn<CheckListInfo, Long> totalFineCol;

    private IssueDAO issueDAO;
    private CheckListInfo cls;
    private CheckInfo cInfo;
    @FXML
    private JFXTextField inputField;
    @FXML
    private TableColumn<CheckListInfo, String> bookIDCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        issueDAO = new IssueDAO();
        cls = new CheckListInfo();
        //cInfo = new CheckInfo();
        
        try {
            //cInfo = new CheckInfo();
            issueDAO.setCurDate();
        } catch (SQLException ex) {
            Logger.getLogger(CheckController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        try {
            
            ObservableList<CheckListInfo> checkList = issueDAO.getLongDateForCheck();
            checkTableView.getItems().addAll(checkList);
            
           // for(CheckListInfo temp : checkList){
             //   System.out.println();
                //checkTableView.setItems(((temp.getCheckDate())/86400000) * Preferences.getPreferences().getFinePerDay());
                //checkList.add(((temp.getCheckDate())/86400000) * Preferences.getPreferences().getFinePerDay());
           // }
            
//            List<Long> list = issueDAO.getExpireDate();
//            for(Long tempList : list){
//                System.out.println(tempList);
//            }
            
            
//            for (Long tempList : list) {
//
//                long return_date = tempList + (86400000 * Preferences.getPreferences().getnOfDaysWithoutFine());
//                Date now = new Date();
//                long nowLong = now.getTime();
//
//                long expireDay = (nowLong - return_date) / 86400000;
//                long finePerDay = expireDay * Preferences.getPreferences().getFinePerDay();
//                // cInfo.setFinePerDay(finePerDay);
//                
//                
//                if (nowLong > return_date) {
//                    // cls.setFinePerDay(finePerDay);
//                    cls.setFinePerDay(finePerDay);
//                    //checkTableView.getItems().addAll(checkList);
//                    checkTableView.setItems(checkList);
//                    
//                }
//                // System.out.println(nowLong);
//                //System.out.println(tempList);
//
//                // System.out.println(Preferences.getPreferences().getFinePerDay());
//                System.out.println(finePerDay + "Kyats");
//                // System.out.println(CheckListInfo.getFinePerDay());
//            }

            initColumn();

            //filter and search data from lmsdb.check table
            FilteredList<CheckListInfo> filteredData = new FilteredList<>(checkList, e -> true);
            inputField.setOnKeyReleased(e -> {
                inputField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    filteredData.setPredicate((Predicate<? super CheckListInfo>) checklist -> {
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        } else if (checklist.getMemberID().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (checklist.getBookID().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (checklist.getIssueDate().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });
                    SortedList<CheckListInfo> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(checkTableView.comparatorProperty());
                    checkTableView.setItems(sortedData);
                });
            });
            

        } catch (SQLException ex) {
            Logger.getLogger(CheckController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
//        try{
//            Parent parent = FXMLLoader.load(getClass().getResource("/jfxlibraryassistant/ui/check/check.fxml"));
//            Stage stage = new Stage(StageStyle.DECORATED);
//            stage.setScene(new Scene(parent));
//            stage.setOnCloseRequest((WindowEvent event1) -> {
//                try {
//                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    issueDAO.removeCurDate();
//                } catch (SQLException ex) {
//                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//        }catch(Exception e){
//            e.getMessage();
//        }
        
    }

    public void initColumn() {
        memberIDCol.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        limitedDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        totalFineCol.setCellValueFactory(new PropertyValueFactory<>("checkDate"));

    }
    
    

}
