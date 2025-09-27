package sistema;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        
        // 1. Se crea la instancia del sistema
        Sistema sistema = new Sistema();
        
        // 2. Se le ordena al sistema que cargue los datos desde los archivos CSV
        sistema.cargarDatosDesdeArchivos();
        
        // 3. Se lanza la interfaz gráfica, que recibe el sistema con los datos cargados
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VentanaMenu(sistema);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
