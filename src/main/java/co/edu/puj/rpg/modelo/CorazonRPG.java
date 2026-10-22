package co.edu.puj.rpg.modelo;

/**
 * Núcleo de atributos vitales. COMPOSICIÓN 1..1 con Personaje:
 *  - Solo Personaje lo instancia (dentro de su constructor).
 *  - No hay setter público en Personaje que permita reemplazarlo.
 *  - Sus métodos de modificación son package-private: nadie fuera del
 *    paquete modelo puede alterarlo sin pasar por el Personaje.
 */
public class CorazonRPG {

    private int saludBase;
    private int manaBase;

    public CorazonRPG(int saludBase, int manaBase) {
        this.saludBase = Math.max(saludBase, 0);
        this.manaBase = Math.max(manaBase, 0);
    }

    public int getSaludBase() {
        return saludBase;
    }

    public int getManaBase() {
        return manaBase;
    }

    // Manipulación interna (visible solo dentro del paquete)
    void setManaBase(int manaBase) {
        this.manaBase = Math.max(manaBase, 0);
    }

    @Override
    public String toString() {
        return new StringBuilder("CorazonRPG[salud base=")
                .append(saludBase)
                .append(", maná base=")
                .append(manaBase)
                .append("]")
                .toString();
    }
}

