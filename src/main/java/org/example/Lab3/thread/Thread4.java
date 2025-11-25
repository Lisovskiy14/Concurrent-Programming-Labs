package org.example.Lab3.thread;

import org.example.Lab3.Data;
import org.example.Lab3.Monitor;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class Thread4 implements Runnable {
    private final Monitor monitor;

    public Thread4(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        // Уведення даних MM, Z, d
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(Data.inputPath + Data.t4InputPath))) {
            Data.MM = (int[][]) is.readObject();
            Data.Z = (int[]) is.readObject();
            Data.d = (int) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Сигнал іншим потокам про уведення даних
        monitor.signalInput();

        // Чекати на завершення уведення даних у потоці T2
        monitor.waitInput();

        // Обчислення e4 = min(Bh)
        int[] Bh = Data.divideVector(Data.B);
        int e4 = Arrays.stream(Bh).min().getAsInt();

        // Обчислення e = min(e, e2)
        monitor.calculateMinE(e4);

        // Обчислення S1h = sort(d*Bh + Z*(MMh*MX))
        Data.S14 = monitor.calculateS1h(Bh, Data.divideMatrix(Data.MM));

        // Сигнал потокам T2, T3 про обчислення S1h
        monitor.signalS1h();

        // Чекати на завершення обчислення S2h в потоках T2, T3
        monitor.waitS2h();

        // Обчислення S = sort(S2h, S2h)
        int[] combinedS2h = new int[Data.S21.length + Data.S22.length];
        System.arraycopy(Data.S21, 0, combinedS2h, 0, Data.S21.length);
        System.arraycopy(Data.S22, 0, combinedS2h, Data.S21.length, Data.S22.length);
        Arrays.sort(combinedS2h);
        Data.S = combinedS2h;

        // Сигнал іншим потокам про обчислення S
        monitor.signalS();

        // Копіювання e4 = e
        e4 = monitor.copyE();

        // Обчислення Xh = Sh * e4
        Data.calculateXh(Data.divideVector(Data.S), e4);

        // Чекати на завершення обчислення Xh в інших потоках
        monitor.waitXh();

        // Виведення X
        Data.writeResult();
    }
}
