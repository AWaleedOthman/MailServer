package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.DataStructures.SinglyLinkedList;
import Classes.Misc.AES;
import Classes.Misc.Birthday;
import Classes.Misc.Utils;
import Interfaces.MailServer.IUser;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class User implements IUser {

    private final static String path = System.getProperty("user.dir") + "\\system\\users";
    private final static File fList = new File(path + "\\list.txt");
    private static DoublyLinkedList list = new DoublyLinkedList();

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

    /**
     * it is advised to call validSignup before this to know the problem if there is one.
     *
     * @param address  email address
     * @param password password
     * @return user created or null if invalid signup
     * @throws IOException file not found
     */
    public static User signup(String address, String password, String name, String gender, Birthday bd, boolean validated) throws IOException {
        if (!validated && (validateSignup(address, password) != null)) return null;
        User user;
        try {
        user = new User(address, AES.encrypt(password, password));} catch (Exception e) {
            return null;
        }
        Utils.addToSorted(user, list);
        exportList();
        //create new folder with info
        Folder usersFolder = new Folder(path);
        usersFolder.createUserFolder(address, name, gender, bd);
        user.setName(name);
        user.setGender(gender);
        user.setBirthday(bd);
        user.setFilePath(path + "\\" + address);
        return user;
    }

    /**
     * it validates the email address and the password.
     * birthday must be validated before hand using Birthday.valid() in case it is **typed** by user
     *
     * @param address  email address
     * @param password password
     * @return 0: successfully signed-up
     * -1: already signed-up
     * -2: invalid email address (a valid address contains letters, numbers or non-consecutive periods then "@thetrio.com")
     * -3: invalid password (a valid password must be between 6 and 30 characters inclusive)
     * -4: both -2 and -3
     */
    public static String validateSignup(String address, String password) {
        String s = null;
        if (Utils.binarySearch(address, list) != null) return "alreadyExists"; //already signed up
        if (!Utils.validAddress(address)) s = "invalidEmailAddress"; //invalid address

        if (password.length() < 6 || password.length() > 30 || password.indexOf(' ') != -1) {
            if (s != null)
                s = "invalidEmailAddressANDPassword";
            else s = "invalidPassword";
        }
        /*uncomment the next line if the birthday fields are **typed** by the user*/
        //if (!Birthday.valid(day, month, year)) return -4; //invalid birthday
        return s;
    }

    /**
     * call loadUsers before calling this
     *
     * @param address  to search for
     * @param password to match
     * @return user if valid sign-in, null otherwise
     */
    public static User signin(String address, String password) throws IOException {
        User user = Utils.binarySearch(address, list);
        if (user == null) return null; //invalid sign-in
        try {
            if (!Objects.equals(AES.decrypt(user.encryptedPassword, password), password)) return null;
        } catch (Exception e) {
            return null;
        }
        /*load user info*/
        Scanner sc = new Scanner(new File(path + "\\" + address + "\\info.txt"));
        user.setFilePath(path + "\\" + address);
        user.setName(sc.nextLine());
        user.setGender(sc.nextLine());
        /*nextLine instead of nextInt to avoid problem of newLine being taken in next nextLine call*/
        user.setBirthday(new Birthday(Integer.parseInt(sc.nextLine()), Integer.parseInt(sc.nextLine()), Integer.parseInt(sc.nextLine())));
        while (sc.hasNext()) {
            user.addContact(sc.nextLine(), sc.nextLine());
        }
        sc.close();
        return user;
    }


    private String address, encryptedPassword;
    private String name, filePath;
    private String gender;
    private Birthday birthday;

    private SinglyLinkedList contacts = new SinglyLinkedList();
    //TODO methods for searching contacts linearly

    /**
     * adds a contact to the user's list of contacts
     *
     * @param name    contact's name
     * @param address contact's address
     * @throws IOException file not found
     */
    public void addContact(String name, String address) throws IOException {
        Contact c = new Contact(name, address);
        contacts.add(c);
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\info.txt", true));
        writer.write(name);
        writer.newLine();
        writer.write(address);
        writer.newLine();
        writer.close();
    }

    public String getFilePath() {
        return filePath;
    }

    public User(String address, String encryptedpassword) {
        this.address = address;
        this.encryptedPassword = encryptedpassword;
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
}
