package sistema;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaMenu {

    private JFrame frame;
    private JTextArea areaTexto;
    private Sistema sistema; // Referencia a la lógica del sistema

    /**
     * Constructor que recibe la instancia del sistema.
     */
    public VentanaMenu(Sistema sistema) {
        this.sistema = sistema;
        initialize();
        this.frame.setVisible(true); // Hacemos visible la ventana
    }

    /**
     * Inicializa los contenidos del frame.
     */
    private void initialize() {
        // --- Configuración de la Ventana Principal ---
        frame = new JFrame("Sistema de Arriendo de Equipos");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Para controlar el cierre manualmente
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Se ejecuta cuando el usuario presiona la 'X'
                sistema.guardarDatosAlSalir(); // Guardar todo
                System.exit(0); // Cierre del programa
            }
        });
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- Panel de Botones (Oeste) ---
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 5, 5)); // Layout en grilla vertical
        frame.getContentPane().add(panelBotones, BorderLayout.WEST);

        // --- Área de Texto para mostrar información (Centro) ---
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(areaTexto); // Panel con scroll
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        // --- Título ---
        JLabel lblTitulo = new JLabel("Menú de Opciones");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        panelBotones.add(lblTitulo);


        // --- Creación de los Botones y sus Acciones ---

        JButton btnMostrarSucursales = new JButton("Mostrar Sucursales");
        btnMostrarSucursales.addActionListener(e -> mostrarSucursales());
        panelBotones.add(btnMostrarSucursales);
        
        JButton btnAgregarSucursal = new JButton("Agregar Sucursal");
        btnAgregarSucursal.addActionListener(e -> agregarSucursal());
        panelBotones.add(btnAgregarSucursal);

        JButton btnAgregarEquipo = new JButton("Agregar Equipo a Sucursal");
        btnAgregarEquipo.addActionListener(e -> agregarEquipoASucursal());
        panelBotones.add(btnAgregarEquipo);

        JButton btnArrendar = new JButton("Arrendar Equipo");
        btnArrendar.addActionListener(e -> arrendarEquipo());
        panelBotones.add(btnArrendar);
        
        JButton btnSalir = new JButton("Guardar y Salir");
        btnSalir.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        panelBotones.add(btnSalir);
    }

    // --- Métodos para cada Acción ---

    private void mostrarSucursales() {
        areaTexto.setText(""); // Limpiar el área de texto
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
                        sb.append("  - ").append(entry.getKey().getNombre())
                          .append(": ").append(entry.getValue()).append(" unidades\n");
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
            mostrarSucursales(); // Actualizar la vista
        }
    }
    
    private void agregarEquipoASucursal() {
        // 1. Seleccionar Sucursal
        Sucursal sucSeleccionada = seleccionarSucursal();
        if (sucSeleccionada == null) return; // El usuario canceló

        // 2. Pedir datos del equipo
        String nombreEq = JOptionPane.showInputDialog(frame, "Nombre del nuevo equipo:", "Agregar Equipo", JOptionPane.PLAIN_MESSAGE);
        if (nombreEq == null || nombreEq.trim().isEmpty()) return;

        String cantStr = JOptionPane.showInputDialog(frame, "Cantidad:", "Agregar Equipo", JOptionPane.PLAIN_MESSAGE);
        if (cantStr == null || cantStr.trim().isEmpty()) return;

        try {
            int cantidad = Integer.parseInt(cantStr);
            sucSeleccionada.agregarEquipo(nombreEq, nombreEq, cantidad); // Usamos nombre como código por simplicidad
            JOptionPane.showMessageDialog(frame, "Equipo agregado a la sucursal " + sucSeleccionada.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mostrarSucursales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void arrendarEquipo() {
        // 1. Seleccionar Cliente
        Cliente cliente = seleccionarCliente();
        if (cliente == null) return;

        // 2. Seleccionar Sucursal
        Sucursal sucursal = seleccionarSucursal();
        if (sucursal == null) return;

        // 3. Seleccionar Equipo
        Equipo equipo = seleccionarEquipo(sucursal);
        if (equipo == null) return;
        
        int disponible = sucursal.getInventario().getOrDefault(equipo, 0);

        // 4. Ingresar cantidad
        String cantStr = JOptionPane.showInputDialog(frame, "Equipo: " + equipo.getNombre() + "\nStock disponible: " + disponible + "\n\nIngrese cantidad a arrendar:", "Arrendar Equipo", JOptionPane.PLAIN_MESSAGE);
        if (cantStr == null) return;

        try {
            int cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0 || cantidad > disponible) {
                JOptionPane.showMessageDialog(frame, "Cantidad no válida o sin stock suficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 5. Realizar arriendo
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
    
    // --- Métodos de ayuda para seleccionar en diálogos ---
    
    private Sucursal seleccionarSucursal() {
        List<Sucursal> sucursales = sistema.getSucursales();
        if (sucursales.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay sucursales registradas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String[] opciones = sucursales.stream().map(Sucursal::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(frame, "Seleccione una sucursal:", "Selección de Sucursal", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        
        if (seleccion == null) return null; // El usuario cerró el diálogo
        
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
        String[] opciones = equiposDisponibles.stream().map(Equipo::getNombre).toArray(String[]::new);
        
        String seleccion = (String) JOptionPane.showInputDialog(frame, "Seleccione un equipo:", "Selección de Equipo en " + sucursal.getNombre(), JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == null) return null;

        return equiposDisponibles.stream().filter(e -> e.getNombre().equals(seleccion)).findFirst().orElse(null);
    }
}
