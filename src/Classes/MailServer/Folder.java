package Classes.MailServer;

import Classes.Misc.Birthday;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
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
        File userInfo = new File(userFolder.getPath() + "\\info.txt");
        userInfo.createNewFile();
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
        File inboxIndex = new File(inbox.getPath() + "\\index.txt");
        inboxIndex.createNewFile();
        Folder archive = userFolder.addSubFolder("archive");
        File archiveIndex = new File(archive.getPath() + "\\index.txt");
        archiveIndex.createNewFile();
        Folder sent = userFolder.addSubFolder("sent");
        File sentIndex = new File(sent.getPath() + "\\index.txt");
        sentIndex.createNewFile();
        Folder trash = userFolder.addSubFolder("trash");
        File trashIndex = new File(trash.getPath() + "\\index.txt");
        trashIndex.createNewFile();

    }

    public String getPath() {
        return path;
    }
}
