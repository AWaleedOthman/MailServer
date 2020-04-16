package Classes.Misc;

import Classes.DataStructures.SinglyLinkedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void name() {
        SinglyLinkedList list = Utils.getValues("\"hi\"\" hi\"\" kman hi\", \"another\"");
        assertEquals("hi\"\" hi\"\" kman hi", list.get(0));
        assertEquals("another", list.get(1));
    }
}
