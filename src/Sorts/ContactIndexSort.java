package Sorts;

import Classes.MailServer.Contact;
import Interfaces.MailServer.ISort;

import java.util.Comparator;

public class ContactIndexSort implements ISort {

    @Override
    public Comparator sortAttribute() {
        return new IndexOrder();
    }

    private static class IndexOrder implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Contact c1 = (Contact) o1;
            Contact c2 = (Contact) o1;
            return Integer.compare(c1.getIndex(), c2.getIndex());

        }
    }
}
