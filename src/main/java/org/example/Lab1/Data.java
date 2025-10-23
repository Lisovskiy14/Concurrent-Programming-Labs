package org.example.Lab1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Data {
    private static final Random random = new Random();
    private static final int maxNum = 3;
    public static final int N = 1000;

    public static int numT1;
    public static int numT2;
    public static int numT3;

    public static final String sourcePath = "src/main/resources/Lab1/";
    public static final String t1FilePath = "T1_result.txt";
    public static final String t2FilePath = "T2_result.txt";
    public static final String t3FilePath = "T3_result.txt";

    // Метод для запису даних у файли
    public static synchronized <T> void writeResults(T result, String filepath) {
        String path = sourcePath + filepath;

        try (BufferedWriter os = new BufferedWriter(new FileWriter(path))) {
            if (result instanceof int[]) {
                os.write(Arrays.toString((int[]) result));
            } else if (result instanceof int[][]) {
                List<List<Integer>> matrix = new ArrayList<>();
                for (int[] column : (int[][]) result) {
                    matrix.add(Arrays.stream(column).boxed().toList());
                }
                os.write(matrix.toString());
            } else {
                os.write(result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для генерації векторів з випадковими числами
    public static int[] generateRandomVector() {
        int[] vector = new int[N];
        for (int i = 0; i < N; i++) {
            vector[i] = random.nextInt(maxNum);
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
                matrix[i][j] = random.nextInt(maxNum);
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

    // Метод для різниці векторів
    public static int[] vectorDifference(int[] A, int[] B) {
        int length = A.length;
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = A[i] - B[i];
        }

        return result;
    }

    // Метод для множення матриць
    public static int[][] matrixProduct(int[][] MA, int[][] MB) {
        int[][] result = new int[MA.length][MB[0].length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                int product = 0;
                for (int k = 0; k < MA[0].length; k++) {
                    product += MA[i][k] * MB[k][j];
                }
                result[i][j] = product;
            }
        }

        return result;
    }

    // Метод для різниці матриць
    public static int[][] matrixDifference(int[][] MA, int[][] MB) {
        int[][] result = new int[MA.length][MA.length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = MA[i][j] - MB[i][j];
            }
        }

        return result;
    }
}
