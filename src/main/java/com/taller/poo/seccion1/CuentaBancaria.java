package com.taller.poo.seccion1;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    public CuentaBancaria() {
        this.numeroCuenta = "000000";
        this.titular = "Sin Nombre";
        this.saldo = 0.0;
        this.limiteCredito = 0.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0.0;
        this.limiteCredito = 100.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular, double saldo, Double limiteCredito) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        if (monto > 0) {
            // Si limiteCredito es null se considera 0 para evitar NullPointerException.
            double credito;
            if (limiteCredito != null) {
                credito = limiteCredito;
            } else {
                credito = 0.0;
            }
            if (saldo + credito >= monto) {
                saldo -= monto;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular() { return titular; }
    public double getSaldo() { return saldo; }
    public Double getLimiteCredito() { return limiteCredito; }

    /*
     * RESPUESTAS A LAS PREGUNTAS DE ANÁLISIS:
     * 1. 'saldo' (double primitivo) guarda el valor directamente dentro del objeto (8 bytes) y,
     *    si no se inicializa, vale 0.0 por defecto. 'limiteCredito' (Double) es una referencia a otro
     *    objeto en el heap: ocupa más memoria (referencia + objeto Double) y, si no se inicializa,
     *    vale null (no apunta a ningún objeto).
     * 2. Al operar aritméticamente con un Double se hace unboxing automático (limiteCredito.doubleValue()).
     *    Si la referencia es null no hay objeto del cual extraer el valor, por lo que se lanza
     *    NullPointerException. El double primitivo nunca es null: ya contiene 0.0 y opera con normalidad.
     */
}