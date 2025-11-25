package org.example.Lab3;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Data {
    private static final Random rand = new Random();
    private static final int maxNum = 3;
    private static final int N = 1000;
    private static final int threadsNum = 4;
    private static final int partLength = N / threadsNum;

    public static final String inputPath = "src/main/resources/Lab3/input/";
    public static final String t2InputPath = "t2Input.txt";
    public static final String t4InputPath = "t4Input.txt";
    public static final String outputPath = "src/main/resources/Lab3/output/output.txt";

    public static int d;
    public static int[] B;
    public static int[] Z;
    public static int[][] MM;
    public static int[][] MX;
    public static int[] X = new int[N];

    public static int e = Integer.MAX_VALUE;
    public static int[] S;

    public static int[] S11;
    public static int[] S12;
    public static int[] S13;
    public static int[] S14;
    public static int[] S21;
    public static int[] S22;


    // Метод для генерації вхідних даних, заповнених 1
    public static void generateInputDataWith1() {
        int d = 1;
        int[] B = generateVectorAndFill(1);
        int[] Z = generateVectorAndFill(1);
        int[][] MM = generateMatrixAndFill(1);
        int[][] MX = generateMatrixAndFill(1);

        writeInputData(d, B, Z, MM, MX);
    }

    // Метод для генерації вхідних даних, заповнених випадковими числами
    public static void generateRandomInputData() {
        int d = generateRandomScalar();
        int[] B = generateRandomVector();
        int[] Z = generateRandomVector();
        int[][] MM = generateRandomMatrix();
        int[][] MX = generateRandomMatrix();

        writeInputData(d, B, Z, MM, MX);
    }

    // Метод для запису вхідних даних у файл
    private static void writeInputData(int d, int[] B, int[] Z, int[][] MM, int[][] MX) {

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(inputPath + t2InputPath))) {
            os.writeObject(B);
            os.writeObject(MX);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(inputPath + t4InputPath))) {
            os.writeObject(MM);
            os.writeObject(Z);
            os.writeObject(d);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для обчислення S1h
    public static int[] calculateS1h(int[] Bh, int[][] MMh) {
        int[] leftVector = new int[partLength];
        for (int i = 0; i < partLength; i++) {
            leftVector[i] = d * Bh[i];
        }

        int[][] MC = new int[partLength][N];
        for (int i = 0; i < partLength; i++) {
            for (int j = 0; j < N; j++) {
                int product = 0;
                for (int k = 0; k < N; k++) {
                    product += MMh[i][k] * MX[k][j];
                }
                MC[i][j] = product;
            }
        }

        int[] rightVector = new int[partLength];
        for (int i = 0; i < partLength; i++) {
            int product = 0;
            for (int j = 0; j < N; j++) {
                product += Z[j] * MC[i][j];
            }
            rightVector[i] = product;
        }

        int[] combinedVector = new int[partLength];
        for (int i = 0; i < partLength; i++) {
            combinedVector[i] = leftVector[i] + rightVector[i];
        }

        Arrays.sort(combinedVector);
        return combinedVector;
    }

    public static void calculateXh(int[] Sh, int ei) {
        int[] Xh = new int[partLength];
        for (int i = 0; i < partLength; i++) {
            Xh[i] = Sh[i] * ei;
        }
        combineIntoX(Xh);
    }

    // Метод для поєднання частин Xh в X
    public static void combineIntoX(int[] Xh) {
        int number = getThreadNumber();
        int left = number * partLength;
        System.arraycopy(Xh, 0, X, left, partLength);
    }

    private static int getThreadNumber() {
        return Integer.parseInt(Thread.currentThread().getName().substring(7, 8));
    }



    // Метод для генерації випадкового скаляра
    public static int generateRandomScalar() {
        return generateRandomNumber();
    }

    // Метод для генерації векторів з випадковими числами
    public static int[] generateRandomVector() {
        int[] vector = new int[N];
        for (int i = 0; i < N; i++) {
            vector[i] = generateRandomNumber();
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
                matrix[i][j] = generateRandomNumber();
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

    private static int generateRandomNumber() {
        return rand.nextInt(maxNum) + 1;
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


    // Метод для виведення результату у файл
    public static void writeResult() {
        try (BufferedWriter os = new BufferedWriter(new FileWriter(outputPath))) {
            os.write(Arrays.toString(X));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}