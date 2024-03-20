package com.example.texteditor;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.FileSystem;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable {

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnLock;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnSave;

    @FXML
    private Label lblDisplay;

    @FXML
    private MenuItem mnClose;

    @FXML
    private Label lblCols;


    @FXML
    private MenuItem mnNew;

    @FXML
    private MenuItem mnOpen;

    @FXML
    private MenuItem mnSave;

    @FXML
    private TextArea txtEditor;
    @FXML
    private Label lblLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnLogin;



    boolean isSaved = false;
    FileChooser fileChooser = new FileChooser();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save changes?", ButtonType.OK, ButtonType.CANCEL);
    Alert loginAlert = new Alert(Alert.AlertType.INFORMATION,"Invalid username and password",ButtonType.OK);

    @FXML
    void getCharacters(KeyEvent event) {
        lblDisplay.setText( "Characters Typed: " + txtEditor.getText().length());
        isSaved = false; // Mark changes as unsaved
        lblCols.setText("Ln " + getLineAndCol()[0] + ", Col " + getLineAndCol()[1]);
    }

    @FXML
    void onEdit(ActionEvent event) {
        txtEditor.setEditable(true);
        btnLock.setText(txtEditor.isEditable() ? "Lock" : "Unlock");
    }

    @FXML
    void onExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onNew(ActionEvent event){
        if (isSaved){
        txtEditor.clear();
        lblDisplay.setText( "Characters Typed: " + txtEditor.getText().length());
    }else {
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        try {
                            onSave(event);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @FXML
    void onLock(ActionEvent event) {
        txtEditor.setEditable(!txtEditor.isEditable());
        btnLock.setText(txtEditor.isEditable() ? "Lock" : "Unlock");
    }

    @FXML
    void onOpen(ActionEvent event) throws FileNotFoundException {
        //check if the file is saved
        if(txtEditor.getLength() <= 0 || isSaved){
            fileChooser.setTitle("Open File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
               Scanner scanner = new Scanner(selectedFile);
               txtEditor.clear();
               while (scanner.hasNextLine()) {
                   txtEditor.appendText(scanner.nextLine()+"\n");
               }
               scanner.close();
             }
            lblDisplay.setText( "Characters Typed: " + txtEditor.getText().length());
        }
        else{
            alert.showAndWait()
//                    .filter(response -> response == ButtonType.OK || response == ButtonType.CANCEL)
                    .ifPresent(response ->{
                        if (response == ButtonType.OK){
                            try {
                                onSave(event);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            txtEditor.clear();
                            lblDisplay.setText( "Characters Typed: " + txtEditor.getText().length());
                        }

                    });
        }
    }

    @FXML
    void onSave(ActionEvent event) throws FileNotFoundException {
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File SelectedFile = fileChooser.showSaveDialog(new Stage());
        if (SelectedFile != null) {
            PrintWriter writer = new PrintWriter(SelectedFile);
            writer.println(txtEditor.getText());
            writer.close();
            isSaved = true;
        }

    }

    int[] getLineAndCol(){
        int lineCount = 1;
        int colCount = 1;
        int caretPosition = txtEditor.getCaretPosition();
        for (int i = 0; i < caretPosition; i++) {
            if (txtEditor.getText().charAt(i) == '\n' ){
                lineCount += 1;
                colCount = 1;
            }
            else {
                colCount ++;
            }
        }
        return new int[]{lineCount, colCount};
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\Users"));
    }


    @FXML
    void onLogin(ActionEvent event) throws IOException {
        if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()){
            Alert loginAlert = new Alert(Alert.AlertType.INFORMATION,"Please enter your credentials",ButtonType.OK);
            loginAlert.showAndWait();
        }
        if (txtUsername.getText().equals("admin") && txtPassword.getText().equals("1234")){
            lblLogin.setText("Valid Login Details");
            lblLogin.setTextFill(Color.GREEN);
            txtUsername.setText("");
            txtPassword.setText("");
            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            loginStage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load(), 873, 624);
            stage.setTitle("Text Editor");
            stage.setScene(scene);
            stage.show();
        }else {
            loginAlert.showAndWait()
                            .ifPresent(response ->{
                                txtPassword.setText("");
                                txtUsername.setText("");
                            });
            lblLogin.setText("Invalid username and password");
            lblLogin.setTextFill(Color.RED);
        }
    }
}
//get caret position
//loop till current caret position and count the number of new lines and add one since the lines cc counted from 0
// that becomes the line position