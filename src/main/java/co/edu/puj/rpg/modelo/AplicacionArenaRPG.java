package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.Arma;
import co.edu.puj.rpg.modelo.Escuadron;
import co.edu.puj.rpg.modelo.Gremio;
import co.edu.puj.rpg.modelo.Guerrero;
import co.edu.puj.rpg.modelo.Mago;
import co.edu.puj.rpg.modelo.Personaje;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menú de gestión de la Arena RPG (Unidad 2.5: relaciones entre clases).
 */
public class AplicacionArenaRPG {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        // Colecciones globales
        List<Personaje> personajes = new ArrayList<>();
        List<Gremio> gremios = new ArrayList<>();
        List<Escuadron> escuadrones = new ArrayList<>();

        do {
            System.out.println("\n=== MENÚ ARENA DE HÉROES RPG ===");
            System.out.println("1. Crear Personaje");
            System.out.println("2. Crear Gremio");
            System.out.println("3. Reclutar Personaje a Escuadrón");
            System.out.println("4. Equipar / Desequipar Arma a Personaje");
            System.out.println("5. Mostrar Estado Detallado (Personaje y Corazón RPG)");
            System.out.println("6. Unir / Retirar Personaje de Gremio");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero(scanner);

            switch (opcion) {
                case 1:
                    crearPersonaje(scanner, personajes);
                    break;
                case 2:
                    crearGremio(scanner, gremios);
                    break;
                case 3:
                    reclutarEnEscuadron(scanner, personajes, escuadrones);
                    break;
                case 4:
                    gestionarArma(scanner, personajes);
                    break;
                case 5:
                    mostrarEstado(personajes, gremios, escuadrones);
                    break;
                case 6:
                    gestionarGremio(scanner, personajes, gremios);
                    break;
                case 7:
                    System.out.println("Cerrando la Arena de Héroes...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    // ===================== OPCIONES DEL MENÚ =====================

    private static void crearPersonaje(Scanner scanner, List<Personaje> personajes) {
        System.out.print("Nombre del personaje: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }
        System.out.print("Tipo (1=Guerrero, 2=Mago): ");
        int tipo = leerEntero(scanner);
        System.out.print("Nivel inicial (mínimo 1): ");
        int nivel = leerEntero(scanner);

        Personaje nuevo;
        if (tipo == 1) {
            nuevo = new Guerrero(nombre, 120, 15, 0, 0, 10);
        } else if (tipo == 2) {
            nuevo = new Mago(nombre, 80, 25, 0, 0, 50);
        } else {
            System.out.println("Tipo inválido. No se creó el personaje.");
            return;
        }
        nuevo.setNivel(nivel); // el setter ignora niveles menores a 1
        personajes.add(nuevo);
        System.out.println("Personaje creado: " + nuevo.getNombre() + " (" + nuevo.getClass().getSimpleName() + ")");
    }

    private static void crearGremio(Scanner scanner, List<Gremio> gremios) {
        System.out.print("Nombre del gremio: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }
        gremios.add(new Gremio(nombre));
        System.out.println("Gremio '" + nombre + "' creado.");
    }

    private static void reclutarEnEscuadron(Scanner scanner, List<Personaje> personajes,
                                            List<Escuadron> escuadrones) {
        Personaje personaje = elegirPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }
        Escuadron escuadron = elegirOCrearEscuadron(scanner, escuadrones);
        if (escuadron == null) {
            return;
        }
        if (escuadron.reclutarPersonaje(personaje)) {
            System.out.println(personaje.getNombre() + " fue reclutado en '" + escuadron.getNombre() + "'.");
        } else {
            System.out.println(personaje.getNombre() + " ya pertenece a ese escuadrón.");
        }
    }

    private static void gestionarArma(Scanner scanner, List<Personaje> personajes) {
        Personaje personaje = elegirPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }
        System.out.println("1. Equipar arma nueva");
        System.out.println("2. Desequipar arma");
        System.out.print("Elige: ");
        int eleccion = leerEntero(scanner);
        if (eleccion == 1) {
            System.out.print("Nombre del arma: ");
            String nombreArma = scanner.nextLine().trim();
            System.out.print("Daño base: ");
            int danio = leerEntero(scanner);
            if (nombreArma.isEmpty() || danio < 0) {
                System.out.println("Datos del arma inválidos.");
                return;
            }
            personaje.equiparArma(new Arma(nombreArma, danio));
        } else if (eleccion == 2) {
            personaje.desequiparArma();
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private static void gestionarGremio(Scanner scanner, List<Personaje> personajes, List<Gremio> gremios) {
        Personaje personaje = elegirPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }
        System.out.println("1. Unir a un gremio");
        System.out.println("2. Retirar de su gremio actual");
        System.out.print("Elige: ");
        int eleccion = leerEntero(scanner);
        if (eleccion == 1) {
            if (gremios.isEmpty()) {
                System.out.println("No hay gremios creados. Usa la opción 2 primero.");
                return;
            }
            for (int i = 0; i < gremios.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, gremios.get(i).getNombre());
            }
            System.out.print("Elige el gremio: ");
            int indice = leerEntero(scanner);
            if (indice < 1 || indice > gremios.size()) {
                System.out.println("Gremio inválido.");
                return;
            }
            personaje.unirseAGremio(gremios.get(indice - 1));
            System.out.println(personaje.getNombre() + " ahora pertenece a '"
                    + personaje.getGremio().getNombre() + "'.");
        } else if (eleccion == 2) {
            if (personaje.getGremio() == null) {
                System.out.println(personaje.getNombre() + " no pertenece a ningún gremio.");
            } else {
                String anterior = personaje.getGremio().getNombre();
                personaje.salirDeGremio();
                System.out.println(personaje.getNombre() + " abandonó '" + anterior + "'.");
            }
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private static void mostrarEstado(List<Personaje> personajes, List<Gremio> gremios,
                                      List<Escuadron> escuadrones) {
        System.out.println("\n================ REPORTE DE LA ARENA ================");
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes registrados.");
        } else {
            System.out.printf("%-12s %-9s %5s %9s %12s %-18s %-12s%n",
                    "NOMBRE", "TIPO", "NIVEL", "SALUD", "ATAQUE TOTAL", "ARMA", "GREMIO");
            System.out.println("-".repeat(85));
            for (Personaje p : personajes) {
                String gremioNombre = p.getGremio() != null ? p.getGremio().getNombre() : "-";
                System.out.printf("%-12s %-9s %5d %9d %12d %-18s %-12s%n",
                        p.getNombre(), p.getClass().getSimpleName(), p.getNivel(),
                        p.getSaludBase(), p.calcularAtaqueTotal(), armaDe(p), gremioNombre);
            }
            System.out.println("\n--- Detalle (incluye CorazonRPG) ---");
            for (Personaje p : personajes) {
                System.out.println(p); // toString con StringBuilder
                System.out.printf("   Corazón -> salud base: %d | maná base: %d%n",
                        p.getSaludBase(), p.getManaBase());
            }
        }

        System.out.println("\n--- Gremios ---");
        if (gremios.isEmpty()) {
            System.out.println("Sin gremios.");
        }
        for (Gremio g : gremios) {
            System.out.println(g);
        }

        System.out.println("\n--- Escuadrones ---");
        if (escuadrones.isEmpty()) {
            System.out.println("Sin escuadrones.");
        }
        for (Escuadron e : escuadrones) {
            System.out.println(e);
        }
    }

    // ===================== UTILIDADES =====================

    /** Muestra la lista y devuelve el personaje elegido, o null si no hay/elección inválida. */
    private static Personaje elegirPersonaje(Scanner scanner, List<Personaje> personajes) {
        if (personajes.isEmpty()) {
            System.out.println("No hay personajes. Crea uno con la opción 1.");
            return null;
        }
        for (int i = 0; i < personajes.size(); i++) {
            System.out.printf("  %d. %s (%s)%n", i + 1, personajes.get(i).getNombre(),
                    personajes.get(i).getClass().getSimpleName());
        }
        System.out.print("Elige el personaje: ");
        int indice = leerEntero(scanner);
        if (indice < 1 || indice > personajes.size()) {
            System.out.println("Selección inválida.");
            return null;
        }
        return personajes.get(indice - 1);
    }

    private static Escuadron elegirOCrearEscuadron(Scanner scanner, List<Escuadron> escuadrones) {
        System.out.println("  0. Crear escuadrón nuevo");
        for (int i = 0; i < escuadrones.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, escuadrones.get(i).getNombre());
        }
        System.out.print("Elige el escuadrón: ");
        int indice = leerEntero(scanner);
        if (indice == 0) {
            System.out.print("Nombre del nuevo escuadrón: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
                return null;
            }
            Escuadron nuevo = new Escuadron(nombre);
            escuadrones.add(nuevo);
            return nuevo;
        }
        if (indice < 1 || indice > escuadrones.size()) {
            System.out.println("Selección inválida.");
            return null;
        }
        return escuadrones.get(indice - 1);
    }

    private static String armaDe(Personaje p) {
        return p.getArmaEquipada() != null ? p.getArmaEquipada().getNombre() : "desarmado";
    }

    /** Devuelve -1 si la entrada no es numérica, para no romper el programa. */
    private static int leerEntero(Scanner scanner) {
        String linea = scanner.nextLine().trim();
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
