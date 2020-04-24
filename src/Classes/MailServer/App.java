package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.Misc.AES;
import Classes.Misc.Birthday;
import Classes.Misc.Utils;
import Interfaces.DataStructures.ILinkedList;
import Interfaces.MailServer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class App implements IApp {

    private final String path = System.getProperty("user.dir") + "\\system\\users";
    private final File fList = new File(path + "\\list.txt");
    private final DoublyLinkedList list = new DoublyLinkedList();
    private User loggedinUser;

    public User getLoggedinUser() {
        return loggedinUser;
    }

    @Override
    public boolean signin(String email, String password) {
        User user = Utils.binarySearch(email, list);
        if (user == null) return false; //invalid sign-in
        try {
            if (!Objects.equals(AES.decrypt(user.getEncryptedPassword(), password), password)) return false;
        } catch (Exception e) {
            return false;
        }
        loggedinUser = loadInfo(user.getAddress());
        return true;
    }

    @Override
    public boolean signup(IContact contact) {
        User user;
        user = (User) contact;
        Utils.addToSorted(user, list);
        try {
            exportList();
            //create new folder with info
            Folder usersFolder = new Folder(path);
            usersFolder.createUserFolder(user.getAddress(), user.getName(), user.getGender(), user.getBirthday());
            user.setFilePath(path + "\\" + user.getAddress());
        } catch (IOException e) {
            return false;
        }
        loggedinUser = loadInfo(user.getAddress());
        return true;
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
		// Setting the specified filter
		if (filter != null) {
			this.currentFilter = (Filter) filter;
		}
		// Setting the specified sort
		if (sort != null) {
			Sort sortClass = (Sort)sort; 
			mails.Qsort(sortClass.sortAttribute());
		}
    }

    @Override
    public IMail[] listEmails(int page) {
        // If no loaded mails then Set viewing options has not been called yet
		if (this.mails == null) { throw new IllegalStateException();}
		
		Mail[] returnedMails = new Mail[10];
		int counter = 0;
		
		Iterator it = mails.iterator(!reverseSorting);
		// Skip the unwanted pages
		while (it.hasNext() && counter < (page-1) * 10) {
			Mail tmpMail = (Mail) it.next();
			if (this.currentFilter.filter(tmpMail)) {
				counter ++;
			}
		}
		// Load the wanted page
		while (it.hasNext() && counter < (page) * 10) {
			Mail tmpMail = (Mail) it.next();
			if (this.currentFilter.filter(tmpMail)) {
				returnedMails[counter%10] = tmpMail;
				counter ++;
			}
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
    	String content = mail.getID() + "," + mail.getTitle() + "," + mail.getSenderAddress() + "," + mail.getSenderName() + ","
				+ "," + (mail.getPriority().ordinal()+1) + "," + mail.getDate();
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
								+ mail.getDate().toString() + "\n" + mail.getPriority().toString() + "\n";
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
									+ mail.getDate().toString() + "\n" + mail.getPriority().toString() + "\n" + reciever;
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
    	String content = mail.getID() + "," + mail.getTitle() + "," + mail.getSenderAddress() + "," + mail.getSenderName() + ","
				+ "," + (mail.getPriority().ordinal()+1) + "," + mail.getDate();
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
							+ mail.getDate().toString() + "\n" + mail.getPriority().toString() + "\n";
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
    /**
     * loads the list of users from the file to a static doubly linked list
     *
     * @throws FileNotFoundException file not found
     */
    public void loadUsers() throws FileNotFoundException {
        Scanner sc = new Scanner(fList);
        while (sc.hasNext()) {
            list.add(new User(sc.nextLine(), sc.nextLine()));
        }
        sc.close();
    }

    public boolean addressExists(String address) {
        User user = Utils.binarySearch(address, list);
        return user != null;
    }

    public int addressesExist(String @NotNull [] arr) {
        int i = 0;
        if (arr.length == 0) return i;
        for (String address : arr) {
            if (Utils.binarySearch(address, list) == null) return i;
            else ++i;
        }
        return i;
    }

    /**
     * overwrites the file "list"
     *
     * @throws IOException file not found
     */
    private void exportList() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fList, false));
        if (list.isEmpty()) return;
        Iterator<User> i = list.iterator(true);
        while (i.hasNext()) {
            User user = i.next();
            writer.write(user.getAddress());
            writer.newLine();
            writer.write(user.getEncryptedPassword());
            writer.newLine();
        }
        writer.close();
    }

    private @Nullable User loadInfo(String address) {
        /*load user info*/
        User user = new User(address);
        Scanner sc;
        try {
            sc = new Scanner(new File(path + "\\" + address + "\\info.txt"));
        } catch (FileNotFoundException e) {
            Utils.fileNotFound();
            return null;
        }
        user.setFilePath(path + "\\" + address);
        user.setName(sc.nextLine());
        user.setGender(sc.nextLine());
        /*nextLine instead of nextInt to avoid problem of newLine being taken in next nextLine call*/
        user.setBirthday(new Birthday(Integer.parseInt(sc.nextLine()), Integer.parseInt(sc.nextLine()), Integer.parseInt(sc.nextLine())));
        sc.close();
        try {
            sc = new Scanner(new File(path + "\\" + address + "\\contacts.csv"));
        } catch (FileNotFoundException e) {
            Utils.fileNotFound();
        }
        while (sc.hasNext()) {
            String s = sc.nextLine();
            String[] arr = s.split(",", 2);
            Contact c = new Contact(this, arr[0], user, user.getContacts().size());
            if (!arr[1].isEmpty())
                c.addAddresses(arr[1].split(","));
            user.getContacts().add(c);

        }
        sc.close();
        return user;
    }


    /*
	 * Get the number of thwe available mails in the specified filter and folder
	 * */
	public int availableMailsCount() {
		if (this.currentFilter == null) { return mails.size();}
		int counter = 0;
		Iterator it = mails.iterator(true);
		while (it.hasNext()) {
			if (this.currentFilter.filter((Mail)it.next())) {
				counter++;
			}
		}
		return counter;
	}
	
	public void SortByPriority() {
		PriorityQueue PQueue = new PriorityQueue();
		Iterator it = mails.iterator(true);
		while (it.hasNext()) {
			Mail tmpMail = (Mail)it.next();
			PQueue.insert(tmpMail, tmpMail.getPriority());
		}
		mails.clear();
		while (!PQueue.isEmpty()) {
			mails.add(PQueue.removeMin());
		}
	}
	
    /*
    * Reverse list sorting 
    * */
	public void reverseSort(boolean flag) {
		this.reverseSorting = flag;
	}
    
    public void loadMails() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(this.currentFolder.getIndexPath()));
			String row;
			while ((row = reader.readLine()) != null) {
			    String[] data = row.split(",");
			    /*
			     * At data
			     * Index 0 for ID
			     * Index 1 for Title
			     * Index 2 for SenderAddress
			     * Index 3 for SenderName
			     * Index 4 for Date
			     * Index 5 for Priority
			     * */
			    
			    mails.add(new Mail(Integer.parseInt(data[0]), data[1], data[2], data[3],
			    		new SimpleDateFormat("dd/MM/yyyy").parse(data[4]), Integer.parseInt(data[5])));
			    
			}
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading file");
		}
	}
    
    
}
