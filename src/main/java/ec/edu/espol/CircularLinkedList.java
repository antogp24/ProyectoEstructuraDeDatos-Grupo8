package ec.edu.espol;

import java.util.Iterator;

public class CircularLinkedList<T> {
    protected Node<T> head;
    protected int length;

    public static class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;

        public Node(T value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    public CircularLinkedList() {
        this.head = null;
        this.length = 0;
    }

    public boolean is_empty() {
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
        if (is_empty()) {
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
        if (is_empty()) {
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
        add(cursor, value);
    }

    public void add(Node<T> cursor, T value) {
        if (cursor == null && !is_empty()) return;

        if (cursor == head) {
            add(value);
            return;
        }
        if (cursor == head.prev) {
            add_first(value);
            return;
        }

        // head node tail
        if (is_empty()) {
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

    public void remove(int index) {
        Node<T> cursor = head;
        for (int i = 0; i < length; i++) {
            if (i == index) break;
            cursor = cursor.next;
        }
        remove_at_node(cursor);
    }

    public T removeFirst() {
        if (is_empty()) return null;
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
        if (is_empty()) return null;
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

    public T remove_at_node(Node<T> cursor) {
        if (is_empty()) return null;

        if (cursor == head) return removeFirst();
        if (cursor == head.prev) return removeLast();
        T removed = cursor.value;

        if (head.next == head) {
            head = null;
        } else {
            cursor.prev.next = cursor.next;
            cursor.next.prev = cursor.prev;
        }

        length--;
        return removed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("CircularLinkedList{");

        Node<T> node = head;
        for (int i = 0; i < length; i++) {
            if (i > 0) builder.append(", ");
            builder.append(node.value);
            node = node.next;
        }

        builder.append("}");
        return builder.toString();
    }
}
