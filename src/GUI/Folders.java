package GUI;

import Classes.DataStructures.DoublyLinkedList;
import Classes.MailServer.App;
import Classes.MailServer.User;
import Classes.Misc.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Folders {

    private App app;
    private User user;
    @FXML
    private ListView<String> list;
    @FXML
    private TextField searchField;


    protected void setApp(App app) {
        this.app = app;
        user = this.app.getLoggedinUser();
        setUp();
    }

    protected void setUp() {
        File file = new File(user.getFilePath() + "\\inbox");
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        list.getItems().clear();
        for (String name : directories) {
            list.getItems().add(name);
        }
    }

    @FXML
    private void del() {
        String name = list.getSelectionModel().getSelectedItem();
        if (name == null) return;
        deleteRec(name);
        setUp();
        user.editFolders();
    }

    private void deleteRec(String name) {
        File selected = new File(user.getFilePath() + "\\inbox\\" + name);
        String[] entries = selected.list();
        for (String string : entries) {
            File currentFile = new File(selected.getPath(), string);
            if (!currentFile.delete()) deleteRec(name + "\\" + string);
        }
        //noinspection ResultOfMethodCallIgnored
        selected.delete();
    }

    @FXML
    private void add() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addFolder.fxml"));
            root = loader.load();
            AddFolder controller = loader.getController();
            controller.setApp(app, this);
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        Stage stage = new Stage();
        stage.setTitle("Add Folder");
        stage.getIcons().add(new Image("/icon.png"));
        stage.sizeToScene();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void search() {
        String text = searchField.getText();
        if (text.isEmpty()) {
            setUp();
        } else {
            File file = new File(user.getFilePath() + "\\inbox");
            String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
            DoublyLinkedList dll = Utils.matchArray(directories, text);
            Iterator<String> iter = dll.iterator(true);
            list.getItems().clear();
            while (iter.hasNext()) {
                list.getItems().add(iter.next());
            }
        }
    }

    @FXML
    private void rename() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rename.fxml"));
            root = loader.load();
            Rename controller = loader.getController();
            controller.setApp(app, this, list.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        Stage stage = new Stage();
        stage.setTitle("Rename folder");
        stage.getIcons().add(new Image("/icon.png"));
        stage.sizeToScene();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void back() {
        //TODO
    }
}
