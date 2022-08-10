package controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;

public class TextEditorFormController {
    public AnchorPane pneContainer;
    public JFXTextArea txtEditor;
    public MenuItem mnuNew;
    public MenuItem mnuOpen;
    public MenuItem mnuSave;
    public MenuItem mnuPrint;
    public MenuItem mnuClose;
    public MenuItem mnuCut;
    public MenuItem mnuCopy;
    public MenuItem mnuPaste;
    public MenuItem mnuSelectAll;
    public MenuItem mnuAbout;

    public void initialize() {
    }

    public void mnuNew_OnAction(ActionEvent actionEvent) {
        txtEditor.setText("");
    }

    public void mnuOpen_OnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Select a file to open");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.txt","*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.dep9","*.dep9"));
        File file = fileChooser.showOpenDialog(pneContainer.getScene().getWindow());
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            if (file.getName().contains(".txt")) {
                while (true) {
                    byte[] buffer = new byte[1024 * 10];
                    int read = bis.read(buffer);
                    if (read == -1) break;
                    for (byte b : buffer) {
                        txtEditor.appendText(String.valueOf((char) b));
                    }
                }
                bis.close();
            }
            else if (file.getName().contains(".dep9")) {
                while (true) {
                    byte[] buffer = new byte[1024 * 10];
                    int read = bis.read(buffer);
                    if (read == -1) break;
                    for (int i = 0; i < read; i++) {
                        buffer[i] = (byte) (buffer[i] - 1);
                        txtEditor.appendText(String.valueOf((char)buffer[i]));
                    }
                }
                bis.close();
            }
        }
    }

    public void mnuSave_OnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/SaveForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent root = fxmlLoader.load();

        SaveFormController ctrl = fxmlLoader.getController();
        ctrl.setData(txtEditor.getText());

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Save File");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public void mnuPrint_OnAction(ActionEvent actionEvent) {
        if (Printer.getDefaultPrinter() == null) {
            new Alert(Alert.AlertType.ERROR,"No default printer has been selected").showAndWait();
            return;
        }
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            printerJob.showPageSetupDialog(pneContainer.getScene().getWindow());
            boolean success = printerJob.printPage(txtEditor);
            if (success) {
                printerJob.endJob();
            }
            else {
                new Alert(Alert.AlertType.ERROR,"Failed to print, try again").showAndWait();
            }
        }
        else {
            new Alert(Alert.AlertType.ERROR,"Failed to initialize a new printer job").show();
        }
    }

    public void mnuClose_OnAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void mnuAbout_OnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/AboutForm.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
