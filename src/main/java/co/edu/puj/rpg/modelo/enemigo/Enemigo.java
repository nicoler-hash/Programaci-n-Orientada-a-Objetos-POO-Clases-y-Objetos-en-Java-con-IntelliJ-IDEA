package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

/**
 * Enemigo controlado por la IA. Es un Personaje más, por lo que puede
 * guardarse y tratarse polimórficamente.
 */
public class Enemigo extends Personaje {

    public Enemigo(String nombre, int vida, int ataqueBase, int posX, int posY) {
        super(nombre, vida, ataqueBase, posX, posY);
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        System.out.printf("%s embiste a %s (%d de daño)%n", nombre, objetivo.getNombre(), ataqueBase);
        objetivo.defender(ataqueBase);
    }

    @Override
    public char getSimbolo() {
        return 'E';
    }
}
