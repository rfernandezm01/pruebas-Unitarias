package main.java.ex1.ex2;

// Esta clase ahora está en su propio archivo.
// La hacemos con visibilidad de paquete (sin 'public' explícito antes de 'class')
// o 'public' si prefieres. Visibilidad de paquete es suficiente si solo la usa HashTable en main.java.ex1.ex2.
// Por consistencia con main.java.ex1.ex3 y main.java.ex1.ex4, la haré pública.
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