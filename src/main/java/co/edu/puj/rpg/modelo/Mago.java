package co.edu.puj.rpg.modelo;

/** Especialización de combate mágico a distancia. */
public class Mago extends Personaje {

    private static final int COSTO_HECHIZO = 10;
    private static final int REGENERACION_MANA = 5;

    protected int puntosMana; // atributo específico

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, ataqueBase, posX, posY);
        this.puntosMana = Math.max(puntosMana, 0);
        configurarManaBase(this.puntosMana); // el CorazonRPG registra el maná base
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (puntosMana >= COSTO_HECHIZO) {
            puntosMana -= COSTO_HECHIZO;
            System.out.printf("%s lanza una BOLA DE FUEGO sobre %s (%d de daño, maná: %d)%n",
                    nombre, objetivo.getNombre(), ataqueBase, puntosMana);
            objetivo.defender(ataqueBase);
        } else {
            int dano = Math.max(ataqueBase / 5, 1);
            System.out.printf("%s no tiene maná y golpea con el bastón a %s (%d de daño)%n",
                    nombre, objetivo.getNombre(), dano);
            objetivo.defender(dano);
        }
    }

    /** Regenera un poco de maná al comenzar cada turno. */
    @Override
    public void iniciarTurno() {
        puntosMana += REGENERACION_MANA;
    }

    @Override
    public int getAlcance() {
        return 3;
    }

    @Override
    public char getSimbolo() {
        return 'M';
    }

    @Override
    public String getResumen() {
        return super.getResumen() + " | Maná: " + puntosMana;
    }

    public int getPuntosMana() {
        return puntosMana;
    }
}