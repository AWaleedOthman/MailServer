package Classes;

import Interfaces.IContact;
import Misc.Utils;

import java.io.IOException;
import java.util.Iterator;

public class Contact implements IContact {

    private String name;
    private final DoublyLinkedList addresses = new DoublyLinkedList();
    private User owner;
    private int index;
    private String mainAddress;


    public Contact(String name, User owner, int index) {
        this.name = name;
        this.owner = owner;
        this.index = index;
    }

    public Contact(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    protected void setOwner(User owner) {
        this.owner = owner;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    public boolean delete() {
        return owner.delContact(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        try {
            owner.exportContacts();
        } catch (IOException e) {
            Utils.fileNotFound();
        }
    }

    public DoublyLinkedList getAddresses() {
        return addresses;
    }

    public void addAddresses(String[] arr) {
        if (arr == null) return;
        for (String s : arr) {
            this.addresses.add(s);
        }
        mainAddress = (String) addresses.get(0);
    }
//
//    public boolean addAddress(String address) {
//        if (!app.addressExists(address)) return false;
//        addresses.add(address);
//        try {
//            owner.exportContacts();
//        } catch (IOException e) {
//            Utils.fileNotFound();
//        }
//        return true;
//    }
//
//    public boolean changeAddress(String old, String address) {
//        Iterator<String> iter = addresses.iterator(true);
//
//        for (int i = 0; iter.hasNext(); i++) {
//            if (old.equals(iter.next())) {
//                addresses.set(i, address);
//                try {
//                    owner.exportContacts();
//                } catch (IOException e) {
//                    Utils.fileNotFound();
//                }
//                mainAddress = (String) addresses.get(0);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void deleteAddress(@NotNull String old) {
//        Iterator<String> iter = addresses.iterator(true);
//
//        for (int i = 0; iter.hasNext(); i++) {
//            if (old.equals(iter.next())) {
//                addresses.remove(i);
//                try {
//                    owner.exportContacts();
//                } catch (IOException e) {
//                    Utils.fileNotFound();
//                }
//                return;
//            }
//        }
//    }

    public String getAddressesString() {
        StringBuilder sb = new StringBuilder();
        if (addresses.isEmpty()) return "";
        Iterator<Object> iter = addresses.iterator(true);
        sb.append(iter.next());
        while (iter.hasNext()) {
            sb.append(",").append(iter.next());
        }
        return sb.toString();
    }

    public String getMainAddress() {
        return mainAddress;
    }
}
