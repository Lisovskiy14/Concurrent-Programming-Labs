package org.example.Lab2.deprecated;


public class MySemaphore {
    private final int requiredSignals;
    private final int requiredWaited;
    private int signals = 0;
    private boolean wasNotified = false;
    private int waitedThreads = 0;

    public MySemaphore(int requiredSignals, int requiredWaited) {
        this.requiredSignals = requiredSignals;
        this.requiredWaited = requiredWaited;
    }

    public void doWait() {
        synchronized (this) {
            while (!wasNotified) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            waitedThreads++;
            if (waitedThreads == requiredWaited) {
                wasNotified = false;
            }
        }
    }

    public synchronized void doNotify() {
        signals++;
        if (signals == requiredSignals) {
            notifyAll();
            signals = 0;
            wasNotified = true;
        }
    }
}
