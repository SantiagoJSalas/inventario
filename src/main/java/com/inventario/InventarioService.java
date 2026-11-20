package com.inventario;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Administra los productos del inventario.
 *
 * <p>Los productos se guardan en memoria, identificados por su código.
 * Se usa {@link LinkedHashMap} para conservar el orden de registro.</p>
 */
public class InventarioService {

    private final Map<String, Producto> productos = new LinkedHashMap<>();

    /**
     * Registra un producto nuevo en el inventario.
     *
     * @param producto producto a registrar (no nulo)
     * @throws IllegalArgumentException si el producto es nulo o si ya existe
     *                                  otro producto con el mismo código
     */
    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (productos.containsKey(producto.getCodigo())) {
            throw new IllegalArgumentException(
                    "Ya existe un producto con el código: " + producto.getCodigo());
        }
        productos.put(producto.getCodigo(), producto);
    }

    /**
     * Busca un producto por su código.
     *
     * @param codigo código del producto
     * @return un {@link Optional} con el producto, o vacío si no existe
     */
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(productos.get(codigo));
    }

    /**
     * Devuelve todos los productos registrados, en orden de registro.
     *
     * @return lista no modificable de productos
     */
    public List<Producto> listarProductos() {
        return List.copyOf(productos.values());
    }

    /**
     * Calcula el valor total del inventario: la suma del valor total
     * (precio × cantidad) de cada producto registrado.
     *
     * @return valor total del inventario en colones
     */
    public long calcularValorTotalInventario() {
        long total = 0;
        for (Producto producto : productos.values()) {
            total += producto.getPrecioUnitario();
        }
        return total;
    }
}
