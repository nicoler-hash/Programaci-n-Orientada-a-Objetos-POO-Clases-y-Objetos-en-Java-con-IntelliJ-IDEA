package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {

    // ----- Atributos privados: inaccesibles desde Main o cualquier otra clase -----
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel = 1;          // Nivel inicial por defecto
    private double oro;
    private boolean estaVivo;

    // Datos del último ataque (también privados) para poder registrarlos en el log
    private int ultimoDano;
    private boolean ultimoGolpeCritico;

    /**
     * Constructor completo. Usa 'this' para diferenciar parámetros de atributos
     * y valida los datos para que el objeto nazca siempre en un estado coherente.
     */
    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Héroe" : nombre;
        this.vidaMaxima = Math.max(vidaMaxima, 1);        // Vida máxima mínima: 1
        this.puntosAtaque = Math.max(puntosAtaque, 0);    // Ataque no negativo
        this.oro = Math.max(oro, 0.0);                    // Oro inicial no negativo
        this.nivel = 1;
        // Se inicializa la vida a través del setter (puntosVida = vidaMaxima, estaVivo = true)
        this.setPuntosVida(this.vidaMaxima);
    }

    // ===================== SETTERS CON VALIDACIÓN =====================

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

    /**
     * Regla de negocio: el oro nunca puede ser negativo.
     * Si la operación dejaría el saldo en negativo, se rechaza sin alterar el atributo.
     */
    public void setOro(double oro) {
        if (oro < 0.0) {
            System.out.println("Saldo insuficiente: la operación fue rechazada.");
            return; // El atributo NO se modifica
        }
        this.oro = oro;
    }

    /**
     * Regla de negocio: el nivel mínimo es 1.
     * Si el valor es inválido se conserva el nivel actual.
     */
    public void setNivel(int nivel) {
        if (nivel < 1) {
            return; // Se mantiene el valor actual
        }
        this.nivel = nivel;
    }

    // ===================== GETTERS (API pública de lectura) =====================

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

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public int getUltimoDano() {
        return ultimoDano;
    }

    public boolean isUltimoGolpeCritico() {
        return ultimoGolpeCritico;
    }

    // ===================== MÉTODOS DE COMPORTAMIENTO =====================

    /**
     * Ataca al enemigo. Con 15% de probabilidad el golpe es crítico (daño x2).
     * El daño se aplica a través de enemigo.recibirDano(), nunca tocando
     * directamente los atributos privados del enemigo.
     */
    public void atacar(Enemigo enemigo) {
        int dano = this.puntosAtaque;
        this.ultimoGolpeCritico = Math.random() < 0.15;
        if (this.ultimoGolpeCritico) {
            dano *= 2;
        }
        this.ultimoDano = dano;
        enemigo.recibirDano(dano);
    }

    /**
     * Restaura vida. El tope de vidaMaxima y el estado estaVivo
     * los controla setPuntosVida().
     */
    public void usarPocion(int cantidadRestaurada) {
        if (cantidadRestaurada <= 0) {
            return; // Una poción no puede quitar vida
        }
        this.setPuntosVida(this.puntosVida + cantidadRestaurada);
    }

    /**
     * Reduce vida delegando la validación (piso en 0 y estaVivo = false) al setter.
     */
    public void recibirDano(int cantidadDano) {
        if (cantidadDano < 0) {
            return; // Un daño negativo no puede curar
        }
        this.setPuntosVida(this.puntosVida - cantidadDano);
    }
    }
