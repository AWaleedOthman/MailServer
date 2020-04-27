package Classes;

import Misc.Birthday;
import Misc.Utils;
import Interfaces.IContact;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

public class User implements IContact {


    private final String address;
    private final DoublyLinkedList contacts = new DoublyLinkedList();
    private String encryptedPassword;
    private String name, filePath;
    private String gender;
    private Birthday birthday;
    private final String sep = System.getProperty("file.separator");

    public User(String address, String encryptedpassword) {
        this.address = address;
        this.encryptedPassword = encryptedpassword;
    }

    public User(String address) {
        this.address = address;
    }

    /**
     * adds to list and to csv file
     *
     * @param contact to be added
     * @throws IOException file not found
     */
    public void addContact(Contact contact) {
        contact.setIndex(contacts.size());
        contact.setOwner(this);
        contacts.add(contact);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", true));
            writer.write(contact.getName() + "," + contact.getIndex() + "," + contact.getAddressesString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            Utils.fileNotFound();
        }
    }

    public DoublyLinkedList sortContactsByIndex() {
        Sort sort = new Sort(SortAttribute.contactIndex);
        Comparator<Object> c = sort.sortAttribute();
        contacts.Qsort(c);
        return contacts;
    }

    public DoublyLinkedList sortContactsByName() {
        Sort sort = new Sort(SortAttribute.contactName);
        Comparator<Object> c = sort.sortAttribute();
        contacts.Qsort(c);
        return contacts;
    }

    public DoublyLinkedList getContactByName(String cName) {
        cName = cName.toLowerCase();
        DoublyLinkedList res = new DoublyLinkedList();
        Iterator<Object> iter = contacts.iterator(true);
        while (iter.hasNext()) {
            Contact c = (Contact) iter.next();
            if (c.getName().toLowerCase().startsWith(cName)) res.add(c);
        }
        if (res.isEmpty()) {
            iter = contacts.iterator(true);
            while (iter.hasNext()) {
                Contact c = (Contact) iter.next();
                if (c.getName().toLowerCase().contains(cName)) res.add(c);
            }
        }
        return res;
    }

    public DoublyLinkedList getContactByAddress(String address) {
        address = address.toLowerCase();
        DoublyLinkedList res = new DoublyLinkedList();
        Iterator<Object> iter1 = contacts.iterator(true);
        while (iter1.hasNext()) {
            Contact c = (Contact) iter1.next();
            Iterator<Object> iter2 = c.getAddresses().iterator(true);
            while (iter2.hasNext()) {
                String s = (String) iter2.next();
                if (s.startsWith(address)) res.add(c);
            }
        }
        if (res.isEmpty()) {
            iter1 = contacts.iterator(true);
            while (iter1.hasNext()) {
                Contact c = (Contact) iter1.next();
                Iterator<Object> iter2 = c.getAddresses().iterator(true);
                while (iter2.hasNext()) {
                    String s = (String) iter2.next();
                    if (s.contains(address)) res.add(c);
                }
            }
        }
        return res;
    }

    protected boolean delContact(Contact contact) {
        Iterator<Object> iter = contacts.iterator(true);
        int index;
        for (int i = 0; iter.hasNext(); i++) {
            Contact con = (Contact) iter.next();
            if (contact.equals(con)) {
                contacts.remove(i);
                index = con.getIndex();

                Iterator<Object> iter2 = contacts.iterator(true);
                //minus 1 from indexes
                while (iter2.hasNext()) {
                    Contact c = (Contact) iter2.next();
                    if (c.getIndex() > index) {
                        c.setIndex(c.getIndex() - 1);
                    }
                }
                try {
                    exportContacts();
                } catch (IOException e) {
                    Utils.fileNotFound();
                }
                return true;
            }
        }
        return false;
    }

    protected void exportContacts() throws IOException {
        if (contacts.isEmpty()) return;
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", false));
        Iterator<Object> iter = contacts.iterator(true);
        while (iter.hasNext()) {
            Contact contact = (Contact) iter.next();
            writer.write(contact.getName() + "," + contact.getIndex() + "," + contact.getAddressesString());
            writer.newLine();
        }
        writer.close();
    }

    public void editFolders() {
        File file = new File(this.getFilePath() + "\\inbox");
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "\\inbox\\folders.txt", false));
            for (String name : directories) {
                writer.write(name);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            Utils.fileNotFound();
        }

    }

    public void addFolder(String name) {
        try {
            File index = new File(filePath + sep + "inbox" + sep + name + sep + "index.csv");
            index.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + sep + "inbox" + sep
                    + "folders.txt", true));
            writer.write(name);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            Utils.fileNotFound();
        }

    }

    public String getFilePath() {
        return filePath;
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
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setBirthday(Birthday bd) {
        this.birthday = bd;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DoublyLinkedList getContacts() {
        return contacts;
    }
}
