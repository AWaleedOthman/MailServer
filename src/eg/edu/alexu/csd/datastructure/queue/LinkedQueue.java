package eg.edu.alexu.csd.datastructure.queue;

import eg.edu.alexu.csd.datastructure.linkedList.SinglyLinkedList;

public class LinkedQueue implements IQueue, ILinkedBased {

    private SinglyLinkedList sll;

    public LinkedQueue() {
        sll = new SinglyLinkedList();
    }

    /**
     * adds item to the rear of the queue/the head of the singly linked list
     *
     * @param item to be added to rear of queue
     */
    /*
    the item is added to the head of the linked list because it is faster and enqueue is called more than dequeue
     */
    @Override
    public void enqueue(Object item) {
        sll.add(0, item);
    }

    /**
     * removes item from queue's front/singly-linked-list's tail
     *
     * @return the item removed
     */
    /*
    see comment on enqueue on why sll's tail and not head
     */
    @Override
    public Object dequeue() {
        if (sll.size() == 0) throw new ArrayIndexOutOfBoundsException("Empty queue");
        Object item = sll.get(sll.size() - 1);
        sll.remove(sll.size() - 1);
        return item;
    }

    /**
     * @return true if the queue id empty and false otherwise
     */
    @Override
    public boolean isEmpty() {
        return sll.isEmpty();
    }

    /**
     * @return the size of the queue
     */
    @Override
    public int size() {
        return sll.size();
    }
}
