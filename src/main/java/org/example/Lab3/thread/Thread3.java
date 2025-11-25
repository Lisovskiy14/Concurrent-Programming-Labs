package org.example.Lab3.thread;

import org.example.Lab3.Data;
import org.example.Lab3.Monitor;

import java.util.Arrays;

public class Thread3 implements Runnable {
    private final Monitor monitor;

    public Thread3(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Чекати на завершення уведення даних у потоках T2, T4
        monitor.waitInput();

        // Обчислення e3 = min(Bh)
        int[] Bh = Data.divideVector(Data.B);
        int e3 = Arrays.stream(Bh).min().getAsInt();

        // Обчислення e = min(e, e3)
        monitor.calculateMinE(e3);

        // Обчислення S1h = sort(d*Bh + Z*(MMh*MX))
        Data.S13 = monitor.calculateS1h(Bh, Data.divideMatrix(Data.MM));

        // Сигнал потоку T2 про обчислення S1h
        monitor.signalS1h();

        // Чекати на завершення обчислення S1h в інших потоках
        monitor.waitS1h();

        // Обчислення S2h = sort(S1h, S1h)
        int[] combinedS1h = new int[Data.S13.length + Data.S14.length];
        System.arraycopy(Data.S13, 0, combinedS1h, 0, Data.S13.length);
        System.arraycopy(Data.S14, 0, combinedS1h, Data.S13.length, Data.S14.length);
        Arrays.sort(combinedS1h);
        Data.S22 = combinedS1h;

        // Сигнал потоку T4 про обчислення S2h
        monitor.signalS2h();

        // Чекати на завершення обчислення S в потоці T4
        monitor.waitS();

        // Копіювання e3 = e
        e3 = monitor.copyE();

        // Обчислення Xh = Sh * e3
        Data.calculateXh(Data.divideVector(Data.S), e3);

        // Сигнал потоку T4 про завершення обчислення Xh
        monitor.signalXh();
    }
}
