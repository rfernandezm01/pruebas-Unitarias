package main.java.ex3;



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