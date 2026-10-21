package co.edu.puj.rpg.modelo.inventario;

import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor genérico. Con InventarioT&lt;Pocion&gt; el compilador solo permite
 * almacenar Pocion y devuelve Pocion sin casteo explícito.
 *
 * Nota: el archivo se llama InventarioT.java (como en la estructura del proyecto),
 * por eso la clase pública se llama InventarioT. Si prefieres el nombre Inventario<T>
 * de la guía, renombra la clase con Shift+F6 en IntelliJ y se actualiza todo.
 */
public class InventarioT<T> {

    private final List<T> items = new ArrayList<>();

    public void agregar(T item) {
        if (item != null) {
            items.add(item);
        }
    }

    public T obtener(int index) {
        return items.get(index);
    }

    /** Elimina y devuelve el elemento de la posición indicada. */
    public T remover(int index) {
        return items.remove(index);
    }

    public int size() {
        return items.size();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public List<T> getItems() {
        return items;
    }
}