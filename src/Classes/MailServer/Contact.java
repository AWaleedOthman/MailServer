package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.Misc.Utils;
import Interfaces.MailServer.IContact;

import java.io.IOException;

public class Contact implements IContact {
    private String name;
    private final DoublyLinkedList addresses = new DoublyLinkedList();
    private final User owner;

    public Contact(String name, User owner) {
        this.name = name;
        this.owner = owner;
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

    public int addAddresses(String addresses) {
        int i = 0;
        if (addresses.isEmpty()) return i;
        String[] arr = addresses.split(",");
        for (String s : arr) {
            if (!User.addressExists(s)) return i;
            this.addresses.add(s);
            ++i;
        }
        return i;
    }

    public boolean addAddress(String address) {
        if (!User.addressExists(address)) return false;
        addresses.add(address);
        try {
            owner.exportContacts();
        } catch (IOException e) {
            Utils.fileNotFound();
        }
        return true;
    }

    public boolean changeAddress(String old, String address) {
        if (!User.addressExists(address)) return false;
        if (old.equals(addresses.get(0))) {
            addresses.set(0, address);
            try {
                owner.exportContacts();
            } catch (IOException e) {
                Utils.fileNotFound();
            }
            return true;
        }
        for (int i = 1; addresses.hasNext(); i++) {
            if (old.equals(addresses.getNext())) {
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

    public void deleteAddress(String old) {
        if (old.equals(addresses.get(0))) {
            addresses.remove(0);
            try {
                owner.exportContacts();
            } catch (IOException e) {
                Utils.fileNotFound();
            }
            return;
        }
        for (int i = 1; addresses.hasNext(); i++) {
            if (old.equals(addresses.getNext())) {
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
        sb.append(addresses.get(0));
        while (addresses.hasNext()) {
            sb.append(",").append(addresses.getNext());
        }
        return sb.toString();
    }
}
