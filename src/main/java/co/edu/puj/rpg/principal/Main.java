package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Scanner;

/**
 * Simulador de Batalla RPG por consola.
 *
 * ENCAPSULAMIENTO: esta clase está en otro paquete, por lo que solo ve la API
 * pública de Personaje y Enemigo. Una línea como  heroe.puntosVida = 200;
 * produce el error: 'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'.
 */
public class Main {

    private static final double COSTO_POCION_GRANDE = 30.0;
    private static final int CURA_POCION_GRANDE = 60;
    private static final int CURA_POCION_PEQUENA = 30;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        Enemigo enemigo = new Enemigo("Orco Guerrero", 80, 12);

        int pocionesPequenas = 2;   // Inventario inicial
        int pocionesGrandes = 0;
        int ronda = 0;
        boolean huyo = false;

        // Registro de batalla: acumula los eventos de todos los turnos
        StringBuilder registro = new StringBuilder();
        registro.append("=== INICIO DE LA BATALLA: ")
                .append(heroe.getNombre()).append(" vs ").append(enemigo.getNombre())
                .append(" ===\n");

        System.out.println("===== ARENA DE HÉROES =====");
        System.out.printf("%s se enfrenta a %s%n", heroe.getNombre(), enemigo.getNombre());

        do {
            System.out.println();
            System.out.println("--- MENÚ DE COMBATE ---");
            System.out.println("1. Atacar");
            System.out.println("2. Usar Poción");
            System.out.println("3. Ver Estado");
            System.out.println("4. Tienda");
            System.out.println("5. Huir");
            System.out.print("Elige una opción: ");

            int opcion = leerEntero(teclado);
            boolean turnoConsumido = false;

            switch (opcion) {
                case 1: // ATACAR
                    heroe.atacar(enemigo);
                    if (heroe.isUltimoGolpeCritico()) {
                        registro.append(String.format("Ronda %d: ¡GOLPE CRÍTICO! %s inflige %d de daño a %s.%n",
                                ronda + 1, heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
                    } else {
                        registro.append(String.format("Ronda %d: %s inflige %d de daño a %s.%n",
                                ronda + 1, heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
                    }
                    if (!enemigo.isEstaVivo()) {
                        registro.append(String.format("%s ha sido derrotado.%n", enemigo.getNombre()));
                    }
                    turnoConsumido = true;
                    break;

                case 2: // USAR POCIÓN
                    int vidaAntes = heroe.getPuntosVida();
                    if (pocionesPequenas > 0) {
                        pocionesPequenas--;
                        heroe.usarPocion(CURA_POCION_PEQUENA);
                        registro.append(String.format("Ronda %d: %s usa una Poción y recupera %d de vida.%n",
                                ronda + 1, heroe.getNombre(), heroe.getPuntosVida() - vidaAntes));
                        turnoConsumido = true;
                    } else if (pocionesGrandes > 0) {
                        pocionesGrandes--;
                        heroe.usarPocion(CURA_POCION_GRANDE);
                        registro.append(String.format("Ronda %d: %s usa una Poción Grande y recupera %d de vida.%n",
                                ronda + 1, heroe.getNombre(), heroe.getPuntosVida() - vidaAntes));
                        turnoConsumido = true;
                    } else {
                        System.out.println("No tienes pociones. Visita la tienda (opción 4).");
                    }
                    break;

                case 3: // VER ESTADO (solo usa getters)
                    System.out.println();
                    System.out.println("========== ESTADO ==========");
                    System.out.printf("Héroe : %s | Nivel: %d%n", heroe.getNombre(), heroe.getNivel());
                    System.out.printf("Vida  : %d / %d%n", heroe.getPuntosVida(), heroe.getVidaMaxima());
                    System.out.printf("Ataque: %d%n", heroe.getPuntosAtaque());
                    System.out.printf("Oro   : %.2f%n", heroe.getOro());
                    System.out.printf("Vivo  : %s%n", heroe.isEstaVivo() ? "Sí" : "No");
                    System.out.printf("Pociones -> pequeñas: %d | grandes: %d%n", pocionesPequenas, pocionesGrandes);
                    System.out.println("----------------------------");
                    System.out.printf("Enemigo: %s%n", enemigo.getNombre());
                    System.out.printf("Vida   : %d / %d%n", enemigo.getPuntosVida(), enemigo.getVidaMaxima());
                    System.out.printf("Ataque : %d%n", enemigo.getPuntosAtaque());
                    System.out.println("============================");
                    break;

                case 4: // TIENDA
                    System.out.println();
                    System.out.println("--- TIENDA ---");
                    System.out.printf("Oro disponible: %.2f%n", heroe.getOro());
                    System.out.printf("1. Poción Grande de Salud (+%d vida) - %.2f oro%n",
                            CURA_POCION_GRANDE, COSTO_POCION_GRANDE);
                    System.out.println("2. Volver");
                    System.out.print("Elige una opción: ");
                    int opcionTienda = leerEntero(teclado);

                    if (opcionTienda == 1) {
                        double saldoAnterior = heroe.getOro();
                        // La compra pasa OBLIGATORIAMENTE por el setter validado
                        heroe.setOro(saldoAnterior - COSTO_POCION_GRANDE);
                        if (heroe.getOro() != saldoAnterior) {
                            pocionesGrandes++;
                            System.out.println("¡Compra exitosa! Obtuviste una Poción Grande de Salud.");
                            registro.append(String.format("Tienda: %s compra una Poción Grande (-%.2f oro).%n",
                                    heroe.getNombre(), COSTO_POCION_GRANDE));
                        } else {
                            // setOro rechazó la operación: no se entrega el ítem
                            registro.append(String.format("Tienda: compra rechazada por saldo insuficiente.%n"));
                        }
                    } else if (opcionTienda == 2) {
                        System.out.println("Saliendo de la tienda...");
                    } else {
                        System.out.println("Opción de tienda inválida.");
                    }
                    break;

                case 5: // HUIR
                    huyo = true;
                    registro.append(String.format("%s huye del combate.%n", heroe.getNombre()));
                    break;

                default: // ENTRADA ERRÓNEA
                    if (opcion == -1) {
                        System.out.println("Entrada inválida: ingresa un número entre 1 y 5.");
                    } else {
                        System.out.println("Opción " + opcion + " no existe. Elige entre 1 y 5.");
                    }
                    break;
            }

            // Turno del enemigo (solo si el héroe gastó su turno y el enemigo sigue vivo)
            if (turnoConsumido && enemigo.isEstaVivo()) {
                enemigo.atacar(heroe);
                registro.append(String.format("Ronda %d: %s inflige %d de daño a %s.%n",
                        ronda + 1, enemigo.getNombre(), enemigo.getPuntosAtaque(), heroe.getNombre()));
                if (!heroe.isEstaVivo()) {
                    registro.append(String.format("%s ha caído en combate.%n", heroe.getNombre()));
                }
            }

            // Al finalizar la ronda se imprime el historial acumulado
            if (turnoConsumido) {
                ronda++;
                System.out.println();
                System.out.println("----- REGISTRO DE BATALLA -----");
                System.out.print(registro);
                System.out.printf("Vida %s: %d/%d | Vida %s: %d/%d%n",
                        heroe.getNombre(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                        enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
            }

        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        // Resultado final
        System.out.println();
        System.out.println("===== FIN DEL COMBATE =====");
        if (huyo) {
            System.out.println("Escapaste de la arena.");
        } else if (heroe.isEstaVivo()) {
            System.out.printf("¡Victoria! %s derrotó a %s.%n", heroe.getNombre(), enemigo.getNombre());
        } else {
            System.out.printf("Derrota... %s fue vencido por %s.%n", heroe.getNombre(), enemigo.getNombre());
        }

        teclado.close();
    }

    /**
     * Lee un entero del teclado. Si la entrada no es numérica devuelve -1
     * para que el programa no se interrumpa.
     */
    private static int leerEntero(Scanner teclado) {
        String linea = teclado.nextLine().trim();
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}