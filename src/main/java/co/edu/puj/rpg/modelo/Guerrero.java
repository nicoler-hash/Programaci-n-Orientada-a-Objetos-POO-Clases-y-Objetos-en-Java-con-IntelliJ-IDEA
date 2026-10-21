package co.edu.puj.rpg.modelo;

/** Especialización de combate físico. */
public class Guerrero extends Personaje {

    protected int fuerzaEscudo; // atributo específico

    public Guerrero(String nombre, int vida, int ataqueBase, int posX, int posY, int fuerzaEscudo) {
        super(nombre, vida, ataqueBase, posX, posY); // inicializa el estado heredado primero
        this.fuerzaEscudo = Math.max(fuerzaEscudo, 0);
    }

    // ----- Sobrecarga (polimorfismo estático) -----
    public void atacar(Personaje objetivo) {
        atacar(objetivo, false);
    }

    public void atacar(Personaje objetivo, boolean modoFuria) {
        int dano = modoFuria ? ataqueBase * 2 : ataqueBase;
        if (modoFuria) {
            System.out.printf("%s descarga un GOLPE FURIOSO sobre %s (%d de daño)%n",
                    nombre, objetivo.getNombre(), dano);
        } else {
            System.out.printf("%s golpea a %s (%d de daño)%n", nombre, objetivo.getNombre(), dano);
        }
        objetivo.defender(dano);
    }

    // ----- Sobreescritura (polimorfismo dinámico) -----
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        atacar(objetivo, true);
    }

    /** El escudo absorbe parte del daño antes de aplicar la defensa base. */
    @Override
    public void defender(int danoRecibido) {
        int absorbido = fuerzaEscudo / 2;
        int reducido = Math.max(danoRecibido - absorbido, 1);
        System.out.printf("   (El escudo de %s absorbe %d de daño)%n", nombre, danoRecibido - reducido);
        super.defender(reducido);
    }

    @Override
    public char getSimbolo() {
        return 'G';
    }

    @Override
    public String getResumen() {
        return super.getResumen() + " | Escudo: " + fuerzaEscudo;
    }

    public int getFuerzaEscudo() {
        return fuerzaEscudo;
    }
}