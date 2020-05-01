package Classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.StreamSupport;

import Interfaces.IFolder;
import Misc.Birthday;

public class Folder implements IFolder {
	/*
	 * FolderPath member stores the folder path on the system
	 * */
	private final String path;

    public Folder(String path) {
        this.path = path;
    }

    private static String sep = System.getProperty("file.separator");
    
    public void createUserFolder(String address, String name, String gender, Birthday bd) throws IOException {
        Folder userFolder = addSubFolder(address);
        File userInfo = new File(userFolder.getPath() + sep + "info.txt");
        userInfo.createNewFile();
        File contacts = new File(userFolder.getPath() + sep + "contacts.csv");
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
        File inboxIndex = new File(inbox.getPath() + sep + "index.csv");
        inboxIndex.createNewFile();
        File inboxFolders = new File(inbox.getPath() + sep + "folders.txt");
        inboxFolders.createNewFile();
        Folder archive = userFolder.addSubFolder("archive");
        File archiveIndex = new File(archive.getPath() + sep + "index.csv");
        archiveIndex.createNewFile();
        Folder sent = userFolder.addSubFolder("sent");
        File sentIndex = new File(sent.getPath() + sep + "index.csv");
        sentIndex.createNewFile();
        Folder trash = userFolder.addSubFolder("trash");
        File trashIndex = new File(trash.getPath() + sep + "index.csv");
        trashIndex.createNewFile();
        Folder drafts = userFolder.addSubFolder("drafts");
        File draftsIndex = new File(drafts.getPath() + sep + "index.csv");
        draftsIndex.createNewFile();

    }	

    public String folderName() {
    	String[] names = splitPath(this.path);
    	return names[names.length-1];
    }
    
    private static String[] splitPath(String pathString) {
        Path path = Paths.get(pathString);
        return StreamSupport.stream(path.spliterator(), false).map(Path::toString)
                            .toArray(String[]::new);
    }
    
    protected Folder addSubFolder(String name) {
        File f = new File(path + sep + name);
        f.mkdir();
        return new Folder(path + sep + name);
    }
	
    
    
    public static boolean copyFiles(File from, String desPath) {
		try {
			byte[] fileContent = Files.readAllBytes(from.toPath());
			File out = new File(desPath);
			FileOutputStream fos = new FileOutputStream(out);
			fos.write(fileContent);
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
    }
    
    public static void deleteMailFolder(String path) {
    	Folder currentFolder = new Folder(path); 
    	File file = new File(currentFolder.getPath() + sep + "attachment");
		if (file.exists()) {
			String[] files = file.list();
			for (String pathname : files) {
				File srcFile = new File(currentFolder.getPath() + sep + "attachment" + sep + pathname);
				srcFile.delete();
	        }
			File originalAttachmentFolder = new File(currentFolder.getPath() + sep + "attachment");
			originalAttachmentFolder.delete();
		}
		// deleting original files
		File originalFile = new File(currentFolder.getPath() + sep + currentFolder.folderName() + ".txt");
		originalFile.delete();
		File originalFolder = new File(currentFolder.getPath());
		originalFolder.delete();
    }

	public String getPath() {
		return path;
	}

	public String getIndexPath() {
		return path + sep +"index.csv";
	}
	
}
