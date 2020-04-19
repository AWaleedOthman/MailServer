package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.Misc.AES;
import Classes.Misc.Birthday;
import Classes.Misc.Utils;
import Interfaces.MailServer.IContact;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class User implements IContact {

    private final static String path = System.getProperty("user.dir") + "\\system\\users";
    private final static File fList = new File(path + "\\list.txt");
    private static final DoublyLinkedList list = new DoublyLinkedList();

//    /**
//     * it validates the email address and the password.
//     * birthday must be validated before hand using Birthday.valid() in case it is **typed** by user
//     *
//     * @param address  email address
//     * @param password password
//     * @return 0: successfully signed-up
//     * -1: already signed-up
//     * -2: invalid email address (a valid address contains letters, numbers or non-consecutive periods then "@thetrio.com")
//     * -3: invalid password (a valid password must be between 6 and 30 characters inclusive)
//     * -4: both -2 and -3
//     */
//    public static String validateSignup(String address, String password) {
//        String s = null;
//        if (Utils.binarySearch(address, list) != null) return "alreadyExists"; //already signed up
//        if (!Utils.validAddress(address)) s = "invalidEmailAddress"; //invalid address
//
//        if (!Utils.validPassword(password)) {
//            if (s != null)
//                s = "invalidEmailAddressANDPassword";
//            else s = "invalidPassword";
//        }
//        /*uncomment the next line if the birthday fields are **typed** by the user*/
//        //if (!Birthday.valid(day, month, year)) return -4; //invalid birthday
//        return s;
//    }

    /**
     * MUST be validated
     *
     * @param contact validated user to be added
     * @return user added
     */
    public static boolean signup(IContact contact) {
        User user;
        user = (User) contact;
        Utils.addToSorted(user, list);
        try {
            exportList();
            //create new folder with info
            Folder usersFolder = new Folder(path);
            usersFolder.createUserFolder(user.address, user.name, user.gender, user.birthday);
            user.setFilePath(path + "\\" + user.address);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * loads the list of users from the file to a static doubly linked list
     *
     * @throws FileNotFoundException file not found
     */
    public static void loadUsers() throws FileNotFoundException {
        Scanner sc = new Scanner(fList);
        while (sc.hasNext()) {
            list.add(new User(sc.nextLine(), sc.nextLine()));
        }
        sc.close();
    }

    /**
     * call loadUsers before calling this
     *
     * @param address  to search for
     * @param password to match
     * @return user if valid sign-in, null otherwise
     */
    public static boolean signin(String address, String password) {
        User user = Utils.binarySearch(address, list);
        if (user == null) return false; //invalid sign-in
        try {
            if (!Objects.equals(AES.decrypt(user.encryptedPassword, password), password)) return false;
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    private final String address;

    public static boolean addressExists(String address) {
        User user = Utils.binarySearch(address, list);
        return user != null;
    }

    /**
     * overwrites the file "list"
     *
     * @throws IOException file not found
     */
    private static void exportList() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fList, false));
        if (list.isEmpty()) return;
        User user = (User) list.get(0);
        writer.write(user.getAddress());
        writer.newLine();
        writer.write(user.getEncryptedPassword());
        writer.newLine();
        while (list.hasNext()) {
            user = (User) list.getNext();
            writer.write(user.getAddress());
            writer.newLine();
            writer.write(user.getEncryptedPassword());
            writer.newLine();
        }
        writer.close();
    }

    private final DoublyLinkedList contacts = new DoublyLinkedList();
    private String encryptedPassword;
    private String name, filePath;
    private String gender;
    private Birthday birthday;

    public static User loadInfo(String address) {
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
                Contact c = new Contact(arr[0], user);
                c.addAddresses(arr[1]);
                user.addContact(c);

            } catch (IOException e) {
                Utils.fileNotFound();
            }
        }
        sc.close();
        return user;
    }
    //TODO methods for searching and sorting contacts

    /**
     * adds to list and to csv file
     * @param contact to be added
     * @throws IOException file not found
     */
    public void addContact(Contact contact) throws IOException {
        contacts.add(contact);
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", true));
        writer.write(contact.getName() + "," + contact.getAddressesString());
        writer.newLine();
        writer.close();
    }

    protected boolean delContact(Contact contact) {
        if (contact.equals(contacts.get(0))) {
            contacts.remove(0);
            try {
                exportContacts();
            } catch (IOException e) {
                Utils.fileNotFound();
            }
            return true;
        }
        for (int i = 1; contacts.hasNext(); i++) {
            if (contact.equals(contacts.getNext())) {
                contacts.remove(i);
                try {
                    exportContacts();
                } catch (IOException e) {
                    Utils.fileNotFound();
                }
                return true;
            }
        }
        return false;
    }

    protected void exportContacts() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", false));
        if (contacts.isEmpty()) return;
        Contact contact = (Contact) contacts.get(0);
        writer.write(contact.getName() + "," + contact.getAddressesString());
        writer.newLine();
        while (contacts.hasNext()) {
            contact = (Contact) contacts.getNext();
            writer.write(contact.getName() + "," + contact.getAddressesString());
            writer.newLine();
        }
        writer.close();
    }

    public String getFilePath() {
        return filePath;
    }

    public User(String address, String encryptedpassword) {
        this.address = address;
        this.encryptedPassword = encryptedpassword;
    }
    public User(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setBirthday(Birthday bd) {
        this.birthday = bd;
    }

    public String getBirthday() {
        return birthday.getBirthday();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DoublyLinkedList getContacts() {
        return contacts;
    }
}
