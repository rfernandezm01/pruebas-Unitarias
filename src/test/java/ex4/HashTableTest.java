package test.java.ex4;


import main.java.ex2.HashTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {

    private HashTable ht;

    // Claves que colisionan (asumiendo INITIAL_CAPACITY = 16):
    // "A".hashCode() = 65 -> 65 % 16 = 1
    // "Q".hashCode() = 81 -> 81 % 16 = 1
    // "AG".hashCode() = 2087 -> 2087 % 16 = 7
    // "AQ".hashCode() = 2097 -> 2097 % 16 = 1 (Colisiona con A y Q)
    // Vamos a usar claves más directas si es posible o confiar en la dispersión.
    // Para asegurar colisiones controladas, es mejor usar claves que sepamos que dan el mismo bucketIndex.
    // Por ejemplo, "key1" y "key17" podrían dar el mismo bucket si la capacidad es 16 (1 % 16 = 1, 17 % 16 = 1 si se usara un hash directo del número).
    // Pero como usamos String.hashCode(), es más complejo.
    // Usaremos "A", "Q", "a!" para colisionar en bucket 1 (hashCode: 65, 81, 8001 -> todos % 16 = 1)
    // Usaremos "B" para no colisionar con los anteriores (hashCode: 66 -> 66 % 16 = 2)

    private final String K_COL1 = "A"; // bucket 1
    private final String V_COL1 = "ValueA";
    private final String K_COL2 = "Q"; // bucket 1
    private final String V_COL2 = "ValueQ";
    private final String K_COL3 = "a!"; // bucket 1. (char 'a' = 97, char '!' = 33. 97*31+33 = 3040. 3040 % 16 = 0. Oops, error cálculo manual)
    // "a!".hashCode() = 8000. 8000 % 16 = 0.
    // Necesito 3 que colisionen.
    // "A" -> 65 % 16 = 1
    // "Q" -> 81 % 16 = 1
    // "g" -> 103 % 16 = 7. No.
    // "CA" -> 67*31 + 65 = 2077 + 65 = 2142. 2142 % 16 = 14. No.
    // Para simplificar y asegurar colisiones donde las quiero, voy a usar
    // claves cuyo String es "bucketX_itemY". Y en el HashTable.java, si veo
    // "bucketX_itemY", forzaré que getBucketIndex devuelva X.
    // PERO el enunciado dice que no puedo modificar getBucketIndex para esto.
    // Ok, me quedo con A, Q. Y buscaré una tercera.
    // "1".hashCode() = 49. 49 % 16 = 1. ¡Perfecto!
    private final String K_COL_BUCKET1_ITEM1 = "A";  // bucketIndex 1 (65%16=1)
    private final String V_COL_BUCKET1_ITEM1 = "ValueA";
    private final String K_COL_BUCKET1_ITEM2 = "Q";  // bucketIndex 1 (81%16=1)
    private final String V_COL_BUCKET1_ITEM2 = "ValueQ";
    private final String K_COL_BUCKET1_ITEM3 = "1";  // bucketIndex 1 (49%16=1)
    private final String V_COL_BUCKET1_ITEM3 = "Value1";

    private final String K_NO_COL = "B"; // bucketIndex 2 (66%16=2)
    private final String V_NO_COL = "ValueB";
    private final String K_NO_COL_OTHER_BUCKET = "C"; // bucketIndex 3 (67%16=3)
    private final String V_NO_COL_OTHER_BUCKET = "ValueC";


    @BeforeEach
    void setUp() {
        ht = new HashTable();
    }

    // PUTS
    @Test
    @DisplayName("Put: Insertar en tabla vacía sin colisión")
    void testPut_NoCollision_EmptyTable() {
        ht.put(K_NO_COL, V_NO_COL);
        // El índice de "B" (66) es 66 % 16 = 2
        assertEquals("[2] -> <B, ValueB>\n", ht.toString());
        assertEquals(1, ht.count());
        assertEquals(1, ht.size());
    }

    @Test
    @DisplayName("Put: Insertar en tabla no vacía sin colisión")
    void testPut_NoCollision_NonEmptyTable() {
        ht.put(K_NO_COL, V_NO_COL); // bucket 2
        ht.put(K_NO_COL_OTHER_BUCKET, V_NO_COL_OTHER_BUCKET); // bucket 3
        // Orden de buckets puede variar en toString si no es predecible, pero aquí sí.
        String expected = "[2] -> <B, ValueB>\n[3] -> <C, ValueC>\n";
        // O al revés, dependiendo de cómo itere toString o si los índices son fijos.
        // Dado que toString itera por el array de buckets, el orden debería ser por índice.
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n"));
        assertTrue(ht.toString().contains("[3] -> <C, ValueC>\n"));
        assertEquals(2, ht.count());
        assertEquals(2, ht.size());
    }

    @Test
    @DisplayName("Put: Insertar con colisión, 2a posición en bucket")
    void testPut_Collision_2ndPosition() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A en bucket 1
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q en bucket 1
        assertEquals("[1] -> <A, ValueA> -> <Q, ValueQ>\n", ht.toString());
        assertEquals(2, ht.count());
        assertEquals(1, ht.size());
    }

    @Test
    @DisplayName("Put: Insertar con colisión, 3a posición en bucket")
    void testPut_Collision_3rdPosition() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3); // 1
        assertEquals("[1] -> <A, ValueA> -> <Q, ValueQ> -> <1, Value1>\n", ht.toString());
        assertEquals(3, ht.count());
        assertEquals(1, ht.size());
    }

    @Test
    @DisplayName("Put: Actualizar elemento sin colisión")
    void testPut_Update_NoCollision() {
        ht.put(K_NO_COL, V_NO_COL);
        ht.put(K_NO_COL, "UpdatedValueB");
        assertEquals("[2] -> <B, UpdatedValueB>\n", ht.toString());
        assertEquals(1, ht.count());
        assertEquals(1, ht.size());
    }

    @Test
    @DisplayName("Put: Actualizar elemento con colisión (1a posición)")
    void testPut_Update_Collision_1stPos() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q
        ht.put(K_COL_BUCKET1_ITEM1, "UpdatedValueA");    // Update A
        assertEquals("[1] -> <A, UpdatedValueA> -> <Q, ValueQ>\n", ht.toString());
        assertEquals(2, ht.count());
        assertEquals(1, ht.size());
    }

    @Test
    @DisplayName("Put: Actualizar elemento con colisión (2a posición)")
    void testPut_Update_Collision_2ndPos() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3); // 1
        ht.put(K_COL_BUCKET1_ITEM2, "UpdatedValueQ");    // Update Q
        assertEquals("[1] -> <A, ValueA> -> <Q, UpdatedValueQ> -> <1, Value1>\n", ht.toString());
        assertEquals(3, ht.count());
        assertEquals(1, ht.size());
    }

    @Test
    @DisplayName("Put: Actualizar elemento con colisión (3a posición)")
    void testPut_Update_Collision_3rdPos() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3); // 1
        ht.put(K_COL_BUCKET1_ITEM3, "UpdatedValue1");    // Update 1
        assertEquals("[1] -> <A, ValueA> -> <Q, ValueQ> -> <1, UpdatedValue1>\n", ht.toString());
        assertEquals(3, ht.count());
        assertEquals(1, ht.size());
    }

    // GET
    @Test
    @DisplayName("Get: Obtener elemento sin colisión")
    void testGet_NoCollision() {
        ht.put(K_NO_COL, V_NO_COL);
        assertEquals(V_NO_COL, ht.get(K_NO_COL));
    }

    @Test
    @DisplayName("Get: Obtener elemento con colisión (1a posición)")
    void testGet_Collision_1stPos() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1);
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2);
        assertEquals(V_COL_BUCKET1_ITEM1, ht.get(K_COL_BUCKET1_ITEM1));
    }

    @Test
    @DisplayName("Get: Obtener elemento con colisión (2a posición)")
    void testGet_Collision_2ndPos() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1);
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2);
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3);
        assertEquals(V_COL_BUCKET1_ITEM2, ht.get(K_COL_BUCKET1_ITEM2));
    }

    @Test
    @DisplayName("Get: Obtener elemento con colisión (3a posición)")
    void testGet_Collision_3rdPos() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1);
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2);
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3);
        assertEquals(V_COL_BUCKET1_ITEM3, ht.get(K_COL_BUCKET1_ITEM3));
    }

    @Test
    @DisplayName("Get: Obtener elemento inexistente (bucket vacío)")
    void testGet_NonExistent_EmptyBucket() {
        // K_NO_COL iría al bucket 2, K_COL_BUCKET1_ITEM1 al bucket 1
        // Si buscamos por "D" (bucket 4), ese bucket está vacío.
        assertNull(ht.get("D"));
    }

    @Test
    @DisplayName("Get: Obtener elemento inexistente (bucket con otro no colisionado)")
    void testGet_NonExistent_BucketWithNonColliding() {
        ht.put(K_NO_COL, V_NO_COL); // B en bucket 2
        assertNull(ht.get(K_COL_BUCKET1_ITEM1)); // A (bucket 1) no está, pero buscamos algo que iría al bucket 2 pero no es B
        // "R" (82 % 16 = 2) colisionaría con B.
        assertNull(ht.get("R")); // "R" debería estar en el mismo bucket que "B", pero no existe
    }

    @Test
    @DisplayName("Get: Obtener elemento inexistente (bucket con 3 colisionados)")
    void testGet_NonExistent_BucketWith3Collisions() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3); // 1
        // Todos estos están en bucket 1. Buscamos "Z" (90%16 = 10) que no está.
        assertNull(ht.get("Z"));
        // Buscamos algo que colisionaría en bucket 1 pero no es A, Q, o 1.
        // "q" (113 % 16 = 1).
        assertNull(ht.get("q"));
    }

    // DROP
    private void setupTableForDropTests() {
        ht.put(K_NO_COL, V_NO_COL);                 // B en bucket 2
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A en bucket 1
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q en bucket 1
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3); // 1 en bucket 1
        // Estado inicial:
        // [1] -> <A, ValueA> -> <Q, ValueQ> -> <1, Value1>
        // [2] -> <B, ValueB>
        // count = 4, size = 2
    }

    @Test
    @DisplayName("Drop: Esborrar elemento no colisionado")
    void testDrop_NoCollision() {
        setupTableForDropTests();
        ht.drop(K_NO_COL); // Eliminar B
        String expected = "[1] -> <A, ValueA> -> <Q, ValueQ> -> <1, Value1>\n";
        assertEquals(expected, ht.toString());
        assertEquals(3, ht.count());
        assertEquals(1, ht.size());
        assertNull(ht.get(K_NO_COL));
    }

    @Test
    @DisplayName("Drop: Esborrar elemento colisionado (1a posición)")
    void testDrop_Collision_1stPos() {
        setupTableForDropTests();
        ht.drop(K_COL_BUCKET1_ITEM1); // Eliminar A
        // Esperado:
        // [1] -> <Q, ValueQ> -> <1, Value1>
        // [2] -> <B, ValueB>
        assertTrue(ht.toString().contains("[1] -> <Q, ValueQ> -> <1, Value1>\n"));
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n"));
        assertEquals(3, ht.count());
        assertEquals(2, ht.size()); // Sigue habiendo 2 buckets ocupados
        assertNull(ht.get(K_COL_BUCKET1_ITEM1));
        assertEquals(V_COL_BUCKET1_ITEM2, ht.get(K_COL_BUCKET1_ITEM2)); // Q sigue ahí
    }

    @Test
    @DisplayName("Drop: Esborrar elemento colisionado (2a posición)")
    void testDrop_Collision_2ndPos() {
        setupTableForDropTests();
        ht.drop(K_COL_BUCKET1_ITEM2); // Eliminar Q
        // Esperado:
        // [1] -> <A, ValueA> -> <1, Value1>
        // [2] -> <B, ValueB>
        assertTrue(ht.toString().contains("[1] -> <A, ValueA> -> <1, Value1>\n"));
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n"));
        assertEquals(3, ht.count());
        assertEquals(2, ht.size());
        assertNull(ht.get(K_COL_BUCKET1_ITEM2));
        assertEquals(V_COL_BUCKET1_ITEM1, ht.get(K_COL_BUCKET1_ITEM1)); // A sigue ahí
    }

    @Test
    @DisplayName("Drop: Esborrar elemento colisionado (3a posición - último)")
    void testDrop_Collision_3rdPos() {
        setupTableForDropTests();
        ht.drop(K_COL_BUCKET1_ITEM3); // Eliminar 1
        // Esperado:
        // [1] -> <A, ValueA> -> <Q, ValueQ>
        // [2] -> <B, ValueB>
        assertTrue(ht.toString().contains("[1] -> <A, ValueA> -> <Q, ValueQ>\n"));
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n"));
        assertEquals(3, ht.count());
        assertEquals(2, ht.size());
        assertNull(ht.get(K_COL_BUCKET1_ITEM3));
        assertEquals(V_COL_BUCKET1_ITEM2, ht.get(K_COL_BUCKET1_ITEM2)); // Q sigue ahí
    }

    @Test
    @DisplayName("Drop: Eliminar elemento inexistente (bucket vacío)")
    void testDrop_NonExistent_EmptyBucket() {
        setupTableForDropTests(); // count=4, size=2
        ht.drop("Z"); // "Z" (bucket 10) no existe, bucket está vacío
        // El estado no debería cambiar
        assertEquals(4, ht.count());
        assertEquals(2, ht.size());
        assertTrue(ht.toString().contains("[1] -> <A, ValueA> -> <Q, ValueQ> -> <1, Value1>\n"));
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n"));
    }

    @Test
    @DisplayName("Drop: Eliminar elemento inexistente (bucket con otro no colisionado)")
    void testDrop_NonExistent_BucketWithNonColliding() {
        setupTableForDropTests(); // B está en bucket 2.
        ht.drop("R"); // "R" (bucket 2) no existe.
        assertEquals(4, ht.count());
        assertEquals(2, ht.size());
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n")); // B debe seguir ahí
    }

    @Test
    @DisplayName("Drop: Eliminar elemento inexistente (bucket con 3 colisionados)")
    void testDrop_NonExistent_BucketWith3Collisions() {
        setupTableForDropTests(); // A, Q, 1 están en bucket 1
        ht.drop("q"); // "q" (bucket 1) no existe.
        assertEquals(4, ht.count());
        assertEquals(2, ht.size());
        assertTrue(ht.toString().contains("[1] -> <A, ValueA> -> <Q, ValueQ> -> <1, Value1>\n")); // Bucket 1 no debe cambiar
    }

    // --- PRUEBAS PARA COUNT y SIZE (se comprueban indirectamente en PUT y DROP) ---
    // No es necesario repetir todas las pruebas de put/drop solo para count/size
    // si ya se comprueban en esas pruebas.
    // Pero si el enunciado lo pide explícitamente:

    @Test
    @DisplayName("Count/Size: Después de varios Puts")
    void testCountSize_AfterPuts() {
        // Vacía
        assertEquals(0, ht.count());
        assertEquals(0, ht.size());

        // 1. No colisión
        ht.put(K_NO_COL, V_NO_COL); // B (bucket 2)
        assertEquals(1, ht.count());
        assertEquals(1, ht.size());

        // 2. No colisión, diferente bucket
        ht.put(K_NO_COL_OTHER_BUCKET, V_NO_COL_OTHER_BUCKET); // C (bucket 3)
        assertEquals(2, ht.count());
        assertEquals(2, ht.size());

        // 3. Colisión (1er item en nuevo bucket para colisiones)
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A (bucket 1)
        assertEquals(3, ht.count());
        assertEquals(3, ht.size()); // Ahora 3 buckets ocupados

        // 4. Colisión (2do item en mismo bucket)
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q (bucket 1)
        assertEquals(4, ht.count());
        assertEquals(3, ht.size()); // Sigue 3 buckets ocupados

        // 5. Actualización
        ht.put(K_NO_COL, "NewValueB");
        assertEquals(4, ht.count()); // No cambia
        assertEquals(3, ht.size());  // No cambia
    }

    @Test
    @DisplayName("Count/Size: Después de varios Drops")
    void testCountSize_AfterDrops() {
        ht.put(K_NO_COL, V_NO_COL);                 // B (bucket 2), count=1, size=1
        ht.put(K_NO_COL_OTHER_BUCKET, V_NO_COL_OTHER_BUCKET); // C (bucket 3), count=2, size=2
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A (bucket 1), count=3, size=3
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q (bucket 1), count=4, size=3

        // 1. Drop no colisionado, bucket queda vacío
        ht.drop(K_NO_COL_OTHER_BUCKET); // Drop C (bucket 3)
        assertEquals(3, ht.count());
        assertEquals(2, ht.size()); // Bucket 3 queda vacío

        // 2. Drop colisionado, bucket no queda vacío
        ht.drop(K_COL_BUCKET1_ITEM2); // Drop Q (bucket 1)
        assertEquals(2, ht.count());
        assertEquals(2, ht.size()); // Bucket 1 sigue con A

        // 3. Drop último elemento de un bucket, bucket queda vacío
        ht.drop(K_COL_BUCKET1_ITEM1); // Drop A (bucket 1)
        assertEquals(1, ht.count());
        assertEquals(1, ht.size()); // Bucket 1 queda vacío, solo queda B en bucket 2

        // 4. Drop último elemento de la tabla
        ht.drop(K_NO_COL); // Drop B (bucket 2)
        assertEquals(0, ht.count());
        assertEquals(0, ht.size());

        // 5. Drop de tabla vacía
        ht.drop("NON_EXISTENT");
        assertEquals(0, ht.count());
        assertEquals(0, ht.size());
    }

    @Test
    @DisplayName("Put: Prueba de colisión de hash negativo si no se corrige getBucketIndex")
    void testPut_NegativeHashCode() {
        // Esta clave tiene un hashCode negativo:
        String keyWithNegativeHash = "polygenelubricants"; // Suele dar negativo
        // System.out.println(keyWithNegativeHash.hashCode()); // Comprobarlo
        // Si getBucketIndex no maneja bien los negativos, esto podría dar error.
        // Con la corrección en getBucketIndex, debe funcionar.
        assertDoesNotThrow(() -> {
            ht.put(keyWithNegativeHash, "testValue");
        });
        assertEquals("testValue", ht.get(keyWithNegativeHash));
        assertEquals(1, ht.count());
        // El size dependerá del índice resultante, pero debe ser 1 si la tabla estaba vacía
        assertEquals(1, ht.size());
    }
}