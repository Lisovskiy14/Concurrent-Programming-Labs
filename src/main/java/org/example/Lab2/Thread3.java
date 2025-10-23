package org.example.Lab2;


public class Thread3 implements Runnable {
    private final Runnable task;

    public Thread3(Runnable task) {
        this.task = task;
    }

    @Override
    public void run() {

        // Виконання задачі
        task.run();

        // Сигнал потоку T1 про завершення роботи (S1.1)
        Main.sem.release();
    }
}
