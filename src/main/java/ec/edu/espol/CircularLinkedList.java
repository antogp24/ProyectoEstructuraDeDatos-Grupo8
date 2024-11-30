package ec.edu.espol;

import java.io.Serializable;
import java.util.Comparator;

public class CircularLinkedList<T> implements Iterable<T>, Serializable {
    protected Node<T> head;
    protected int length;

    public static class Node<T> implements Serializable{
        public T value;
        public Node<T> next;
        public Node<T> prev;

        public Node(T value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            return Integer.toHexString(hashCode());
        }
    }

    public CircularLinkedList() {
        this.head = null;
        this.length = 0;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public int size() {
        return length;
    }

    public T get(int index) {
        Node<T> cursor = head;
        for (int i = 0; i < length; i++) {
            if (i == index) break;
            cursor = cursor.next;
        }
        return get(cursor);
    }

    public T getFirst() {
        return get(head);
    }

    public T getLast() {
        return get(head.prev);
    }

    public T get(Node<T> cursor) {
        return cursor.value;
    }

    public void add(T value) {
        if (isEmpty()) {
            head = new Node<>(value);
            head.prev = head;
            head.next = head;
        } else {
            Node<T> added = new Node<>(value);
            Node<T> old_tail = head.prev;
            added.prev = old_tail;
            added.next = head;
            old_tail.next = added;
            head.prev = added;
        }
        length++;
    }

    public void add_first(T value) {
        if (isEmpty()) {
            head = new Node<>(value);
            head.prev = head;
            head.next = head;
        } else {
            Node<T> added = new Node<>(value);
            Node<T> old_tail = head.prev;
            added.prev = old_tail;
            added.next = head;
            old_tail.next = added;
            head = added;
        }
        length++;
    }

    public void add(int index, T value) {
        Node<T> cursor = head;
        for (int i = 0; i < length; i++) {
            if (i == index) break;
            cursor = cursor.next;
        }
        add_at_node(cursor, value);
    }

    public void add_at_node(Node<T> cursor, T value) {
        if (cursor == null && !isEmpty()) return;

        if (cursor == head) {
            add(value);
            return;
        }
        if (cursor == head.prev) {
            add_first(value);
            return;
        }

        // head node tail
        if (isEmpty()) {
            head = new Node<>(value);
            head.prev = head;
            head.next = head;
        } else {
            Node<T> added = new Node<>(value);
            added.prev = cursor.prev;
            added.next = cursor;
            cursor.prev.next = added;
            cursor.prev = added;
        }
        length++;
    }

    public void remove(T element) {
        Node<T> node = head;
        while (node != null) {
            if (node.value.equals(element)) break;
            node = node.next;
        }
        if (node == null) return;
        remove_at_node(node);
    }

    public void removeAt(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> cursor = head;
        for (int i = 0; i < length; i++) {
            if (i == index) break;
            cursor = cursor.next;
        }
        remove_at_node(cursor);
    }

    public T removeFirst() {
        if (isEmpty()) return null;
        T removed = head.value;

        if (head.next == head) {
            head = null;
        } else {
            Node<T> tail = head.prev;
            head = head.next;
            head.prev = tail;
            tail.next = head;
        }

        length--;
        return removed;
    }

    public T removeLast() {
        if (isEmpty()) return null;
        T removed = head.prev.value;

        if (head.next == head) {
            head = null;
        } else {
            Node<T> tail = head.prev;
            tail.prev.next = head;
            head.prev = tail.prev;
        }

        length--;
        return removed;
    }

    public Node<T> remove_at_node(Node<T> cursor) {
        if (isEmpty()) return null;

        if (cursor == head) {
            removeFirst();
            return head;
        }
        if (cursor == head.prev) {
            removeLast();
            return isEmpty() ? null : head.prev;
        }
        Node<T> node_after_removed = null;

        if (head.next == head) {
            node_after_removed = null;
            head = null;
        } else {
            node_after_removed = cursor.next;
            cursor.prev.next = cursor.next;
            cursor.next.prev = cursor.prev;
        }

        length--;
        return node_after_removed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("CircularLinkedList{");

        Node<T> node = head;
        for (int i = 0; i < length; i++) {
            if (i > 0) builder.append(", ");
            builder.append("(" + node.toString() + ":");
            builder.append(node.value);
            builder.append(")");
            node = node.next;
        }

        builder.append("}");
        return builder.toString();
    }

    @Override
    public CircularLinkedListIterator<T> iterator() {
        return new CircularLinkedListIterator<>(this);
    }

    public void sort(Comparator<T> comparator) {
        if (isEmpty() || length == 1) return;

        boolean swapped;
        do {
            swapped = false;
            Node<T> current = head;
            for (int i = 0; i < length - 1; i++) {
                Node<T> next = current.next;
                if (comparator.compare(current.value, next.value) > 0) {
                    // Swap the values of the nodes
                    T temp = current.value;
                    current.value = next.value;
                    next.value = temp;
                    swapped = true;
                }
                current = current.next;
            }
        } while (swapped);
    }
}
