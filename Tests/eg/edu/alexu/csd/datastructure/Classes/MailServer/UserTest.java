package eg.edu.alexu.csd.datastructure.Classes.MailServer;

import eg.edu.alexu.csd.datastructure.Classes.DataStructures.DoublyLinkedList;
import eg.edu.alexu.csd.datastructure.Classes.Misc.Birthday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @BeforeEach
    void setUp() {
    }
    @Test
    void loadUsersTest() throws FileNotFoundException {
        User.loadUsers();
    }

    @Test
    void signupTest() throws IOException {
        User.signup("awaleed@thetrio.com", "password1", "Ahmad Waleed 1", "Male", new Birthday(1,1,2000));
        User.signup("cwaleed@thetrio.com", "password1", "Ahmad Waleed 1", "Male", new Birthday(1,1,2000));
        User.signup("aawaleed@thetrio.com", "password1", "Ahmad Waleed 1", "Male", new Birthday(1,1,2000));
        User.signup("bwaleed@thetrio.com", "password1", "Ahmad Waleed 1", "Male", new Birthday(1,1,2000));
        User.signup("bwaleed@thetrio.com", "password1", "Ahmad Waleed 1", "Male", new Birthday(1,1,2000));
    }

}
