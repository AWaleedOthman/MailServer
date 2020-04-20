package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.Misc.Utils;
import Interfaces.MailServer.IContact;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;

public class Contact implements IContact {

    private final App app;
    private String name;
    private final DoublyLinkedList addresses = new DoublyLinkedList();
    private final User owner;
    private final int index;

    public Contact(App app, String name, User owner, int index) {
        this.app = app;
        this.name = name;
        this.owner = owner;
        this.index = index;
    }

    public int getIndex() {
        return index;
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

    public void addAddresses(String @NotNull [] arr) {
        for (String s : arr) {
            this.addresses.add(s);
        }
    }

    public boolean addAddress(String address) {
        if (!app.addressExists(address)) return false;
        addresses.add(address);
        try {
            owner.exportContacts();
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        return true;
    }

    public boolean changeAddress(String old, String address) {
        if (!app.addressExists(address)) return false;
        Iterator<String> iter = addresses.iterator(true);

        for (int i = 0; iter.hasNext(); i++) {
            if (old.equals(iter.next())) {
                addresses.set(i, address);
                try {
                    owner.exportContacts();
                } catch (IOException e) {
                    Utils.fileNotFound();
                }
                return true;
            }
        }
        return false;
    }

    public void deleteAddress(@NotNull String old) {
        Iterator<String> iter = addresses.iterator(true);

        for (int i = 0; iter.hasNext(); i++) {
            if (old.equals(iter.next())) {
                addresses.remove(i);
                try {
                    owner.exportContacts();
                } catch (IOException e) {
                    Utils.fileNotFound();
                }
                return;
            }
        }
    }

    public String getAddressesString() {
        StringBuilder sb = new StringBuilder();
        if (addresses.isEmpty()) return "";
        Iterator<String> iter = addresses.iterator(true);
        sb.append(iter.next());
        while (iter.hasNext()) {
            sb.append(",").append(iter.next());
        }
        return sb.toString();
    }
}
