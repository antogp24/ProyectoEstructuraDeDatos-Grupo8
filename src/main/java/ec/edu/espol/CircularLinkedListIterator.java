package ec.edu.espol;

import java.util.Iterator;

public class CircularLinkedListIterator<T> implements Iterator<T> {
    private final CircularLinkedList<T> list;
    private final CircularLinkedList.Node<T> start;
    private CircularLinkedList.Node<T> current;
    private boolean looped;

    public CircularLinkedListIterator(CircularLinkedList<T> list) {
        this.list = list;
        this.start = list.head;
        this.current = list.head;
        this.looped = false;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new IllegalStateException("No more elements");
        T value = current.value;
        current = current.next;
        if (current == start) looped = true;
        return value;
    }

    @Override
    public void remove() {
        list.remove_at_node(current);
    }

    public boolean hasLooped() {
        return looped;
    }

    public void reset() {
        current = list.head;
        looped = false;
    }
}
