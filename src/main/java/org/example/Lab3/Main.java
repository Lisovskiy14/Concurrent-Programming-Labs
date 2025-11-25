package org.example.Lab3;

import org.example.Lab3.thread.Thread1;
import org.example.Lab3.thread.Thread2;
import org.example.Lab3.thread.Thread3;
import org.example.Lab3.thread.Thread4;

/*
Лабораторна робота №3 варіант 23 X = sort(d*B + Z*(MM*MX)) * min(B)
Лісовський Назар Русланович, ІО-33
Дата 24.11.2025
 */
public class Main {

    public static void main(String[] args) {
        Monitor monitor = new Monitor();

        Data.generateRandomInputData();

        Thread t1 = new Thread(new Thread1(monitor), "Thread-0");
        Thread t2 = new Thread(new Thread2(monitor), "Thread-1");
        Thread t3 = new Thread(new Thread3(monitor), "Thread-2");
        Thread t4 = new Thread(new Thread4(monitor), "Thread-3");

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        System.out.println(String.format("Час виконання програми: %d мс", end - start));
    }
}
