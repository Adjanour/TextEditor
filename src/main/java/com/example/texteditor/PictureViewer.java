package com.example.texteditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PictureViewer implements Initializable {

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrev;

    FileChooser fileChooser = new FileChooser();
    List<File> acceptedPictures = new ArrayList<>();

    List<String> acceptedExtensions = Arrays.asList("jpg", "jpeg", "png");

    @FXML
    void onNext(ActionEvent event) {

    }

    @FXML
    void onOpenImage(ActionEvent event) {
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures (*.jpg,*.png)", "*.txt", "*.jpg", "*.jpeg", "*.png"));
        File SelectedFile = fileChooser.showOpenDialog(new Stage());
        if (SelectedFile != null) {
            //Do Nothing
        }
        else{
            acceptedPictures.clear();
            btnPrev.setDisable(false);
            btnNext.setDisable(true);
        }
    }
    @FXML
    void onPrev(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnNext.setDisable(true);
        btnPrev.setDisable(true);
    }
}
