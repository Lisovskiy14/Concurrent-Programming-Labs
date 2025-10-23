package org.example.Lab2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Thread2 implements Runnable {
    private final Runnable task;

    public Thread2(Runnable task) {
        this.task = task;
    }

    @Override
    public void run() {

        // Уведення B, MA, p
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(Main.sourcePath + Main.t2InputPath))) {
            Main.B = (int[]) is.readObject();
            Main.MA = (int[][]) is.readObject();
            Main.p = (int) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Виконання задачі
        task.run();

        // Сигнал потоку T1 про завершення роботи (S1.1)
        Main.sem.release();
    }
}
