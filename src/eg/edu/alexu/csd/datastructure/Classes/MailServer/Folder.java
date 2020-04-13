package eg.edu.alexu.csd.datastructure.Classes.MailServer;

import eg.edu.alexu.csd.datastructure.Classes.Misc.Birthday;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Folder {
    private String path;

    public Folder(String path) {
        this.path = path;
    }

    private Folder addSubFolder(String name) {
        File f = new File(path + "\\" + name);
        f.mkdir();
        return new Folder(path + "\\" + name);
    }

    public void createUserFolder(String address, String name, String gender, Birthday bd) throws IOException {
        Folder userFolder = addSubFolder(address);
        File userInfo = new File(userFolder.addSubFolder("info.txt").getPath());
        BufferedWriter writer = new BufferedWriter(new FileWriter(userInfo, false));
        writer.write(name);
        writer.newLine();
        writer.write(gender);
        writer.newLine();
        writer.write(bd.getDay());
        writer.newLine();
        writer.write(bd.getMonth());
        writer.newLine();
        writer.write(bd.getYear());
        writer.newLine();
        Folder inbox = userFolder.addSubFolder("inbox");
        inbox.addSubFolder("index.txt");
        Folder archive = userFolder.addSubFolder("archive");
        archive.addSubFolder("index.txt");
        Folder sent = userFolder.addSubFolder("sent");
        sent.addSubFolder("index.txt");
        Folder trash = userFolder.addSubFolder("trash");
        trash.addSubFolder("index.txt");

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
