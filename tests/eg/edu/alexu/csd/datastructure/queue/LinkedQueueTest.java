package eg.edu.alexu.csd.datastructure.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedQueueTest {

    @Test
    void enqueue() {
        LinkedQueue lq = new LinkedQueue();
        assertTrue(lq.isEmpty());
        lq.enqueue("first");
        assertFalse(lq.isEmpty());
        lq.enqueue("second");
        assertEquals("first", lq.dequeue());
        assertEquals("second", lq.dequeue());
        assertTrue(lq.isEmpty());
    }

    @Test
    void dequeue() {
        LinkedQueue lq = new LinkedQueue();
        lq.enqueue("first");
        lq.enqueue("second");
        assertEquals("first", lq.dequeue());
        assertEquals("second", lq.dequeue());
        assertThrows(ArrayIndexOutOfBoundsException.class, lq::dequeue);
        assertTrue(lq.isEmpty());
    }

    @Test
    void isEmpty() {
        LinkedQueue lq = new LinkedQueue();
        assertTrue(lq.isEmpty());
        lq.enqueue("first");
        assertFalse(lq.isEmpty());
        assertEquals("first", lq.dequeue());
        assertThrows(ArrayIndexOutOfBoundsException.class, lq::dequeue);
        assertTrue(lq.isEmpty());
        lq.enqueue("new");
        assertFalse(lq.isEmpty());
    }

    @Test
    void size() {
        LinkedQueue lq = new LinkedQueue();
        assertEquals(0, lq.size());
        lq.enqueue("first");
        assertEquals(1, lq.size());
        lq.enqueue("second");
        assertEquals(2, lq.size());
        lq.enqueue("third");
        assertEquals(3, lq.size());
        assertEquals("first", lq.dequeue());
        assertEquals(2, lq.size());
        assertEquals("second", lq.dequeue());
        assertEquals(1, lq.size());
        assertEquals("third", lq.dequeue());
        assertThrows(ArrayIndexOutOfBoundsException.class, lq::dequeue);
        assertEquals(0, lq.size());
        lq.enqueue("f");
        assertEquals(1, lq.size());
    }
}
