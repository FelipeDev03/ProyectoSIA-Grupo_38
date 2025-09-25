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

    public Map<Equipo, Integer> getInventario() { return inventario; }

    public void agregarEquipo(Equipo eq, int cantidad) {
        inventario.put(eq, inventario.getOrDefault(eq, 0) + cantidad);
    }

    // Sobrecarga
    public void agregarEquipo(Equipo eq) {
        agregarEquipo(eq, 5);
    }

    public Integer obtenerCantidad(Equipo eq) {
        return inventario.getOrDefault(eq, 0);
    }

    @Override
    public String toString() {
        return "Sucursal [nombre=" + nombre + ", cantidadEquipos=" + inventario.size() + "]";
    }
}
