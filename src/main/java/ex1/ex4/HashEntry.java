package main.java.ex1.ex4;

// No se requieren imports explícitos.

public class HashEntry<K, V> { // K para Clave, V para Valor
    K key;
    V value;
    HashEntry<K, V> next;

    public HashEntry(K key, V value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    // Getters y Setters son buena práctica, especialmente con genéricos
    public K getKey() { return key; }
    public V getValue() { return value; }
    public void setValue(V value) { this.value = value; }
    public HashEntry<K, V> getNext() { return next; }
    public void setNext(HashEntry<K, V> next) { this.next = next; }
}