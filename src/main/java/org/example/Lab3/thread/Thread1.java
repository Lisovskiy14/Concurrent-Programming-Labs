package org.example.Lab3.thread;

import org.example.Lab3.Data;
import org.example.Lab3.Monitor;

import java.util.Arrays;

public class Thread1 implements Runnable {
    private final Monitor monitor;

    public Thread1(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Чекати на завершення уведення даних у потоках T2, T4
        monitor.waitInput();

        // Обчислення e1 = min(Bh)
        int[] Bh = Data.divideVector(Data.B);
        int e1 = Arrays.stream(Bh).min().getAsInt();

        // Обчислення e = min(e, e1)
        monitor.calculateMinE(e1);

        // Обчислення S1h = sort(d*Bh + Z*(MMh*MX))
        Data.S11 = monitor.calculateS1h(Bh, Data.divideMatrix(Data.MM));

        // Сигнал потокам T2, T3 про обчислення S1h
        monitor.signalS1h();

        // Чекати на завершення обчислення S в потоці T4
        monitor.waitS();

        // Копіювання e1 = e
        e1 = monitor.copyE();

        // Обчислення Xh = Sh * e1
        Data.calculateXh(Data.divideVector(Data.S), e1);

        // Сигнал потоку T4 про завершення обчислення Xh
        monitor.signalXh();
    }
}
