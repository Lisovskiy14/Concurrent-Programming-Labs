package org.example.Lab1;


public class Thread1 implements Runnable {

    @Override
    public void run() {
        System.out.println("T1 розпочав свою роботу...");

        // Введення даних
        int[] A = Data.generateRandomVector();
        int[] B = Data.generateRandomVector();
        int[] C = Data.generateRandomVector();
        int[] D = Data.generateRandomVector();
        int[][] MA = Data.generateRandomMatrix();
        int[][] MD = Data.generateRandomMatrix();

        // Обробка даних
        int e = Data.scalarProduct(A, B) + Data.scalarProduct(C, Data.matrixProduct(
                new int[][] {D},
                Data.matrixProduct(MA, MD)
        )[0]);

        // Виведення даних
        Data.writeResults(e, Data.t1FilePath);

        System.out.println("T1 завершив свою роботу.");
    }
}
