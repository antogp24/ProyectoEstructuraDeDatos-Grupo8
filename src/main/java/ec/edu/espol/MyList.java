package ec.edu.espol;

public class MyList<T> {
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

    public int find(T item) {
        for (int i = 0; i < this.length; i++) {
            if (items[i] == item) return i;
        }
        return -1;
    }
}
