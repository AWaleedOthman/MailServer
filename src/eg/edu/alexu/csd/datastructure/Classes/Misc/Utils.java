package eg.edu.alexu.csd.datastructure.Classes.Misc;

import eg.edu.alexu.csd.datastructure.Classes.DataStructures.DoublyLinkedList;
import eg.edu.alexu.csd.datastructure.Classes.MailServer.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * a valid address contains letters, numbers or non-consecutive periods then "@thetrio.com"
     * @param address: email address to be validated
     * @return true if valid, false otherwise
     */
    public static boolean validAddress(String address) {
        Pattern p = Pattern.compile("@");
        Matcher m = p.matcher(address);
        if (!m.find()) return false;
        int index = address.indexOf('@');
        if (index == 0) return false;
        if (!address.substring(index).equals("@thetrio.com")) return false;
        p = Pattern.compile("[^\\w|.]");
        m = p.matcher(address.substring(0, index - 1));
        if (m.find()) return false;
        p = Pattern.compile("[.][.]");
        m = p.matcher(address.substring(0, index - 1));
        return !m.find();
    }

    /**
     * searches for s in a doubly linked list of Users
     * @param s: search key
     * @return the index of the element if s was found, -1 otherwise
     */
    public static User binarySearch(String s, DoublyLinkedList dll) {

        int first = 0, last = dll.size() - 1;
        while (first <= last) {
            int m = (first + last) / 2;
            User temp = (User)dll.get(m);
            int r = s.compareTo(temp.getAddress());
            if (r == 0) return temp;
            else if (r > 0) /*s bigger*/ first = m + 1;
            else last = m - 1;
        }
        return null;
    }

    /**
     * adds the User to the sorted list according to the email address
     * @param u User to be added
     * @param list list of Users
     */
    public static void addToSorted(User u, DoublyLinkedList list) {
        if (list.isEmpty()){
            list.add(u);
            return;
        }
        String addressMain = u.getAddress();
        String address = ((User)list.get(0)).getAddress();
        int index = 0;
        if (addressMain.compareTo(address) <= 0) {
            list.add(0, u);
        } else {
            boolean added = false;
            while (list.hasNext()) {
                address = ((User)list.getNext()).getAddress();
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

}
