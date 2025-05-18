package main.java.ex1.ex4;

// Imports para JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Import para Objects.hash usado en MyObject.hashCode()
import java.util.Objects;

// No es necesario importar main.java.ex1.ex4.HashTable ni main.java.ex1.ex4.HashEntry porque las pruebas están en el mismo paquete
// y MyObject está definida como clase interna de prueba o en el mismo archivo.

class MyObject { // Clase para probar claves/valores personalizados
    int id;
    String data;

    public MyObject(int id, String data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public String toString() { return "MyObj(" + id + ")"; } // Simplificado para pruebas

    // IMPORTANTE: Para que funcione como clave en HashTable, necesita equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyObject myObject = (MyObject) o;
        return id == myObject.id; // Comparación basada solo en id para este ejemplo
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Basar el hashCode en el id
    }
}

public class HashTableTest {

    private HashTable<String, String> htStringString;
    private HashTable<Integer, String> htIntString;
    private HashTable<String, Integer> htStringInt;
    private HashTable<Integer, MyObject> htIntMyObject;
    private HashTable<MyObject, String> htMyObjectString;

    private final String K_S1 = "A", V_S1 = "ValA";
    private final String K_S2 = "Q", V_S2 = "ValQ";
    private final String K_S3 = "1", V_S3 = "Val1";
    private final String K_S_NO_COL = "B", V_S_NO_COL = "ValB";

    @BeforeEach
    void setUp() {
        htStringString = new HashTable<>();
        htIntString = new HashTable<>();
        htStringInt = new HashTable<>();
        htIntMyObject = new HashTable<>();
        htMyObjectString = new HashTable<>();
    }

    // --- PRUEBAS <String, String> (adaptadas) ---
    @Test
    @DisplayName("Put <String,String>: No colisión, tabla vacía")
    void testPut_NoCollision_Empty_SS() {
        htStringString.put(K_S_NO_COL, V_S_NO_COL);
        assertEquals("[2] -> <B, ValB>\n", htStringString.toString());
        assertEquals(1, htStringString.count());
    }

    // ... (El resto de las pruebas de main.java.ex1.ex4/HashTableTest.java como en la respuesta anterior)
    // ... (No se necesitan más imports para el resto de los tests de este archivo)

    @Test
    @DisplayName("Update <MyObject, String>")
    void testUpdate_MyObjKey() {
        MyObject keyOriginal = new MyObject(1, "OriginalData");
        htMyObjectString.put(keyOriginal, "Valor Inicial");

        MyObject keyParaUpdate = new MyObject(1, "NuevosDatosPeroMismoId");
        htMyObjectString.put(keyParaUpdate, "Valor Actualizado");

        assertEquals("Valor Actualizado", htMyObjectString.get(keyOriginal));
        assertEquals(1, htMyObjectString.count());
    }
}