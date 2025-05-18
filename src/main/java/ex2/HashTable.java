package main.java.ex2;


public class HashTable {
    private static final int INITIAL_CAPACITY = 16;
    private HashEntry[] table; // Ahora se refiere a la clase main.java.ex1.ex2.HashEntry (separada)
    private int _count;
    private int _size;

    public HashTable() {
        this.table = new HashEntry[INITIAL_CAPACITY];
        this._count = 0;
        this._size = 0;
    }

    private int getBucketIndex(String key) {
        if (key == null) return 0;
        int hashCode = key.hashCode();
        int index = hashCode % table.length;
        return index < 0 ? index + table.length : index;
    }

    public void put(String key, String value) {
        if (key == null) {
            return;
        }

        int bucketIndex = getBucketIndex(key);
        // HashEntry es ahora la clase separada main.java.ex1.ex2.HashEntry
        HashEntry head = table[bucketIndex];
        HashEntry current = head;
        HashEntry prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }

        _count++;
        HashEntry newEntry = new HashEntry(key, value); // Crea instancia de main.java.ex1.ex2.HashEntry
        if (head == null) {
            table[bucketIndex] = newEntry;
            _size++;
        } else {
            if (prev != null) {
                prev.next = newEntry;
            }
        }
    }

    public String get(String key) {
        if (key == null) return null;

        int bucketIndex = getBucketIndex(key);
        HashEntry current = table[bucketIndex]; // main.java.ex1.ex2.HashEntry

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public void drop(String key) {
        if (key == null) return;

        int bucketIndex = getBucketIndex(key);
        HashEntry current = table[bucketIndex]; // main.java.ex1.ex2.HashEntry
        HashEntry prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                _count--;

                if (prev == null) {
                    table[bucketIndex] = current.next;
                } else {
                    prev.next = current.next;
                }

                if (table[bucketIndex] == null) {
                    _size--;
                }
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public int count() {
        return _count;
    }

    public int size() {
        return _size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                sb.append("[").append(i).append("] -> ");
                HashEntry current = table[i]; // main.java.ex1.ex2.HashEntry
                while (current != null) {
                    sb.append("<").append(current.key).append(", ").append(current.value).append(">");
                    if (current.next != null) {
                        sb.append(" -> ");
                    }
                    current = current.next;
                }
                sb.append("\n");
            }
        }
        if (sb.length() == 0) {
            return "[]";
        }
        return sb.toString();
    }
}