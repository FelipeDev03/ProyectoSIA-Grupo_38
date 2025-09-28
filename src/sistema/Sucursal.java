package sistema;

import java.util.HashMap;
import java.util.Map;

public class Sucursal {
	private String nombre;
    private Map<Equipo, Integer> inventario;

    public Sucursal(String nombre) {
        this.nombre = nombre;
        this.inventario = new HashMap<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Map<Equipo, Integer> getInventario() { return new HashMap<>(inventario); }

    public void agregarEquipo(Equipo eq, int cantidad) {
        inventario.put(eq, inventario.getOrDefault(eq, 0) + cantidad);
    }

    /**
     * Elimina un equipo y todo su stock del inventario.
     * @param equipo El equipo a eliminar.
     */
    public void eliminarEquipo(Equipo equipo) {
        if (equipo != null) {
            this.inventario.remove(equipo);
        }
    }

    /**
     * Modifica el stock de un equipo existente a una nueva cantidad.
     * @param equipo El equipo a modificar.
     * @param nuevoStock La nueva cantidad de stock (no la diferencia).
     */
    public void modificarStockEquipo(Equipo equipo, int nuevoStock) {
        if (equipo != null && nuevoStock >= 0) {
            this.inventario.put(equipo, nuevoStock);
        }
    }
    
    // Sobrecarga
    public void agregarEquipo(Equipo eq) {
        agregarEquipo(eq, 1); // stock 1 por defecto
    }

    public void agregarEquipo(String codigo, String nombre, int cantidad) {
        Equipo eq = new Equipo(codigo, nombre);
        agregarEquipo(eq, cantidad);
    }

    public void agregarEquipo(String codigo, String nombre) {
        agregarEquipo(new Equipo(codigo, nombre), 1); // stock 1 por defecto
    }
    
    public Integer obtenerCantidad(Equipo eq) {
        return inventario.getOrDefault(eq, 0);
    }

    // Para reducir stock usar cantidad en negativo (y viceversa)
    public void modificarStock(Equipo eq, int cantidad) {
        int actual = inventario.getOrDefault(eq, 0);
        int nuevo = actual + cantidad;

        if (nuevo < 0) {
            throw new IllegalArgumentException("No hay stock suficiente para esa operación");
        }

        inventario.put(eq, nuevo);
    }
    
    @Override
    public String toString() {
        return "Sucursal [nombre=" + nombre + ", cantidadEquipos=" + inventario.size() + "]";
    }
}
