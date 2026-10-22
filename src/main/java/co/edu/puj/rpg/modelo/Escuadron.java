package co.edu.puj.rpg.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AGREGACIÓN (0..*): los Personaje se crean fuera del escuadrón y siguen
 * existiendo intactos si el escuadrón se disuelve.
 */
public class Escuadron {

    private String nombre;
    private List<Personaje> miembros; // Agregación (0..*)

    public Escuadron(String nombre) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Escuadrón sin nombre" : nombre;
        this.miembros = new ArrayList<>(); // Solo se inicializa la colección, no los personajes
    }

    public boolean reclutarPersonaje(Personaje p) {
        if (p == null || miembros.contains(p)) {
            return false;
        }
        miembros.add(p); // se agrega la referencia; p conserva su instancia original
        return true;
    }

    public boolean expulsarPersonaje(Personaje p) {
        // p mantiene su estado, vida y objetos intactos en memoria
        return miembros.remove(p);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Escuadrón '").append(nombre).append("' (")
                .append(miembros.size()).append(" integrantes)");
        for (Personaje p : miembros) {
            sb.append(String.format("%n   - %s | Nivel %d | Ataque total %d",
                    p.getNombre(), p.getNivel(), p.calcularAtaqueTotal()));
        }
        return sb.toString();
    }
}