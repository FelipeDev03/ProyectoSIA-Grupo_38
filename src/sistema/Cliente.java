package sistema;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
	private String rut;
    private String nombre;
    private List<Arriendo> arriendos;

    public Cliente(String rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
        this.arriendos = new ArrayList<>();
    }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Arriendo> getArriendos() { return new ArrayList<>(arriendos); }
    public void setArriendos(List<Arriendo> arriendos) { this.arriendos = arriendos; }

    public void agregarArriendo(Arriendo arriendo) {
        this.arriendos.add(arriendo);
    }

    // Sobrecarga
    public void agregarArriendo(Equipo equipo, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            this.arriendos.add(new Arriendo(this, equipo));
        }
    }

    @Override
    public String toString() {
        return "Cliente [rut=" + rut + ", nombre=" + nombre + "]";
    }
}
