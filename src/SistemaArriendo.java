import java.util.*;

public class SistemaArriendo {

    static class Cliente {
        private String rut;
        private String nombre;
        private List<Arriendo> arriendos;
        public Cliente(String rut, String nombre) {
            this.rut = rut;
            this.nombre = nombre;
        }

        public String getRut() { return rut; }
        public void setRut(String rut) { this.rut = rut; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public List<Arriendo> getArriendos() {
            return arriendos;
        }
        public void setArriendos(List<Arriendo> arriendos) {
            this.arriendos = arriendos;
        }
        public void agregarArriendo(Arriendo arriendo) {
            this.arriendos.add(arriendo);
        }

        @Override
        public String toString() {
            return "Cliente [id=" + rut + ", nombre=" + nombre + "]";
        }
        
    }

    static class Equipo {
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

    static class Sucursal {
        private String nombre;
        private Map<Equipo, Integer> inventario;;

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
        public Integer obtenerCantidad(Equipo eq){
        	return inventario.getOrDefault(eq, 0);
        }
        @Override
        public String toString() {
            return "Sucursal [nombre=" + nombre + ", cantidadEquipos=" + inventario.size() + "]";
        }
    }

    static class Arriendo {
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

    public static void main(String[] args) {
        return;
    }
}