/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.memberlist;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxlibraryassistant.DAO.IssueDAO;
import jfxlibraryassistant.DAO.Member;
import jfxlibraryassistant.DAO.MemberDAO;
import jfxlibraryassistant.alert_maker.AlertMaker;
import jfxlibraryassistant.ui.addmember.Add_memberController;
import jfxlibraryassistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class MemberlistController implements Initializable {

    @FXML
    private JFXTextField input;
    @FXML
    private TableView<Member> memberTableView;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> mobileCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    private MemberDAO memberDAO;
    
    private IssueDAO issueDAO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        memberDAO = new MemberDAO();
        issueDAO = new IssueDAO();
        try {
            ObservableList<Member> memberList = memberDAO.getMembers();
            memberTableView.getItems().addAll(memberList);
            initColumn();

            //filter and search data from lmsdb.members table
            FilteredList<Member> filteredData = new FilteredList<>(memberList, e -> true);
            input.setOnKeyReleased(e -> {
                input.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    filteredData.setPredicate((Predicate<? super Member>) member -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (member.getId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (member.getName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (member.getMobile().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (member.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });
                    SortedList<Member> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(memberTableView.comparatorProperty());
                    memberTableView.setItems(sortedData);
                });
            });

        } catch (SQLException ex) {
            Logger.getLogger(MemberlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initColumn() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    @FXML
    private void handleDeleteOption(ActionEvent event) {
        Member selectedMember = memberTableView.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            AlertMaker.warningAlert("Select the member to delete.", "Oops!");
            return;
        }
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you want to delete this member? "
                    + "\n"
                    + "Member ID : " + selectedMember.getId()
                    + "\n"
                    + "Name : " + selectedMember.getName()
            );
            alert.setHeaderText(null);
            alert.setTitle("Message");

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            LibraryAssistantUtil.setStageImage(stage);
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                memberDAO.deleteMember(selectedMember.getId());
                memberTableView.getItems().remove(selectedMember);
                AlertMaker.informationAlert("Success.", "Message");
            }

        } catch (SQLException ex) {
            AlertMaker.errorAlert("Cannot delete this member as in issue list.", "Oops!");
        }
    }

    @FXML
    private void handleEditOption(ActionEvent event) throws SQLException {
        Member selectedMember = memberTableView.getSelectionModel().getSelectedItem();
        if(selectedMember == null){
            AlertMaker.warningAlert("Please select the member for edition.", "Oops!");
            return;
        }
        
        if(issueDAO.checkIssueTable1(selectedMember.getId())){
            AlertMaker.errorAlert("Cannot edit as this member is in issue list.", "Oops!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jfxlibraryassistant/ui/addmember/add_member.fxml"));
            Parent parent = loader.load();
            Add_memberController controller = loader.getController();
            controller.inflateUI(selectedMember);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit ");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageImage(stage);
            stage.setOnCloseRequest((e)->{
                handleRefreshMemberAction(new ActionEvent());
            });
        } catch (IOException ex) {
            Logger.getLogger(MemberlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    public void handleRefreshMemberlist(){
        ObservableList<Member> memberList;
        try {
            memberTableView.getItems().clear();
            memberList = memberDAO.getMembers();
            memberTableView.getItems().addAll(memberList);
        } catch (SQLException ex) {
            Logger.getLogger(MemberlistController.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }

    @FXML
    private void handleRefreshMemberAction(ActionEvent event) {
        handleRefreshMemberlist();
    }

}
