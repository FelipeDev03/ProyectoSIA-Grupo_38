package sistema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Map;

public class Sistema {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Sucursal> sucursales = new ArrayList<>();

    // --- MÉTODOS DE GESTIÓN ---
    public void registrarCliente(String rut, String nombre) {
        this.clientes.add(new Cliente(rut, nombre));
    }
    
    public void registrarCliente(Cliente c) {
        this.clientes.add(c);
    }

    public void registrarSucursal(Sucursal s) {
        this.sucursales.add(s);
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    /**
     * Método público que orquesta la lectura de todos los archivos de datos.
     */
    public void cargarDatosDesdeArchivos() {
        try {
            cargarSucursales("data/sucursales.csv");
            cargarClientes("data/clientes.csv");
            cargarEquipos("data/equipos.csv");
            // arriendos.csv existe pero no implementamos la carga de arriendos.
            // Se añade aca si se llega a implementar
        } catch (IOException e) {
            System.err.println("Error crítico al leer los archivos de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Orquesta el guardado de todos los datos en los archivos CSV.
     */
    public void guardarDatosAlSalir() {
        try {
            guardarSucursales("data/sucursales.csv");
            guardarClientes("data/clientes.csv");
            guardarEquipos("data/equipos.csv");
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void guardarSucursales(String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("nombre\n"); // Escribir la cabecera
            for (Sucursal s : this.sucursales) {
                bw.write(s.getNombre() + "\n");
            }
        }
    }

    private void guardarClientes(String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("rut,nombre\n"); // Escribir la cabecera
            for (Cliente c : this.clientes) {
                bw.write(c.getRut() + "," + c.getNombre() + "\n");
            }
        }
    }

    private void guardarEquipos(String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("codigo,nombre,sucursal,stock\n"); // Escribir la cabecera
            // Iteramos por cada sucursal para acceder a su inventario
            for (Sucursal s : this.sucursales) {
                for (Map.Entry<Equipo, Integer> entry : s.getInventario().entrySet()) {
                    Equipo equipo = entry.getKey();
                    Integer stock = entry.getValue();
                    String linea = String.join(",", 
                        equipo.getCodigo(),
                        equipo.getNombre(),
                        s.getNombre(),
                        stock.toString()
                    );
                    bw.write(linea + "\n");
                }
            }
        }
    }
    
    /**
     * Lee el archivo sucursales.csv y las añade a la lista del sistema.
     */
    private void cargarSucursales(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Salta la primera línea (cabecera)
            while ((linea = br.readLine()) != null) {
                String nombreSucursal = linea.trim();
                this.registrarSucursal(new Sucursal(nombreSucursal));
            }
        }
    }

    /**
     * Lee el archivo clientes.csv y los añade a la lista del sistema.
     */
    private void cargarClientes(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Salta la cabecera
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    this.registrarCliente(datos[0].trim(), datos[1].trim());
                }
            }
        }
    }
    
    /**
     * Lee el archivo equipos.csv y los asigna a la sucursal correspondiente.
     */
    private void cargarEquipos(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Salta la cabecera
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 4) {
                    String codigo = datos[0].trim();
                    String nombreEquipo = datos[1].trim();
                    String nombreSucursal = datos[2].trim();
                    int stock = Integer.parseInt(datos[3].trim());

                    // Busca la sucursal por su nombre para añadirle el equipo
                    Sucursal sucursalAsociada = buscarSucursalPorNombre(nombreSucursal);
                    if (sucursalAsociada != null) {
                        sucursalAsociada.agregarEquipo(codigo, nombreEquipo, stock);
                    } else {
                        System.err.println("Advertencia: No se encontró la sucursal '" + nombreSucursal + "' para el equipo '" + nombreEquipo + "'.");
                    }
                }
            }
        }
    }
    
    /**
     * Método de ayuda para encontrar una sucursal en la lista por su nombre.
     */
    private Sucursal buscarSucursalPorNombre(String nombre) {
        for (Sucursal s : this.sucursales) {
            if (s.getNombre().equalsIgnoreCase(nombre)) {
                return s;
            }
        }
        return null;
    }
}
