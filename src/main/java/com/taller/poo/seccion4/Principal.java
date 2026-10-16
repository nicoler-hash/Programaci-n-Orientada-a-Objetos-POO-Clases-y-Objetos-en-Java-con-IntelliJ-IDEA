package com.taller.poo.seccion4;

import com.taller.poo.seccion1.CuentaBancaria;
import com.taller.poo.seccion1.Estudiante;
import com.taller.poo.seccion1.Vehiculo;
import com.taller.poo.seccion2.ConversorMoneda;

public class Principal {
    public static void main(String[] args) {
        // ----- Vehículos -----
        Vehiculo v1 = new Vehiculo("Toyota", "Corolla", 15000.50, 180, true);
        Vehiculo v2 = new Vehiculo();
        Vehiculo v3 = new Vehiculo("Mazda", "CX-5", 32000.00, 200, false);

        // ----- Estudiantes -----
        Estudiante e1 = new Estudiante("2024001", "Laura Martínez", 4.35, true);
        Estudiante e2 = new Estudiante("2024002", "Juan Rodríguez", 3.80, true);
        Estudiante e3 = new Estudiante("2024003", "Sofía Herrera", 2.95, false);

        // ----- Cuentas (los 3 constructores) -----
        CuentaBancaria c1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.0);
        CuentaBancaria c2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria c3 = new CuentaBancaria();

        // ----- Modificación de estado -----
        c1.depositar(250.75);
        c1.retirar(0);               // monto inválido: no cambia el saldo
        c2.depositar(100.00);
        c2.retirar(50.00);
        c3.depositar(75.50);
        boolean retiroExcesivo = c3.retirar(1000.00);

        // ----- Métodos estáticos -----
        double euros = ConversorMoneda.convertirDolaresAEuros(100.0, 0.92);
        String eurosFormateado = ConversorMoneda.formatearMoneda(euros);

        // ----- Reportes (solo salidas formateadas) -----
        Vehiculo[] vehiculos = {v1, v2, v3};
        System.out.printf("=== REPORTE DE VEHÍCULOS ===%n");
        for (int i = 0; i < vehiculos.length; i++) {
            Vehiculo v = vehiculos[i];
            System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                    i + 1, v.getMarca(), v.getModelo(), v.getKilometraje(), v.getVelocidadMaxima(), v.isEncendido());
        }

        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        Estudiante[] estudiantes = {e1, e2, e3};
        for (Estudiante e : estudiantes) {
            System.out.printf("%s%n", e.obtenerReporteAcademicos());
        }

        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        CuentaBancaria[] cuentas = {c1, c2, c3};
        for (CuentaBancaria c : cuentas) {
            System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: %s | Crédito: %s%n",
                    c.getNumeroCuenta(), c.getTitular(),
                    ConversorMoneda.formatearMoneda(c.getSaldo()),
                    ConversorMoneda.formatearMoneda(c.getLimiteCredito()));
        }
        System.out.printf("Retiro de $1000.00 en la cuenta %s aprobado: %b%n", c3.getNumeroCuenta(), retiroExcesivo);

        System.out.printf("%n=== CONVERSOR DE MONEDA ===%n");
        System.out.printf("100 USD a tasa 0.92 = %s EUR%n", eurosFormateado);
    }
}
