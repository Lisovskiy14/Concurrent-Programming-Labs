package org.example.Lab2.deprecated;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Data {
    private static final Random rand = new Random();
    private static final int maxNum = 3;
    private static final int N = 1000;
    private static final int threadsNum = 4;
    private static final int partLength = N / threadsNum;
    public static final String resultPath = "src/main/resources/Lab2/result.txt";

    // Спільні ресурси
    public static volatile int[] A = new int[N];
    public static volatile int[] B;
    public static volatile int[] C;
    public static volatile int[] D;
    public static volatile int p;
    public static volatile int[] E;
    public static volatile int[][] MA;
    public static volatile int[][] MB;
    public static volatile int x;
    public static volatile AtomicInteger b = new AtomicInteger(0);
    public static final MySemaphore sem1 = new MySemaphore(3, 4);
    public static final MySemaphore sem2 = new MySemaphore(4, 4);
    public static final MySemaphore sem3 = new MySemaphore(3, 1);
    public static final Object monitorOb = new Object();



    // Метод для генерації випадкового скаляра
    public static int generateRandomScalar() {
        return rand.nextInt(maxNum);
    }

    // Метод для генерації векторів з випадковими числами
    public static int[] generateRandomVector() {
        int[] vector = new int[N];
        for (int i = 0; i < N; i++) {
            vector[i] = rand.nextInt(maxNum);
        }
        return vector;
    }

    // Метод для генерації векторів, заповненими заданим числом
    public static int[] generateVectorAndFill(int num) {
        int[] vector = new int[N];
        Arrays.fill(vector, num);
        return vector;
    }

    // Метод для генерації матриць з випадковими числами
    public static int[][] generateRandomMatrix() {
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = rand.nextInt(maxNum);
            }
        }
        return matrix;
    }

    // Метод для генерації матриць із заданим числом
    public static int[][] generateMatrixAndFill(int num) {
        int[][] matrix = new int[N][N];
        for (int[] column : matrix) {
            Arrays.fill(column, num);
        }
        return matrix;
    }



    // Метод для обчислення скалярного добутку
    public static int scalarProduct(int[] A, int[] B) {
        int result = 0;
        for (int i = 0; i < A.length; i++) {
            result += A[i] * B[i];
        }
        return result;
    }

    // Метод для обчислення Ah
    public static int[] calculateAh(int b, int[] Dh, int p, int[] E, int[][] MAh, int[][] MB, int x) {

        int[] leftVector = new int[partLength];
        for (int i = 0; i < partLength; i++) {
            leftVector[i] = Dh[i] * b * p;
        }

        int[][] MC = new int[partLength][N];
        for (int i = 0; i < partLength; i++) {
            for (int j = 0; j < N; j++) {
                int product = 0;
                for (int k = 0; k < N; k++) {
                    product += MAh[i][k] * MB[k][j];
                }
                MC[i][j] = product;
            }
        }

        int[] rightVector = new int[partLength];
        for (int i = 0; i < partLength; i++) {
            int product = 0;
            for (int j = 0; j < N; j++) {
                product += E[j] * MC[i][j];
            }
            rightVector[i] = product * x;
        }

        int[] Ah = new int[partLength];
        for (int i = 0; i < partLength; i++) {
            Ah[i] = leftVector[i] + rightVector[i];
        }

        return Ah;
    }

    // Метод для поєднання частин Ah в A
    public static void combineIntoA(int[] Ah) {
        int number = getThreadNumber();
        int left = number * partLength;
        synchronized (monitorOb) {
            System.arraycopy(Ah, 0, A, left, partLength);
        }
    }


    // Метод для поділу вектора між Потоками
    public static int[] divideVector(int[] vector) {
        int number = getThreadNumber();
        int left = number * partLength;

        int[] result = new int[partLength];
        System.arraycopy(vector, left, result, 0, partLength);

        return result;
    }

    // Метод для поділу матриці між Потоками
    public static int[][] divideMatrix(int[][] matrix) {
        int number = getThreadNumber();
        int left = number * partLength;

        int[][] result = new int[N][partLength];
        System.arraycopy(matrix, left, result, 0, partLength);

        return result;
    }

    private static int getThreadNumber() {
        return Integer.parseInt(Thread.currentThread().getName().substring(7, 8));
    }

    // Метод для виведення результату у файл
    public static void writeResult() {

        try (BufferedWriter os = new BufferedWriter(new FileWriter(resultPath))) {
            os.write(Arrays.toString(A));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
