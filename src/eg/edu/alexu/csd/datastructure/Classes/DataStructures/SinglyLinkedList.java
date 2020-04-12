package eg.edu.alexu.csd.datastructure.Classes.DataStructures;

import eg.edu.alexu.csd.datastructure.Interfaces.DataStructures.ILinkedList;

public class SinglyLinkedList implements ILinkedList {

    private static class ListNode {

        private ListNode next;
        private Object element;

        public ListNode(ListNode node, Object element) {
            this.next = node;
            this.setElement(element);
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }

        public Object getElement() {
            return element;
        }

        public void setElement(Object element) {
            this.element = element;
        }

    }

    private ListNode head;
    private ListNode tail;
    private int size;

    public SinglyLinkedList() {
        size = 0;
    }

    public SinglyLinkedList(ListNode node) {
        head = node;
        tail = node;
        size = 1;
    }

    @Override
    public void add(int index, Object element) {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException();
        }
        ListNode newNode = new ListNode(null, element);
        if (index == 0) {
            newNode.setNext(this.head);
            this.head = newNode;
            size++;
            if (tail == null) {
                this.tail = this.head;
            }

        } else if (index >= size) {
            add(element);
        } else {
            ListNode current = head;
            for (int i = 1; i < index; i++) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
            size++;
        }
    }

    @Override
    public void add(Object element) {
        ListNode newNode = new ListNode(null, element);
        if (this.tail == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            this.tail = newNode;
        }
        size++;
    }

    @Override
    public Object get(int index) {
        if (index < 0 || index >= this.size()) {
            return null;
        }
        // Index starts from 0
        if (getHead() == null) {
            return null;
        }
        ListNode fetcher = getHead();
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
        ListNode fetcher = getHead();
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
        this.head = this.tail = null;
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
            ListNode temp = this.head;
            this.head = this.head.getNext();
            temp.setNext(null);
            size--;
        } else {
            ListNode prev = this.head;
            for (int i = 1; i < index; i++) {
                prev = prev.getNext();
            }
            if (index == size - 1) {
                this.tail = prev;
                prev.setNext(null);
            } else {
                ListNode temp = prev.getNext();
                prev.setNext(prev.getNext().getNext());
                temp.setNext(null);
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
        if (fromIndex < 0 || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        SinglyLinkedList sub = new SinglyLinkedList();
        if (getHead() == null) {
            return null;
        }
        ListNode fetcher = getHead();
        int counter = 0;
        // Iterate to find the fromNode
        while (fetcher != null && counter < fromIndex) {
            if (fetcher == this.tail) {
                fetcher = null;
            } else {
                fetcher = fetcher.getNext();
            }
            counter++;
        }
        int size = 1;
        // If index not found return null
        if (fetcher != null) {
            sub.setHead(fetcher);
            while (fetcher != this.tail && counter < toIndex - 1) {
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
        ListNode fetcher = getHead();
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

    public ListNode getHead() {
        return head;
    }

    private void setHead(ListNode head) {
        this.head = head;
    }

    public ListNode getTail() {
        return tail;
    }

    private void setTail(ListNode tail) {
        this.tail = tail;
    }
}
