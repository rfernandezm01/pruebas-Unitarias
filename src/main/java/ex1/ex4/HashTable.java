package main.java.ex1.ex4;
// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.
// Further modified to incorporate functionalities from ex4.HashTable (generic version, tested with Strings)
// and pass tests from ex4.HashTableTest, while maintaining the original non-generic structure.

import java.util.ArrayList;

/**
 * Implementació d'una taula de hash amb maneig de col·lisions.
 * Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
 */
public class HashTable {
    private int SIZE = 16;
    private int ITEMS = 0; // Número total de elementos
    private HashEntry[] entries = new HashEntry[SIZE];

    // Clase interna auxiliar inspirada en ex4.HashTable.FindResult, pero no genérica
    // y utilizando la HashEntry interna de esta clase.
    private class FindResult {
        HashEntry foundEntry;    // El nodo encontrado que coincide con la clave
        HashEntry previousEntry; // El nodo que precede a foundEntry en la lista del bucket
        // (o el último nodo si foundEntry es null y se está insertando)

        FindResult(HashEntry found, HashEntry prevInList) {
            this.foundEntry = found;
            this.previousEntry = prevInList;
        }
    }

    // Método auxiliar inspirado en ex4.HashTable.findEntryAndPrevious
    // Busca una entrada por clave en un bucket específico y devuelve la entrada y su predecesora.
    // Adaptado para la HashEntry interna no genérica.
    private FindResult findEntryAndPrevious(int bucketIndex, String key) {
        HashEntry current = entries[bucketIndex];
        HashEntry prevInList = null;

        while (current != null) {
            // Acceso directo a los campos de la HashEntry interna
            if (current.key.equals(key)) {
                return new FindResult(current, prevInList);
            }
            prevInList = current;
            current = current.next; // Acceso directo
        }
        // Si no se encuentra, foundEntry es null, y previousEntry es el último nodo del bucket (o null si el bucket está vacío)
        return new FindResult(null, prevInList);
    }


    /**
     * Devuelve el número total de elementos almacenados en la tabla hash.
     * (Corresponde a _count en ex4.HashTable)
     * @return El número total de pares clave-valor.
     */
    public int count(){
        return this.ITEMS;
    }

    /**
     * Devuelve el número de buckets que están actualmente ocupados.
     * (Corresponde a _size en ex4.HashTable)
     * Modificado para coincidir con el comportamiento esperado por los tests (ex4).
     * @return El número de buckets ocupados.
     */
    public int size(){
        int occupiedBuckets = 0;
        for (int i = 0; i < SIZE; i++) {
            if (entries[i] != null) {
                occupiedBuckets++;
            }
        }
        return occupiedBuckets;
    }

    /**
     * Permet afegir un nou element a la taula o actualitzar un existent.
     * Si la clau ja existeix, el valor associat s'actualitza.
     * Si la clau no existeix, s'afegeix un nou parell clau-valor.
     * S'incrementa ITEMS només si s'afegeix un element nou.
     * Lógica adaptada de ex4.HashTable.put(K key, V value).
     * @param key La clau de l'element (String).
     * @param value El valor de l'element (String).
     */
    public void put(String key, String value) {
        if (key == null) { // Comportamiento de ex4.HashTable
            return;
        }

        int hash = getHash(key); // Usa el getHash de esta clase (que será modificado)
        FindResult result = findEntryAndPrevious(hash, key);
        HashEntry entryToUpdate = result.foundEntry;

        if (entryToUpdate != null) {
            // La clave ya existe, actualizar el valor (acceso directo)
            entryToUpdate.value = value;
            return;
        }

        // La clave no existe, añadir nueva entrada
        ITEMS++; // Incrementar contador de elementos
        final HashEntry newHashEntry = new HashEntry(key, value); // Usa la HashEntry interna

        HashEntry lastNodeInBucket = result.previousEntry;

        if (entries[hash] == null) {
            // El bucket está vacío
            entries[hash] = newHashEntry;
            // newHashEntry.prev ya es null por defecto en el constructor de HashEntry
        } else {
            // El bucket no está vacío, añadir al final de la lista
            // lastNodeInBucket es el último nodo en la lista de este bucket
            lastNodeInBucket.next = newHashEntry; // Acceso directo
            newHashEntry.prev = lastNodeInBucket; // Mantener el puntero prev (acceso directo)
        }
    }

    /**
     * Permet recuperar un element dins la taula.
     * Lógica adaptada de ex4.HashTable.get(K key).
     * @param key La clau de l'element a trobar (String).
     * @return El valor associat a la clau (String), o null si la clau no es troba.
     */
    public String get(String key) {
        if (key == null) { // Comportamiento de ex4.HashTable
            return null;
        }
        int hash = getHash(key);

        // Lógica similar a ex4.HashTable.findEntryInBucket, adaptada
        HashEntry current = entries[hash];
        while (current != null) {
            if (current.key.equals(key)) { // Acceso directo
                return current.value;      // Acceso directo
            }
            current = current.next;        // Acceso directo
        }
        return null; // Clave no encontrada
    }

    /**
     * Permet esborrar un element dins de la taula.
     * Si l'element s'esborra, es decrementa ITEMS.
     * Lógica adaptada de ex4.HashTable.drop(K key).
     * @param key La clau de l'element a esborrar (String).
     */
    public void drop(String key) {
        if (key == null) { // Comportamiento de ex4.HashTable
            return;
        }
        int hash = getHash(key);
        FindResult result = findEntryAndPrevious(hash, key);
        HashEntry entryToRemove = result.foundEntry;
        HashEntry previousNodeInList = result.previousEntry;

        if (entryToRemove != null) { // Solo si se encontró la entrada
            ITEMS--; // Decrementar contador de elementos

            if (previousNodeInList == null) {
                // El elemento a eliminar es el primero en el bucket
                entries[hash] = entryToRemove.next; // Acceso directo
                if (entries[hash] != null) { // Si hay un nuevo primer elemento
                    entries[hash].prev = null; // Actualizar su puntero prev (acceso directo)
                }
            } else {
                // El elemento a eliminar está en medio o al final de la lista del bucket
                previousNodeInList.next = entryToRemove.next; // Acceso directo
                if (entryToRemove.next != null) { // Si hay un siguiente elemento (acceso directo)
                    entryToRemove.next.prev = previousNodeInList; // Actualizar su puntero prev (acceso directo)
                }
            }
        }
    }

    /**
     * Calcula l'índex del bucket per a una clau donada.
     * Modificat per gestionar correctament els valors de hashCode negatius,
     * como en ex4.HashTable.getBucketIndex(K key).
     * @param key La clau (String).
     * @return L'índex del bucket (0 a SIZE-1).
     */
    private int getHash(String key) {
        // key == null es manejado en los métodos públicos (put, get, drop).
        // Aquí se asume que key no es null.
        // La lógica de ex4.HashTable.getBucketIndex para key == null (return 0) no se replica aquí
        // porque getHash es privado y los métodos públicos ya filtran el null.
        int hashCode = key.hashCode();
        int index = hashCode % SIZE;
        if (index < 0) {
            index += SIZE;
        }
        return index;
    }

    // La clase interna HashEntry original se mantiene sin cambios estructurales.
    // Sus campos (key, value, next, prev) son String y HashEntry respectivamente.
    private class HashEntry {
        String key;
        String value;

        // Linked list of same hash entries.
        HashEntry next;
        HashEntry prev;

        public HashEntry(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            // El formato original es "[key, value]"
            // El método toString() de la tabla (HashTable) lo formateará como <key, value>
            // para coincidir con los tests.
            return "[" + key + ", " + value + "]";
        }
    }

    /**
     * Genera una representació en cadena de la taula de hash.
     * El format s'ha ajustat per coincidir amb les sortides esperades en HashTableTest (ex4):
     * "[bucketIndex] -> <key1, value1> -> <key2, value2>\n"
     * Si la taula està buida, retorna "[]".
     */
    @Override
    public String toString() {
        StringBuilder hashTableStr = new StringBuilder();
        int occupiedBucketCount = 0;

        for (int bucket = 0; bucket < SIZE; bucket++) {
            HashEntry entry = entries[bucket];
            if (entry == null) {
                continue;
            }
            occupiedBucketCount++;

            hashTableStr.append("[").append(bucket).append("] -> ");

            HashEntry temp = entry;
            boolean firstInChain = true;
            while (temp != null) {
                if (!firstInChain) {
                    hashTableStr.append(" -> ");
                }
                // Construir manualmente el formato <key, value> como esperan los tests,
                // accediendo directamente a los campos de la HashEntry interna.
                hashTableStr.append("<").append(temp.key).append(", ").append(temp.value).append(">");
                temp = temp.next;
                firstInChain = false;
            }
            hashTableStr.append("\n");
        }

        if (occupiedBucketCount == 0) {
            return "[]"; // Formato esperado por tests para tabla vacía
        }
        return hashTableStr.toString();
    }

    // Los métodos getCollisionsForKey se mantienen como en la versión original,
    // ya que no son parte del scope de los tests de ex4.
    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     * @param key La clau que es farà servir per calcular col·lisions.
     * @return Una clau que, de fer-se servir, provoca col·lisió amb la que s'ha donat.
     */
    public String getCollisionsForKey(String key) {
        if (key == null) throw new IllegalArgumentException("Key for collision search cannot be null.");
        ArrayList<String> collisions = getCollisionsForKey(key, 1);
        if (collisions == null || collisions.isEmpty()) return null;
        return collisions.get(0);
    }

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     * @param key La clau que es farà servir per calcular col·lisions.
     * @param quantity La quantitat de col·lisions a calcular.
     * @return Un llistat de claus que, de fer-se servir, provoquen col·lisió.
     */
    public ArrayList<String> getCollisionsForKey(String key, int quantity){
        if (key == null) throw new IllegalArgumentException("Key for collision search cannot be null.");
        if (quantity <= 0) return new ArrayList<>();

        final char[] alphabet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        ArrayList<Integer> newKeyIndices = new ArrayList<>();
        ArrayList<String> foundKeys = new ArrayList<>();

        newKeyIndices.add(0);
        int targetHash = getHash(key); // Usa el getHash modificado de esta clase
        int currentDigitPos = newKeyIndices.size() -1; // Start with the rightmost digit

        while (foundKeys.size() < quantity){
            String currentGeneratedKey = "";
            for(int idx : newKeyIndices) {
                currentGeneratedKey += alphabet[idx];
            }

            if(!currentGeneratedKey.equals(key) && getHash(currentGeneratedKey) == targetHash) {
                foundKeys.add(currentGeneratedKey);
            }

            // Increment logic from original, adapted for newKeyIndices
            newKeyIndices.set(currentDigitPos, newKeyIndices.get(currentDigitPos) + 1);

            if(newKeyIndices.get(currentDigitPos) == alphabet.length) {
                int scanPos = currentDigitPos;
                while(scanPos >= 0 && newKeyIndices.get(scanPos) == alphabet.length) {
                    newKeyIndices.set(scanPos, 0); // Reset current digit
                    scanPos--;                     // Move to the previous digit (left)
                    if(scanPos >= 0) {
                        newKeyIndices.set(scanPos, newKeyIndices.get(scanPos) + 1); // Increment previous
                    }
                }
                if(scanPos < 0) { // All digits overflowed, need to add a new digit
                    // Original logic: newKey.add(0); which adds '0' index to the end of list,
                    // effectively making "9" -> "00", "99" -> "000".
                    // To achieve this, add a 0 (index) for the new leading digit, and set it.
                    // Example: [9] -> [0], then add 0 -> [0,0]
                    // The list newKeyIndices should become [0,0...0] of length+1.
                    // The original code's for-loop reset digits *after* 'previous' (my scanPos)
                    // and `if(previous < 0) newKey.add(0);`
                    // This means, if [9,9] (current=1) became [9,0] (after inner loop), then [0,0] (after inner loop)
                    // then previous = -1. newKey.add(0) => [0,0,0]. current = 2.
                    // My current implementation of carry should lead to the correct state.
                    // If scanPos < 0, it means all existing digits are now 0.
                    // We need to prepend a '1' (or rather, a '0' index that represents the start of the new length)
                    // The original newKey.add(0) effectively increased length and used the last digit.
                    // For "9" (size 1) -> "00" (size 2). newKeyIndices: [9] -> [0]. previous = -1. newKey.add(0) => [0,0]. current = 1.
                    // My logic: [9] -> [0]. scanPos=-1. This part is correct.
                    // The new "currentDigitPos" will be newKeyIndices.size() -1.
                    // If all overflow, a new digit is effectively added *because the number of digits increases*.
                    // Example: "9" -> "0", then new key is "00".
                    // The original code newKey.add(0) makes sense if it adds to the end for [0,0] structure.
                    // Let's stick to the original logic structure for adding a digit:
                    newKeyIndices.add(0); // Effectively adds a new digit '0' at the end of the number representation
                    // e.g., if "9" became "0", it is now "00" (indices [0,0])
                }
                currentDigitPos = newKeyIndices.size() -1; // Always operate on the rightmost digit for next increment
            }
        }
        return foundKeys;
    }

    public static void main(String[] args) {
        HashTable hashTable = new HashTable();

        log("Putting 30 items (0 to 29)");
        for(int i=0; i<30; i++) {
            final String key = String.valueOf(i);
            hashTable.put(key, key);
        }

        log("****   HashTable  ***");
        log(hashTable.toString());
        log("\nValue for key(20) : " + hashTable.get("20") );
        log("Items count: " + hashTable.count());
        log("Buckets occupied (size): " + hashTable.size());

        log("\nDropping key(15)");
        hashTable.drop("15");
        log("Value for key(15) after drop: " + hashTable.get("15"));
        log(hashTable.toString());
        log("Items count: " + hashTable.count());
        log("Buckets occupied (size): " + hashTable.size());

        log("\nUpdating key(10) with 'NewValueFor10'");
        hashTable.put("10", "NewValueFor10");
        log("Value for key(10) after update: " + hashTable.get("10"));
        log(hashTable.toString());

        log("\nTesting some specific keys based on ex4 tests (A, Q, 1 for bucket 1)");
        hashTable.put("A", "ValueA");
        hashTable.put("Q", "ValueQ");
        hashTable.put("1", "Value1");
        log(hashTable.toString());
        log("Count: " + hashTable.count() + ", Size: " + hashTable.size());

        log("\nGet A: " + hashTable.get("A"));
        log("Get Q: " + hashTable.get("Q"));
        log("Get 1: " + hashTable.get("1"));

        log("\nDrop Q");
        hashTable.drop("Q");
        log(hashTable.toString());
        log("Get Q after drop: " + hashTable.get("Q"));
        log("Count: " + hashTable.count() + ", Size: " + hashTable.size());

        log("\nTesting getCollisionsForKey for key '7'");
        // Ensure key exists for a more meaningful collision test, though not strictly necessary
        if (hashTable.get("7") == null) hashTable.put("7","7");
        ArrayList<String> collisions = hashTable.getCollisionsForKey("7", 2);
        log("Collisions for '7': " + collisions);

        log("\nTesting empty table:");
        HashTable emptyTable = new HashTable();
        log("Empty table toString: " + emptyTable.toString());
        log("Empty table count: " + emptyTable.count());
        log("Empty table size: " + emptyTable.size());

        log("\nTesting key that might give negative hash before modulo fix:");
        String negHashKey = "polygenelubricants"; // Common example
        hashTable.put(negHashKey, "WorksWithNegHash");
        log("Value for '" + negHashKey + "': " + hashTable.get(negHashKey));
        log(hashTable.toString());
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}