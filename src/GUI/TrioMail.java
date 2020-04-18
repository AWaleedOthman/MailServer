package GUI;

import Classes.MailServer.User;
import Classes.Misc.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class TrioMail extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            User.loadUsers();
        } catch (IOException e) {
            Utils.fileNotFound();
        }

        Parent root = FXMLLoader.load(getClass().getResource("signin.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TrioMail");
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}
