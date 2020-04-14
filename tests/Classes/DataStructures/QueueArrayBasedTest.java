package Classes.DataStructures;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class QueueArrayBasedTest {
	
	QueueArrayBased q;
	
	@BeforeEach
	void setUp() {
		// Instantiate a array based queue with a capacity of five elements
		q = new QueueArrayBased(5);
		// Initialize queue
		q.enqueue(5);
		q.enqueue(10);
		q.enqueue(59);
	}

	@Test
	void testEnqueue() {
		// Enqueue Element
		q.enqueue(20);
		// Check the size has increased
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
	void testFullQueue() {
		// After enqueueing two more elements the queue become full
		q.enqueue(7);
		q.enqueue(9);
		// Check the queue is Full
		assertEquals(5, q.size());
		// Check on IllegalStateException on adding one more element 
		assertThrows(IllegalStateException.class, ()->q.enqueue(63));
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


	///////////////////////////////////////////////////////////////////////////////////


	@Test
	void enqueue() {
		QueueArrayBased aq = new QueueArrayBased(2);
		assertTrue(aq.isEmpty());
		aq.enqueue("first");
		assertFalse(aq.isEmpty());
		aq.enqueue("second");
		assertThrows(IllegalStateException.class, () -> aq.enqueue("third"));
		assertEquals("first", aq.dequeue());
	}

	@Test
	void dequeue() {
		QueueArrayBased aq = new QueueArrayBased(3);
		aq.enqueue("first");
		aq.enqueue("second");
		assertEquals("first", aq.dequeue());
		assertEquals("second", aq.dequeue());
		assertThrows(IllegalStateException.class, aq::dequeue);
		assertTrue(aq.isEmpty());
	}

	@Test
	void isEmpty() {
		QueueArrayBased aq = new QueueArrayBased(3);
		assertTrue(aq.isEmpty());
		aq.enqueue("first");
		assertFalse(aq.isEmpty());
		assertEquals("first", aq.dequeue());
		assertThrows(IllegalStateException.class, aq::dequeue);
		assertTrue(aq.isEmpty());
		aq.enqueue("new");
		assertFalse(aq.isEmpty());
	}

	@Test
	void size() {
		QueueArrayBased aq = new QueueArrayBased(2);
		assertEquals(0, aq.size());
		aq.enqueue("first");
		assertEquals(1, aq.size());
		aq.enqueue("second");
		assertEquals(2, aq.size());
		assertThrows(IllegalStateException.class, () -> aq.enqueue("third"));
		assertEquals(2, aq.size());
		assertEquals("first", aq.dequeue());
		assertEquals(1, aq.size());
		assertEquals("second", aq.dequeue());
		assertEquals(0, aq.size());
		assertThrows(IllegalStateException.class, aq::dequeue);
		assertEquals(0, aq.size());
		aq.enqueue("f");
		assertEquals(1, aq.size());
	}

	@Test
	void isFull() {
		QueueArrayBased aq = new QueueArrayBased(2);
		assertFalse(aq.isFull());
		aq.enqueue("first");
		assertFalse(aq.isFull());
		aq.enqueue("second");
		assertTrue(aq.isFull());
		aq.dequeue();
		assertFalse(aq.isFull());
	}
}
