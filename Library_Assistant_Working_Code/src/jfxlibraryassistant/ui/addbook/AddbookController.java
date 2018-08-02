/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.addbook;

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
import jfxlibraryassistant.DAO.BookDAO;
import jfxlibraryassistant.alert_maker.AlertMaker;
import jfxlibraryassistant.ui.booklist.BooklistController;
import jfxlibraryassistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class AddbookController implements Initializable {

    @FXML
    private JFXTextField idField;
    @FXML
    private JFXTextField titleField;
    @FXML
    private JFXTextField authorField;
    @FXML
    private JFXTextField publisherField;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;

    private BookDAO bookDAO = new BookDAO();
    private boolean isInEditMode = Boolean.FALSE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void saveBook(ActionEvent event) {
        String id = idField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {                   
            AlertMaker.errorAlert("Please fill in all text fields.", "Oops!");
            return;
        }
        if(isInEditMode)
        {
            handleEditOperation();
            return;
        }

        try {
             bookDAO.addBook(new Book(id, title, author, publisher));
                AlertMaker.informationAlert("Success", "Message");
                
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setHeaderText(null);
//            alert.setContentText("Are you sure to save this informations?" + "\n" + "Please check it again..."
//                    + "\n" + "->"
//                    + "\n" + "Book ID : " + id
//                    + "\n" + "Title : " + title
//                    + "\n" + "Author : " + author
//                    + "\n" + "Publisher : " + publisher);
//            alert.setTitle("Permission");
            
            //Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
            //LibraryAssistantUtil.setStageImage(stage);
            
//            Optional<ButtonType> response = alert.showAndWait();
//            
//            if (response.get() == ButtonType.OK) {
//                bookDAO.addBook(new Book(id, title, author, publisher));
//                AlertMaker.informationAlert("Success", "Message");
//            }
//
            Stage stage1 = (Stage) cancelBtn.getScene().getWindow();
            stage1.close();

        } catch (SQLException ex) {
            AlertMaker.errorAlert("This book ID is already exists." + "\n" + "Please set another book ID", "Message");
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }
    
    
    //for edit book
    public void inflateUI(Book book){
        idField.setText(book.getId());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        publisherField.setText(book.getPublisher());
        idField.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

    private void handleEditOperation() {
        try {
            Book book = new Book(idField.getText(),titleField.getText(),authorField.getText(),publisherField.getText());
            if(bookDAO.updateEditBook(book)){
                AlertMaker.informationAlert("Book updated.", "Success");
                
            }
            else{
                AlertMaker.errorAlert("Failed.", "Message");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AddbookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
