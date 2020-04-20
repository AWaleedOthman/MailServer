package Classes.DataStructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class QueueLinkedBasedTest {

    QueueLinkedBased q;

    @BeforeEach
    void setUp() {
        // Instantiate a array based queue with a capacity of five elements
        q = new QueueLinkedBased();
		// Initialize queue
		q.enqueue(5);
		q.enqueue(10);
		q.enqueue(59);
	}

	@Test
	void testEnqueue() {
		// Enqueue Element
		q.enqueue(20);
		// Check the size has incensed
		assertEquals(4, q.size());
		// Dequeue all elements in the queue before the recently added element
		q.dequeue();
		q.dequeue();
		q.dequeue();
		// Dequeue the added element
		assertEquals(20, q.dequeue());
	}

	@Test
	void testDequeue() {
		// Dequeue the first added element and check its value
		assertEquals(5, q.dequeue());
		// Check the queue size
		assertEquals(2, q.size());
	}
	
	@Test
	void testIsEmpty() {
		// The queue is not empty after its initialization
		assertFalse(q.isEmpty());
		// Dequeue the three elements added in the initialization 
		q.dequeue();
		q.dequeue();
		q.dequeue();
		// Check the queue is empty
		assertTrue(q.isEmpty());
	}
	
	@Test
	void testSize() {
		// Check the queue size after initialization with three elements
		assertEquals(3, q.size());
		// Check the queue size after enqueue one more element
		q.enqueue(85);
		assertEquals(4, q.size());
		// Check the queue size after dequeue two elements
		q.dequeue();
		q.dequeue();
		assertEquals(2, q.size());
	}

	@Test
	void testDequeueEmptyQueue() {
		// Dequeue the three elements in the queue
		q.dequeue();
		q.dequeue();
		q.dequeue();
		// Check the queue is Empty
		assertTrue(q.isEmpty());
		// Check on IllegalStateException on dequeueing one more element 
		assertThrows(IllegalStateException.class, ()->q.dequeue());
	}


	//////////////////////////////////////////////////////////////////////////////////////


	@Test
	void enqueue() {
		QueueLinkedBased lq = new QueueLinkedBased();
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
		QueueLinkedBased lq = new QueueLinkedBased();
		lq.enqueue("first");
		lq.enqueue("second");
		assertEquals("first", lq.dequeue());
		assertEquals("second", lq.dequeue());
		assertThrows(IllegalStateException.class, lq::dequeue);
		assertTrue(lq.isEmpty());
	}

	@Test
	void isEmpty() {
		QueueLinkedBased lq = new QueueLinkedBased();
		assertTrue(lq.isEmpty());
		lq.enqueue("first");
		assertFalse(lq.isEmpty());
		assertEquals("first", lq.dequeue());
		assertThrows(IllegalStateException.class, lq::dequeue);
		assertTrue(lq.isEmpty());
		lq.enqueue("new");
		assertFalse(lq.isEmpty());
	}

	@Test
	void size() {
		QueueLinkedBased lq = new QueueLinkedBased();
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
		assertThrows(IllegalStateException.class, lq::dequeue);
		assertEquals(0, lq.size());
		lq.enqueue("f");
		assertEquals(1, lq.size());
	}

}
