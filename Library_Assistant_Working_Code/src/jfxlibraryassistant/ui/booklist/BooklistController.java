/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.booklist;

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
import jfxlibraryassistant.DAO.Book;
import jfxlibraryassistant.DAO.BookDAO;
import jfxlibraryassistant.DAO.IssueDAO;
import jfxlibraryassistant.alert_maker.AlertMaker;
import jfxlibraryassistant.main.MainController;
import jfxlibraryassistant.ui.addbook.AddbookController;
import jfxlibraryassistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author ChanMyaeOo
 */
public class BooklistController implements Initializable {

    @FXML
    private JFXTextField inputField;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> bookIDCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML
    private TableColumn<Book, String> availableCol;

    private BookDAO bookDAO;
    private IssueDAO issueDAO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bookDAO = new BookDAO();
        issueDAO = new IssueDAO();

        try {
            ObservableList<Book> bookList = bookDAO.getBooks();
            tableView.getItems().addAll(bookList);

            initColumn();

            //filter and search data from lmsdb.books table
            FilteredList<Book> filteredData = new FilteredList<>(bookList, e -> true);
            inputField.setOnKeyReleased(e -> {
                inputField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    filteredData.setPredicate((Predicate<? super Book>) book -> {
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        } else if (book.getId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getPublisher().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if(book.getIs_available().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }

                        return false;
                    });
                    SortedList<Book> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(tableView.comparatorProperty());
                    tableView.setItems(sortedData);
                });
            });

        } catch (SQLException ex) {
            Logger.getLogger(BooklistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initColumn() {
        bookIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("is_available"));
    }

    @FXML
    private void handleDeleteBookOption(ActionEvent event) throws SQLException {
        Book selectedDeleteBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedDeleteBook == null) {
            AlertMaker.warningAlert("Please select the book for deletion.", "Oops!");
            return;
        }

        if (issueDAO.checkIssueTable(selectedDeleteBook.getId())) {
            AlertMaker.errorAlert("Cannot delete this book as it is already issued.", "Oops!");
            return;
        }
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you want to delete this book? "
                    + "\n"
                    + "Book ID : " + selectedDeleteBook.getId()
                    + "\n"
                    + "Title : " + selectedDeleteBook.getTitle()
            );
            alert.setHeaderText(null);
            alert.setTitle("Message");

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            LibraryAssistantUtil.setStageImage(stage);
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                bookDAO.deleteBook(selectedDeleteBook);
                tableView.getItems().remove(selectedDeleteBook);
                AlertMaker.informationAlert("Success.", "Message");
            }
        } catch (SQLException ex) {
            AlertMaker.errorAlert("Cannot delete this book as it was already issued.", "Oops!");
        }
    }

    @FXML
    private void handleEditBookOption(ActionEvent event) throws SQLException {

        Book selectedEditBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedEditBook == null) {
            AlertMaker.warningAlert("Please select the book for edition.", "Oops!");
            return;
        }
        if (issueDAO.checkIssueTable(selectedEditBook.getId())) {
            AlertMaker.errorAlert("Cannot edit this book as it is already issued.", "Oops!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/jfxlibraryassistant/ui/addbook/addbook.fxml"));
            Parent parent = loader.load();
            AddbookController controller = loader.getController();
            controller.inflateUI(selectedEditBook);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit book");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageImage(stage);
              
            stage.setOnCloseRequest((e)->{
              handleRefreshBookOption(new ActionEvent());
            });
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleRefreshBookOption(ActionEvent event) {
        refreshBookTable();
    }

    public void refreshBookTable() {

        ObservableList<Book> bookList;
        try {
            tableView.getItems().clear();   //for refreshing book list           
            bookList = bookDAO.getBooks();
            tableView.getItems().addAll(bookList);
        } catch (SQLException ex) {
            Logger.getLogger(BooklistController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
