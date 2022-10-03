package poker;

import java.util.Arrays;

public class Combinations {

    static int size = 20;

    static int[][] combinations = new int[size][size];

    public static void setUp() {
        for (int i = 0; i < size; i++) {
            combinations[i][0] = 1;
        }
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                combinations[i][j] = combinations[i - 1][j] + combinations[i - 1][j - 1];
            }
        }
    }

    public static int ncr(int n, int r) {
        if (n < r) return 0;
        int prod = 1;
        for (int i = 0; i < r; i++) prod *= (n - i);
        for (int i = 1; i < r + 1; i++) prod /= i;
        return prod;
    }

    public static void setUpTwo() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                combinations[i][j] = ncr(i, j);
            }
        }
    }

    public static void main(String[] args) {
        setUp();
        for (int[] list : combinations) System.out.println(Arrays.toString(list));
        setUpTwo();
        for (int[] list : combinations) System.out.println(Arrays.toString(list));
    }
}
