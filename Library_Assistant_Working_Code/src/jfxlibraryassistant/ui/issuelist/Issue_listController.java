/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.issuelist;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxlibraryassistant.DAO.IssueDAO;
import jfxlibraryassistant.DAO.IssueList;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class Issue_listController implements Initializable {

    @FXML
    private JFXTextField input;
    @FXML
    private TableView<IssueList> tableView;
    @FXML
    private TableColumn<IssueList, String> bookIDCol;
    @FXML
    private TableColumn<IssueList, String> memberIDCol;
    @FXML
    private TableColumn<IssueList, String> issueDateCol;
    @FXML
    private TableColumn<IssueList, String> renewCountCol;

    private IssueDAO issueDAO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        issueDAO = new IssueDAO();
        try {
            ObservableList<IssueList> list = issueDAO.getIssueList();
            tableView.getItems().addAll(list);
            initColumn();

            //filter and search data from issue table
            FilteredList<IssueList> filteredData = new FilteredList<>(list, e -> true);
            input.setOnKeyReleased(e -> {
                input.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    filteredData.setPredicate((Predicate<? super IssueList>) issueList -> {
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (newValue.isEmpty() || newValue == null) {
                            return true;
                        } else if (issueList.getBookID().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (issueList.getMemberID().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }else if(issueList.getIssueDate().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }

                        return false;
                    });
                    SortedList<IssueList> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(tableView.comparatorProperty());
                    tableView.setItems(sortedData);
                });
            });
        } catch (SQLException ex) {
            Logger.getLogger(Issue_listController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initColumn() {
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        memberIDCol.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        renewCountCol.setCellValueFactory(new PropertyValueFactory<>("renewCount"));
    }
}
