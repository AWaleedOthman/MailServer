package GUI;

import Classes.MailServer.App;
import Classes.Misc.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Signin {

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
        if (!app.signin(addressField.getText().toLowerCase(), passwordField.getText())) {
            invalidSigninLabel.setText("Invalid email address or password");
            passwordField.setText("");
        } else {
            AnchorPane pane = null;
            try {
                invalidSigninLabel.setText("");
                passwordField.setText("");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("contacts.fxml"));
                pane = loader.load();
                Contacts controller = loader.getController();
                controller.setApp(app);
            } catch (IOException e) {
                Utils.fileNotFound();
            }
            rootPane.getChildren().setAll(pane);
            rootPane.getScene().getWindow().setHeight(487);
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
        rootPane.getScene().getWindow().setHeight(659);
    }

}
