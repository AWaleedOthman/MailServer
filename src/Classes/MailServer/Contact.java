package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Interfaces.MailServer.IContact;

public class Contact implements IContact {
    private String name;
    private DoublyLinkedList addresses = new DoublyLinkedList();

    public Contact(String name, String addresses) {
        this.name = name;
        String[] arr = addresses.split(",");
        for (String s : arr) {
            this.addresses.add(s);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DoublyLinkedList getAddresses() {
        return addresses;
    }

    public void addAddress(String address) {
        addresses.add(address);
    }

    public boolean changeAddress(String old, String address) {
        if (addresses.isEmpty()) return false;
        if (old.equals(addresses.get(0))) {
            addresses.set(0, address);
            return true;
        }
        for (int i = 1; addresses.hasNext(); i++) {
            if (old.equals(addresses.getNext())) {
                addresses.set(i, address);
                return true;
            }
        }
        return false;
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
