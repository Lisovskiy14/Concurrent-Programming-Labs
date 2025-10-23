package org.example.Lab2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Thread1 implements Runnable {

    private final Runnable task;

    public Thread1(Runnable task) {
        this.task = task;
    }

    @Override
    public void run() {

        // Уведення C, x
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(Main.sourcePath + Main.t1InputPath))) {
            Main.C = (int[]) is.readObject();
            Main.x = (int) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Виконання задачі
        task.run();

        // Чекати на завершення обчислень Ah в інших потоках (W3.(2,3,4))
        try {
            Main.sem.acquire(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Виведення даних А
        Main.writeResult();
    }
}
