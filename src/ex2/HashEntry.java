package ex2;

// Esta clase ahora está en su propio archivo.
// La hacemos con visibilidad de paquete (sin 'public' explícito antes de 'class')
// o 'public' si prefieres. Visibilidad de paquete es suficiente si solo la usa HashTable en ex2.
// Por consistencia con ex3 y ex4, la haré pública.
public class HashEntry {
    String key;
    String value;
    HashEntry next;

    public HashEntry(String key, String value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }
}