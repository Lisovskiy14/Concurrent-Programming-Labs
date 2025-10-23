package org.example.Lab2;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/*
    Лабораторна робота ЛР2 Варіант 1
    A = (B*C)*D*p + E*(MA*MB)*x
    Введення/виведення даних:
    1 - A, C, x
    2 - B, MA, p
    3 - -
    4 - E, D, MB
    Лісовський Назар Русланович, ІО-33
    Дата 13.10.2025
 */
public class Main {
    private static final Random rand = new Random();
    private static final int maxNum = 3;
    private static final int N = 1000;
    private static final int threadsNum = 4;
    private static final int partLength = N / threadsNum;
    public static final String sourcePath = "src/main/resources/Lab2/input/";
    public static final String t1InputPath = "t1Input.txt";
    public static final String t2InputPath = "t2Input.txt";
    public static final String t4InputPath = "t4Input.txt";
    public static final String outputPath = "src/main/resources/Lab2/output/output.txt";

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
    public static final CyclicBarrier barrier = new CyclicBarrier(4);
    public static final Semaphore sem = new Semaphore(0);
    public static final Object monitorOb1 = new Object();
    public static final Object monitorOb2 = new Object();

    // Головний метод
    public static void main(String[] args) {

        // Створюємо файли для вхідних даних
        generateRandomInputData();

        // Описуємо спільну для потоків задачу
        Runnable task = () -> {

            // Сигнал бар’єру з подальшим очікуванням на решту потоків
            try {
                Main.barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Обчислення bi = (Bh * Ch)
            int bi = Main.scalarProduct(
                    Main.divideVector(Main.B),
                    Main.divideVector(Main.C)
            );

            // Обчислення b = b + bi (КД1)
            Main.b.addAndGet(bi);

            // Сигнал бар’єру з подальшим очікуванням на решту потоків
            try {
                Main.barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Копіювання b (КД2)
            int b = Main.b.get();

            // Копіювання p (КД3)
            int p;
            synchronized (Main.monitorOb1) {
                p = Main.p;
            }

            // Копіювання x (КД4)
            int x;
            synchronized (Main.monitorOb2) {
                x = Main.x;
            }

            // Обчислення Ah = b*Dh*p + E*(MAh*MB)*x
            Main.calculateAh(b, p, x);
        };

        // Створюємо потоки
        Thread t1 = new Thread(new Thread1(task));
        Thread t2 = new Thread(new Thread2(task));
        Thread t3 = new Thread(new Thread3(task));
        Thread t4 = new Thread(new Thread4(task));

        // Запуск
        double start = System.currentTimeMillis();
        t1.start(); t2.start(); t3.start(); t4.start();
        try {
            t1.join(); t2.join(); t3.join(); t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double end = System.currentTimeMillis();

        // Виведення часового результату
        System.out.println("Витрачений час: " + (end - start) + " мс.");
    }




    // Метод для генерації вхідних даних, заповнених 1
    private static void generateInputDataWith1() {
        int[] B = generateVectorAndFill(1);
        int[] C = generateVectorAndFill(1);
        int[] D = generateVectorAndFill(1);
        int p = 1;
        int[] E = generateVectorAndFill(1);
        int[][] MA = generateMatrixAndFill(1);
        int[][] MB = generateMatrixAndFill(1);
        int x = 1;

        writeInputData(B, C, D, p, E, MA, MB, x);
    }

    // Метод для генерації вхідних даних, заповнених випадковими числами
    private static void generateRandomInputData() {
        int[] B = generateRandomVector();
        int[] C = generateRandomVector();
        int[] D = generateRandomVector();
        int p = generateRandomScalar();
        int[] E = generateRandomVector();
        int[][] MA = generateRandomMatrix();
        int[][] MB = generateRandomMatrix();
        int x = generateRandomScalar();

        writeInputData(B, C, D, p, E, MA, MB, x);
    }

    // Метод для запису вхідних даних у файл
    private static void writeInputData(int[] B, int[] C, int[] D, int p,
                                       int[] E, int[][] MA, int[][] MB, int x) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sourcePath + t1InputPath))) {
            os.writeObject(C);
            os.writeObject(x);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sourcePath + t2InputPath))) {
            os.writeObject(B);
            os.writeObject(MA);
            os.writeObject(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sourcePath + t4InputPath))) {
            os.writeObject(E);
            os.writeObject(D);
            os.writeObject(MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
    public static void calculateAh(int b, int p, int x) {
        int[] Dh = divideVector(Main.D);
        int[][] MAh = divideMatrix(Main.MA);

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

        combineIntoA(Ah);
    }

    // Метод для поєднання частин Ah в A
    public static void combineIntoA(int[] Ah) {
        int number = getThreadNumber();
        int left = number * partLength;
        System.arraycopy(Ah, 0, A, left, partLength);
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

        try (BufferedWriter os = new BufferedWriter(new FileWriter(outputPath))) {
            os.write(Arrays.toString(A));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}