package Classes.MailServer;

import Classes.DataStructures.SinglyLinkedList;
import Interfaces.MailServer.IFilter;
import Interfaces.MailServer.IMail;

import java.util.Date;

public class Mail implements IMail {
    /*
     * ID member is  a unique ID identifies each mail
     * */
    private int ID;
    /*
     * Text member is  a string field carrying the body message of the mail
     * */
    private String text;
    /*
     * Title member is  a string field the title of the mail
     * */
    private String Title;
    /*
     * SenderID member to identify mail's sender
     * */
    private int senderID;
    /*
     * SenderName member to facilitate sender recognition
     * */
    private String senderName;
    /*
     * RecieverID member to identify mail's reciever
     * */
    private int recieverID;
    /*
     * Attachments member carries mail attachments
     * */
    private SinglyLinkedList attachments;
    /*
     * Date member stores mail sending date
     * */
    private Date date;
    /*
     * Filter member stores mail filter
     * */
    private IFilter filter;
    /*
     * Priority member stores mail priority
     * */
    private int priority;
    // Adding Replies

    private String senderAddress, recieverAddress;

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getRecieverAddress() {
        return recieverAddress;
    }

    public Mail(String title, int senderID, String senderName, Date date, int priority) {
        // this.ID = Generate unique next ID
        this.Title = title;
        this.senderID = senderID;
        this.senderName = senderName;
        this.date = date;
        this.setPriority(priority);
    }

    public Mail(int ID, String title, int senderID, String senderName, Date date, int priority) {
        this.ID = ID;
        this.Title = title;
        this.senderID = senderID;
        this.senderName = senderName;
        this.date = date;
        this.setPriority(priority);
    }

    public int getID() {
        return ID;
    }


    public String getText() {
        return text;
    }

    public void setText(String mailText) {
        this.text = mailText;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public int getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(int receiverID) {
        this.recieverID = receiverID;
    }


    public SinglyLinkedList getAttachments() {
        return attachments;
    }

    public void setAttachments(SinglyLinkedList attachments) {
        this.attachments = attachments;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public IFilter getFilter() {
        return filter;
    }

    public void setFilter(IFilter filter) {
        this.filter = filter;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
