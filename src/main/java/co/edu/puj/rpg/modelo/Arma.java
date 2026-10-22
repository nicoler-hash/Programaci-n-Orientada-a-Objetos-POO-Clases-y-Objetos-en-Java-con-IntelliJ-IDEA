package co.edu.puj.rpg.modelo;

/**
 * Arma equipable. Asociación UNIDIRECCIONAL: el Personaje conoce a su Arma,
 * pero el Arma no guarda ninguna referencia a su dueño.
 */
public class Arma {

    private final String nombre;
    private final int danioBase;

    public Arma(String nombre, int danioBase) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Arma sin nombre" : nombre;
        this.danioBase = Math.max(danioBase, 0);
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanioBase() {
        return danioBase;
    }

    @Override
    public String toString() {
        return nombre + " (daño +" + danioBase + ")";
    }
}
