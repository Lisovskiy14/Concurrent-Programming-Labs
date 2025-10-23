package org.example.Lab2;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Thread4 implements Runnable {
    private final Runnable task;

    public Thread4(Runnable task) {
        this.task = task;
    }

    @Override
    public void run() {

        // Уведення E, D, MB
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(Main.sourcePath + Main.t4InputPath))) {
            Main.E = (int[]) is.readObject();
            Main.D = (int[]) is.readObject();
            Main.MB = (int[][]) is.readObject();
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
