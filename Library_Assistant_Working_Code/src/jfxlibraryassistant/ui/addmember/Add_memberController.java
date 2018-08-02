/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.addmember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import jfxlibraryassistant.DAO.Book;
import jfxlibraryassistant.DAO.Member;
import jfxlibraryassistant.DAO.MemberDAO;
import jfxlibraryassistant.alert_maker.AlertMaker;
import jfxlibraryassistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class Add_memberController implements Initializable {

    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;
    
   
    @FXML
    private JFXTextField idField;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField mobileField;
    @FXML
    private JFXTextField emailField;
    
    private MemberDAO memberDAO;
    private boolean isInEditMode = Boolean.FALSE;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        memberDAO = new MemberDAO();
    }    

    @FXML
    private void saveMember(ActionEvent event) {
        String id = idField.getText();
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String email = emailField.getText();
        
        if(id.isEmpty() || name.isEmpty() || mobile.isEmpty() || email.isEmpty()){
            AlertMaker.errorAlert("Please fill in all text fields.", "Oops!");
            return;
        }
        
        if(isInEditMode){
            editMemberOption();
            return;
        }
        try {
           
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setHeaderText(null);
//            alert.setContentText("Are you sure to save this informations?" + "\n" + "Please check it again..."
//                    + "\n" + "->"
//                    + "\n" + "Member ID : " + id
//                    + "\n" + "Name : " + name
//                    + "\n" + "Mobile : " + mobile
//                    + "\n" + "Email : " + email);
//            alert.setTitle("Permission");
//            
//            Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
//            LibraryAssistantUtil.setStageImage(stage);
//            
//            Optional<ButtonType> response = alert.showAndWait();
//            if (response.get() == ButtonType.OK) {
//                memberDAO.saveMember(new Member(id,name,mobile,email));
//               AlertMaker.informationAlert("Success.", "Message");
//            }
            
             memberDAO.saveMember(new Member(id,name,mobile,email));
               AlertMaker.informationAlert("Success.", "Message");
            Stage stage1 = (Stage) cancelBtn.getScene().getWindow();
            stage1.close();
            
        } catch (SQLException ex) {
            AlertMaker.errorAlert("This member ID is already exists"+ "\n" + "Please set another ID.", "Message");
        }
        
        
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage)cancelBtn.getScene().getWindow();
        stage.close();
    }
    
    public void inflateUI(Member member){
        idField.setText(member.getId());
        nameField.setText(member.getName());
        mobileField.setText(member.getMobile());
        emailField.setText(member.getEmail());
        idField.setEditable(false);
        isInEditMode = Boolean.TRUE;
               
    }

    private void editMemberOption() {
        Member member = new Member(idField.getText(),nameField.getText(),mobileField.getText(),emailField.getText());
        try {
            if(memberDAO.updateForEditMember(member)){
                AlertMaker.informationAlert("Success.", "Message");
            }else{
                AlertMaker.errorAlert("Failed to edit.", "Message");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_memberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
