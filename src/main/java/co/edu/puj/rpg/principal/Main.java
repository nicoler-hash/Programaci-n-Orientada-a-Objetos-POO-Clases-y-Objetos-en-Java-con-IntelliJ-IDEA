package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Guerrero;
import co.edu.puj.rpg.modelo.Mago;
import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.Pocion;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;
import co.edu.puj.rpg.modelo.inventario.InventarioT;
import co.edu.puj.rpg.modelo.tablero.Tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reto integrador: Arena RPG por consola.
 * Combina herencia/polimorfismo, ArrayList, genéricos y matriz 2D.
 */
public class Main {

    private static final int COFRE_X = 2;
    private static final int COFRE_Y = 2;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        // ---------- 1. Inicialización ----------
        Tablero tablero = new Tablero(COFRE_X, COFRE_Y);

        List<Personaje> heroes = new ArrayList<>();
        heroes.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        heroes.add(new Mago("Gandalf", 80, 25, 1, 1, 50));
        Personaje heroePrincipal = heroes.get(0);

        List<Enemigo> enemigos = new ArrayList<>();
        enemigos.add(new Enemigo("Orco", 60, 10, 4, 0));
        enemigos.add(new Enemigo("Goblin", 40, 8, 0, 4));
        enemigos.add(new Enemigo("Troll", 90, 14, 4, 4));

        InventarioT<Pocion> inventario = new InventarioT<>();
        inventario.agregar(new Pocion("Poción Menor", 30));
        inventario.agregar(new Pocion("Poción Menor", 30));
        inventario.agregar(new Pocion("Poción Mayor", 60));

        boolean terminado = false;
        boolean victoriaPorCofre = false;
        int ronda = 0;

        System.out.println("===== ARENA RPG =====");
        System.out.println("Objetivo: elimina a todos los enemigos o lleva a " + heroePrincipal.getNombre()
                + " hasta el cofre [C].");

        // ---------- 2. Ciclo principal ----------
        do {
            ronda++;
            System.out.println();
            System.out.println("================ RONDA " + ronda + " ================");

            // ---------- 3. Renderizado y estado ----------
            tablero.actualizar(heroes, enemigos);
            tablero.mostrarTablero();
            mostrarEstado(heroes, enemigos, inventario);

            // ---------- 4. Turno del jugador ----------
            for (Personaje heroe : heroes) {
                if (terminado) {
                    break;
                }
                if (!heroe.estaVivo()) {
                    continue;
                }
                heroe.iniciarTurno();
                boolean turnoCompletado = false;

                while (!turnoCompletado) {
                    System.out.println();
                    System.out.println("--- Turno de " + heroe.getResumen() + " ---");
                    System.out.println("1. Atacar");
                    System.out.println("2. Usar Inventario");
                    System.out.println("3. Mover en Tablero");
                    System.out.println("4. Pasar turno");
                    System.out.print("Elige una opción: ");

                    int opcion = leerEntero(teclado);

                    switch (opcion) {
                        case 1:
                            turnoCompletado = accionAtacar(heroe, enemigos, teclado);
                            break;
                        case 2:
                            turnoCompletado = accionInventario(heroe, inventario, teclado);
                            break;
                        case 3:
                            turnoCompletado = accionMover(heroe, tablero, teclado);
                            break;
                        case 4:
                            System.out.println(heroe.getNombre() + " pasa el turno.");
                            turnoCompletado = true;
                            break;
                        default:
                            System.out.println("Opción inválida. Elige un número entre 1 y 4.");
                            break;
                    }
                    tablero.actualizar(heroes, enemigos);
                }

                // Condiciones de victoria evaluadas tras cada acción de héroe
                if (contarVivos(enemigos) == 0) {
                    terminado = true;
                } else if (heroePrincipal.estaVivo()
                        && tablero.esCofre(heroePrincipal.getPosicionX(), heroePrincipal.getPosicionY())) {
                    terminado = true;
                    victoriaPorCofre = true;
                }
            }

            // ---------- 5. Turno de los enemigos ----------
            if (!terminado) {
                System.out.println();
                System.out.println("--- Turno de los enemigos ---");
                turnoEnemigos(heroes, enemigos, tablero);
            }

            // ---------- 6. Limpieza de bajas ----------
            heroes.removeIf(h -> !h.estaVivo());
            enemigos.removeIf(e -> !e.estaVivo());
            tablero.actualizar(heroes, enemigos);

        } while (!terminado && !heroes.isEmpty() && !enemigos.isEmpty() && sumaVida(heroes) > 0);

        // ---------- Resultado ----------
        System.out.println();
        tablero.mostrarTablero();
        System.out.println("===== FIN DE LA BATALLA =====");
        if (victoriaPorCofre) {
            System.out.println("¡VICTORIA! " + heroePrincipal.getNombre() + " alcanzó el cofre del tesoro.");
        } else if (enemigos.isEmpty() || contarVivos(enemigos) == 0) {
            System.out.println("¡VICTORIA! Todos los enemigos fueron eliminados.");
        } else {
            System.out.println("DERROTA. Tu escuadrón ha sido aniquilado.");
        }
        teclado.close();
    }

    // ===================== ACCIONES DEL JUGADOR =====================

    /** Devuelve true si el turno se consumió. */
    private static boolean accionAtacar(Personaje heroe, List<Enemigo> enemigos, Scanner teclado) {
        List<Enemigo> alAlcance = new ArrayList<>();
        for (Enemigo e : enemigos) {
            if (e.estaVivo() && distancia(heroe, e) <= heroe.getAlcance()) {
                alAlcance.add(e);
            }
        }
        if (alAlcance.isEmpty()) {
            System.out.printf("Ningún enemigo al alcance (alcance de %s: %d). Muévete primero.%n",
                    heroe.getNombre(), heroe.getAlcance());
            return false;
        }
        System.out.println("Enemigos al alcance:");
        for (int i = 0; i < alAlcance.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, alAlcance.get(i).getResumen());
        }
        System.out.print("Elige el objetivo: ");
        int eleccion = leerEntero(teclado);
        if (eleccion < 1 || eleccion > alAlcance.size()) {
            System.out.println("Objetivo inválido.");
            return false;
        }
        // Llamada polimórfica: se ejecuta el ataque de Guerrero o de Mago según la instancia real
        heroe.realizarAtaqueEspecial(alAlcance.get(eleccion - 1));
        return true;
    }

    private static boolean accionInventario(Personaje heroe, InventarioT<Pocion> inventario, Scanner teclado) {
        if (inventario.estaVacio()) {
            System.out.println("El inventario está vacío.");
            return false;
        }
        System.out.println("Inventario:");
        for (int i = 0; i < inventario.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, inventario.obtener(i)); // sin casteo gracias a los genéricos
        }
        System.out.print("Elige una poción (0 para cancelar): ");
        int eleccion = leerEntero(teclado);
        if (eleccion == 0) {
            return false;
        }
        if (eleccion < 1 || eleccion > inventario.size()) {
            System.out.println("Selección inválida.");
            return false;
        }
        Pocion pocion = inventario.remover(eleccion - 1);
        int antes = heroe.getVida();
        heroe.curar(pocion.getPuntosCuracion());
        System.out.printf("%s usa %s y recupera %d de vida (vida: %d/%d)%n",
                heroe.getNombre(), pocion.getNombre(), heroe.getVida() - antes,
                heroe.getVida(), heroe.getVidaMaxima());
        return true;
    }

    private static boolean accionMover(Personaje heroe, Tablero tablero, Scanner teclado) {
        System.out.print("Dirección (W=arriba, S=abajo, A=izquierda, D=derecha): ");
        String entrada = teclado.nextLine().trim().toUpperCase();
        int nx = heroe.getPosicionX();
        int ny = heroe.getPosicionY();
        switch (entrada) {
            case "W":
                ny--;
                break;
            case "S":
                ny++;
                break;
            case "A":
                nx--;
                break;
            case "D":
                nx++;
                break;
            default:
                System.out.println("Dirección inválida.");
                return false;
        }
        // Validación de límites: evita IndexOutOfBoundsException en la matriz
        if (!tablero.estaDentro(nx, ny)) {
            System.out.println("Movimiento fuera de los límites del tablero.");
            return false;
        }
        char celda = tablero.getCelda(nx, ny);
        if (celda != Tablero.VACIA && celda != Tablero.COFRE) {
            System.out.println("La celda (" + nx + "," + ny + ") está ocupada.");
            return false;
        }
        heroe.setPosicion(nx, ny);
        System.out.printf("%s se mueve a (%d,%d)%n", heroe.getNombre(), nx, ny);
        return true;
    }

    // ===================== IA ENEMIGA =====================

    private static void turnoEnemigos(List<Personaje> heroes, List<Enemigo> enemigos, Tablero tablero) {
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) {
                continue;
            }
            Personaje objetivo = heroeMasCercano(enemigo, heroes);
            if (objetivo == null) {
                return; // no quedan héroes vivos
            }
            if (distancia(enemigo, objetivo) <= enemigo.getAlcance()) {
                enemigo.realizarAtaqueEspecial(objetivo);
            } else {
                moverHacia(enemigo, objetivo, tablero);
            }
            tablero.actualizar(heroes, enemigos);
        }
    }

    private static Personaje heroeMasCercano(Personaje enemigo, List<Personaje> heroes) {
        Personaje mejor = null;
        for (Personaje h : heroes) {
            if (h.estaVivo() && (mejor == null || distancia(enemigo, h) < distancia(enemigo, mejor))) {
                mejor = h;
            }
        }
        return mejor;
    }

    private static void moverHacia(Personaje enemigo, Personaje objetivo, Tablero tablero) {
        int dx = Integer.compare(objetivo.getPosicionX(), enemigo.getPosicionX());
        int dy = Integer.compare(objetivo.getPosicionY(), enemigo.getPosicionY());
        boolean prioridadHorizontal = Math.abs(objetivo.getPosicionX() - enemigo.getPosicionX())
                >= Math.abs(objetivo.getPosicionY() - enemigo.getPosicionY());

        boolean movido;
        if (prioridadHorizontal) {
            movido = intentarMover(enemigo, enemigo.getPosicionX() + dx, enemigo.getPosicionY(), tablero)
                    || intentarMover(enemigo, enemigo.getPosicionX(), enemigo.getPosicionY() + dy, tablero);
        } else {
            movido = intentarMover(enemigo, enemigo.getPosicionX(), enemigo.getPosicionY() + dy, tablero)
                    || intentarMover(enemigo, enemigo.getPosicionX() + dx, enemigo.getPosicionY(), tablero);
        }
        if (!movido) {
            System.out.println(enemigo.getNombre() + " no puede avanzar y espera.");
        }
    }

    /** Los enemigos solo pueden pisar celdas vacías (no el cofre ni otras unidades). */
    private static boolean intentarMover(Personaje p, int nx, int ny, Tablero tablero) {
        if (nx == p.getPosicionX() && ny == p.getPosicionY()) {
            return false;
        }
        if (tablero.getCelda(nx, ny) != Tablero.VACIA) {
            return false;
        }
        p.setPosicion(nx, ny);
        System.out.printf("%s avanza a (%d,%d)%n", p.getNombre(), nx, ny);
        return true;
    }

    // ===================== UTILIDADES =====================

    private static void mostrarEstado(List<Personaje> heroes, List<Enemigo> enemigos,
                                      InventarioT<Pocion> inventario) {
        System.out.println();
        System.out.println("-- Héroes --");
        for (Personaje h : heroes) {
            System.out.println("  " + h.getResumen());
        }
        System.out.println("-- Enemigos --");
        for (Enemigo e : enemigos) {
            System.out.println("  " + e.getResumen());
        }
        System.out.printf("-- Pociones en inventario: %d --%n", inventario.size());
    }

    private static int distancia(Personaje a, Personaje b) {
        return Math.max(Math.abs(a.getPosicionX() - b.getPosicionX()),
                Math.abs(a.getPosicionY() - b.getPosicionY()));
    }

    private static int contarVivos(List<? extends Personaje> lista) {
        int vivos = 0;
        for (Personaje p : lista) {
            if (p.estaVivo()) {
                vivos++;
            }
        }
        return vivos;
    }

    private static int sumaVida(List<? extends Personaje> lista) {
        int total = 0;
        for (Personaje p : lista) {
            total += p.getVida();
        }
        return total;
    }

    /** Devuelve -1 si la entrada no es numérica para no interrumpir el programa. */
    private static int leerEntero(Scanner teclado) {
        String linea = teclado.nextLine().trim();
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}