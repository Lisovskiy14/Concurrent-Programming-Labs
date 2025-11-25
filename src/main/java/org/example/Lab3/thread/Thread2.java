package org.example.Lab3.thread;

import org.example.Lab3.Data;
import org.example.Lab3.Monitor;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class Thread2 implements Runnable {
    private final Monitor monitor;

    public Thread2(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Уведення даних B, MX
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(Data.inputPath + Data.t2InputPath))) {
            Data.B = (int[]) is.readObject();
            Data.MX = (int[][]) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Сигнал іншим потокам про уведення даних
        monitor.signalInput();

        // Чекати на завершення уведення даних у потоці T4
        monitor.waitInput();

        // Обчислення e2 = min(Bh)
        int[] Bh = Data.divideVector(Data.B);
        int e2 = Arrays.stream(Bh).min().getAsInt();

        // Обчислення e = min(e, e2)
        monitor.calculateMinE(e2);

        // Обчислення S1h = sort(d*Bh + Z*(MMh*MX))
        Data.S12 = monitor.calculateS1h(Bh, Data.divideMatrix(Data.MM));

        // Сигнал потоку T3 про обчислення S1h
        monitor.signalS1h();

        // Чекати на завершення обчислення S1h в інших потоках
        monitor.waitS1h();

        // Обчислення S2h = sort(S1h, S1h)
        int[] combinedS1h = new int[Data.S11.length + Data.S12.length];
        System.arraycopy(Data.S11, 0, combinedS1h, 0, Data.S11.length);
        System.arraycopy(Data.S12, 0, combinedS1h, Data.S11.length, Data.S12.length);
        Arrays.sort(combinedS1h);
        Data.S21 = combinedS1h;

        // Сигнал потоку T4 про обчислення S2h
        monitor.signalS2h();

        // Чекати на завершення обчислення S в потоці T4
        monitor.waitS();

        // Копіювання e2 = e
        e2 = monitor.copyE();

        // Обчислення Xh = Sh * e2
        Data.calculateXh(Data.divideVector(Data.S), e2);

        // Сигнал потоку T4 про завершення обчислення Xh
        monitor.signalXh();
    }
}
