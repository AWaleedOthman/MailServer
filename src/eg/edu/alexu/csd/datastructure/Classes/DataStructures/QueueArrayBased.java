package eg.edu.alexu.csd.datastructure.Classes.DataStructures;

import eg.edu.alexu.csd.datastructure.Interfaces.DataStructures.IArrayBased;
import eg.edu.alexu.csd.datastructure.Interfaces.DataStructures.IQueue;

public class QueueArrayBased implements IQueue, IArrayBased {

    Object[] Queue;
    int f, r, capacity;

    public QueueArrayBased(int cap) {
        Queue = new Object[cap];
        f = r = 0;
        capacity = cap+1;
    }

    @Override
    public void enqueue(Object item) {
        if (this.isFull()) {
            throw new IllegalStateException();
        }
        Queue[r] = item;
        r = (r + 1) % capacity;
    }

    @Override
    public Object dequeue() {
        if(this.isEmpty()) {
            throw new IllegalStateException();
        }
        Object temp = Queue[f];
        Queue[f] = null;
        f = (f + 1) % capacity;
        return temp;
    }

    @Override
    public boolean isEmpty() {
        return f == r;
    }

    @Override
    public int size() {
        return (capacity - f + r) % capacity;
    }

    public boolean isFull() {return size() == capacity - 1;}

}
