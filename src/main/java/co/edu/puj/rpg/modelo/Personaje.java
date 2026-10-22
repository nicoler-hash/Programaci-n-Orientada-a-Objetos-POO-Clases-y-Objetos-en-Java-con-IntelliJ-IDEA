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
    protected int nivel = 1;

    // ----- Relaciones entre clases (Unidad 2.5) -----
    private Arma armaEquipada;            // Asociación unidireccional (0..1)
    private Gremio gremio;                // Asociación bidireccional (0..*  a 1)
    private final CorazonRPG corazon;     // Composición (1..1)

    public Personaje(String nombre, int vida, int ataqueBase, int posicionX, int posicionY) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Sin nombre" : nombre;
        this.vida = Math.max(vida, 1);
        this.vidaMaxima = this.vida;
        this.ataqueBase = Math.max(ataqueBase, 0);
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        // COMPOSICIÓN: el corazón se crea aquí dentro; no existe setter que lo reemplace
        this.corazon = new CorazonRPG(this.vida, 0);
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

    // ===================== Asociación unidireccional: Arma =====================

    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) {
            System.out.println("No se puede equipar un arma nula.");
            return;
        }
        this.armaEquipada = nuevaArma;
        System.out.println(nombre + " equipa " + nuevaArma);
    }

    public void desequiparArma() {
        if (this.armaEquipada != null) {
            System.out.println(nombre + " guarda " + armaEquipada.getNombre() + " y queda desarmado.");
            this.armaEquipada = null;
        } else {
            System.out.println(nombre + " no tiene ningún arma para retirar.");
        }
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public int calcularAtaqueTotal() {
        int ataqueTotal = this.nivel * 5;
        if (this.armaEquipada != null) { // evita NullPointerException
            ataqueTotal += this.armaEquipada.getDanioBase();
        }
        return ataqueTotal;
    }

    // ===================== Asociación bidireccional: Gremio =====================

    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null || nuevoGremio == this.gremio) {
            return;
        }
        if (this.gremio != null) {
            salirDeGremio(); // se desvincula del gremio anterior
        }
        this.gremio = nuevoGremio;
        nuevoGremio.agregarMiembro(this); // sincronización cruzada
    }

    public void salirDeGremio() {
        if (this.gremio == null) {
            return;
        }
        Gremio anterior = this.gremio;
        this.gremio = null;
        anterior.removerMiembro(this); // sincronización cruzada
    }

    public Gremio getGremio() {
        return gremio;
    }

    // ===================== Composición: CorazonRPG (sin getter/setter del objeto) =====================

    public int getSaludBase() {
        return corazon.getSaludBase();
    }

    public int getManaBase() {
        return corazon.getManaBase();
    }

    /** Las subclases (mismo paquete) configuran el maná del corazón al construirse. */
    protected void configurarManaBase(int mana) {
        corazon.setManaBase(mana);
    }

    // ===================== Nivel =====================

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        if (nivel < 1) {
            return; // se conserva el valor actual
        }
        this.nivel = nivel;
    }

    /** Solo muestra el NOMBRE del gremio para no provocar recursión infinita. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("{nombre=").append(nombre)
                .append(", nivel=").append(nivel)
                .append(", vida=").append(vida).append("/").append(vidaMaxima)
                .append(", ataqueTotal=").append(calcularAtaqueTotal())
                .append(", arma=").append(armaEquipada != null ? armaEquipada : "ninguna")
                .append(", gremio=").append(gremio != null ? gremio.getNombre() : "ninguno")
                .append(", ").append(corazon)
                .append("}");
        return sb.toString();
    }
}