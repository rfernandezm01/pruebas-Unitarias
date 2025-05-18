package main.java.ex1.ex3;

// Imports para JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// No es necesario importar main.java.ex1.ex3.HashTable porque la clase de prueba está en el mismo paquete.
// No es necesario importar main.java.ex1.ex3.HashEntry porque las pruebas interactúan con HashTable.

public class HashTableTest {
    // El contenido de esta clase de prueba es idéntico al de main.java.ex1.ex2/HashTableTest.java,
    // solo que aquí se instancia 'new main.java.ex1.ex3.HashTable()'.
    // Por brevedad, no lo repetiré todo. Asegúrate de copiar el cuerpo de main.java.ex1.ex2/HashTableTest.java aquí.
    // Ejemplo de una prueba:
    private HashTable ht;

    @BeforeEach
    void setUp() {
        ht = new HashTable(); // Crea instancia de main.java.ex1.ex3.HashTable
    }

    @Test
    @DisplayName("Put: Insertar en tabla vacía sin colisión")
    void testPut_NoCollision_EmptyTable() {
        ht.put("B", "ValueB");
        assertEquals("[2] -> <B, ValueB>\n", ht.toString());
        assertEquals(1, ht.count());
        assertEquals(1, ht.size());
    }
    // ... (resto de las pruebas)
}