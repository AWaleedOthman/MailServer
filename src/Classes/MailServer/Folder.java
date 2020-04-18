package Classes.MailServer;

import Classes.Misc.Birthday;
import Interfaces.MailServer.IFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Folder implements IFolder {
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
        File userInfo = new File(userFolder.getPath() + "\\info.txt");
        userInfo.createNewFile();
        File contacts = new File(userFolder.getPath() + "\\contacts.csv");
        contacts.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(userInfo, false));
        writer.write(name);
        writer.newLine();
        writer.write(gender);
        writer.newLine();
        writer.write(Integer.toString(bd.getDay()));
        writer.newLine();
        writer.write(Integer.toString(bd.getMonth()));
        writer.newLine();
        writer.write(Integer.toString(bd.getYear()));
        writer.newLine();
        writer.close();
        Folder inbox = userFolder.addSubFolder("inbox");
        File inboxIndex = new File(inbox.getPath() + "\\index.csv");
        inboxIndex.createNewFile();
        Folder archive = userFolder.addSubFolder("archive");
        File archiveIndex = new File(archive.getPath() + "\\index.csv");
        archiveIndex.createNewFile();
        Folder sent = userFolder.addSubFolder("sent");
        File sentIndex = new File(sent.getPath() + "\\index.csv");
        sentIndex.createNewFile();
        Folder trash = userFolder.addSubFolder("trash");
        File trashIndex = new File(trash.getPath() + "\\index.csv");
        trashIndex.createNewFile();

    }

    public String getPath() {
        return path;
    }
}
