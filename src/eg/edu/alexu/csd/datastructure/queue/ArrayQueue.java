package eg.edu.alexu.csd.datastructure.queue;

public class ArrayQueue implements IQueue, IArrayBased {

    private Object[] arr;
    private int size = 0; //size used of the array arr

    public ArrayQueue(int n) {
        arr = new Object[n];
    }

    /**
     * adds item to end of array/rear of queue
     * throws exception if maximum queue size/array size is reached
     * @param item Object to be added
     */
    @Override
    public void enqueue(Object item) {
        if (size == arr.length) throw new ArrayIndexOutOfBoundsException("Queue limit reached");
        arr[size] = item;
        ++size;
    }

    /**
     * removes the first item in array/item in queue front
     * throws exception in case of empty queue
     * @return item removed
     */
    @Override
    public Object dequeue() {
        if (size == 0) throw new ArrayIndexOutOfBoundsException("Empty queue");
        Object item = arr[0];
        System.arraycopy(arr, 1, arr, 0, size - 1);
        arr[size - 1] = null;
        --size;
        return item;
    }

    /**
     * @return true if the queue is empty and false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return true if maximum size of queue/size of array has been reached and false otherwise
     */
    public boolean isFull() {
        return size == arr.length;
    }

    /**
     * @return the size of the queue/size of elements used in the array
     */
    @Override
    public int size() {
        return size;
    }

}
