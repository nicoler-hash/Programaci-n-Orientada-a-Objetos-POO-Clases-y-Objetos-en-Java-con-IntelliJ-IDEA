package co.edu.puj.rpg.modelo;

/**
 * Ítem consumible que se almacena en Inventario&lt;Pocion&gt;.
 * Encapsulada: atributos private + getters públicos.
 */
public class Pocion {

    private final String nombre;
    private final int puntosCuracion;

    public Pocion(String nombre, int puntosCuracion) {
        this.nombre = nombre;
        this.puntosCuracion = Math.max(puntosCuracion, 0);
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosCuracion() {
        return puntosCuracion;
    }

    @Override
    public String toString() {
        return nombre + " (+" + puntosCuracion + " vida)";
    }
}
