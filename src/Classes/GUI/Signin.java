package Classes.GUI;

import Classes.MailServer.User;
import Classes.Misc.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Signin {

    @FXML
    private TextField addressField, passwordField;
    @FXML
    private Label invalidSigninLabel;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void signin() {
        User user = null;
        try {
            user = User.signin(addressField.getText(), passwordField.getText());
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        passwordField.setText("");
        if (user == null) {
            invalidSigninLabel.setText("Invalid email address or password");
        } else {
            invalidSigninLabel.setText("");
            //TODO open new scene passing user
        }
    }

    @FXML
    private void loadSignup() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("signup.fxml"));
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        rootPane.getChildren().setAll(pane);
        rootPane.getScene().getWindow().sizeToScene();
    }

}
