package co.edu.puj.rpg.modelo;

/**
 * Contrato comportamental puro: toda entidad que participe en combate
 * debe saber defenderse. No tiene estado ni implementación.
 */
public interface ICombatiente {

    /**
     * Procesa el daño recibido aplicando la mitigación propia de la entidad.
     *
     * @param danoRecibido daño bruto antes de mitigación
     */
    void defender(int danoRecibido);
}
