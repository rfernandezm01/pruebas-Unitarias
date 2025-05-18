package main.java.ex1.ex4;

// No es necesario importar main.java.ex1.ex4.HashEntry porque está en el mismo paquete y es accesible.

public class HashTable<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private HashEntry<K, V>[] table;
    private int _count;
    private int _size;

    @SuppressWarnings("unchecked") // Necesario para la creación de array genérico
    public HashTable() {
        this.table = (HashEntry<K, V>[]) new HashEntry[INITIAL_CAPACITY];
        this._count = 0;
        this._size = 0;
    }

    private int getBucketIndex(K key) { // Clave de tipo K
        if (key == null) return 0; // Podría lanzar IllegalArgumentException
        int hashCode = key.hashCode(); // K debe tener un hashCode() válido
        int index = hashCode % table.length;
        return index < 0 ? index + table.length : index;
    }

    // Clase helper interna genérica
    private static class FindResult<K_FR, V_FR> {
        HashEntry<K_FR, V_FR> foundEntry;
        HashEntry<K_FR, V_FR> previousEntry;
        FindResult(HashEntry<K_FR, V_FR> found, HashEntry<K_FR, V_FR> prev) {
            this.foundEntry = found;
            this.previousEntry = prev;
        }
    }

    private HashEntry<K, V> findEntryInBucket(int bucketIndex, K key) {
        HashEntry<K, V> current = table[bucketIndex];
        while (current != null) {
            // Para comparar claves de tipo K, usamos .equals()
            // K debe tener un método equals() bien implementado.
            if (current.getKey().equals(key)) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

    private FindResult<K,V> findEntryAndPrevious(int bucketIndex, K key) {
        HashEntry<K, V> head = table[bucketIndex];
        HashEntry<K, V> current = head;
        HashEntry<K, V> prev = null;

        while (current != null) {
            if (current.getKey().equals(key)) { // Usar .equals() para comparar claves
                return new FindResult<>(current, prev);
            }
            prev = current;
            current = current.getNext();
        }
        return new FindResult<>(null, prev);
    }

    public void put(K key, V value) { // Métodos usan K y V
        if (key == null) {
            // System.err.println("La clave no puede ser nula.");
            return;
        }

        int bucketIndex = getBucketIndex(key);
        FindResult<K,V> result = findEntryAndPrevious(bucketIndex, key);
        HashEntry<K,V> entryToUpdate = result.foundEntry;

        if (entryToUpdate != null) {
            entryToUpdate.setValue(value); // Usar setter
            return;
        }

        _count++;
        HashEntry<K, V> newEntry = new HashEntry<>(key, value); // Crear HashEntry<K,V>

        HashEntry<K,V> lastNodeInBucket = result.previousEntry;

        if (table[bucketIndex] == null) { // Bucket estaba vacío
            table[bucketIndex] = newEntry;
            _size++;
        } else {
            lastNodeInBucket.setNext(newEntry); // Usar setter
        }
    }

    public V get(K key) { // Devuelve un valor de tipo V
        if (key == null) return null;
        int bucketIndex = getBucketIndex(key);

        HashEntry<K,V> entry = findEntryInBucket(bucketIndex, key);
        return (entry != null) ? entry.getValue() : null; // Usar getter
    }

    public void drop(K key) {
        if (key == null) return;

        int bucketIndex = getBucketIndex(key);
        FindResult<K,V> result = findEntryAndPrevious(bucketIndex, key);
        HashEntry<K,V> entryToRemove = result.foundEntry;
        HashEntry<K,V> previousNode = result.previousEntry;


        if (entryToRemove != null) {
            _count--;
            if (previousNode == null) {
                table[bucketIndex] = entryToRemove.getNext();
            } else {
                previousNode.setNext(entryToRemove.getNext()); // Usar setter
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
                HashEntry<K, V> current = table[i];
                while (current != null) {
                    sb.append("<").append(current.getKey()).append(", ").append(current.getValue()).append(">");
                    if (current.getNext() != null) {
                        sb.append(" -> ");
                    }
                    current = current.getNext();
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