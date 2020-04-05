package eg.edu.alexu.csd.datastructure.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayQueueTest {

    @Test
    void enqueue() {
        ArrayQueue aq = new ArrayQueue(2);
        assertTrue(aq.isEmpty());
        aq.enqueue("first");
        assertFalse(aq.isEmpty());
        aq.enqueue("second");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> aq.enqueue("third"));
        assertEquals("first", aq.dequeue());
    }

    @Test
    void dequeue() {
        ArrayQueue aq = new ArrayQueue(3);
        aq.enqueue("first");
        aq.enqueue("second");
        assertEquals("first", aq.dequeue());
        assertEquals("second", aq.dequeue());
        assertThrows(ArrayIndexOutOfBoundsException.class, aq::dequeue);
        assertTrue(aq.isEmpty());
    }

    @Test
    void isEmpty() {
        ArrayQueue aq = new ArrayQueue(3);
        assertTrue(aq.isEmpty());
        aq.enqueue("first");
        assertFalse(aq.isEmpty());
        assertEquals("first", aq.dequeue());
        assertThrows(ArrayIndexOutOfBoundsException.class, aq::dequeue);
        assertTrue(aq.isEmpty());
        aq.enqueue("new");
        assertFalse(aq.isEmpty());
    }

    @Test
    void size() {
        ArrayQueue aq = new ArrayQueue(2);
        assertEquals(0, aq.size());
        aq.enqueue("first");
        assertEquals(1, aq.size());
        aq.enqueue("second");
        assertEquals(2, aq.size());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> aq.enqueue("third"));
        assertEquals(2, aq.size());
        assertEquals("first", aq.dequeue());
        assertEquals(1, aq.size());
        assertEquals("second", aq.dequeue());
        assertEquals(0, aq.size());
        assertThrows(ArrayIndexOutOfBoundsException.class, aq::dequeue);
        assertEquals(0, aq.size());
        aq.enqueue("f");
        assertEquals(1, aq.size());
    }

    @Test
    void isFull() {
        ArrayQueue aq = new ArrayQueue(2);
        assertFalse(aq.isFull());
        aq.enqueue("first");
        assertFalse(aq.isFull());
        aq.enqueue("second");
        assertTrue(aq.isFull());
        aq.dequeue();
        assertFalse(aq.isFull());
    }
}
