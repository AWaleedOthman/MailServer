package Classes.GUI;

import Classes.MailServer.User;
import Classes.Misc.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class Signup {

    @FXML
    private TextField nameField, passwordField, confirmField, addressField;
    @FXML
    private Label invalidNameLabel, invalidAddressLabel, invalidPasswordLabel, invalidConfirmLabel;
    @FXML
    private ToggleGroup t1;
    @FXML
    private DatePicker datePicker;

    @FXML
    private void checkName() {
        if (!nameField.getText().isEmpty() && Utils.validString(nameField.getText())) {
            invalidNameLabel.setTextFill(Color.web("#006600"));
            invalidNameLabel.setText("valid name");
        } else {
            invalidNameLabel.setTextFill(Color.web("#ff0000"));
            invalidNameLabel.setText("invalid name");
        }
    }

    @FXML
    private void checkAddress() {
        String text = addressField.getText();
        if (User.addressExists(text)) {
            invalidAddressLabel.setTextFill(Color.web("#ff0000"));
            invalidAddressLabel.setText("email address already exists!\n try signing-in instead.");
        } else if (!text.isEmpty() && Utils.validAddress(addressField.getText())) {
            invalidAddressLabel.setTextFill(Color.web("#006600"));
            invalidAddressLabel.setText("valid address");
        } else {
            invalidAddressLabel.setTextFill(Color.web("#ff0000"));
            invalidAddressLabel.setText("invalid address\n(address can only contain numbers, letters, periods or underscore)");
        }
    }

    @FXML
    private void checkPassword() {
        if (passwordField.getText().isEmpty() || !Utils.validPassword(passwordField.getText())) {
            invalidPasswordLabel.setText("invalid password\n(password must be between 6 and 30 characters and cannot contain any spaces)");
        } else {
            invalidPasswordLabel.setText("");
        }
        if (!confirmField.getText().isEmpty())
            checkConfirm();
    }

    @FXML
    private void checkConfirm() {
        if (!confirmField.getText().isEmpty() && confirmField.getText().equals(passwordField.getText())) {
            invalidConfirmLabel.setTextFill(Color.web("#006600"));
            invalidConfirmLabel.setText("passwords match");
        } else {
            invalidConfirmLabel.setTextFill(Color.web("#ff0000"));
            invalidConfirmLabel.setText("passwords are not matching");
        }
    }

    @FXML
    private void callSignup() {
        System.out.println(t1.getSelectedToggle().toString());
        System.out.println(datePicker.getValue().getDayOfMonth());
        System.out.println(datePicker.getValue().getMonthValue());
        System.out.println(datePicker.getValue().getYear());
    }
}
//TODO translate stage on y coordinate
//TODO fix address lower case in whole project
//TODO create sign-in hyperlink
//TODO fix newline problem in labels
//TODO sign-up button
//TODO get people's opinion on final result
