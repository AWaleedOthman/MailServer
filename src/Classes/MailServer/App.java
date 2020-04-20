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

    }

    @Override
    public IMail[] listEmails(int page) {
        return new IMail[0];
    }

    @Override
    public void deleteEmails(ILinkedList mails) {

    }

    @Override
    public void moveEmails(ILinkedList mails, IFolder des) {

    }

    @Override
    public boolean compose(IMail email) {
        return false;
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
            try {
                String s = sc.nextLine();
                String[] arr = s.split(",", 2);
                Contact c = new Contact(this, arr[0], user, user.getContacts().size());
                c.addAddresses(arr[1].split(","));
                user.addContact(c);

            } catch (IOException e) {
                Utils.fileNotFound();
            }
        }
        sc.close();
        return user;
    }

}
