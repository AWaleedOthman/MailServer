package Classes.MailServer;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {

    @Test
    void validateSignupTest() throws FileNotFoundException {
        String s;
        User.loadUsers();
        s = User.validateSignup("a..waleedothman@thetrio.com", "strong password");
        assertEquals("invalidEmailAddressANDPassword", s);
        s = User.validateSignup("a.waleedhman@thetrio.com", "ssord");
        assertEquals("invalidPassword", s);
        s = User.validateSignup("a.waleedhman@thetrio.com", "P  assword");
        assertEquals("invalidPassword", s);
        s = User.validateSignup("a.w@leedothman@thetrio.com", "passssword");
        assertEquals("invalidEmailAddress", s);
        s = User.validateSignup("a.waldothman@thetrio.com", "passssword");
        assertNull(s);
        s = User.validateSignup("a.waleedothman@thetrio.com", "passssword");
        assertEquals("alreadyExists", s);
    }


    @Test
    void signinTest() throws IOException {
        User.loadUsers();
        User mine = User.signin("a.waleedothman@thetrio.com", "PA$$w0rd");
        assert mine != null;
        assertEquals("Ahmad Waleed", mine.getName());
        assertEquals("a.waleedothman@thetrio.com", mine.getAddress());
        assertEquals("1/1/2000", mine.getBirthday());
        assertEquals("Male", mine.getGender());
        mine = User.signin("a.waleedothman@thetrio.com", "wrongPassword");
        assertNull(mine);
        mine = User.signin("a.waleedthman@thetrio.com", "PA$$w0rd");
        assertNull(mine);
    }
}
