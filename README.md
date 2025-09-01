# 📦 Sistema de Arriendo de Equipos

Este proyecto implementa un **sistema de arriendo de equipos de construcción** en **Java**, usando un menú de consola.  
Permite gestionar **clientes, sucursales, equipos e inventarios**, y registrar los arriendos con un resumen detallado.

---

## Características

- Registrar nuevas **sucursales** y agregar equipos a ellas.
- Listar las **sucursales disponibles**.
- Mostrar los **equipos por sucursal** junto con su inventario.
- Registrar un **arriendo de equipos** indicando cliente, sucursal, equipo y cantidad.
- Mantener actualizado el **stock** de equipos en cada sucursal.
- Generar un **resumen del arriendo**.


## Estructura del Código

Todo está contenido en una sola clase `Sistema.java` que incluye las siguientes clases internas:

- `Cliente` → Representa a un cliente (rut, nombre, lista de arriendos).
- `Equipo` → Representa un equipo (código, nombre, disponibilidad).
- `Sucursal` → Representa una sucursal con su inventario de equipos.
- `Arriendo` → Representa un arriendo realizado por un cliente de un equipo en una fecha específica.
- `Sistema` → Clase principal que contiene la lógica, menú y helpers.


##  Requisitos

- **Java 8 o superior** instalado.  
- Consola o terminal para ejecutar el programa.


## ▶️ Cómo ejecutar

1. Guarda el código en un archivo llamado **`Sistema.java`**.  
2. Compila el programa con:

   ```bash
   javac Sistema.java
3. Ejecuta el programa con:
     java Sistema

## Menú
El menú del programa se verá algo asi:
MENÚ SISTEMA ARRIENDO
1) Agregar Sucursal
2) Mostrar Sucursales
3) Equipos por Sucursal
4) Arrendar Equipo
5) Salir
Seleccione una opción:

Opciones del menú:

Agregar Sucursal:
-Ingresa el nombre de la sucursal.

-Decide si deseas agregar equipos y su cantidad.

Mostrar Sucursales:
-Muestra todas las sucursales registradas y su inventario total.

Equipos por Sucursal:
-Lista los equipos disponibles en una sucursal específica junto con sus cantidades.

Arrendar Equipo
Paso 1: Selecciona un cliente (ej: 1 para Pedro).

Paso 2: Selecciona una sucursal.

Paso 3: Selecciona un equipo disponible en esa sucursal.

Paso 4: Ingresa la cantidad a arrendar.

El sistema actualizará el inventario y mostrará un resumen del arriendo:

--- ARRIENDO REGISTRADO ---
Cliente : 111 - Pedro
Equipo  : Pala
Cantidad: 2
Sucursal: Centro
----------------------------


Salir

Cierra el programa.


## Contribuidores

-Fernando Cárdenas
-Felipe Abarca
-Antonio Arancibia
