package main.java.ex3;


public class HashTable {
    private static final int INITIAL_CAPACITY = 16;
    private HashEntry[] table;
    private int _count;
    private int _size;

    public HashTable() {
        this.table = new HashEntry[INITIAL_CAPACITY]; // Usa main.java.ex1.ex3.HashEntry
        this._count = 0;
        this._size = 0;
    }

    private int getBucketIndex(String key) {
        if (key == null) return 0;
        int hashCode = key.hashCode();
        int index = hashCode % table.length;
        return index < 0 ? index + table.length : index;
    }

    private static class FindResult { // Clase helper interna
        HashEntry foundEntry;
        HashEntry previousEntry;
        FindResult(HashEntry found, HashEntry prev) {
            this.foundEntry = found;
            this.previousEntry = prev;
        }
    }

    private HashEntry findEntryInBucket(int bucketIndex, String key) {
        HashEntry current = table[bucketIndex];
        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private FindResult findEntryAndPrevious(int bucketIndex, String key) {
        HashEntry head = table[bucketIndex];
        HashEntry current = head;
        HashEntry prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                return new FindResult(current, prev);
            }
            prev = current;
            current = current.next;
        }
        return new FindResult(null, prev);
    }

    public void put(String key, String value) {
        if (key == null) return;

        int bucketIndex = getBucketIndex(key);
        FindResult result = findEntryAndPrevious(bucketIndex, key);
        HashEntry entryToUpdate = result.foundEntry;

        if (entryToUpdate != null) {
            entryToUpdate.value = value;
            return;
        }

        _count++;
        HashEntry newEntry = new HashEntry(key, value); // Usa main.java.ex1.ex3.HashEntry

        HashEntry head = table[bucketIndex]; // No es realmente necesario re-obtener 'head' aquí
        // 'result.previousEntry' es suficiente
        HashEntry lastNodeInBucket = result.previousEntry;

        if (table[bucketIndex] == null) { // Si el bucket estaba vacío (head era null)
            table[bucketIndex] = newEntry;
            _size++;
        } else {
            // 'lastNodeInBucket' es el último nodo si la clave no se encontró
            // y el bucket no estaba vacío.
            lastNodeInBucket.next = newEntry;
        }
    }

    public String get(String key) {
        if (key == null) return null;
        int bucketIndex = getBucketIndex(key);

        HashEntry entry = findEntryInBucket(bucketIndex, key);
        return (entry != null) ? entry.value : null;
    }

    public void drop(String key) {
        if (key == null) return;

        int bucketIndex = getBucketIndex(key);
        FindResult result = findEntryAndPrevious(bucketIndex, key);
        HashEntry entryToRemove = result.foundEntry;
        HashEntry previousNode = result.previousEntry;

        if (entryToRemove != null) {
            _count--;
            if (previousNode == null) {
                table[bucketIndex] = entryToRemove.next;
            } else {
                previousNode.next = entryToRemove.next;
            }

            if (table[bucketIndex] == null) {
                _size--;
            }
        }
    }

    public int count() { return _count; }
    public int size() { return _size; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                sb.append("[").append(i).append("] -> ");
                HashEntry current = table[i];
                while (current != null) {
                    sb.append("<").append(current.key).append(", ").append(current.value).append(">");
                    if (current.next != null) sb.append(" -> ");
                    current = current.next;
                }
                sb.append("\n");
            }
        }
        return sb.length() == 0 ? "[]" : sb.toString();
    }
}