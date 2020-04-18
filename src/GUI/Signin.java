package GUI;

import Classes.MailServer.App;
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
        App app = new App();
        ;
        if (!app.signin(addressField.getText().toLowerCase(), passwordField.getText())) {
            invalidSigninLabel.setText("Invalid email address or password");
            passwordField.setText("");
        } else {
            invalidSigninLabel.setText("");
            user = User.loadInfo(addressField.getText());
            passwordField.setText("");
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
