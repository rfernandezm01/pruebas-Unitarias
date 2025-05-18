package main.java.ex2; // DECLARACIÓ DE PACKAGE CORRECTA

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