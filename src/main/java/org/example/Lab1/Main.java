package org.example.Lab1;

/*
Лабораторна робота ЛР1 Варіант 14 (1.27, 2.2, 3.22)
F1: e = (A*B) + (C*(D*(MA*MD)))
F2: MF = MG*(MK*ML) – MK
F3: S = SORT(O*(MS*MT)) – P
Лісовський Назар Русланович, ІО-33
Дата 05.09.2025
 */

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\nN = " + Data.N + "\n");

        // Ініціалізація потоків
        Thread t1 = new Thread(new Thread1());
        t1.setPriority(1);
        Thread t2 = new Thread(new Thread2());
        t2.setPriority(2);
        Thread t3 = new Thread(new Thread3());
        t3.setPriority(3);

        // Запуск потоків на виконання
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t3.start();

        // Очікування до їх повного виконання
        t1.join();
        t2.join();
        t3.join();
        long end = System.currentTimeMillis();

        System.out.println("\nЧас виконання програми: " + (end - start) + " мс.");
    }
}