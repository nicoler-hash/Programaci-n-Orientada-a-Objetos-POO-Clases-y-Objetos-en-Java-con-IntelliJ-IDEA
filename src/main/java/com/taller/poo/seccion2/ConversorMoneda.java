package com.taller.poo.seccion2;

public class ConversorMoneda {
    // Constructor privado: impide instanciar la clase utilitaria.
    private ConversorMoneda() {}

    public static double convertirDolaresAEuros(double montoDolares, double tasaCambio) {
        if (montoDolares <= 0 || tasaCambio <= 0) {
            return 0.0;
        }
        return montoDolares * tasaCambio;
    }

    public static String formatearMoneda(double monto) {
        return String.format("$%.2f", monto);
    }

}
