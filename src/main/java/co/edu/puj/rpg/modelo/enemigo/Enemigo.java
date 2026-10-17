package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

public class Enemigo {

    // ----- Atributos privados -----
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private boolean estaVivo;

    /**
     * Constructor con 'this' y validación de datos de entrada.
     */
    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Enemigo" : nombre;
        this.vidaMaxima = Math.max(vidaMaxima, 1);
        this.puntosAtaque = Math.max(puntosAtaque, 0);
        this.setPuntosVida(this.vidaMaxima); // puntosVida = vidaMaxima y estaVivo = true
    }

    // ===================== SETTER VALIDADO =====================

    /**
     * Regla de negocio:
     *  - vida <= 0          -> puntosVida = 0 y estaVivo = false
     *  - vida > vidaMaxima  -> puntosVida = vidaMaxima
     *  - valor intermedio   -> se asigna y estaVivo = true
     */
    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > this.vidaMaxima) {
            this.puntosVida = this.vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    // ===================== GETTERS PÚBLICOS =====================

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    // ===================== COMPORTAMIENTO =====================

    /**
     * Reduce la vida delegando las reglas al setter validado.
     */
    public void recibirDano(int dano) {
        if (dano < 0) {
            return; // Un daño negativo no puede curar
        }
        this.setPuntosVida(this.puntosVida - dano);
    }

    /**
     * Ataca al personaje usando únicamente su API pública (recibirDano).
     */
    public void atacar(Personaje personaje) {
        personaje.recibirDano(this.puntosAtaque);
    }
}
