package Classes;

import Interfaces.ILinkedBased;
import Interfaces.IQueue;

public class QueueLinkedBased implements IQueue, ILinkedBased {

	singleLList Queue = new singleLList();
	
	@Override
	public void enqueue(Object item) {
		Queue.add(item);
	}

	@Override
	public Object dequeue() {
		if (Queue.isEmpty()) {
			throw new IllegalStateException();
		}
		Object temp = Queue.get(0);
		Queue.remove(0);
		return temp;
	}

	@Override
	public boolean isEmpty() {
		return Queue.isEmpty();
	}

	@Override
	public int size() {
		return Queue.size();
	}

}
