package test.java.ex2;

import main.java.ex2.HashTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {

    private HashTable ht;

    private final String K_COL1 = "A"; // bucket 1
    private final String V_COL1 = "ValueA";
    private final String K_COL2 = "Q"; // bucket 1
    private final String V_COL2 = "ValueQ";
    private final String K_COL3 = "a!";

    private final String K_COL_BUCKET1_ITEM1 = "A";
    private final String V_COL_BUCKET1_ITEM1 = "ValueA";
    private final String K_COL_BUCKET1_ITEM2 = "Q";
    private final String V_COL_BUCKET1_ITEM2 = "ValueQ";
    private final String K_COL_BUCKET1_ITEM3 = "1";
    private final String V_COL_BUCKET1_ITEM3 = "Value1";

    private final String K_NO_COL = "B";
    private final String V_NO_COL = "ValueB";
    private final String K_NO_COL_OTHER_BUCKET = "C";
    private final String V_NO_COL_OTHER_BUCKET = "ValueC";


    @BeforeEach
    void setUp() {
        ht = new main.java.ex2.HashTable();
    }

    // PUTS
    @Test
    @DisplayName("Put: Insertar en tabla vacía sin colisión")
    void testPut_NoCollision_EmptyTable() {
        ht.put(K_NO_COL, V_NO_COL);

        assertEquals("[2] -> <B, ValueB>\n", ht.toString());
        assertEquals(1, ht.count());
        assertEquals(1, ht.size());
    }

    @Test
    @DisplayName("Put: Insertar en tabla no vacía sin colisión")
    void testPut_NoCollision_NonEmptyTable() {
        ht.put(K_NO_COL, V_NO_COL);
        ht.put(K_NO_COL_OTHER_BUCKET, V_NO_COL_OTHER_BUCKET);

        String expected = "[2] -> <B, ValueB>\n[3] -> <C, ValueC>\n";

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
        ht.put(K_NO_COL, V_NO_COL);
        assertNull(ht.get(K_COL_BUCKET1_ITEM1));

        assertNull(ht.get("R"));
    }

    @Test
    @DisplayName("Get: Obtener elemento inexistente (bucket con 3 colisionados)")
    void testGet_NonExistent_BucketWith3Collisions() {
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3); // 1

        assertNull(ht.get("Z"));

        assertNull(ht.get("q"));
    }

    // DROP
    private void setupTableForDropTests() {
        ht.put(K_NO_COL, V_NO_COL);                 // B en bucket 2
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A en bucket 1
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q en bucket 1
        ht.put(K_COL_BUCKET1_ITEM3, V_COL_BUCKET1_ITEM3); // 1 en bucket 1

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
        ht.drop(K_COL_BUCKET1_ITEM1);

        assertTrue(ht.toString().contains("[1] -> <Q, ValueQ> -> <1, Value1>\n"));
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n"));
        assertEquals(3, ht.count());
        assertEquals(2, ht.size());
        assertNull(ht.get(K_COL_BUCKET1_ITEM1));
        assertEquals(V_COL_BUCKET1_ITEM2, ht.get(K_COL_BUCKET1_ITEM2)); // Q sigue ahí
    }

    @Test
    @DisplayName("Drop: Esborrar elemento colisionado (2a posición)")
    void testDrop_Collision_2ndPos() {
        setupTableForDropTests();
        ht.drop(K_COL_BUCKET1_ITEM2);

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
        ht.drop(K_COL_BUCKET1_ITEM3);

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
        setupTableForDropTests();
        ht.drop("Z");

        assertEquals(4, ht.count());
        assertEquals(2, ht.size());
        assertTrue(ht.toString().contains("[1] -> <A, ValueA> -> <Q, ValueQ> -> <1, Value1>\n"));
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n"));
    }

    @Test
    @DisplayName("Drop: Eliminar elemento inexistente (bucket con otro no colisionado)")
    void testDrop_NonExistent_BucketWithNonColliding() {
        setupTableForDropTests();
        ht.drop("R");
        assertEquals(4, ht.count());
        assertEquals(2, ht.size());
        assertTrue(ht.toString().contains("[2] -> <B, ValueB>\n")); // B debe seguir ahí
    }

    @Test
    @DisplayName("Drop: Eliminar elemento inexistente (bucket con 3 colisionados)")
    void testDrop_NonExistent_BucketWith3Collisions() {
        setupTableForDropTests();
        ht.drop("q");
        assertEquals(4, ht.count());
        assertEquals(2, ht.size());
        assertTrue(ht.toString().contains("[1] -> <A, ValueA> -> <Q, ValueQ> -> <1, Value1>\n")); // Bucket 1 no debe cambiar
    }



    @Test
    @DisplayName("Count/Size: Después de varios Puts")
    void testCountSize_AfterPuts() {

        assertEquals(0, ht.count());
        assertEquals(0, ht.size());


        ht.put(K_NO_COL, V_NO_COL); // B (bucket 2)
        assertEquals(1, ht.count());
        assertEquals(1, ht.size());


        ht.put(K_NO_COL_OTHER_BUCKET, V_NO_COL_OTHER_BUCKET); // C (bucket 3)
        assertEquals(2, ht.count());
        assertEquals(2, ht.size());


        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1); // A (bucket 1)
        assertEquals(3, ht.count());
        assertEquals(3, ht.size()); // Ahora 3 buckets ocupados


        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2); // Q (bucket 1)
        assertEquals(4, ht.count());
        assertEquals(3, ht.size()); // Sigue 3 buckets ocupados

        ht.put(K_NO_COL, "NewValueB");
        assertEquals(4, ht.count()); // No cambia
        assertEquals(3, ht.size());  // No cambia
    }

    @Test
    @DisplayName("Count/Size: Después de varios Drops")
    void testCountSize_AfterDrops() {
        ht.put(K_NO_COL, V_NO_COL);
        ht.put(K_NO_COL_OTHER_BUCKET, V_NO_COL_OTHER_BUCKET);
        ht.put(K_COL_BUCKET1_ITEM1, V_COL_BUCKET1_ITEM1);
        ht.put(K_COL_BUCKET1_ITEM2, V_COL_BUCKET1_ITEM2);


        ht.drop(K_NO_COL_OTHER_BUCKET);
        assertEquals(3, ht.count());
        assertEquals(2, ht.size());


        ht.drop(K_COL_BUCKET1_ITEM2);
        assertEquals(2, ht.count());
        assertEquals(2, ht.size());


        ht.drop(K_COL_BUCKET1_ITEM1);
        assertEquals(1, ht.count());
        assertEquals(1, ht.size());


        ht.drop(K_NO_COL);
        assertEquals(0, ht.count());
        assertEquals(0, ht.size());


        ht.drop("NON_EXISTENT");
        assertEquals(0, ht.count());
        assertEquals(0, ht.size());
    }

    @Test
    @DisplayName("Put: Prueba de colisión de hash negativo si no se corrige getBucketIndex")
    void testPut_NegativeHashCode() {

        String keyWithNegativeHash = "polygenelubricants"; // Suele dar negativo

        assertDoesNotThrow(() -> {
            ht.put(keyWithNegativeHash, "testValue");
        });
        assertEquals("testValue", ht.get(keyWithNegativeHash));
        assertEquals(1, ht.count());

        assertEquals(1, ht.size());
    }
}