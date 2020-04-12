package eg.edu.alexu.csd.datastructure.Classes.Misc;

import eg.edu.alexu.csd.datastructure.Classes.DataStructures.DoublyLinkedList;
import eg.edu.alexu.csd.datastructure.Classes.MailServer.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    private String decryptrd = "password";

    @Test
    void decrypt() {
        String encrypted = AES.encrypt(decryptrd, "secretKey");
        assertEquals(decryptrd, AES.decrypt(encrypted, "secretKey"));
        encrypted = AES.encrypt(decryptrd, decryptrd);
        assertEquals(decryptrd, AES.decrypt(encrypted, decryptrd));
        decryptrd = "](D!$gdA99V=@'r>";
        encrypted = AES.encrypt(decryptrd, decryptrd);
        assertEquals(decryptrd, AES.decrypt(encrypted, decryptrd));
        assertEquals("testStr0ngPa$$word", AES.decrypt("Zo3b2brshWRBQhvC9sQoC0vXhEoHob/c0yFY2Ks20pE=", "testStr0ngPa$$word"));
    }

    @Test
    void name() throws IOException {
        User.exportList();
    }
}
