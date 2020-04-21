package GUI;

import Classes.MailServer.App;
import Classes.MailServer.User;
import Classes.Misc.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Signin implements Initializable {

    private App app;

    protected void setApp(App app) {
        this.app = app;
    }

    @FXML
    private TextField addressField, passwordField;
    @FXML
    private Label invalidSigninLabel;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void signin() {
        User user = null;
        if (!app.signin(addressField.getText().toLowerCase(), passwordField.getText())) {
            invalidSigninLabel.setText("Invalid email address or password");
            passwordField.setText("");
        } else {
            invalidSigninLabel.setText("");
            passwordField.setText("");
            //TODO open new scene
        }
    }

    @FXML
    private void loadSignup() {
        AnchorPane pane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            pane = loader.load();
            Signup signupController = loader.getController();
            signupController.setApp(app);
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        rootPane.getChildren().setAll(pane);
        rootPane.getScene().getWindow().sizeToScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
