package GUI;

import Classes.App;
import Classes.DoublyLinkedList;
import Classes.User;
import Misc.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class FolderChooser {
    private final String sep = System.getProperty("file.separator");
    private User user;
    @FXML
    private ListView<String> list;

    protected void setApp(App app) {
        user = app.getLoggedinUser();
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
    protected void move() {
        String selected = list.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        //TODO
    }
}
