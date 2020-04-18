package Classes.MailServer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    void signinTest() throws IOException {
        User.loadUsers();
        assertTrue(User.signin("a.waleedothman@thetrio.com", "PA$$w0rd"));
        User mine = User.loadInfo("a.waleedothman@thetrio.com");
        assert mine != null;
        assertEquals("Ahmad Waleed", mine.getName());
        assertEquals("a.waleedothman@thetrio.com", mine.getAddress());
        assertEquals("1/1/2000", mine.getBirthday());
        assertEquals("Male", mine.getGender());
        assertFalse(User.signin("a.waleedothman@thetrio.com", "wrongPassword"));
        assertFalse(User.signin("a.waleedthman@thetrio.com", "PA$$w0rd"));
    }
}
