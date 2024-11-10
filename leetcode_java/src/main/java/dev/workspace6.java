package dev;

public class workspace6 {
    public static void main(String[] args) {

        // inverse order
        int[] array_1 = new int[3];
        array_1[0] = 1;
        array_1[1] = 2;
        array_1[2] = 3;

        for (int i = array_1.length - 1; i >= 0; i--) {
            System.out.println(array_1[i]);
        }
    }
}
