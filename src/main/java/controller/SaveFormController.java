package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.*;
import java.util.Optional;

public class SaveFormController {

    public AnchorPane pneSaveForm;
    public JFXTextField txtFileName;
    public JFXButton btnOK;
    public JFXButton btnBrowsePath;
    public JFXTextField txtFilePath;
    private String content;
    private File file;

    public void initialize() {
        FadeTransition fd = new FadeTransition(Duration.millis(1500),pneSaveForm);
        fd.setFromValue(0);
        fd.setToValue(1);
        fd.playFromStart();
    }
    public void setData(String editorContent) {
        content = editorContent;
    }

    public void btnOK_OnAction(ActionEvent actionEvent) throws IOException {
        if (txtFileName.getText().isBlank()) {
            new Alert(Alert.AlertType.ERROR,"File name cannot be empty!").showAndWait();
            txtFileName.selectAll();
            txtFileName.requestFocus();
            txtFilePath.clear();
            return;
        }
        if (!isName(txtFileName.getText())) {
            new Alert(Alert.AlertType.ERROR,"Invalid file name format!").showAndWait();
            txtFileName.selectAll();
            txtFileName.requestFocus();
            txtFilePath.clear();
            return;
        }
        if (txtFilePath.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Save location path is empty").showAndWait();
            btnBrowsePath.requestFocus();
            return;
        }
        pneSaveForm.getScene().getWindow().hide();
        file.createNewFile();
        byte[] bytes = content.getBytes();
        FileOutputStream fos = new FileOutputStream(file);
        for (byte aByte : bytes) {
            fos.write(aByte + 1);
        }
        new Alert(Alert.AlertType.INFORMATION,"Files has been saved successfully").showAndWait();
    }
    public void btnBrowsePath_OnAction(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        directoryChooser.setTitle("Select a directory to save");
        File directory = directoryChooser.showDialog(pneSaveForm.getScene().getWindow());
        if (directory != null) {
            file = new File(directory.getAbsolutePath(), txtFileName.getText() + ".dep9");
            if (file.exists()) {
                Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION,
                        file.getName()+ " file is already exists. Do you want to overwrite?",
                        ButtonType.YES, ButtonType.NO).showAndWait();
                if (result.get() == ButtonType.NO) {
                    return;
                }
            }
            txtFilePath.setText(file.getAbsolutePath());
        }
    }

    private boolean isName(String input) {
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            if (!Character.isLetter(aChar) && (!Character.isDigit(aChar)) && aChar != ' ' && aChar != '-') {
                return false;
            }
        }
        return true;
    }
}
