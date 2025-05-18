package ex3;

// No se requieren imports explícitos.

public class HashEntry { // Hecha pública para claridad, podría ser de paquete.
    String key;
    String value;
    HashEntry next;

    public HashEntry(String key, String value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }
}