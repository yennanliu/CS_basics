package AlgorithmJava;

// https://neetcode.io/courses/lessons/sorting-algorithms
/**
 *  Quick sort is a divide and conquer
 *  algorithm that picks an element as pivot and partitions
 *  the given array around the picked pivot.
 *
 *  Unlike merge sort, quick sort is not a stable sort.
 *  This means that the input order of equal elements in
 *  the sorted output may not be preserved.
 *
 *  - Time complexity: O(N log N)
 *  - Space complexity: O(N log N)
 *
 */
public class QuickSortV3 {
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}