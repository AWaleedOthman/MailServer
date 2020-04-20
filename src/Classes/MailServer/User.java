package Classes.MailServer;

import Classes.DataStructures.DoublyLinkedList;
import Classes.Misc.Birthday;
import Classes.Misc.Utils;
import Interfaces.MailServer.IContact;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class User implements IContact {


    private final String address;
    private final DoublyLinkedList contacts = new DoublyLinkedList();
    private String encryptedPassword;
    private String name, filePath;
    private String gender;
    private Birthday birthday;

    //TODO methods for searching and sorting contacts

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
    public void addContact(Contact contact) throws IOException {
        contacts.add(contact);
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", true));
        writer.write(contact.getName() + "," + contact.getAddressesString());
        writer.newLine();
        writer.close();
    }

    protected boolean delContact(@NotNull Contact contact) {
        Iterator<Contact> iter = contacts.iterator(true);

        for (int i = 0; iter.hasNext(); i++) {
            if (contact.equals(iter.next())) {
                contacts.remove(i);
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
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath() + "\\contacts.csv", false));
        if (contacts.isEmpty()) return;
        Iterator<Contact> iter = contacts.iterator(true);
        while (iter.hasNext()) {
            Contact contact = iter.next();
            writer.write(contact.getName() + "," + contact.getAddressesString());
            writer.newLine();
        }
        writer.close();
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

    public String getBirthdayString() {
        return birthday.getBirthday();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DoublyLinkedList getContacts() {
        return contacts;
    }
}
