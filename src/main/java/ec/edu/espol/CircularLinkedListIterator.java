package ec.edu.espol;

import java.util.Iterator;

public class CircularLinkedListIterator<T> implements Iterator<T> {
    private final CircularLinkedList<T> list;
    private CircularLinkedList.Node<T> current_node;
    private boolean looped;

    public CircularLinkedListIterator(CircularLinkedList<T> list) {
        this.list = list;
        this.current_node = list.head;
        this.looped = false;
    }

    public CircularLinkedListIterator(CircularLinkedListIterator<T> other) {
        this.list = other.list;
        this.current_node = other.current_node;
        this.looped = other.looped;
    }

    public CircularLinkedListIterator<T> copy() {
        return new CircularLinkedListIterator<>(this);
    }

    public CircularLinkedList<T> getList() {
        return list;
    }

    @Override
    public boolean hasNext() {
        return current_node != null && current_node.next != null;
    }

    @Override
    public T next() {
        if (!hasNext()) return null;
        T value = current_node.value;
        current_node = current_node.next;
        if (current_node == list.head) looped = true;
        return value;
    }

    public boolean hasPrev() {
        return current_node != null && current_node.prev != null;
    }

    public T prev() {
        if (!hasPrev()) return null;
        T value = current_node.value;
        current_node = current_node.prev;
        if (current_node == list.head.prev) looped = true;
        return value;
    }

    @Override
    public void remove() {
        current_node = list.remove_at_node(current_node);
    }

    public void removeConsumed() {
        current_node = list.remove_at_node(current_node.prev);
    }

    public void changeConsumedNodeValue(T value) {
        current_node.prev.value = value;
    }

    public void changeCurrentNodeValue(T value) {
        current_node.value = value;
    }

    public boolean hasLooped() {
        return looped;
    }

    public CircularLinkedList.Node<T> getCurrentNode() {
        return current_node;
    }

    public void reset() {
        this.current_node = list.head;
        this.looped = false;
    }
}
