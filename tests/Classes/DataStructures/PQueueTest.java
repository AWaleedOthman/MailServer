package Test;
import Classes.PQueue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PQueueTest {
	PQueue queue ;
	@Test
	void test() {
		queue = new PQueue();
		assertEquals(true, queue.isEmpty());
		assertEquals(0, queue.size());
		queue.insert(2, 1);
		assertEquals(false, queue.isEmpty());
		assertEquals(1, queue.size());
		queue.insert(3, 2);
		queue.insert(5, 3);
		queue.insert(8, 4);
		queue.insert(4, 3);
		queue.insert(10, 3);
		assertEquals(6, queue.size());
		queue.removeMin();
		queue.removeMin();
		queue.removeMin();
		assertEquals(3, queue.size());
		assertEquals(4, queue.min());
		queue.insert(2, 1);
		assertEquals(4, queue.size());
		assertEquals(2, queue.min());
	}
	
	void test2() {
		queue = new PQueue();
		assertEquals(true, queue.isEmpty());
		assertEquals(0, queue.size());
		queue.insert(2, 1);
		assertEquals(false, queue.isEmpty());
		assertEquals(1, queue.size());
		queue.insert(3, 1);
		queue.insert(5, 1);
		queue.insert(8, 1);
		queue.insert(4, 1);
		queue.insert(10, 1);
		assertEquals(6, queue.size());
		queue.removeMin();
		assertEquals(3, queue.min());
		queue.removeMin();
		queue.removeMin();
		assertEquals(3, queue.size());
		assertEquals(8, queue.min());
		queue.insert(2, 1);
		assertEquals(4, queue.size());
		assertEquals(8, queue.min());
	}
	

}
