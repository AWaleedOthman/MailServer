package eg.edu.alexu.csd.datastructure.queue;

import eg.edu.alexu.csd.datastructure.linkedList.SinglyLinkedList;

public class LinkedQueue implements IQueue,ILinkedBased {

    private SinglyLinkedList sll;

    public LinkedQueue() {
        sll = new SinglyLinkedList();
    }

    @Override
    public void enqueue(Object item) {
        sll.add(0, item);
    }

    @Override
    public Object dequeue() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
