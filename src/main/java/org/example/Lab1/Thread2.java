package org.example.Lab1;


public class Thread2 implements Runnable {

    @Override
    public void run() {
        System.out.println("T2 розпочав свою роботу...");

        // Введення даних
        int[][] MG = Data.generateRandomMatrix();
        int[][] MK = Data.generateRandomMatrix();
        int[][] ML = Data.generateRandomMatrix();

        // Обробка даних
        int[][] MF = Data.matrixDifference(Data.matrixProduct(MG, Data.matrixProduct(MK, ML)), MK);

        // Виведення даних
        Data.writeResults(MF, Data.t2FilePath);

        System.out.println("T2 завершив свою роботу.");
    }
}
