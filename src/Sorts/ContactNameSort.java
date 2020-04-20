package Sorts;

import Classes.MailServer.Contact;
import Interfaces.MailServer.ISort;

import java.util.Comparator;

public class ContactNameSort implements ISort {
    @Override
    public Comparator sortAttribute() {
        return new NameOrder();
    }

    private static class NameOrder implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Contact c1 = (Contact) o1;
            Contact c2 = (Contact) o1;
            return c1.getName().compareTo(c2.getName());

        }
    }
}
