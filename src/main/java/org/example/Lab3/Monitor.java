package org.example.Lab3;

public class Monitor {
    private int F1 = 0;
    private int F2 = 0;
    private int F3 = 0;
    private int F4 = 0;
    private int F5 = 0;

    public synchronized void signalInput() {
        F1++;
        if (F1 == 2) notifyAll();
    }

    public synchronized void waitInput() {
        try {
            while (F1 < 2) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void signalS1h() {
        F2++;
        if (F2 == 4) notifyAll();
    }

    public synchronized void waitS1h() {
        try {
            while (F2 < 4) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void signalS2h() {
        F3++;
        if (F3 == 2) notifyAll();
    }

    public synchronized void waitS2h() {
        try {
            while (F3 < 2) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void signalS() {
        F4++;
        if (F4 == 1) notifyAll();
    }

    public synchronized void waitS() {
        try {
            while (F4 < 1) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void signalXh() {
        F5++;
        if (F5 == 3) notifyAll();
    }

    public synchronized void waitXh() {
        try {
            while (F5 < 3) wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void calculateMinE(int eh) {
        Data.e = Math.min(Data.e, eh);
    }

    public synchronized int[] calculateS1h(int[] Bh, int[][] MMh) {
        return Data.calculateS1h(Bh, MMh);
    }

    public synchronized int copyE() {
        return Data.e;
    }
}
