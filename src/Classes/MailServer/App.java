package Classes.MailServer;

import Classes.Misc.Utils;
import Interfaces.DataStructures.ILinkedList;
import Interfaces.MailServer.*;

import java.io.IOException;

public class App implements IApp {
    @Override
    public boolean signin(String email, String password) {
        return User.signin(email, password);
    }

    @Override
    public boolean signup(IContact contact) {
        return User.signup(contact);
    }

    @Override
    public void setViewingOptions(IFolder folder, IFilter filter, ISort sort) {

    }

    @Override
    public IMail[] listEmails(int page) {
        return new IMail[0];
    }

    @Override
    public void deleteEmails(ILinkedList mails) {

    }

    @Override
    public void moveEmails(ILinkedList mails, IFolder des) {

    }

    @Override
    public boolean compose(IMail email) {
        return false;
    }
}
