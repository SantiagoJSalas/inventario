package com.inventario;

/**
 * Representa un producto del inventario.
 *
 * <p>La clase es inmutable: sus datos no cambian después de crearla.
 * Todas las validaciones se hacen en el constructor, por lo que no puede
 * existir un producto con datos inválidos.</p>
 *
 * <p>El precio se maneja en colones enteros ({@code long}) para evitar
 * los errores de redondeo que produce {@code double} con valores monetarios.</p>
 */
public final class Producto {

    private final String codigo;
    private final String nombre;
    private final long precioUnitario;
    private final int cantidad;

    /**
     * Crea un producto validando sus datos.
     *
     * @param codigo         identificador único del producto (no vacío)
     * @param nombre         nombre del producto (no vacío)
     * @param precioUnitario precio de una unidad en colones (no negativo)
     * @param cantidad       unidades disponibles (no negativa)
     * @throws IllegalArgumentException si algún dato es inválido
     */
    public Producto(String codigo, String nombre, long precioUnitario, int cantidad) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo: " + precioUnitario);
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa: " + cantidad);
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public long getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    /**
     * Calcula el valor total del producto en inventario.
     *
     * @return precio unitario multiplicado por la cantidad
     */
    public long calcularValorTotal() {
        return precioUnitario * cantidad;
    }

    @Override
    public String toString() {
        return "Producto{codigo='" + codigo + "', nombre='" + nombre
                + "', precioUnitario=" + precioUnitario + ", cantidad=" + cantidad + "}";
    }
}
