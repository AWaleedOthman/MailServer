package eg.edu.alexu.csd.datastructure.Classes.MailServer;

import eg.edu.alexu.csd.datastructure.Classes.DataStructures.DoublyLinkedList;
import eg.edu.alexu.csd.datastructure.Classes.DataStructures.SinglyLinkedList;
import eg.edu.alexu.csd.datastructure.Classes.Misc.AES;
import eg.edu.alexu.csd.datastructure.Classes.Misc.Birthday;
import eg.edu.alexu.csd.datastructure.Classes.Misc.Utils;
import eg.edu.alexu.csd.datastructure.Interfaces.MailServer.IUser;

import java.io.*;
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
     * @throws IOException file not found
     */
    public static void exportList() throws IOException {
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
        if (!validated && (validSignup(address, password, bd.getDay(), bd.getMonth(), bd.getYear()) != 0)) return null;
        User user = new User(address, AES.encrypt(password, password));
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
     *
     * @param address email address
     * @param password password
     * @return   0: successfully signed-up
     *          -1: already signed-up
     *          -2: invalid email address (a valid address contains letters, numbers or non-consecutive periods then "@thetrio.com")
     *          -3: invalid password (a valid password must be between 6 and 30 characters inclusive)
     *          -4: invalid birthday
     */
    public static int validSignup(String address, String password, int day, int month, int year) {
        if (Utils.binarySearch(address, list) != null) return -1; //already signed up
        if (!Utils.validAddress(address)) return -2; //invalid address
        if (password.length() < 6 || password.length() > 30) return -3; //invalid password
        if (!Birthday.valid(day, month, year)) return -4; //invalid birthday
        return 0;
    }

    /**
     *
     * @param address to search for
     * @param password to match
     * @return user if valid sign-in, null otherwise
     */
    public static User signin(String address, String password) throws IOException {
        User user = Utils.binarySearch(address, list);
        if (user == null) return null; //invalid sign-in
        if (!AES.decrypt(user.encryptedPassword, password).equals(password)) return null;
        /*load user info*/
        Scanner sc = new Scanner(path + "\\" + address + "\\info.txt");
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
    private Gender gender;
    private Birthday birthday;
    private enum Gender {Male, Female, Other}
    private SinglyLinkedList contacts = new SinglyLinkedList();

    /**
     * adds a contact to the user's list of contacts
     * @param name contact's name
     * @param address contact's address
     * @throws IOException file not found
     */
    public void addContact(String name, String address) throws IOException {
        Contact c = new Contact(name, address);
        contacts.add(c);
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "\\" + address + "\\info.txt", true));
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
        this.gender = Gender.valueOf(gender);
    }

    public String getGender() {
        return gender.toString();
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
