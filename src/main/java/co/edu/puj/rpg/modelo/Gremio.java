package co.edu.puj.rpg.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Asociación BIDIRECCIONAL con Personaje (0..* a 1). Ambos extremos se
 * sincronizan; las validaciones evitan la recursión infinita.
 */
public class Gremio {

    private final String nombre;
    private final List<Personaje> miembros = new ArrayList<>();

    public Gremio(String nombre) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Gremio sin nombre" : nombre;
    }

    public void agregarMiembro(Personaje p) {
        if (p == null || miembros.contains(p)) {
            return; // evita nulos y duplicados (y corta la recursión)
        }
        miembros.add(p);
        if (p.getGremio() != this) {
            p.unirseAGremio(this); // sincronización cruzada
        }
    }

    public void removerMiembro(Personaje p) {
        if (p == null) {
            return;
        }
        if (miembros.remove(p) && p.getGremio() == this) {
            p.salirDeGremio(); // sincronización cruzada
        }
    }

    public String getNombre() {
        return nombre;
    }

    /** Vista de solo lectura: nadie puede alterar la lista sin pasar por agregar/remover. */
    public List<Personaje> getMiembros() {
        return Collections.unmodifiableList(miembros);
    }

    /** Solo imprime NOMBRES de los miembros para evitar StackOverflowError. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Gremio '").append(nombre).append("' (")
                .append(miembros.size()).append(" miembros): ");
        if (miembros.isEmpty()) {
            sb.append("sin miembros");
        } else {
            for (int i = 0; i < miembros.size(); i++) {
                sb.append(miembros.get(i).getNombre());
                if (i < miembros.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }
}
