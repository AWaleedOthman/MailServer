package Classes.DataStructures;

import Interfaces.DataStructures.ILinkedList;

public class DoublyLinkedList implements ILinkedList {
    private static class dListNode {
        private dListNode next;
        private dListNode prev;
        private Object element;

        public dListNode(dListNode next, dListNode prev, Object element) {
            this.next = next;
            this.prev = prev;
            this.element = element;
        }

        public dListNode getNext() {
            return next;
        }

        public void setNext(dListNode next) {
            this.next = next;
        }

        public dListNode getPrev() {
            return prev;
        }

        public void setPrev(dListNode prev) {
            this.prev = prev;
        }

        public Object getElement() {
            return element;
        }

        public void setElement(Object element) {
            this.element = element;
        }
    }

    private dListNode head;
    private dListNode current;
    private dListNode tail;
    private int size;

    public DoublyLinkedList() {
        size = 0;
    }


    @Override
    public void add(int index, Object element) {
        dListNode newNode = new dListNode(null, null, element);
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            newNode.setNext(this.head);
            if (tail == null) {
                this.tail = this.head = this.current = newNode;
            } else {
                this.head.setPrev(newNode);
                this.head = newNode;
            }
            size++;
        } else if (index >= size) {
            add(element);
        } else {
            dListNode current = this.head;
            for (int i = 1; i < index; i++) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.getNext().setPrev(newNode);
            current.setNext(newNode);
            size++;
        }
    }

    @Override
    public void add(Object element) {
        dListNode newNode = new dListNode(null, null, element);
        if (this.tail == null) {
            head = tail = current = newNode;
        } else {
            this.tail.setNext(newNode);
            newNode.setPrev(this.tail);
            this.tail = newNode;
        }
        size++;
    }

    @Override
    public Object get(int index) {
        if (index < 0 || index >= this.size()) {
            return null;
        }
        // Index starts from 0 excluding
        if (getHead() == null) {
            return null;
        }
        dListNode fetcher;
        if (index < this.size / 2) {
            fetcher = getHead();
            int counter = 0;
            while (counter < index) {
                fetcher = fetcher.getNext();
                counter++;
            }
        } else {
            fetcher = getTail();
            int counter = this.size - 1;
            while (counter > index) {
                fetcher = fetcher.prev;
                --counter;
            }
        }
        if (fetcher != null) {
            current = fetcher;
            return fetcher.getElement();
        }
        return null;
    }

    @Override
    public void set(int index, Object element) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (getHead() == null) {
            return;
        }
        dListNode fetcher = getHead();
        int counter = 0;
        while (fetcher != null && counter < index) {
            if (fetcher == this.tail) {
                fetcher = null;
            } else {
                fetcher = fetcher.getNext();
            }
            counter++;
        }
        if (fetcher != null) {
            fetcher.setElement(element);
        }
    }

    @Override
    public void clear() {
        this.head = this.tail = this.current = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size < 1;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else if (size == 1) {
            this.clear();
        } else if (index == 0) {
            dListNode temp = this.head;
            this.head = this.head.getNext();
            this.head.setPrev(null);
            temp.setNext(null);
            size--;
        } else {
            dListNode prev = this.head;
            for (int i = 1; i < index; i++) {
                prev = prev.getNext();
            }
            if (index == size - 1) {
                this.tail.setPrev(null);
                this.tail = prev;
                prev.setNext(null);
            } else {
                dListNode temp = prev.getNext();
                prev.setNext(prev.getNext().getNext());
                prev.getNext().getNext().setPrev(prev);
                temp.setNext(null);
                temp.setPrev(null);
            }
            size--;
        }
    }

    @Override
    public int size() {

        return this.size;
    }

    @Override
    public ILinkedList sublist(int fromIndex, int toIndex) {
        DoublyLinkedList sub = new DoublyLinkedList();
        if (fromIndex < 0 || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        if (getHead() == null) {
            return null;
        }
        dListNode fetcher = getHead();
        int counter = 0;
        // Iterate to find the fromNode
        while (fetcher != null && counter < fromIndex) {
            fetcher = fetcher.getNext();
            counter++;
        }
        int size = 1;
        // If index not found return null
        if (fetcher != null) {
            sub.setHead(fetcher);
            while (fetcher != this.getTail() && counter < toIndex - 1) {
                fetcher = fetcher.getNext();
                counter++;
                size++;
            }
            sub.setTail(fetcher);
            sub.setSize(size);
            return sub;
        }

        return null;
    }

    @Override
    public boolean contains(Object o) {
        int[] element = (int[]) o;
        if (getHead() == null) {
            return false;
        }
        dListNode fetcher = getHead();
        while (fetcher != null) {
            if (((int[]) fetcher.getElement())[0] == element[0] &&
                    ((int[]) fetcher.getElement())[1] == element[1]) {
                return true;
            }
            if (fetcher == this.tail) {
                fetcher = null;
            } else {
                fetcher = fetcher.getNext();
            }
        }
        return false;
    }

    private void setSize(int size) {
        this.size = size;
    }

    public dListNode getHead() {
        return head;
    }

    public void setHead(dListNode head) {
        this.head = head;
    }

    public dListNode getTail() {
        return tail;
    }

    public void setTail(dListNode tail) {
        this.tail = tail;
    }

    //////////////////////////////////////////////////////////////////

    public boolean hasNext() {
        return current != null /*handles if current node has been removed*/&& current.next != null;
    }

    public Object getNext() {
        if (!hasNext()) return null;
        current = current.next;
        return current.element;
    }

}
