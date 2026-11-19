package com.inventario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del sistema de inventario.
 *
 * <p>Cada prueba sigue el patrón Arrange–Act–Assert (Preparar–Actuar–Verificar)
 * y recibe un inventario nuevo y vacío, por lo que las pruebas son
 * independientes entre sí y no dependen del orden de ejecución.</p>
 */
@DisplayName("Pruebas del sistema de inventario")
class InventarioServiceTest {

    private InventarioService inventario;

    @BeforeEach
    void prepararInventarioVacio() {
        inventario = new InventarioService();
    }

    // =====================================================================
    // PRUEBAS EXITOSAS
    // =====================================================================

    @Test
    @DisplayName("Registra correctamente un producto válido")
    void registrarProducto_productoValido_quedaRegistrado() {
        // Arrange
        Producto teclado = new Producto("P001", "Teclado", 1000, 5);

        // Act
        inventario.registrarProducto(teclado);

        // Assert
        List<Producto> productos = inventario.listarProductos();
        assertEquals(1, productos.size(), "El inventario debe contener exactamente un producto");
        assertSame(teclado, productos.get(0), "El producto registrado debe ser el mismo que se agregó");
    }

    @Test
    @DisplayName("Calcula el valor total de un producto: ₡1.000 × 5 = ₡5.000")
    void calcularValorTotal_precioYCantidad_devuelvePrecioPorCantidad() {
        // Arrange
        Producto teclado = new Producto("P001", "Teclado", 1000, 5);

        // Act
        long valorTotal = teclado.calcularValorTotal();

        // Assert
        assertEquals(5000, valorTotal, "El valor total debe ser precio × cantidad");
    }

    @Test
    @DisplayName("Calcula el valor total del inventario con varios productos")
    void calcularValorTotalInventario_variosProductos_devuelveSumaDeValores() {
        // Arrange
        inventario.registrarProducto(new Producto("P001", "Teclado", 1000, 5)); // ₡5.000
        inventario.registrarProducto(new Producto("P002", "Mouse", 2500, 2));   // ₡5.000

        // Act
        long total = inventario.calcularValorTotalInventario();

        // Assert
        assertEquals(10000, total,
                "El total debe ser la suma de precio × cantidad de cada producto");
    }

    @Test
    @DisplayName("El valor total de un inventario vacío es cero")
    void calcularValorTotalInventario_inventarioVacio_devuelveCero() {
        // Arrange: el inventario ya está vacío (ver @BeforeEach)

        // Act
        long total = inventario.calcularValorTotalInventario();

        // Assert
        assertEquals(0, total, "Un inventario sin productos debe valer cero");
    }

    @Test
    @DisplayName("Consulta un producto existente por su código")
    void buscarPorCodigo_productoExistente_devuelveProducto() {
        // Arrange
        inventario.registrarProducto(new Producto("P002", "Mouse", 2500, 2));

        // Act
        Optional<Producto> resultado = inventario.buscarPorCodigo("P002");

        // Assert
        assertTrue(resultado.isPresent(), "El producto P002 debe encontrarse");
        assertEquals("Mouse", resultado.get().getNombre());
    }

    @Test
    @DisplayName("Acepta productos con cantidad igual a cero")
    void calcularValorTotal_cantidadCero_devuelveCero() {
        // Arrange
        Producto cable = new Producto("P003", "Cable HDMI", 1500, 0);

        // Act
        inventario.registrarProducto(cable);
        long valorTotal = cable.calcularValorTotal();

        // Assert
        assertEquals(0, valorTotal, "Un producto sin existencias debe valer cero");
        assertTrue(inventario.buscarPorCodigo("P003").isPresent(),
                "Un producto sin existencias también debe poder registrarse");
    }

    // =====================================================================
    // PRUEBAS DE VALIDACIÓN
    // =====================================================================

    @Test
    @DisplayName("Rechaza un producto con precio negativo")
    void crearProducto_precioNegativo_lanzaExcepcion() {
        // Arrange + Act
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> new Producto("P004", "Monitor", -100, 3));

        // Assert
        assertEquals("El precio no puede ser negativo: -100", excepcion.getMessage());
    }

    @Test
    @DisplayName("Rechaza un producto con cantidad negativa")
    void crearProducto_cantidadNegativa_lanzaExcepcion() {
        // Arrange + Act
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> new Producto("P005", "Audífonos", 8000, -1));

        // Assert
        assertEquals("La cantidad no puede ser negativa: -1", excepcion.getMessage());
    }

    @Test
    @DisplayName("Consultar un producto inexistente devuelve un resultado vacío")
    void buscarPorCodigo_productoInexistente_devuelveVacio() {
        // Arrange
        inventario.registrarProducto(new Producto("P001", "Teclado", 1000, 5));

        // Act
        Optional<Producto> resultado = inventario.buscarPorCodigo("NO-EXISTE");

        // Assert
        assertTrue(resultado.isEmpty(), "No debe encontrarse un producto que no fue registrado");
    }

    @Test
    @DisplayName("Rechaza el registro de un producto con código duplicado")
    void registrarProducto_codigoDuplicado_lanzaExcepcion() {
        // Arrange
        inventario.registrarProducto(new Producto("P001", "Teclado", 1000, 5));
        Producto duplicado = new Producto("P001", "Teclado gamer", 3000, 1);

        // Act
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> inventario.registrarProducto(duplicado));

        // Assert
        assertEquals("Ya existe un producto con el código: P001", excepcion.getMessage());
        assertEquals(1, inventario.listarProductos().size(),
                "El producto duplicado no debe agregarse");
        assertEquals("Teclado", inventario.buscarPorCodigo("P001").get().getNombre(),
                "Debe conservarse el producto original");
    }
}
