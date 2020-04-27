package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.DataStructures.PQueue;
import Classes.Misc.AES;
import Classes.Misc.Birthday;
import Classes.Misc.Utils;
import Classes.Priority;
import Interfaces.DataStructures.ILinkedList;
import Interfaces.MailServer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class App implements IApp {

	private final String path = System.getProperty("user.dir") + "\\system\\users";
	private final File fList = new File(path + "\\list.txt");
	private final DoublyLinkedList list = new DoublyLinkedList();
	private User loggedinUser;

	private final DoublyLinkedList mails = null;
	private Folder currentFolder = null;
	private Filter currentFilter = null;
	private boolean reverseSorting;


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
			Sort sortClass = (Sort) sort;
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
		String content = " ";

		// data i want to enter in csv file
		content = ((Mail) email).getID() + "," + ((Mail) email).getTitle() + "," + ((Mail) email).getSenderAddress() + "," + ((Mail) email).getSenderName() + ","
				+ ((Mail) email).getRecieverAddress() + "," + ((Mail) email).getFilter() + "," + ((Mail) email).getPriority() + "," + ((Mail) email).getDate();

		try {
			//appending data in csv file
			BufferedWriter edit = new BufferedWriter(
					new FileWriter("system\\users\\" + ((Mail) email).getSenderAddress() + "\\inbox\\" + "index.csv", true));
			edit.append(content);
			edit.append("\n");
			edit.flush();
			edit.close();

			BufferedWriter infoEdit = new BufferedWriter(
					new FileWriter("system\\users\\" + ((Mail) email).getRecieverAddress() + "\\inbox\\" + "index.csv", true));
			infoEdit.append(content);
			infoEdit.append("\n");
			infoEdit.flush();
			infoEdit.close();

			// writing in txt file
			Folder newMessage = new Folder("system\\users\\" + ((Mail) email).getRecieverAddress() + "\\inbox\\");
			newMessage.addSubFolder(Integer.toString(((Mail) email).getID()));
			File newMail = new File("system\\users\\" + ((Mail) email).getRecieverAddress() + "\\inbox\\" + ((Mail) email).getID() + ".txt");
			newMail.createNewFile();

			String newMailContent = ((Mail) email).getID() + "\n" + ((Mail) email).getTitle() + "\n" + ((Mail) email).getSenderAddress() + "\n"
					+ ((Mail) email).getRecieverAddress() + "\n";
			FileWriter fw = new FileWriter(newMail);
			fw.write(newMailContent);

			System.out.println("type here \n");
			Scanner sc = new Scanner(System.in);
			String text = sc.nextLine();
			fw.write(text);

			Folder dir = new Folder("system\\users\\" + ((Mail) email).getSenderAddress() + "\\sent\\");
			dir.addSubFolder(Integer.toString(((Mail) email).getID()));
			File directory = new File("system\\users\\" + ((Mail) email).getSenderAddress() + "\\sent\\" + ((Mail) email).getID() + ".txt");

			if (!directory.exists()) {
				FileWriter fw2 = new FileWriter(directory);
				directory.createNewFile();
				fw2.write(newMailContent);
				fw2.write(text);
				fw2.close();
			}

			fw.close();
			sc.close();
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
			String[] arr = s.split(",", 3);
			Contact c = new Contact(this, arr[0], user, Integer.parseInt(arr[1]));
			if (!arr[2].isEmpty())
				c.addAddresses(arr[2].split(","));
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
		PQueue pQueue = new PQueue();
		Iterator it = mails.iterator(true);
		while (it.hasNext()) {
			Mail tmpMail = (Mail) it.next();
			//pQueue.insert(tmpMail, tmpMail.getPriority());
		}
		mails.clear();
		while (!pQueue.isEmpty()) {
			mails.add(pQueue.removeMin());
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
						Priority.values()[Integer.parseInt(data[5]) - 1]));

			}
			reader.close();
			Object workingList = mails.copyView();
		}
		catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException("Error loading file");
		}
    }
    public void moveEmails(DoublyLinkedList mails, Folder des) {
		Iterator it = mails.iterator(true);
		//String indexFile = "system\\users\\" + currentMail.getSenderAddress() + "\\inbox\\" + "index.csv";

		//deleteIndex(mails, indexFile);
		// looping through mails
		while (it.hasNext()) {
			Mail currentMail = (Mail) it.next();
			
			String content = currentMail.getID() + "," + currentMail.getTitle() + "," + currentMail.getSenderAddress()
					+ "," + currentMail.getSenderName() + "," + currentMail.getRecieverAddress() 
					+ "," + currentMail.getPriority() + "," + currentMail.getDate();
			BufferedWriter edit; // editing index file of the des folder
			try {
				edit = new BufferedWriter(new FileWriter(des.getPath() + "\\index.csv", true));
				edit.append(content);
				edit.append("\n");
				edit.flush();
				edit.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// moving folders by creating a copy
			File originalFile = new File("system\\users\\" + currentMail.getSenderAddress() + "\\inbox\\"
					+ currentMail.getID() + "\\" + currentMail.getID() + ".txt");

			des.addSubFolder(String.valueOf(currentMail.getID()));
			File destination = new File(
					des.getPath() + "\\" + currentMail.getID() + "\\" + currentMail.getID() + ".txt");

			try {
				Files.copy(originalFile.toPath(), destination.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// deleting original files
			originalFile.delete();
			File originalFolder = new File(
					"system\\users\\" + currentMail.getSenderAddress() + "\\inbox\\" + currentMail.getID());
			originalFolder.delete();

		}
	}
	public void deleteEmails(DoublyLinkedList mails) {
		// 4il mo2 di w 7ot mkanha l 7aga l bt4awr 3l currentuser
		Folder des = new Folder("system\\users\\mo2@thetrio.com\\trash");
		moveEmails(mails, des);
	}
	public void updateTrash() {
		File trashIndex = new File("system\\users\\mo2@thetrio.com\\trash\\index.csv");
		DoublyLinkedList toBeDeletedList = new DoublyLinkedList();

		try (BufferedReader br = new BufferedReader(new FileReader(trashIndex))) {
			String line = "";
			String splitBy = ",";
			while ((line = br.readLine()) != null) {
				String[] data = line.split(splitBy);
				Date d1 = new Date();
				Date d2 = new SimpleDateFormat("EEEE - MMM dd - yyyy HH:mm:ss a").parse(data[4]);
				int diff = getDaysDiff(d2, d1);
				if (diff > 30) {
					toBeDeletedList.add(new Mail(Integer.parseInt(data[0]), data[1], data[2], data[3],
							new SimpleDateFormat("EEEE - MMM dd - yyyy HH:mm:ss a").parse(data[4]),
							Priority.values()[Integer.parseInt(data[5]) - 1]));
				}
			}
			br.close();
			deleteIndex(toBeDeletedList, trashIndex.toString());
			Iterator it = toBeDeletedList.iterator(true);
			while (it.hasNext()) {
				Mail current = (Mail) it.next();
				File dFolder = new File(
						"system\\users\\mo2@thetrio.com\\trash\\" + current.getID() + "\\" + current.getID() + ".txt");
				dFolder.delete();
				File dFolder2 = new File("system\\users\\mo2@thetrio.com\\trash\\" + current.getID());
				dFolder2.delete();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// sub helping functions functions 
	//calculate days between 2 dates
	public int getDaysDiff(Date date1, Date date2) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Instant instant = date1.toInstant();
		LocalDate localDate1 = instant.atZone(defaultZoneId).toLocalDate();
		instant = date2.toInstant();
		LocalDate localDate2 = instant.atZone(defaultZoneId).toLocalDate();
		int diff = (int) ChronoUnit.DAYS.between(localDate1, localDate2);
		return diff;
	}
	// delete record in index file
	public void deleteIndex(DoublyLinkedList list, String path) {
		File index = new File(path);
		String line = "";
		String splitBy = ",";
		File tmp = new File(path + "copy.csv");
		try (BufferedReader br = new BufferedReader(new FileReader(index))) {
			tmp.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(tmp, true));
			while ((line = br.readLine()) != null) {
				String[] data = line.split(splitBy);
				if (findID(list,Integer.parseInt (data[0]))) {
					continue;
				} else {
					bw.append(line);
					bw.append("\n");
				}
			}
			bw.close();
			br.close();
			index.delete();
			tmp.renameTo(index);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	public boolean findID(DoublyLinkedList list, int ID) {
		Iterator it = list.iterator(true);
		while (it.hasNext()) {
			Mail currentNode = (Mail) it.next();
			if (currentNode.getID() == ID) {
				return true;
			}
		}
		return false;
	}
    
}
