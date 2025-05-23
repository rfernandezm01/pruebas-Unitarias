package main.java.ex1.ex3;
// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.
// Further modified to incorporate functionalities from ex3.HashTable
// and pass tests from ex3.HashTableTest, while maintaining original structure.

import java.util.ArrayList;

/**
 * Implementació d'una taula de hash amb maneig de col·lisions.
 * Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
 */
public class HashTable {
    private int SIZE = 16;
    private int ITEMS = 0; // Número total de elementos
    private HashEntry[] entries = new HashEntry[SIZE];

    // Clase interna auxiliar inspirada en HashTable3.FindResult
    // No es estática para poder instanciarla directamente si es necesario, aunque privada.
    private class FindResult {
        HashEntry foundEntry;    // El nodo encontrado que coincide con la clave
        HashEntry previousEntry; // El nodo que precede a foundEntry en la lista del bucket
        // (o el último nodo si foundEntry es null y se está insertando)

        FindResult(HashEntry found, HashEntry prevInList) {
            this.foundEntry = found;
            this.previousEntry = prevInList;
        }
    }

    // Método auxiliar inspirado en HashTable3.findEntryAndPrevious
    // Busca una entrada por clave en un bucket específico y devuelve la entrada y su predecesora.
    private FindResult findEntryAndPrevious(int bucketIndex, String key) {
        HashEntry current = entries[bucketIndex];
        HashEntry prevInList = null;

        while (current != null) {
            if (current.key.equals(key)) {
                return new FindResult(current, prevInList);
            }
            prevInList = current;
            current = current.next;
        }
        // Si no se encuentra, foundEntry es null, y previousEntry es el último nodo del bucket (o null si el bucket está vacío)
        return new FindResult(null, prevInList);
    }


    /**
     * Devuelve el número total de elementos almacenados en la tabla hash.
     * @return El número total de pares clave-valor.
     */
    public int count(){
        return this.ITEMS;
    }

    /**
     * Devuelve el número de buckets que están actualmente ocupados.
     * Modificado para coincidir con el comportamiento esperado por los tests (ex3).
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
     * @param key La clau de l'element.
     * @param value El valor de l'element.
     */
    public void put(String key, String value) {
        if (key == null) { // Comportamiento de HashTable3
            return;
        }

        int hash = getHash(key);
        FindResult result = findEntryAndPrevious(hash, key);
        HashEntry entryToUpdate = result.foundEntry;

        if (entryToUpdate != null) {
            // La clave ya existe, actualizar el valor
            entryToUpdate.value = value;
            return;
        }

        // La clave no existe, añadir nueva entrada
        ITEMS++; // Incrementar contador de elementos
        final HashEntry newHashEntry = new HashEntry(key, value);

        if (entries[hash] == null) {
            // El bucket está vacío
            entries[hash] = newHashEntry;
            // newHashEntry.prev ya es null por defecto
        } else {
            // El bucket no está vacío, añadir al final de la lista
            // result.previousEntry es el último nodo en la lista de este bucket
            HashEntry lastNodeInBucket = result.previousEntry;
            lastNodeInBucket.next = newHashEntry;
            newHashEntry.prev = lastNodeInBucket; // Mantener el puntero prev
        }
    }

    /**
     * Permet recuperar un element dins la taula.
     * @param key La clau de l'element a trobar.
     * @return El propi element que es busca (null si no s'ha trobat).
     */
    public String get(String key) {
        if (key == null) { // Comportamiento de HashTable3
            return null;
        }
        int hash = getHash(key);

        // Usar lógica similar a findEntryAndPrevious, pero solo necesitamos el valor
        HashEntry current = entries[hash];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null; // Clave no encontrada
    }

    /**
     * Permet esborrar un element dins de la taula.
     * Si l'element s'esborra, es decrementa ITEMS.
     * @param key La clau de l'element a esborrar.
     */
    public void drop(String key) {
        if (key == null) { // Comportamiento de HashTable3
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
                entries[hash] = entryToRemove.next;
                if (entries[hash] != null) { // Si hay un nuevo primer elemento
                    entries[hash].prev = null; // Actualizar su puntero prev
                }
            } else {
                // El elemento a eliminar está en medio o al final de la lista del bucket
                previousNodeInList.next = entryToRemove.next;
                if (entryToRemove.next != null) { // Si hay un siguiente elemento
                    entryToRemove.next.prev = previousNodeInList; // Actualizar su puntero prev
                }
            }
            // El puntero entryToRemove.prev no necesita ser modificado ya que el nodo va a ser eliminado (GC)
            // y entryToRemove.next ya ha sido manejado.
        }
    }

    /**
     * Calcula l'índex del bucket per a una clau donada.
     * Modificat per gestionar correctament els valors de hashCode negatius.
     * @param key La clau.
     * @return L'índex del bucket (0 a SIZE-1).
     */
    private int getHash(String key) {
        // piggy backing on java string hashcode implementation.
        // y corrección para hashcodes negativos como en HashTable3.getBucketIndex
        int hashCode = key.hashCode();
        int index = hashCode % SIZE;
        if (index < 0) {
            index += SIZE;
        }
        return index;
    }

    private class HashEntry {
        String key;
        String value;

        // Linked list of same hash entries.
        HashEntry next;
        HashEntry prev; // Mantenemos prev para la estructura original

        public HashEntry(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            // El formato original es "[key, value]"
            // El método toString() de la tabla lo formateará como <key, value>
            return "[" + key + ", " + value + "]";
        }
    }

    /**
     * Genera una representació en cadena de la taula de hash.
     * El format s'ha ajustat per coincidir amb les sortides esperades en HashTableTest (ex3):
     * "[bucketIndex] -> <key1, value1> -> <key2, value2>\n"
     * Si la taula està buida, retorna "[]".
     */
    @Override
    public String toString() {
        StringBuilder hashTableStr = new StringBuilder();
        boolean firstBucket = true; // Para controlar el \n inicial si el primer bucket con datos no es el 0
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
                // Construir manualmente el formato <key, value> como esperan los tests
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

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     * @param key La clau que es farà servir per calcular col·lisions.
     * @return Una clau que, de fer-se servir, provoca col·lisió amb la que s'ha donat.
     */
    public String getCollisionsForKey(String key) {
        // Este método y el siguiente se mantienen tal cual, ya que no son el foco de la refactorización
        // basada en HashTable3 y sus tests.
        if (key == null) throw new IllegalArgumentException("Key for collision search cannot be null.");
        ArrayList<String> collisions = getCollisionsForKey(key, 1);
        if (collisions.isEmpty()) return null; // Podría no encontrar colisiones
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
        ArrayList<Integer> newKeyIndices = new ArrayList<>(); // Usaré newKeyIndices para evitar confusión con la variable 'key'
        ArrayList<String> foundKeys = new ArrayList<>();

        newKeyIndices.add(0); // Inicia con la representación de "0"
        int targetHash = getHash(key); // Hash de la clave original

        // El índice 'current' en el código original se refería a la posición del dígito a incrementar
        // en la lista de índices del alfabeto ('newKey' en el original, 'newKeyIndices' aquí).
        // La estrategia es generar claves numéricas ("0", "1", ..., "9", "00", "01", ...)
        int currentDigitToIncrement = newKeyIndices.size() - 1;


        while (foundKeys.size() < quantity){
            // Construir la clave actual a partir de los índices del alfabeto
            StringBuilder currentKeyBuilder = new StringBuilder();
            for(int indexInAlphabet : newKeyIndices) {
                currentKeyBuilder.append(alphabet[indexInAlphabet]);
            }
            String currentGeneratedKey = currentKeyBuilder.toString();

            if(!currentGeneratedKey.equals(key) && getHash(currentGeneratedKey) == targetHash) {
                foundKeys.add(currentGeneratedKey);
            }

            // Incrementar la clave actual (representada por newKeyIndices)
            // Esto es como un contador en base 'alphabet.length'
            newKeyIndices.set(currentDigitToIncrement, newKeyIndices.get(currentDigitToIncrement) + 1);

            // Manejar acarreo (overflow)
            if(newKeyIndices.get(currentDigitToIncrement) == alphabet.length) {
                int scannedDigit = currentDigitToIncrement;
                while (scannedDigit >= 0 && newKeyIndices.get(scannedDigit) == alphabet.length) {
                    newKeyIndices.set(scannedDigit, 0); // Resetear dígito actual
                    scannedDigit--; // Moverse al dígito anterior (a la izquierda)
                    if (scannedDigit >= 0) {
                        newKeyIndices.set(scannedDigit, newKeyIndices.get(scannedDigit) + 1); // Incrementar dígito anterior
                    }
                }

                // Si todos los dígitos se desbordaron (scannedDigit < 0), necesitamos añadir un nuevo dígito
                if (scannedDigit < 0) {
                    // La lógica original era newKey.add(0), lo que añadía un 0 al final.
                    // Ejemplo: "9" (índice [9]) -> overflow -> [0], scannedDigit=-1 -> newKeyIndices.add(0) -> [0,0] -> "00"
                    // Esto es consistente con la idea de probar claves más largas "0", "1", ..., "9", "00", "01"...
                    newKeyIndices.add(0); // Esto aumenta la longitud de la clave, ej. "9" -> "00"
                    // Pero en realidad, la lógica original añadía un nuevo '0' al *final* de la lista de índices.
                    // Y luego 'current' (mi currentDigitToIncrement) se reseteaba a newKey.size()-1.
                    // Vamos a mantenerlo lo más fiel al original:
                    // El for loop `for(int i = previous + 1; i < newKey.size(); i++) newKey.set(i, 0);`
                    // y el `if(previous < 0) newKey.add(0);`
                    // La variable 'previous' en el original es mi 'scannedDigit'.
                    // Si previous < 0, se hacía newKey.add(0). Esto hacía que [9] -> [0], luego [0,0].
                    // Si la longitud era 2, [9,9] -> [0,0], luego [0,0,0].
                    // Esta lógica ya está cubierta por el bucle while de acarreo y la condición if (scannedDigit < 0)
                    // si se añade el nuevo dígito al principio (o se reconstruye la lista con un 0 extra).
                    // La lógica original newKey.add(0) añade al final de la lista de arrays.
                    // Si newKeyIndices era [9], se vuelve [0]. Si previous (scannedDigit) < 0,
                    // newKeyIndices.add(0) lo haría [0,0]. Esto significa que después de "9", la siguiente clave a probar es "00".
                    // Esto está bien.
                }
                currentDigitToIncrement = newKeyIndices.size() - 1; // Siempre el dígito más a la derecha
            }
        }
        return foundKeys;
    }

    public static void main(String[] args) {
        HashTable hashTable = new HashTable();

        // Put some key values.
        log("Putting 30 items (0 to 29)");
        for(int i=0; i<30; i++) {
            final String key = String.valueOf(i);
            hashTable.put(key, key);
        }

        // Print the HashTable structure
        log("****   HashTable  ***");
        log(hashTable.toString());
        log("\nValue for key(20) : " + hashTable.get("20") );
        log("Value for key(15) : " + hashTable.get("15") );
        log("Value for key(35) (non-existent) : " + hashTable.get("35") );

        log("\nItems count: " + hashTable.count()); // Debería ser 30
        log("Buckets occupied (size): " + hashTable.size());

        log("\nDropping key(20)");
        hashTable.drop("20");
        log("Value for key(20) after drop: " + hashTable.get("20"));
        log("Items count after drop: " + hashTable.count()); // Debería ser 29
        log("HashTable after drop(20):");
        log(hashTable.toString());

        log("\nUpdating key(10) with value 'NewValueFor10'");
        hashTable.put("10", "NewValueFor10");
        log("Value for key(10) after update: " + hashTable.get("10"));
        log("Items count after update (should be same): " + hashTable.count()); // Debería ser 29
        log("HashTable after update(10):");
        log(hashTable.toString());

        // Prueba de poner una clave que colisiona con una existente, luego otra
        String keyA = "A"; // Supongamos que "A", "Q", "1" colisionan (bucket 1 en tests)
        String keyQ = "Q";
        String key1 = "1";
        log("\nPutting A, Q, 1 (expected to collide in bucket 1)");
        hashTable.put(keyA, "ValueA");
        hashTable.put(keyQ, "ValueQ");
        hashTable.put(key1, "Value1");
        log(hashTable.toString());
        log("Count: " + hashTable.count());
        log("Size: " + hashTable.size());

        log("\nDropping Q (middle of collision chain)");
        hashTable.drop(keyQ);
        log(hashTable.toString());
        log("Count: " + hashTable.count());
        log("Size: " + hashTable.size());
        log("Get A: " + hashTable.get(keyA));
        log("Get Q: " + hashTable.get(keyQ));
        log("Get 1: " + hashTable.get(key1));

        log("\nTesting getCollisionsForKey for key '0'");
        ArrayList<String> collisions = hashTable.getCollisionsForKey("0", 2);
        log("Collisions for '0': " + collisions);

        HashTable emptyTable = new HashTable();
        log("\nEmpty table toString: " + emptyTable.toString());
        log("Empty table count: " + emptyTable.count());
        log("Empty table size: " + emptyTable.size());

        log("\nTesting negative hash code key");
        String negHashKey = "polygenelubricants";
        hashTable.put(negHashKey, "NegativeTestValue");
        log("Value for negHashKey ('" + negHashKey + "'): " + hashTable.get(negHashKey));
        log(hashTable.toString());
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}