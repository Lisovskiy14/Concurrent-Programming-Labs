package org.example.Lab1;

import java.util.Arrays;

public class Thread3 implements Runnable {

    @Override
    public void run() {
        System.out.println("T3 розпочав свою роботу...");

        // Введення даних
        int[] O = Data.generateRandomVector();
        int[] P = Data.generateRandomVector();
        int[][] MS = Data.generateRandomMatrix();
        int[][] MT = Data.generateRandomMatrix();

        // Обробка даних
        int[] S = Data.vectorDifference(
                Arrays.stream(Data.matrixProduct(
                        new int[][] {O},
                        Data.matrixProduct(MS, MT)
                )[0]).sorted().toArray(),
                P
        );

        // Виведення даних
        Data.writeResults(S, Data.t3FilePath);

        System.out.println("T3 завершив свою роботу.");
    }
}
