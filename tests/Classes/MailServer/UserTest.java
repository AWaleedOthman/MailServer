package Classes.MailServer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    App app = new App();
    @Test
    void signinTest() throws IOException {
        app.loadUsers();
        assertTrue(app.signin("a.waleed@thetrio.com", "password"));
        User mine = app.getLoggedinUser();
        assert mine != null;
        assertEquals("Ahmad Waleed", mine.getName());
        assertEquals("a.waleed@thetrio.com", mine.getAddress());
        assertEquals("1/4/2000", mine.getBirthday().toString());
        assertEquals("Male", mine.getGender());
        assertFalse(app.signin("a.waleed@thetrio.com", "wrongPassword"));
        assertFalse(app.signin("a.waleedthman@thetrio.com", "PA$$w0rd"));
    }

}
