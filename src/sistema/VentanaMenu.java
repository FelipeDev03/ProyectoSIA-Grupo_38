package sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VentanaMenu {

    private JFrame frame;
    private JTextArea areaTexto;
    private Sistema sistema;

    public VentanaMenu(Sistema sistema) {
        this.sistema = sistema;
        initialize();
        this.frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Sistema de Arriendo de Equipos");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sistema.guardarDatosAlSalir();
                System.exit(0);
            }
        });

        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 5, 5));
        frame.getContentPane().add(panelBotones, BorderLayout.WEST);

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JLabel lblTitulo = new JLabel("Menú de Opciones");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        panelBotones.add(lblTitulo);

        JButton btnMostrarSucursales = new JButton("Mostrar Sucursales");
        btnMostrarSucursales.addActionListener(e -> mostrarSucursales());
        panelBotones.add(btnMostrarSucursales);
        
        JButton btnVerClientes = new JButton("Ver Clientes y Arriendos");
        btnVerClientes.addActionListener(e -> verClientesYArriendos());
        panelBotones.add(btnVerClientes);

        JButton btnAgregarCliente = new JButton("Agregar Cliente");
        btnAgregarCliente.addActionListener(e -> agregarCliente());
        panelBotones.add(btnAgregarCliente);

        JButton btnAgregarSucursal = new JButton("Agregar Sucursal");
        btnAgregarSucursal.addActionListener(e -> agregarSucursal());
        panelBotones.add(btnAgregarSucursal);

        JButton btnAgregarEquipo = new JButton("Agregar Equipo a Sucursal");
        btnAgregarEquipo.addActionListener(e -> agregarEquipoASucursal());
        panelBotones.add(btnAgregarEquipo);

        JButton btnArrendar = new JButton("Arrendar Equipo");
        btnArrendar.addActionListener(e -> arrendarEquipo());
        panelBotones.add(btnArrendar);
        
        JButton btnGestionarInventario = new JButton("Gestionar Inventario");
        btnGestionarInventario.addActionListener(e -> gestionarInventario());
        panelBotones.add(btnGestionarInventario);

        JButton btnGestionarArriendos = new JButton("Gestionar Arriendos");
        btnGestionarArriendos.addActionListener(e -> gestionarArriendos());
        panelBotones.add(btnGestionarArriendos);
        
        JButton btnDevolverEquipo = new JButton("Devolver Equipo");
        btnDevolverEquipo.addActionListener(e -> devolverEquipo());
        panelBotones.add(btnDevolverEquipo);

        JButton btnReportes = new JButton("Generar Reportes");
        btnReportes.addActionListener(e -> abrirVentanaReportes());
        panelBotones.add(btnReportes);
        
        JButton btnSalir = new JButton("Guardar y Salir");
        btnSalir.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        panelBotones.add(btnSalir);
    }

    // --- MÉTODOS EXISTENTES  ---
    
    private void mostrarSucursales() {
        areaTexto.setText("");
        StringBuilder sb = new StringBuilder("--- LISTADO DE SUCURSALES Y SU INVENTARIO ---\n\n");
        List<Sucursal> sucursales = sistema.getSucursales();

        if (sucursales.isEmpty()) {
            sb.append("No hay sucursales registradas.");
        } else {
            for (Sucursal s : sucursales) {
                sb.append("Sucursal: ").append(s.getNombre()).append("\n");
                if (s.getInventario().isEmpty()) {
                    sb.append("  (Sin equipos en inventario)\n");
                } else {
                    for (Map.Entry<Equipo, Integer> entry : s.getInventario().entrySet()) {
                        sb.append(String.format("  - [%s] %s: %d unidades\n", 
                                  entry.getKey().getCodigo(), entry.getKey().getNombre(), entry.getValue()));
                    }
                }
                sb.append("\n");
            }
        }
        areaTexto.setText(sb.toString());
    }
    
    private void agregarSucursal() {
        String nombre = JOptionPane.showInputDialog(frame, "Ingrese nombre de la nueva sucursal:", "Agregar Sucursal", JOptionPane.PLAIN_MESSAGE);
        if (nombre != null && !nombre.trim().isEmpty()) {
            sistema.registrarSucursal(new Sucursal(nombre));
            JOptionPane.showMessageDialog(frame, "Sucursal '" + nombre + "' agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mostrarSucursales();
        }
    }
    
    private void agregarEquipoASucursal() {
        Sucursal sucSeleccionada = seleccionarSucursal();
        if (sucSeleccionada == null) return;

        String codigoEq = JOptionPane.showInputDialog(frame, "Código del nuevo equipo:", "Agregar Equipo", JOptionPane.PLAIN_MESSAGE);
        if (codigoEq == null || codigoEq.trim().isEmpty()) return;
        
        String nombreEq = JOptionPane.showInputDialog(frame, "Nombre del nuevo equipo:", "Agregar Equipo", JOptionPane.PLAIN_MESSAGE);
        if (nombreEq == null || nombreEq.trim().isEmpty()) return;

        String cantStr = JOptionPane.showInputDialog(frame, "Cantidad:", "Agregar Equipo", JOptionPane.PLAIN_MESSAGE);
        if (cantStr == null || cantStr.trim().isEmpty()) return;

        try {
            int cantidad = Integer.parseInt(cantStr);
            sucSeleccionada.agregarEquipo(codigoEq, nombreEq, cantidad);
            JOptionPane.showMessageDialog(frame, "Equipo agregado a la sucursal " + sucSeleccionada.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mostrarSucursales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void arrendarEquipo() {
        Cliente cliente = seleccionarCliente();
        if (cliente == null) return;
        Sucursal sucursal = seleccionarSucursal();
        if (sucursal == null) return;
        Equipo equipo = seleccionarEquipo(sucursal);
        if (equipo == null) return;
        
        int disponible = sucursal.getInventario().getOrDefault(equipo, 0);
        String cantStr = JOptionPane.showInputDialog(frame, "Equipo: " + equipo.getNombre() + "\nStock disponible: " + disponible + "\n\nIngrese cantidad a arrendar:", "Arrendar Equipo", JOptionPane.PLAIN_MESSAGE);
        if (cantStr == null) return;

        try {
            int cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0 || cantidad > disponible) {
                JOptionPane.showMessageDialog(frame, "Cantidad no válida o sin stock suficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cliente.agregarArriendo(equipo, cantidad);
            sucursal.modificarStock(equipo, -cantidad);
            String resumen = String.format("--- ARRIENDO REGISTRADO ---\nCliente: %s\nEquipo: %s\nCantidad: %d\nSucursal: %s",
                                           cliente.getNombre(), equipo.getNombre(), cantidad, sucursal.getNombre());
            areaTexto.setText(resumen);
            JOptionPane.showMessageDialog(frame, "Arriendo registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarCliente() {
        String rut = JOptionPane.showInputDialog(frame, "RUT del nuevo cliente:", "Agregar Cliente", JOptionPane.PLAIN_MESSAGE);
        if (rut == null || rut.trim().isEmpty()) return;
        
        String nombre = JOptionPane.showInputDialog(frame, "Nombre del nuevo cliente:", "Agregar Cliente", JOptionPane.PLAIN_MESSAGE);
        if (nombre == null || nombre.trim().isEmpty()) return;

        sistema.registrarCliente(rut, nombre);
        JOptionPane.showMessageDialog(frame, "Cliente '" + nombre + "' agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        verClientesYArriendos();
    }
    
    private void verClientesYArriendos() {
        areaTexto.setText("");
        StringBuilder sb = new StringBuilder("--- LISTADO DE CLIENTES Y SUS ARRIENDOS ---\n\n");
        List<Cliente> clientes = sistema.getClientes();

        if (clientes.isEmpty()) {
            sb.append("No hay clientes registrados.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Cliente c : clientes) {
                sb.append("Cliente: ").append(c.getNombre()).append(" (RUT: ").append(c.getRut()).append(")\n");
                if (c.getArriendos().isEmpty()) {
                    sb.append("  (Sin arriendos registrados)\n");
                } else {
                    for (Arriendo a : c.getArriendos()) {
                        sb.append(String.format("  - Equipo: %s | Fecha: %s\n", 
                                  a.getEquipo().getNombre(), sdf.format(a.getFecha())));
                    }
                }
                sb.append("\n");
            }
        }
        areaTexto.setText(sb.toString());
    }

    private void gestionarInventario() {
        Sucursal sucursal = seleccionarSucursal();
        if (sucursal == null) return;

        String[] opciones = {"Modificar Stock", "Eliminar Equipo"};
        int eleccion = JOptionPane.showOptionDialog(frame, 
            "¿Qué desea hacer en la sucursal " + sucursal.getNombre() + "?", 
            "Gestionar Inventario", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (eleccion == -1) return; // El usuario cerró el diálogo

        if (eleccion == 0) { // Modificar Stock
            Equipo equipo = seleccionarEquipo(sucursal);
            if (equipo == null) return;
            
            int stockActual = sucursal.getInventario().get(equipo);
            String nuevoStockStr = JOptionPane.showInputDialog(frame, "Equipo: " + equipo.getNombre() + "\nStock actual: " + stockActual + "\n\nIngrese el nuevo stock:", "Modificar Stock", JOptionPane.PLAIN_MESSAGE);
            
            if (nuevoStockStr == null) return;
            try {
                int nuevoStock = Integer.parseInt(nuevoStockStr);
                sucursal.modificarStockEquipo(equipo, nuevoStock);
                JOptionPane.showMessageDialog(frame, "Stock actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                mostrarSucursales();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else { // Eliminar Equipo
            Equipo equipo = seleccionarEquipo(sucursal);
            if (equipo == null) return;

            int confirm = JOptionPane.showConfirmDialog(frame, 
                "¿Está seguro de que desea eliminar '" + equipo.getNombre() + "' del inventario?\nEsta acción no se puede deshacer.", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                sucursal.eliminarEquipo(equipo);
                JOptionPane.showMessageDialog(frame, "Equipo eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                mostrarSucursales();
            }
        }
    }
    
    private void gestionarArriendos() {
        Cliente cliente = seleccionarCliente();
        if (cliente == null) return;

        List<Arriendo> arriendos = cliente.getArriendos();
        if (arriendos.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "El cliente " + cliente.getNombre() + " no tiene arriendos registrados.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Formatear arriendos para mostrarlos en el diálogo
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] opciones = arriendos.stream()
            .map(a -> String.format("Equipo: %s | Fecha: %s", a.getEquipo().getNombre(), sdf.format(a.getFecha())))
            .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(frame, 
            "Seleccione el arriendo que desea eliminar para " + cliente.getNombre() + ":", 
            "Gestionar Arriendos", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == null) return;

        int indexSeleccionado = java.util.Arrays.asList(opciones).indexOf(seleccion);
        Arriendo arriendoAEliminar = arriendos.get(indexSeleccionado);

        int confirm = JOptionPane.showConfirmDialog(frame, 
            "¿Está seguro de que desea eliminar este arriendo?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            cliente.eliminarArriendo(arriendoAEliminar);
            JOptionPane.showMessageDialog(frame, "Arriendo eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            verClientesYArriendos();
        }
    }

    // --- MÉTODOS DE AYUDA (selectores) ---
    
    private Sucursal seleccionarSucursal() {
        List<Sucursal> sucursales = sistema.getSucursales();
        if (sucursales.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay sucursales registradas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String[] opciones = sucursales.stream().map(Sucursal::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(frame, "Seleccione una sucursal:", "Selección de Sucursal", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) return null;
        return sucursales.stream().filter(s -> s.getNombre().equals(seleccion)).findFirst().orElse(null);
    }

    private Cliente seleccionarCliente() {
        List<Cliente> clientes = sistema.getClientes();
         if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay clientes registrados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String[] opciones = clientes.stream().map(c -> c.getNombre() + " (" + c.getRut() + ")").toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(frame, "Seleccione un cliente:", "Selección de Cliente", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) return null;
        int index = java.util.Arrays.asList(opciones).indexOf(seleccion);
        return clientes.get(index);
    }
    
    private Equipo seleccionarEquipo(Sucursal sucursal) {
        Map<Equipo, Integer> inventario = sucursal.getInventario();
        if (inventario.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "La sucursal no tiene equipos en inventario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        List<Equipo> equiposDisponibles = new ArrayList<>(inventario.keySet());
        String[] opciones = equiposDisponibles.stream().map(e -> e.getNombre() + " [" + e.getCodigo() + "]").toArray(String[]::new);
        
        String seleccion = (String) JOptionPane.showInputDialog(frame, "Seleccione un equipo:", "Selección de Equipo en " + sucursal.getNombre(), JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) return null;

        int index = java.util.Arrays.asList(opciones).indexOf(seleccion);
        return equiposDisponibles.get(index);
    }
    private void devolverEquipo() {
        Cliente cliente = seleccionarCliente();
        if (cliente == null) return;

        List<Arriendo> arriendos = cliente.getArriendos();
        if (arriendos.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Este cliente no tiene arriendos pendientes.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear una lista de opciones para el diálogo
        String[] opciones = arriendos.stream()
            .map(a -> a.getEquipo().getNombre() + " (Arrendado el " + new SimpleDateFormat("dd/MM/yyyy").format(a.getFecha()) + ")")
            .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(frame,
                "Seleccione el equipo a devolver:",
                "Devolución de Equipo",
                JOptionPane.QUESTION_MESSAGE, null,
                opciones, opciones[0]);

        if (seleccion == null) return;

        // Encontrar el arriendo correspondiente a la selección
        int indexSeleccionado = -1;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                indexSeleccionado = i;
                break;
            }
        }
        
        Arriendo arriendoADevolver = arriendos.get(indexSeleccionado);
        Equipo equipoDevuelto = arriendoADevolver.getEquipo();
        
        // Buscar la sucursal del equipo
        Sucursal sucursalOrigen = sistema.buscarSucursalPorEquipo(equipoDevuelto);
        if (sucursalOrigen == null) {
            JOptionPane.showMessageDialog(frame, "Error: No se pudo encontrar la sucursal de origen del equipo.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lógica de devolución
        if (cliente.eliminarArriendo(arriendoADevolver)) {
            sucursalOrigen.modificarStock(equipoDevuelto, 1); // Incrementa el stock en 1
            JOptionPane.showMessageDialog(frame, "Devolución registrada con éxito.\nEl stock ha sido actualizado en la sucursal " + sucursalOrigen.getNombre() + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            verClientesYArriendos(); // Actualizar vista
        } else {
            JOptionPane.showMessageDialog(frame, "No se pudo procesar la devolución.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaReportes() {
        // Esta línea asume que crearás la clase VentanaReportes.java
        // Por ahora, solo mostramos un mensaje.
        // new VentanaReportes(sistema); 
        
        // --- Implementación de reportes directamente aquí para simplicidad ---
        String[] opcionesReporte = {"Arriendos por Cliente", "Arriendos Totales"};
        String seleccion = (String) JOptionPane.showInputDialog(frame, "Seleccione el tipo de reporte:", "Generar Reportes", JOptionPane.QUESTION_MESSAGE, null, opcionesReporte, opcionesReporte[0]);

        if (seleccion == null) return;

        if (seleccion.equals(opcionesReporte[0])) { // Arriendos por Cliente
            generarReportePorCliente();
        } else { // Arriendos Totales
            generarReporteTotal();
        }
    }

    private void generarReportePorCliente() {
        Cliente cliente = seleccionarCliente();
        if (cliente == null) return;

        StringBuilder reporte = new StringBuilder("--- REPORTE DE ARRIENDOS PARA " + cliente.getNombre().toUpperCase() + " ---\n\n");
        List<Arriendo> arriendos = cliente.getArriendos();

        if (arriendos.isEmpty()) {
            reporte.append("El cliente no tiene arriendos registrados.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Arriendo arriendo : arriendos) {
                Sucursal sucursal = sistema.buscarSucursalPorEquipo(arriendo.getEquipo());
                String nombreSucursal = (sucursal != null) ? sucursal.getNombre() : "No encontrada";
                reporte.append(String.format("- Equipo: %s\n  Código: %s\n  Sucursal: %s\n  Fecha de Arriendo: %s\n\n",
                    arriendo.getEquipo().getNombre(),
                    arriendo.getEquipo().getCodigo(),
                    nombreSucursal,
                    sdf.format(arriendo.getFecha())));
            }
        }
        areaTexto.setText(reporte.toString());
    }

    private void generarReporteTotal() {
        StringBuilder reporte = new StringBuilder("--- REPORTE DE TODOS LOS ARRIENDOS ACTIVOS ---\n\n");
        boolean hayArriendos = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Cliente cliente : sistema.getClientes()) {
            if (!cliente.getArriendos().isEmpty()) {
                hayArriendos = true;
                reporte.append(">> Cliente: ").append(cliente.getNombre()).append(" (").append(cliente.getRut()).append(")\n");
                for (Arriendo arriendo : cliente.getArriendos()) {
                     Sucursal sucursal = sistema.buscarSucursalPorEquipo(arriendo.getEquipo());
                     String nombreSucursal = (sucursal != null) ? sucursal.getNombre() : "No encontrada";
                     reporte.append(String.format("   - Equipo: %s | Código: %s | Sucursal: %s | Fecha: %s\n",
                        arriendo.getEquipo().getNombre(),
                        arriendo.getEquipo().getCodigo(),
                        nombreSucursal,
                        sdf.format(arriendo.getFecha())));
                }
                reporte.append("\n");
            }
        }

        if (!hayArriendos) {
            reporte.append("No hay ningún equipo arrendado en todo el sistema.");
        }
        areaTexto.setText(reporte.toString());
    }
}
