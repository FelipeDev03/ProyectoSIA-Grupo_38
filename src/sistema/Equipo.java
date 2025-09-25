package sistema;

public class Equipo {
	private String codigo;
    private String nombre;
    private boolean disponible;

    public Equipo(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.disponible = true;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return "Equipo [codigo=" + codigo + ", nombre=" + nombre + ", disponible=" + disponible + "]";
    }
}
