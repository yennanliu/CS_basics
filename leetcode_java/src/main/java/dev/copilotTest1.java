package dev;

public class copilotTest1 {
    public static void main(String[] args) {
        // offer a for loop from 1 to 10
        for (int i = 1; i <= 10; i++) {
            // print the value of i
            System.out.println(i);
        }

        // offer a for loop from 10 to 1
        for (int i = 10; i >= 1; i--) {
            // print the value of i
            System.out.println(i);
        }

        // offer a for loop from 1 to 10
        for (int i = 1; i <= 10; i++) {
            // print the value of i
            System.out.println(i);
        }

        // offer a for loop from -1 to -10
        for (int i = -1; i >= -10; i--) {
            // print the value of i
            System.out.println(i);
        }

        // offer a binrary search implementation
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int target = 5;
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                System.out.println("Found at index " + mid);
                break;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }


}
