package Classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import Interfaces.IApp;
import Interfaces.IContact;
import Interfaces.IFilter;
import Interfaces.IFolder;
import Interfaces.ILinkedList;
import Interfaces.IMail;
import Interfaces.ISort;

public class App implements IApp {

	private Folder currentFolder = new Folder(".");
	public DoublyLinkedList mails = new DoublyLinkedList();
	public DoublyLinkedList workingList;
	boolean reverseSorting = false;
	
	public App() {
		loadMails();
	}
	
	@Override
	public boolean signin(String email, String password) {
		return false;
	}

	@Override
	public boolean signup(IContact contact) {
		return false;
	}

	@Override
	public void setViewingOptions(IFolder folder, IFilter filter, ISort sort) {
		// No loaded mails and no specified folder to load from
		if (folder == null && mails == null) { throw new IllegalArgumentException();}
		// Setting the specified folder
		if (folder != null) {
			this.currentFolder = (Folder) folder;
			mails.clear();
			loadMails();
		}
		workingList = mails.copyView();
		// Setting the specified filter
		if (filter != null) {
			workingList = (DoublyLinkedList) ((Filter) filter).filter(workingList, currentFolder);
		}
		// Setting the specified sort
		if (sort != null) {
			Sort sortClass = (Sort)sort; 
			workingList.Qsort(sortClass.sortAttribute());
		}
	}
	
	@Override
	public IMail[] listEmails(int page) {
		// If no loaded mails then Set viewing options has not been called yet
		if (this.mails == null || this.workingList == null) { throw new IllegalStateException();}
		
		Mail[] returnedMails = new Mail[10];
		int counter = 0;
		
		Iterator<Object> it = workingList.iterator(!reverseSorting);
		// Skip the unwanted pages
		while (it.hasNext() && counter < (page-1) * 10) {
			counter ++;
		}
		// Load the wanted page
		while (it.hasNext() && counter < (page) * 10) {
			Mail tmpMail = (Mail) it.next();
			returnedMails[counter%10] = tmpMail;
			counter ++;
		}
		return returnedMails;
	}

	@Override
	public void deleteEmails(ILinkedList mails) {
		
	}

	@Override
	public void moveEmails(ILinkedList mails, IFolder des) {
		
	}

	@Override
	public boolean compose(IMail email) {
		Mail  mail = (Mail)email;
    	// Data to be entered in index file
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE - MMM dd - yyyy HH:mm:ss a");
		String date = formatter.format(mail.getDate());
    	String content = mail.getID() + "," + mail.getTitle() + "," + mail.getSenderAddress() + "," + mail.getSenderName()
				+ "," + date + "," + (mail.getPriority().ordinal()+1);
    	String s = System.getProperty("file.separator");
		try {
			//appending data in csv file
			// Appending in the sender sent folder index
			BufferedWriter edit = new BufferedWriter(
					new FileWriter("system" + s + "users" + s + mail.getSenderAddress() + s + "sent" + s + "index.csv", true));
			edit.append(content);
			edit.append("\n");
			edit.flush();
			edit.close();
			// Creating mail folder in the sent folder
			Folder dir = new Folder("system" + s + "users"+ s + mail.getSenderAddress() + s + "sent" + s);
			dir.addSubFolder(mail.getID()+"");
			Folder mailDir = new Folder("system" + s + "users"+ s + mail.getSenderAddress() + s + "sent" + s + mail.getID() + s);
			mailDir.addSubFolder("attachment");
			File directory = new File("system"+ s +"users"+ s + mail.getSenderAddress() + s + "sent" + s + mail.getID() + s +  mail.getID() + ".txt");
			// Creating body file
			String mailBody = mail.getID() + "\n" + mail.getTitle() + "\n" + mail.getSenderAddress() + "\n" + mail.getSenderName() + "\n"
								+ date + "\n" + mail.getPriority().toString() + "\n";
			Iterator<Object> it = mail.getRecieverAddress().iterator();
			while (it.hasNext()) {
				mailBody += it.next().toString() + ",";
			}
			mailBody += "\n" + mail.getText() + "\n";
			FileWriter writer = new FileWriter(directory);
			directory.createNewFile();
			writer.write(mailBody);
			writer.close();
			// Upload attachments
			it = mail.getAttachments().iterator();
			while (it.hasNext()) {
				File file = (File)it.next();
				String dest = "system" + s + "users" + s + mail.getSenderAddress() + s + "sent" + s + mail.getID() + s + "attachment" + s + file.getName();
				if(!Folder.copyFiles(file, dest)) { return false;}
				//Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
			}
			
			
			// Sending Mail
			QueueLinkedList queue = new QueueLinkedList();
			it = mail.getRecieverAddress().iterator();
			while (it.hasNext()) {
				queue.enqueue(it.next());
			}
			while (!queue.isEmpty()) {
				
				String reciever = queue.dequeue().toString();
				edit = new BufferedWriter(
						new FileWriter("system" + s + "users" + s + reciever + s + "inbox" + s + "index.csv", true));
				edit.append(content);
				edit.append("\n");
				edit.flush();
				edit.close();
				
				// Creating mail folder in the reciever's inbox folder
				dir = new Folder("system" + s + "users"+ s + reciever + s + "inbox" + s);
				dir.addSubFolder(mail.getID()+"");
				mailDir = new Folder("system" + s + "users"+ s + reciever + s + "inbox" + s + mail.getID() + s);
				mailDir.addSubFolder("attachment");
				directory = new File("system"+ s +"users"+ s + reciever + s + "inbox" + s + mail.getID() + s +  mail.getID() + ".txt");
				// Creating body file
				mailBody = mail.getID() + "\n" + mail.getTitle() + "\n" + mail.getSenderAddress() + "\n" + mail.getSenderName() + "\n"
									+ date + "\n" + mail.getPriority().toString() + "\n" + reciever;
				mailBody += "\n" + mail.getText() + "\n";
				writer = new FileWriter(directory);
				directory.createNewFile();
				writer.write(mailBody);
				writer.close();
				// Upload attachments
				it = mail.getAttachments().iterator();
				while (it.hasNext()) {
					File file = (File)it.next();
					File dest = new File("system" + s + "users" + s + reciever + s + "inbox" + s + mail.getID() + s + "attachment" + s + file.getName());
					Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
				}
			}
		} catch (Exception e) {
			System.out.println("not found");
			return false;
		}

		return true;
	}
	
	
	public boolean draft(IMail email) {
		Mail  mail = (Mail)email;
    	// Data to be entered in index file
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE - MMM dd - yyyy HH:mm:ss a");
		String date = formatter.format(mail.getDate());
    	String content = mail.getID() + "," + mail.getTitle() + "," + mail.getSenderAddress() + "," + mail.getSenderName() + ","
				+ date + "," + (mail.getPriority().ordinal()+1);
    	String s = System.getProperty("file.separator");
		try {
			//appending data in csv file
			// Appending in the sender draft folder index
			BufferedWriter edit = new BufferedWriter(
					new FileWriter("system" + s + "users" + s + mail.getSenderAddress() + s + "draft" + s + "index.csv", true));
			edit.append(content);
			edit.append("\n");
			edit.flush();
			edit.close();
			// Creating mail folder in the draft folder
			Folder dir = new Folder("system" + s + "users"+ s + mail.getSenderAddress() + s + "draft" + s);
			dir.addSubFolder(mail.getID()+"");
			Folder mailDir = new Folder("system" + s + "users"+ s + mail.getSenderAddress() + s + "draft" + s + mail.getID() + s);
			mailDir.addSubFolder("attachment");
			File directory = new File("system"+ s +"users"+ s + mail.getSenderAddress() + s + "draft" + s + mail.getID() + s +  mail.getID() + ".txt");
			// Creating body file
			String mailBody = mail.getID() + "\n" + mail.getTitle() + "\n" + mail.getSenderAddress() + "\n" + mail.getSenderName() + "\n"
								+ date + "\n" + mail.getPriority().toString() + "\n";
			Iterator<Object> it = mail.getRecieverAddress().iterator();
			while (it.hasNext()) {
				mailBody += it.next().toString() + ",";
			}
			mailBody += "\n" + mail.getText() + "\n";
			FileWriter writer = new FileWriter(directory);
			directory.createNewFile();
			writer.write(mailBody);
			writer.close();
			// Upload attachments
			it = mail.getAttachments().iterator();
			while (it.hasNext()) {
				File file = (File)it.next();
				String dest = "system" + s + "users" + s + mail.getSenderAddress() + s + "draft" + s + mail.getID() + s + "attachment" + s + file.getName();
				if(!Folder.copyFiles(file, dest)) { return false;}
			}
		} catch (Exception e) {
			System.out.println("not found");
			return false;
		}
		return true;
	}
	

	public void loadMails() {
		BufferedReader reader;
		mails.clear();
		try {
			System.out.println(this.currentFolder.getIndexPath());
			reader = new BufferedReader(new FileReader(this.currentFolder.getIndexPath()));
			String row;
			row = reader.readLine();
			while ((row = reader.readLine()) != null) {
			    String[] data = row.split(",");
			    /*
			     * At data
			     * Index 0 for ID
			     * Index 1 for Title
			     * Index 2 for SenderID
			     * Index 3 for SenderName
			     * Index 4 for Date
			     * Index 5 for Priority
			     * */
			    mails.add(new Mail(Integer.parseInt(data[0]), data[1], data[2], data[3],
			    		new SimpleDateFormat("EEEE - MMM dd - yyyy HH:mm:ss a").parse(data[4]), 
			    		Priority.values()[Integer.parseInt(data[5])-1]));
			    
			}
			reader.close();
			workingList = mails.copyView();
		}
		catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException("Error loading file");
		}
	}
	
	/*
	 * Gets the folder in which the user is browsing
	 * */
	public Folder getCurrentFolder() {
		return this.currentFolder;
	}
	
	/*
	 * Get the number of thwe available mails in the specified filter and folder
	 * */
	public int availableMailsCount() {
		return workingList.size();
	}
	
	public void SortByPriority() {
		PriorityQueue PQueue = new PriorityQueue();
		Iterator<Object> it = workingList.iterator(true);
		while (it.hasNext()) {
			Mail tmpMail = (Mail)it.next();
			PQueue.insert(tmpMail, tmpMail.getPriority().ordinal()+1);
		}
		workingList.clear();
		while (!PQueue.isEmpty()) {
			workingList.add(PQueue.removeMin());
		}
	}
	
	public void reverseSort(boolean flag) {
		this.reverseSorting = flag;
	}
	
}
