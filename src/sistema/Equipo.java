package sistema;

import java.util.Objects;

public class Equipo {
    private String codigo;
    private String nombre;
    private boolean disponible = true;

    public Equipo(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Equipo [codigo=" + codigo + ", nombre=" + nombre + "]";
    }
    
    /**
     * Le dice a Java que dos objetos Equipo son iguales si su 'codigo' es el mismo.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return Objects.equals(codigo, equipo.codigo);
    }

    /**
     * Genera un identificador para el objeto basado en el 'codigo',
     * permitiendo que los Mapas (como el inventario) funcionen correctamente.
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
