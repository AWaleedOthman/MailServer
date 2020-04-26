package GUI;

import Classes.MailServer.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;


public class AddFolder {

    private App app;
    private Folders foldersController;

    @FXML
    private Button cancelBtn;
    @FXML
    private TextField nameField;
    @FXML
    private Label invalid;

    protected void setApp(App app, Folders controller) {
        this.app = app;
        foldersController = controller;
    }

    @FXML
    private void add() {
        String name = nameField.getText();
        File f = new File(app.getLoggedinUser().getFilePath() + "\\inbox\\" + name);
        if (!f.mkdir()) invalid.setText("invalid folder name");
        else {
            invalid.setText("");
            foldersController.setUp();
            app.getLoggedinUser().addFolder(name);
            cancel();
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }


}
