package eg.edu.alexu.csd.datastructure.Classes.MailServer;

import eg.edu.alexu.csd.datastructure.Classes.DataStructures.DoublyLinkedList;
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
    public static User signup(String address, String password, String name, String gender, Birthday bd) throws IOException {
        if (validSignup(address, password) != 0) return null;
        User user = new User(address, AES.encrypt(password, password));
        Utils.addToSorted(user, list);
        exportList();
        //TODO
        //create new folder and set user file path
        user.setName(name);
        user.setGender(gender);
        user.setBirthday(bd);
        user.setFilePath("");
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
     */
    public static int validSignup(String address, String password) {
        if (Utils.binarySearch(address, list) != null) return -1; //already signed up
        if (!Utils.validAddress(address)) return -2; //invalid address
        if (password.length() < 6 || password.length() > 30) return -3; //invalid password
        return 0;
    }

    public static User signin(String address, String password) {


        //TODO
        //load User info
        return null;
    }


    private String address, encryptedPassword;
    private String name;
    private Gender gender;
    private Birthday birthday;
    private enum Gender {Male, Female, Other}
    private String filePath;
    //TODO add contacts

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
