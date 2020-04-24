package Classes.DataStructures;

import org.junit.jupiter.api.Test;

class DoublyLinkedListTest {

    @Test
    public void test() {
        DoublyLinkedList dll = new DoublyLinkedList();
        dll.add("0");
        dll.add("1");
        dll.add("2");
        dll.remove(1);
    }
}
