package com.taller.poo;

import com.taller.poo.seccion1.CuentaBancaria;

import java.util.Locale;
import java.util.Scanner;

public class SistemaGestionCuentas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Locale.US: el punto es el separador decimal (evita errores si el sistema usa coma).
        scanner.useLocale(Locale.US);

        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria("1003", "Luis Torres", 300.00, 200.0);

        int opcion;
        do {
            System.out.printf("SISTEMA DE GESTIÓN DE CUENTAS ");
            System.out.printf("1. Depositar");
            System.out.printf("2. Retirar");
            System.out.printf("3. Ver reporte consolidado");
            System.out.printf("4. Salir");
            System.out.printf("Seleccione una opción: ");

            if (!scanner.hasNextInt()) {
                scanner.next(); // descarta la entrada inválida
                opcion = 0;
            } else {
                opcion = scanner.nextInt();
            }

            switch (opcion) {
                case 1:
                    System.out.printf("Número de cuenta: ");
                    String numDeposito = scanner.next();
                    System.out.printf("Monto a depositar: ");
                    if (!scanner.hasNextDouble()) {
                        scanner.next();
                        System.out.printf("Error: el monto debe ser un número válido.%n");
                        break;
                    }
                    double montoDeposito = scanner.nextDouble();
                    if (montoDeposito <= 0) {
                        System.out.printf("Error: el monto debe ser mayor a cero.%n");
                    } else {
                        CuentaBancaria cuentaD = buscarCuenta(cuentas, numDeposito);
                        if (cuentaD == null) {
                            System.out.printf("Error: la cuenta %s no existe.%n", numDeposito);
                        } else if (cuentaD.depositar(montoDeposito)) {
                            System.out.printf("Depósito exitoso. Nuevo saldo: %.2f%n", cuentaD.getSaldo());
                        } else {
                            System.out.printf("No se pudo realizar el depósito.%n");
                        }
                    }
                    break;

                case 2:
                    System.out.printf("Número de cuenta: ");
                    String numRetiro = scanner.next();
                    System.out.printf("Monto a retirar: ");
                    if (!scanner.hasNextDouble()) {
                        scanner.next();
                        System.out.printf("Error: el monto debe ser un número válido.%n");
                        break;
                    }
                    double montoRetiro = scanner.nextDouble();
                    if (montoRetiro <= 0) {
                        System.out.printf("Error: el monto debe ser mayor a cero.%n");
                    } else {
                        CuentaBancaria cuentaR = buscarCuenta(cuentas, numRetiro);
                        if (cuentaR == null) {
                            System.out.printf("Error: la cuenta %s no existe.%n", numRetiro);
                        } else if (cuentaR.retirar(montoRetiro)) {
                            System.out.printf("Retiro exitoso. Nuevo saldo: %.2f%n", cuentaR.getSaldo());
                        } else {
                            System.out.printf("Fondos insuficientes (saldo + crédito).%n");
                        }
                    }
                    break;

                case 3:
                    mostrarReporte(cuentas);
                    break;

                case 4:
                    System.out.printf("Saliendo del sistema. ¡Hasta pronto!%n");
                    break;

                default:
                    System.out.printf("Opción inválida. Intente de nuevo.%n");
            }
        } while (opcion != 4);

        scanner.close();
    }

    /** Recorre el arreglo buscando por numeroCuenta; retorna null si no existe. */
    private static CuentaBancaria buscarCuenta(CuentaBancaria[] cuentas, String numeroCuenta) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                return cuentas[i];
            }
        }
        return null;
    }

    private static void mostrarReporte(CuentaBancaria[] cuentas) {
        StringBuilder detalle = new StringBuilder();
        double total = 0.0;
        for (int i = 0; i < cuentas.length; i++) {
            detalle.append(String.format("Cuenta: %s | Titular: %s | Saldo: %.2f%n",
                    cuentas[i].getNumeroCuenta(), cuentas[i].getTitular(), cuentas[i].getSaldo()));
            total += cuentas[i].getSaldo();
        }
        System.out.printf("%n=== REPORTE CONSOLIDADO ===%n");
        System.out.printf("%s", detalle);
        System.out.printf("Saldo total acumulado: %.2f%n", total);
    }
}