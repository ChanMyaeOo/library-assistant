/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.smattme.MysqlExportService;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;
import jfxlibraryassistant.DAO.Book;
import jfxlibraryassistant.DAO.BookDAO;
import jfxlibraryassistant.DAO.IssueDAO;
import jfxlibraryassistant.DAO.IssueInfo;
import jfxlibraryassistant.DAO.Member;
import jfxlibraryassistant.DAO.MemberDAO;
import jfxlibraryassistant.alert_maker.AlertMaker;
import jfxlibraryassistant.database.Database;
import jfxlibraryassistant.util.LibraryAssistantUtil;



/**
 *
 * @author ChanMyaeOo
 */
public class MainController implements Initializable {

    @FXML
    private Button addBookBtn;
    @FXML
    private HBox book_info;
    @FXML
    private HBox member_info;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text titleText;
    @FXML
    private Text authorText;
    private Text publisherText;
    @FXML
    private TextField memberIDInput;
    @FXML
    private Text nameText;
    @FXML
    private Text mobileText;
    @FXML
    private Text availableText;

    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private IssueDAO issueDAO = new IssueDAO();
    
    @FXML
    private Text emailText;
    @FXML
    private JFXButton issueButton;
    @FXML
    private JFXTextField bookIDForSubmission;
    @FXML
    private JFXButton renewButton;
    @FXML
    private JFXButton subButton;
    @FXML
    private ListView<String> dataList;      //for issue list to show in submission session
    @FXML
    private StackPane rootPane;
    @FXML
    private MenuItem backUpItem;
    @FXML
    private MenuItem restoreItem;
    
    private static final String BACKUP = "http://localhost/phpmyadmin/server_export.php";
    private static final String RESTORE = "http://localhost/phpmyadmin/server_import.php";
    @FXML
    private MenuItem aboutItem;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);
    }

    @FXML
    private void loadAddBookWindow(ActionEvent event) throws IOException {
        loadWindow("/jfxlibraryassistant/ui/addbook/addbook.fxml", "Add new book");
    }

    @FXML
    private void loadAddMemberWindow(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/addmember/add_member.fxml", "Add new member");
    }

    @FXML
    private void loadMemberListWindow(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/memberlist/memberlist.fxml", "Member List");
    }

    @FXML
    private void loadBookListWindow(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/booklist/booklist.fxml", "Book List");
    }

    @FXML
    private void loadIssueListWindow(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/issuelist/issue_list.fxml", "Issue List");
    }

    @FXML
    private void loadSettingWindow(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/settings/settings.fxml", "Setting");
    }
    
    
    @FXML
    private void loadCheckWindow(ActionEvent event) {
         //loadWindow("/jfxlibraryassistant/ui/check/check.fxml", "Check Date and Fine");
          try {
            
            Parent parent = FXMLLoader.load(getClass().getResource("/jfxlibraryassistant/ui/check/check.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Check Date and Fine");
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageImage(stage);
            
            stage.setOnCloseRequest((WindowEvent event1) -> {
                try {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    issueDAO.removeCurDate();
                } catch (SQLException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    //for menu bar and menu item
    @FXML
    private void handleAddMemberAction(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/addmember/add_member.fxml", "Add new member");
    }

    @FXML
    private void handleAddBookAction(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/addbook/addbook.fxml", "Add new book");
    }

    @FXML
    private void handleMemberListAction(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/memberlist/memberlist.fxml", "Member List");
    }

    @FXML
    private void handleBookListAction(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/booklist/booklist.fxml", "Book List");
    }

    @FXML
    private void handleIssueListAction(ActionEvent event) {
        loadWindow("/jfxlibraryassistant/ui/issuelist/issue_list.fxml", "Issue List");
    }
    
     @FXML
    private void loadAboutWindow(ActionEvent event) {
        //loadWindow("/jfxlibraryassistant/about/About.fxml", "About me");
        try {
            
            Parent parent = FXMLLoader.load(getClass().getResource("/jfxlibraryassistant/about/About.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Contact");
            stage.setScene(new Scene(parent));
            stage.setResizable(false);      //i want to do this because of not using load window method
            stage.show();
            LibraryAssistantUtil.setStageImage(stage);

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //for comman use to this method
    public void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            LibraryAssistantUtil.setStageImage(stage);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void closeMainWindow(ActionEvent event) {
       
        Stage stage = (Stage) addBookBtn.getScene().getWindow();
        stage.close();
        try {
            issueDAO.removeCurDate();
            Database.getInstance().disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    //search book information for issue button and to show in book session at main window
    @FXML
    private void searchBookInfo(ActionEvent event) {
        String id = bookIDInput.getText();

        try {
            Book book = bookDAO.getBook(id);
            if (book != null) {
                titleText.setText("- " + book.getTitle());
                authorText.setText("- " + book.getAuthor());
                availableText.setText("- " + book.getIs_available());
            } else if (id.isEmpty()) {

                titleText.setText("- Title");
                authorText.setText("- Author");
                availableText.setText("- Is available?");
            } else {
                titleText.setText("-");
                authorText.setText("No such book available.");
                availableText.setText("-");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //serarh member information for issue button and to show in member session at main window
    @FXML
    private void searchMemberInfo(ActionEvent event) {
        String id = memberIDInput.getText();
        try {
            Member member = memberDAO.getMember(id);
            if (member != null) {
                nameText.setText("- " + member.getName());
                mobileText.setText("- " + member.getMobile());
                emailText.setText("- " + member.getEmail());
            } else if (id.isEmpty()) {
                nameText.setText("- Name");
                mobileText.setText("- Mobile");
                emailText.setText("- Email");
            } else {
                nameText.setText("-");
                mobileText.setText("No such member available.");
                emailText.setText("-");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //set data to issue table if user pressed issue button and then update boolean isAvailable value to false
    @FXML
    private void issueBook(ActionEvent event) throws SQLException {
        String bookID = bookIDInput.getText();
        String memberID = memberIDInput.getText();

        try {
            if (issueDAO.checkIssueTable(bookID)) {

                if (issueDAO.checkIssueTable1(memberID)) {
                    AlertMaker.errorAlert("This book is already issued.", "Message");
                    return;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            issueDAO.setIssueBook(bookID, memberID);

            bookDAO.updateAvailable(bookID, false);
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText(null);
            alert1.setContentText("Complete issue book..."
                    + "\n" + "->"
                    + "\n" + "Book ID : " + bookID
                    + "\n" + "Member ID : " + memberID
            );
            alert1.setTitle("Success.");
            alert1.show();
            //to clear bookID textfield and memberID textfield
            bookIDInput.setText("");
            memberIDInput.setText("");
            titleText.setText("- Title");
            authorText.setText("- Author");
            availableText.setText("- Is available?");
            nameText.setText("- Name");
            mobileText.setText("- Mobile");
            emailText.setText("- Email");
        } catch (SQLException ex) {

            if (memberID.isEmpty() || bookID.isEmpty()) {
                AlertMaker.warningAlert("Please fill Book ID and Member ID to complete issue.", "Message");
                return;
            } else {
                AlertMaker.errorAlert("Book ID or Member ID is invalid." + "\n" + "Please! Check again.", "Message");
                return;
            }
        }

    }

    @FXML
    private void searchIssueInfo(ActionEvent event) {
        ObservableList<String> issueData = FXCollections.observableArrayList();

        String inputBookID = bookIDForSubmission.getText();
        try {
            IssueInfo issueInfo = issueDAO.getIssueInfo(inputBookID);
            if (issueInfo == null) {
                System.out.println("Error... because of null pointer at issueDAO.getIssueInfo()");

            }
            if (issueInfo != null) {
                issueData.add("Issue Date : " + issueInfo.getIssueDate());
                issueData.add("Renew Count : " + " " + issueInfo.getRenewCount());

                Book book = bookDAO.getBook(issueInfo.getBookID());
                issueData.add("Book Information :- ");
                issueData.add("\t" + "Book ID : " + book.getId());
                issueData.add("\t" + "Title : " + book.getTitle());
                issueData.add("\t" + "Author : " + book.getAuthor());
                issueData.add("\t" + "Publisher : " + book.getPublisher());

                Member member = memberDAO.getMember(issueInfo.getMemberID());
                issueData.add("Member Information :- ");
                issueData.add("\t" +"Member ID : " + member.getId());
                issueData.add("\t" +"Name : " + member.getName());
                issueData.add("\t" +"Mobile : " + member.getMobile());
                issueData.add("\t" +"Email : " + member.getEmail());
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        dataList.getItems().setAll(issueData);
        //renewButton.setDisable(false);
    }

    //update renew count value if renewButton is pressed
    @FXML
    private void updateRenewCountValue(ActionEvent event) {
        String bookID = bookIDForSubmission.getText();

        try {
            if (issueDAO.checkIssueTable(bookID)) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you really want to count renew on this book?");
                alert.setTitle("Permission");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                LibraryAssistantUtil.setStageImage(stage);
                Optional<ButtonType> response = alert.showAndWait();
                if (response.get() == ButtonType.OK) {
                    issueDAO.updateRenewCount(bookID);
                    dataList.getItems().clear();
                    bookIDForSubmission.setText("");
                    AlertMaker.informationAlert("Success.", "Message");

                }
            }

            //alert.show();
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //for submission book
    @FXML
    private void startSubmission(ActionEvent event) {
        String input = bookIDForSubmission.getText();

        try {
            if (issueDAO.checkIssueTable(input)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you really want to complete submission to this book?");
                alert.setTitle("Permission");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                LibraryAssistantUtil.setStageImage(stage);
                Optional<ButtonType> response = alert.showAndWait();
                if (response.get() == ButtonType.OK) {
                    issueDAO.deleteIssueInfo(input);
                    bookDAO.updateAvailable(input, true);
                    dataList.getItems().clear();
                    bookIDForSubmission.setText("");

                    AlertMaker.informationAlert("Success.", "Message");

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //backup and restore
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
        stage.setTitle("Error Handling");
        stage.show();
        LibraryAssistantUtil.setStageImage(stage);
    }
    @FXML
    private void backupFile(ActionEvent event){
        loadWebpage(BACKUP);
    }
    

    @FXML
    private void restoreFile(ActionEvent event) {
       loadWebpage(RESTORE);
    }
}


