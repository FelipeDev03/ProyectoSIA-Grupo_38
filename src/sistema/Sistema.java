package sistema;
import java.util.*;

public class Sistema {

    // ATRIBUTOS DEL SISTEMA
    private List<Cliente> clientes;
    private List<Sucursal> sucursales;

    // Constructor
    public Sistema() {
        this.clientes = new ArrayList<>();
        this.sucursales = new ArrayList<>();
    }

    // Métodos del sistema
    public void registrarCliente(Cliente c) {
        clientes.add(c);
    }
    
    // Sobrecarga para registrar mas rapido
    public void registrarCliente(String rut, String nombre) {
        clientes.add(new Cliente(rut, nombre));
    }

    public void registrarSucursal(Sucursal s) {
        sucursales.add(s);
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }

    public List<Sucursal> getSucursales() {
        return new ArrayList<>(sucursales);
    }
    //Estos son helpers que ayudan en la opcion 4 del menú.
    private static int leerIndice1(Scanner sc, int max, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("Entrada vacía. Intenta de nuevo.");
                continue;
            }
            try {
                int idx1 = Integer.parseInt(s);
                if (idx1 >= 1 && idx1 <= max) return idx1 - 1;
                System.out.printf("Opción fuera de rango (1-%d). Intenta de nuevo.\n", max);
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar un número. Intenta de nuevo.");
            }
        }
    }
    //Estos son helpers que ayudan en la opcion 4 del menú.
    private static int leerCantidad(Scanner sc, int max, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n <= 0) {
                    System.out.println("La cantidad debe ser mayor a 0.");
                } else if (n > max) {
                    System.out.printf("No hay stock suficiente (máximo %d).\n", max);
                } else {
                    return n;
                }
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar un número entero válido.");
            }
        }
    }

    // METODO PARA INICIAR PROGRAMA CON DATOS INICIALES Y MENÚ
    public void iniciar() {
        Scanner sc = new Scanner(System.in);
        Sistema sistema = new Sistema();

        // Datos Iniciales de
        Sucursal suc1 = new Sucursal("Centro");
        Sucursal suc2 = new Sucursal("Norte");

        // Datos Iniciales de equipos
        suc1.agregarEquipo("EQ001", "Pala", 5);
        suc1.agregarEquipo("EQ002", "Andamio", 3);
        suc2.agregarEquipo("EQ002", "Andamio", 2);
        suc2.agregarEquipo("EQ003", "Carretilla", 4);

        sistema.registrarSucursal(suc1);
        sistema.registrarSucursal(suc2);

        // Datos Iniciales de clientes
        sistema.registrarCliente("11111111-1", "Pedro");
        sistema.registrarCliente("22222222-2", "María");

        // Menú
        boolean salir = false;
        while (!salir) {
            System.out.println("\nMENÚ SISTEMA ARRIENDO");
            System.out.println("1) Agregar Sucursal");
            System.out.println("2) Mostrar Sucursales y su inventario");
            System.out.println("3) Agregar equipo a sucursal");
            System.out.println("4) Equipos por Sucursal");
            System.out.println("5) Arrendar Equipo");
            System.out.println("6) Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            switch(opcion) {
                case 1:
                    System.out.print("Ingrese nombre de la sucursal: ");
                    String nombre = sc.nextLine();
                    Sucursal suc = new Sucursal(nombre);

                    System.out.print("¿Desea agregar un equipo? (s/n): ");
                    String resp = sc.nextLine();
                    while(resp.equalsIgnoreCase("s")) {
                        System.out.print("Nombre del equipo: ");
                        String nombreEq = sc.nextLine();
                        System.out.print("Cantidad: ");
                        int cant = sc.nextInt();
                        sc.nextLine(); // limpiar buffer
                        suc.agregarEquipo(new Equipo(UUID.randomUUID().toString(), nombreEq), cant);
                        System.out.print("¿Agregar otro equipo? (s/n): ");
                        resp = sc.nextLine();
                    }

                    sistema.registrarSucursal(suc);
                    System.out.println("Sucursal agregada correctamente!");
                    break;

                case 2:
                	System.out.println("\n--- LISTADO DE SUCURSALES ---");
                    for (Sucursal s : sistema.getSucursales()) {
                        System.out.println("Sucursal: " + s.getNombre());
                        if (s.getInventario().isEmpty()) {
                            System.out.println("  (Sin equipos en inventario)");
                        } else {
                            for (Map.Entry<Equipo, Integer> entry : s.getInventario().entrySet()) {
                                System.out.println("  - " + entry.getKey().getNombre() + ": " + entry.getValue() + " unidades");
                            }
                        }
                    }
                    break;
                case 3:
                	if (sistema.getSucursales().isEmpty()) {
                        System.out.println("No hay sucursales registradas.");
                        break;
                    }

                    System.out.println("Sucursales disponibles:");
                    for (int i = 0; i < sistema.getSucursales().size(); i++) {
                        System.out.println((i + 1) + ") " + sistema.getSucursales().get(i).getNombre());
                    }
                    int idx = leerIndice1(sc, sistema.getSucursales().size(), "Seleccione sucursal: ");
                    Sucursal sucSeleccionada = sistema.getSucursales().get(idx);

                    System.out.print("Nombre del equipo: ");
                    String nombreEq = sc.nextLine();
                    System.out.print("Cantidad: ");
                    int canti = sc.nextInt();
                    sc.nextLine();

                    sucSeleccionada.agregarEquipo(new Equipo(UUID.randomUUID().toString(), nombreEq), canti);
                    System.out.println("Equipo agregado al inventario de la sucursal " + sucSeleccionada.getNombre());
                    break;
                    
                    
                case 4: // Mostrar Equipos por Sucursal
                	System.out.println("Sucursales disponibles:");
                	for (Sucursal s : sistema.getSucursales()) {
                	    System.out.println(" - " + s.getNombre());
                	}
                    System.out.print("Ingrese el nombre de la sucursal: ");
                    String nombreSucursal = sc.nextLine();
                    boolean encontrada = false;
                    for (Sucursal s : sistema.getSucursales()) {
                        if (s.getNombre().equalsIgnoreCase(nombreSucursal)) {
                            System.out.println("\nEquipos en la sucursal " + s.getNombre() + ":");
                            for (Map.Entry<Equipo, Integer> entry : s.getInventario().entrySet()) {
                                System.out.println(" - " + entry.getKey().getNombre() + " : " + entry.getValue() + " unidades");
                            }
                            encontrada = true;
                            break;
                        }
                    }
                    if (!encontrada) {
                        System.out.println("Sucursal no encontrada.");
                    }
                    break;
                case 5: { // ARRENDAR EQUIPO
                    // 1) Clientes
                    List<Cliente> listaClientes = sistema.getClientes();
                    if (listaClientes == null || listaClientes.isEmpty()) {
                        System.out.println("No hay clientes cargados.");
                        break;
                    }

                    System.out.println("\nClientes disponibles:");
                    for (int i = 0; i < listaClientes.size(); i++) {
                        Cliente c = listaClientes.get(i);
                        System.out.println((i + 1) + ") " + c.getRut() + " - " + c.getNombre());
                    }
                    int clienteIndex = leerIndice1(sc, listaClientes.size(), "Seleccione un cliente por número: ");
                    Cliente clienteSeleccionado = listaClientes.get(clienteIndex);

                    // 2) Sucursales
                    List<Sucursal> listaSucursales = sistema.getSucursales();
                    if (listaSucursales == null || listaSucursales.isEmpty()) {
                        System.out.println("No hay sucursales cargadas.");
                        break;
                    }

                    System.out.println("\nSucursales disponibles:");
                    for (int i = 0; i < listaSucursales.size(); i++) {
                        System.out.println((i + 1) + ") " + listaSucursales.get(i).getNombre());
                    }
                    int sucursalIndex = leerIndice1(sc, listaSucursales.size(), "Seleccione una sucursal por número: ");
                    Sucursal sucursalSeleccionada = listaSucursales.get(sucursalIndex);

                    // 3) Equipos disponibles en la sucursal
                    Map<Equipo, Integer> inventario = sucursalSeleccionada.getInventario();
                    if (inventario == null || inventario.isEmpty()) {
                        System.out.println("La sucursal no tiene inventario disponible.");
                        break;
                    }

                    List<Equipo> equipos = new ArrayList<>(inventario.keySet());
                    System.out.println("\nEquipos disponibles en " + sucursalSeleccionada.getNombre() + ":");
                    for (int i = 0; i < equipos.size(); i++) {
                        Equipo e = equipos.get(i);
                        int cant = inventario.getOrDefault(e, 0);
                        System.out.println((i + 1) + ") " + e.getNombre() + " - " + cant + " unidades disponibles");
                    }
                    int equipoIndex = leerIndice1(sc, equipos.size(), "Seleccione un equipo por número: ");
                    Equipo equipoSeleccionado = equipos.get(equipoIndex);

                    int disponible = inventario.getOrDefault(equipoSeleccionado, 0);
                    if (disponible <= 0) {
                        System.out.println("No hay stock disponible de ese equipo.");
                        break;
                    }

                    // 4) Cantidad a arrendar
                    int cantidadArrendar = leerCantidad(sc, disponible, "Ingrese cantidad a arrendar: ");

                    // 5) Registrar arriendo
                    clienteSeleccionado.agregarArriendo(equipoSeleccionado, cantidadArrendar);

                    sucursalSeleccionada.modificarStock(equipoSeleccionado, -cantidadArrendar);

                    // 7) Output del resumen
                    System.out.println("\n--- ARRIENDO REGISTRADO ---");
                    System.out.println("Cliente : " + clienteSeleccionado.getRut() + " - " + clienteSeleccionado.getNombre());
                    System.out.println("Equipo  : " + equipoSeleccionado.getNombre());
                    System.out.println("Cantidad: " + cantidadArrendar);
                    System.out.println("Sucursal: " + sucursalSeleccionada.getNombre());
                    System.out.println("----------------------------");
                    break;
                }

                case 6:
                	salir = true;
                	break;
                	
                default:
                    System.out.println("Opción inválida!");
            }
       }

       sc.close();
    }
}
