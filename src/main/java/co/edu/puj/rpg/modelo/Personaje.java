package co.edu.puj.rpg.modelo;

/**
 * Clase abstracta base. Concentra el estado común (protected, visible para las
 * subclases aunque estén en otro paquete) y la lógica compartida. Obliga a las
 * subclases a definir su ataque especial. No puede instanciarse directamente.
 */
public abstract class Personaje implements ICombatiente {

    protected String nombre;
    protected int vida;
    protected int vidaMaxima;
    protected int ataqueBase;
    protected int posicionX; // columna en el tablero
    protected int posicionY; // fila en el tablero

    public Personaje(String nombre, int vida, int ataqueBase, int posicionX, int posicionY) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Sin nombre" : nombre;
        this.vida = Math.max(vida, 1);
        this.vidaMaxima = this.vida;
        this.ataqueBase = Math.max(ataqueBase, 0);
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    // ----- Método abstracto: cada subclase define su ataque único -----
    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    // ----- Implementación base del contrato ICombatiente -----
    @Override
    public void defender(int danoRecibido) {
        recibirDano(danoRecibido);
        System.out.printf("   -> %s recibe %d de daño (vida: %d/%d)%n",
                nombre, danoRecibido, vida, vidaMaxima);
        if (!estaVivo()) {
            System.out.printf("   -> %s ha caído en combate.%n", nombre);
        }
    }

    // ----- Métodos concretos compartidos -----
    public boolean estaVivo() {
        return vida > 0;
    }

    /** Descuenta salud garantizando que no descienda de cero. */
    public void recibirDano(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        this.vida = Math.max(this.vida - cantidad, 0);
    }

    /** Recupera salud sin superar vidaMaxima. */
    public void curar(int cantidad) {
        if (cantidad <= 0 || !estaVivo()) {
            return;
        }
        this.vida = Math.min(this.vida + cantidad, this.vidaMaxima);
    }

    /** Gancho que se ejecuta al iniciar el turno (las subclases pueden sobreescribirlo). */
    public void iniciarTurno() {
        // Por defecto no hace nada
    }

    /** Alcance de ataque en celdas (distancia de Chebyshev). */
    public int getAlcance() {
        return 1;
    }

    /** Carácter con el que se dibuja al personaje en el tablero. */
    public char getSimbolo() {
        return '?';
    }

    /** Línea de estado para la consola. */
    public String getResumen() {
        return String.format("%s [%c] Vida: %d/%d | Pos: (%d,%d)",
                nombre, getSimbolo(), vida, vidaMaxima, posicionX, posicionY);
    }

    // ----- Getters / setter de posición -----
    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }
}