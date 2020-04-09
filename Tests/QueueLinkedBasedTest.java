package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Classes.QueueLinkedBased;

class QueueLinkedBasedTest {

	QueueLinkedBased q;
	
	@BeforeEach
	void setUp() throws Exception {
		// Instantiate a array based queue with a capacity of five elements
		q = new QueueLinkedBased();
		// Intialize queue
		q.enqueue(5);
		q.enqueue(10);
		q.enqueue(59);
	}

	@Test
	void testEnqueue() {
		// Enqueue Element
		q.enqueue(20);
		// Check the size has inceased
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
		// The queue is not empty after its intialization
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
		// Check the queue size after intialization with three elements
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

}
