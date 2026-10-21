package co.edu.puj.rpg.modelo.tablero;

import co.edu.puj.rpg.modelo.Personaje;

import java.util.List;

/**
 * Mapa 5x5 representado con char[][]. Las celdas se indexan [fila = Y][columna = X].
 */
public class Tablero {

    public static final int TAMANO = 5;
    public static final char VACIA = '.';
    public static final char COFRE = 'C';
    public static final char FUERA = '#'; // centinela para coordenadas inválidas

    private final char[][] matriz = new char[TAMANO][TAMANO];
    private final int cofreX;
    private final int cofreY;

    public Tablero(int cofreX, int cofreY) {
        this.cofreX = cofreX;
        this.cofreY = cofreY;
        limpiar();
    }

    private void limpiar() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = VACIA;
            }
        }
        matriz[cofreY][cofreX] = COFRE;
    }

    /** Redibuja el mapa a partir del estado actual de ambos bandos (solo los vivos). */
    public void actualizar(List<? extends Personaje> heroes, List<? extends Personaje> enemigos) {
        limpiar();
        for (Personaje p : heroes) {
            if (p.estaVivo() && estaDentro(p.getPosicionX(), p.getPosicionY())) {
                matriz[p.getPosicionY()][p.getPosicionX()] = p.getSimbolo();
            }
        }
        for (Personaje p : enemigos) {
            if (p.estaVivo() && estaDentro(p.getPosicionX(), p.getPosicionY())) {
                matriz[p.getPosicionY()][p.getPosicionX()] = p.getSimbolo();
            }
        }
    }

    public boolean estaDentro(int x, int y) {
        return x >= 0 && x < TAMANO && y >= 0 && y < TAMANO;
    }

    public char getCelda(int x, int y) {
        return estaDentro(x, y) ? matriz[y][x] : FUERA;
    }

    public boolean esCofre(int x, int y) {
        return x == cofreX && y == cofreY;
    }

    public void mostrarTablero() {
        System.out.println("     0  1  2  3  4  (columna X)");
        for (int i = 0; i < matriz.length; i++) {
            System.out.print("  " + i + " ");
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print("[" + matriz[i][j] + "]");
            }
            System.out.println();
        }
        System.out.println("(fila Y)  G=Guerrero M=Mago E=Enemigo C=Cofre .=Vacía");
    }
}