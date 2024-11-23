package ec.edu.espol;

import java.io.Serializable;
import java.util.Iterator;

public class MyList<T> implements Serializable, Iterable<T> {
    protected Object[] items;
    protected int length;
    protected int capacity;
    
    private static final int DEFAULT_CAPACITY = 8;
    
    public MyList(int capacity, boolean fill_size) {
        this.length = fill_size ? capacity : 0;
        this.capacity = capacity;
        this.items = new Object[capacity];
    }
    
    public MyList(int capacity) {
        this(capacity, true);
    }
    
    public MyList() {
        this(DEFAULT_CAPACITY, false);
    }

    public int size() {
        return length;
    }
    
    @SuppressWarnings("unchecked")
    public T get(int i) {
        return (T)items[i];
    }

    public void set(int i, T value) {
        items[i] = value;
    }

    protected void crecerArreglo() {
        Object[] old_items = items;
        int old_capacity = capacity;

        capacity *= 2;
        items = new Object[capacity];

        for (int i = 0; i < old_capacity; i++) {
            items[i] = old_items[i];
        }
    }

    public void add(T value) {
        if (length + 1 > capacity) crecerArreglo();
        items[length++] = value;
    }

    public void add(int index, T value) {
        if (length + 1 > capacity) crecerArreglo();

        for (int i = length; i > index; i--) {
            items[i] = items[i-1];
        }
        items[index] = value;

        length++;
    }

    public int find(T item) {
        for (int i = 0; i < this.length; i++) {
            if (items[i] == item) return i;
        }
        return -1;
    }

    /* */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");

        for (int i = 0; i < length; i++) {
            if (i > 0) builder.append(", ");
            builder.append(get(i));
        }
        builder.append("}");

        return builder.toString();
    }
    


    //-------------- Iterador Provisional(Para ArrayList)---------------

    // Devuelve un iterador personalizado
    public CustomMyListIterator iterator() {
        return new CustomMyListIterator();
    }

    // Clase interna que implementa el iterador personalizado
    public class CustomMyListIterator implements Iterator<T> {
        private int currentIndex = 0;

        // Devuelve true si hay más elementos por iterar
        @Override
        public boolean hasNext() {
            return currentIndex < length;
        }

        // Devuelve el siguiente elemento
        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            if (!hasNext()) throw new IllegalStateException("No more elements");
            return (T) items[currentIndex++];
        }

        // Elimina el elemento actual
        @Override
        public void remove() {
            if (currentIndex <= 0) throw new IllegalStateException("No current element to remove");
            for (int i = currentIndex - 1; i < length - 1; i++) {
                items[i] = items[i + 1];
            }
            items[--length] = null;
            currentIndex--;
        }

        // Reinicia el iterador al inicio de la lista
        public void reset() {
            currentIndex = 0;
        }

        // Devuelve el próximo elemento sin avanzar
        @SuppressWarnings("unchecked")
        public T peek() {
            if (!hasNext()) throw new IllegalStateException("No more elements to peek");
            return (T) items[currentIndex];
        }

        // Salta n elementos
        public void skip(int n) {
            currentIndex += n;
            if (currentIndex > length) currentIndex = length; // No salirse del límite
        }

        // Devuelve el número de elementos restantes
        public int countRemaining() {
            return length - currentIndex;
        }

        // Verifica si está en el primer elemento
        public boolean isFirst() {
            return currentIndex == 0;
        }

        // Verifica si está en el último elemento
        public boolean isLast() {
            return currentIndex == length - 1;
        }
    }


}
