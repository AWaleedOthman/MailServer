package Classes.Misc;

import Classes.DataStructures.DoublyLinkedList;
import Classes.DataStructures.SinglyLinkedList;
import Classes.MailServer.User;
import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * a valid address contains letters, numbers or non-consecutive periods then "@thetrio.com"
     *
     * @param address: email address to be validated
     * @return true if valid, false otherwise
     */
    public static boolean validAddress(String address) {
        int index = address.indexOf('@');
        if (index == -1 || index == 0) return false;
        if (!address.substring(index).equals("@thetrio.com")) return false;
        Pattern p = Pattern.compile("[^\\w|.]");
        Matcher m = p.matcher(address.substring(0, index));
        if (m.find()) return false;
        return !address.contains("..");
    }

    /**
     * password may contain anything but whitespace.
     * it also has to be between 6 and 30 chars inclusive
     * @param password to be validated
     * @return whether the password is valid
     */
    public static boolean validPassword(String password) {
        return password.length() >= 6 && password.length() <= 30 && password.indexOf(' ') == -1;
    }

    /**
     * searches for address in a doubly linked list of Users
     *
     * @param address: search key
     * @return the user if found, null otherwise
     */
    public static User binarySearch(String address, DoublyLinkedList dll) {

        int first = 0, last = dll.size() - 1;
        while (first <= last) {
            int m = (first + last) / 2;
            User temp = (User) dll.get(m);
            int r = address.compareTo(temp.getAddress());
            if (r == 0) return temp;
            else if (r > 0) /*s bigger*/ first = m + 1;
            else last = m - 1;
        }
        return null;
    }

    /**
     * adds the User to the sorted list according to the email address
     *
     * @param u    User to be added
     * @param list list of Users
     */
    public static void addToSorted(User u, DoublyLinkedList list) {
        if (list.isEmpty()) {
            list.add(u);
            return;
        }
        String addressMain = u.getAddress();
        String address = ((User) list.get(0)).getAddress();
        int index = 0;
        if (addressMain.compareTo(address) <= 0) {
            list.add(0, u);
        } else {
            boolean added = false;
            while (list.hasNext()) {
                address = ((User) list.getNext()).getAddress();
                ++index;
                if (addressMain.compareTo(address) <= 0) {
                    list.add(index, u);
                    added = true;
                    break;
                }
            }
            if (!added) {
                list.add(index + 1, u);
            }
        }
    }

    public static SinglyLinkedList getValues(String csLine) {
        SinglyLinkedList list = new SinglyLinkedList();
        int length = csLine.length(), l, r;
        Pattern p = Pattern.compile("\"[^\"]*\"");
        Matcher m = p.matcher(csLine);
        while (m.find()) {
            l = m.start();
            r = m.end();
            while (r < length && csLine.charAt(r) == '"') {
                if (!m.find()) return null;
                r = m.end();
            }
            list.add(csLine.substring(l + 1, r - 1));
        }
        return list;
    }

    public static boolean validString(String s) {
        return s.indexOf(',') == -1 && s.indexOf('"') == -1;
    }

    public static void fileNotFound() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("File not found!");
        alert.setContentText("Oops! It looks like a file has been deleted.");
        alert.showAndWait();
        System.exit(0);
    }
}
