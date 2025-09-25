package sistema;

import java.util.Date;

public class Arriendo {
	private Cliente cliente;
    private Equipo equipo;
    private Date fecha;

    public Arriendo(Cliente cliente, Equipo equipo) {
        this.cliente = cliente;
        this.equipo = equipo;
        this.fecha = new Date();
        equipo.setDisponible(false);
    }

    public Cliente getCliente() { return cliente; }
    public Equipo getEquipo() { return equipo; }
    public Date getFecha() { return fecha; }

    @Override
    public String toString() {
        return "Arriendo [cliente=" + cliente.getNombre() + ", equipo=" + equipo.getNombre() + ", fecha=" + fecha + "]";
    }
}
