package eg.edu.alexu.csd.datastructure.Classes.DataStructures;

import eg.edu.alexu.csd.datastructure.Interfaces.DataStructures.ILinkedBased;
import eg.edu.alexu.csd.datastructure.Interfaces.DataStructures.IQueue;

public class QueueLinkedBased implements IQueue, ILinkedBased {

    SinglyLinkedList Queue = new SinglyLinkedList();

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
