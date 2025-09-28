package sistema;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Sistema {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Sucursal> sucursales = new ArrayList<>();

    // --- MÉTODOS DE GESTIÓN ---
    public void registrarCliente(String rut, String nombre) { this.clientes.add(new Cliente(rut, nombre)); }
    public void registrarCliente(Cliente c) { this.clientes.add(c); }
    public void registrarSucursal(Sucursal s) { this.sucursales.add(s); }
    public List<Cliente> getClientes() { return clientes; }
    public List<Sucursal> getSucursales() { return sucursales; }

    // --- LÓGICA DE CARGA Y GUARDADO ---

    public void cargarDatosDesdeArchivos() {
        try {
            cargarSucursales("data/sucursales.csv");
            cargarClientes("data/clientes.csv");
            cargarEquipos("data/equipos.csv");
            cargarArriendos("data/arriendos.csv");
        } catch (IOException e) {
            System.err.println("Error crítico al leer los archivos de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void guardarDatosAlSalir() {
        try {
            guardarSucursales("data/sucursales.csv");
            guardarClientes("data/clientes.csv");
            guardarEquipos("data/equipos.csv");
            guardarArriendos("data/arriendos.csv");
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --- MÉTODOS PRIVADOS DE CARGA ---
    private void cargarSucursales(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                this.registrarSucursal(new Sucursal(linea.trim()));
            }
        }
    }

    private void cargarClientes(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    this.registrarCliente(datos[0].trim(), datos[1].trim());
                }
            }
        }
    }
    
    private void cargarEquipos(String archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 4) {
                    String codigo = datos[0].trim();
                    String nombreEquipo = datos[1].trim();
                    String nombreSucursal = datos[2].trim();
                    int stock = Integer.parseInt(datos[3].trim());
                    
                    Sucursal sucursalAsociada = buscarSucursalPorNombre(nombreSucursal);
                    if (sucursalAsociada != null) {
                        sucursalAsociada.agregarEquipo(codigo, nombreEquipo, stock);
                    }
                }
            }
        }
    }

    private void cargarArriendos(String archivo) throws IOException {
        File f = new File(archivo);
        if (!f.exists()) return;
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 3) {
                    String rutCliente = datos[0].trim();
                    String codigoEquipo = datos[1].trim();
                    Date fecha = sdf.parse(datos[2].trim());
                    
                    Cliente cliente = buscarClientePorRut(rutCliente);
                    Equipo equipo = buscarEquipoPorCodigo(codigoEquipo);

                    if (cliente != null && equipo != null) {
                        Arriendo arriendo = new Arriendo(cliente, equipo);
                        arriendo.setFecha(fecha);
                        cliente.agregarArriendo(arriendo);
                    }
                }
            }
        } catch (ParseException e) {
            System.err.println("Error al parsear la fecha de un arriendo: " + e.getMessage());
        }
    }

    // --- MÉTODOS PRIVADOS DE GUARDADO ---
    private void guardarSucursales(String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("nombre\n");
            for (Sucursal s : this.sucursales) {
                bw.write(s.getNombre() + "\n");
            }
        }
    }

    private void guardarClientes(String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("rut,nombre\n");
            for (Cliente c : this.clientes) {
                bw.write(c.getRut() + "," + c.getNombre() + "\n");
            }
        }
    }
    
    private void guardarEquipos(String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("codigo,nombre,sucursal,stock\n");
            for (Sucursal s : this.sucursales) {
                for (Map.Entry<Equipo, Integer> entry : s.getInventario().entrySet()) {
                    Equipo equipo = entry.getKey();
                    String linea = String.join(",", equipo.getCodigo(), equipo.getNombre(), s.getNombre(), entry.getValue().toString());
                    bw.write(linea + "\n");
                }
            }
        }
    }

    private void guardarArriendos(String archivo) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("rut_cliente,codigo_equipo,fecha\n");
            for (Cliente cliente : this.clientes) {
                for (Arriendo arriendo : cliente.getArriendos()) {
                    String linea = String.join(",", cliente.getRut(), arriendo.getEquipo().getCodigo(), sdf.format(arriendo.getFecha()));
                    bw.write(linea + "\n");
                }
            }
        }
    }
    
    // --- MÉTODOS DE AYUDA ---
    private Sucursal buscarSucursalPorNombre(String nombre) {
        for (Sucursal s : this.sucursales) {
            if (s.getNombre().equalsIgnoreCase(nombre)) {
                return s;
            }
        }
        return null;
    }

    private Cliente buscarClientePorRut(String rut) {
        for (Cliente c : this.clientes) {
            if (c.getRut().equalsIgnoreCase(rut)) {
                return c;
            }
        }
        return null;
    }

    private Equipo buscarEquipoPorCodigo(String codigo) {
        for (Sucursal s : this.sucursales) {
            for (Equipo e : s.getInventario().keySet()) {
                if (e.getCodigo().equalsIgnoreCase(codigo)) {
                    return e;
                }
            }
        }
        return null;
    }
    /**
     * Busca un equipo por su nombre en todas las sucursales.
     * Como varios equipos pueden tener el mismo nombre, devuelve una lista.
     * @param nombre El nombre del equipo a buscar.
     * @return Una lista de equipos que coinciden con el nombre.
     */
    public List<Equipo> buscarEquipoPorNombre(String nombre) {
        List<Equipo> equiposEncontrados = new ArrayList<>();
        for (Sucursal s : this.sucursales) {
            for (Equipo e : s.getInventario().keySet()) {
                if (e.getNombre().equalsIgnoreCase(nombre)) {
                    equiposEncontrados.add(e);
                }
            }
        }
        return equiposEncontrados;
    }

    /**
     * Busca la sucursal a la que pertenece un equipo específico.
     * @param equipo El equipo cuya sucursal se quiere encontrar.
     * @return La sucursal que contiene el equipo, o null si no se encuentra.
     */
    public Sucursal buscarSucursalPorEquipo(Equipo equipo) {
        if (equipo == null) return null;
        for (Sucursal s : this.sucursales) {
            if (s.getInventario().containsKey(equipo)) {
                return s;
            }
        }
        return null;
    }
}