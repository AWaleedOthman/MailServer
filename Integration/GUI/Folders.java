package GUI;

import Classes.App;
import Classes.DoublyLinkedList;
import Classes.User;
import Misc.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Folders {

    private final String sep = System.getProperty("file.separator");
    private App app;
    private User user;

    @FXML
    private AnchorPane rootPane;
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
        DoublyLinkedList dll = new DoublyLinkedList();
        try {
            Scanner sc = new Scanner(new File(user.getFilePath() + sep + "inbox" + sep + "folders.txt"));
            while (sc.hasNext()) {
                dll.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            Utils.fileNotFound();
        }
        Iterator<Object> iter = dll.iterator(true);
        list.getItems().clear();
        while (iter.hasNext()) {
            list.getItems().add(iter.next().toString());
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
        try {
            stage.getIcons().add(new Image("icon.png"));
        } catch (Exception e) {
            stage.getIcons().add(new Image("GUI" + sep + "icon.png"));
        }
        stage.sizeToScene();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getScene().getWindow().centerOnScreen();
        stage.show();
    }

    @FXML
    private void search() {
        String text = searchField.getText();
        if (text.isEmpty()) {
            setUp();
        } else {
            File file = new File(user.getFilePath() + sep + "inbox");
            String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
            DoublyLinkedList dll = Utils.matchArray(directories, text);
            Iterator<Object> iter = dll.iterator(true);
            list.getItems().clear();
            while (iter.hasNext()) {
                list.getItems().add(iter.next().toString());
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
        try {
            stage.getIcons().add(new Image("icon.png"));
        } catch (Exception e) {
            stage.getIcons().add(new Image("GUI" + sep + "icon.png"));
        }
        stage.sizeToScene();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getScene().getWindow().centerOnScreen();
        stage.show();
    }

    @FXML
    private void back() {
        AnchorPane pane = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.fileNotFound();
        }
        HomeController home = loader.getController();
        home.initialize(app);
        rootPane.getChildren().setAll(pane);
        rootPane.getScene().getWindow().setHeight(722);
        rootPane.getScene().getWindow().setWidth(1175);
        rootPane.getScene().getWindow().centerOnScreen();
    }
}
