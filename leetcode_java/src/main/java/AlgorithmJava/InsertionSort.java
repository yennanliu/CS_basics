package AlgorithmJava;

// https://neetcode.io/courses/lessons/sorting-algorithms
/**
 * 1. Insertion Sort
 *
 * Insertion sort is a simple sorting algorithm that builds the final sorted array one item at a time.
 *
 * It sorts the array by inserting each element into its correct position. At any point, the left side of the array is sorted while the right side is unsorted. We choose the first element in the unsorted array and insert it into the sorted array in the correct position. We then repeat this process for the next element in the unsorted array.
 *
 * It is much less efficient on large lists than more advanced algorithms such as quicksort, heapsort, or merge sort.
 *
 *
 *  - Time complexity: O(N ^2)
 *  - Space complexity: O(1)
 *
 */
public class InsertionSort {

    // V1
    public static void insertionSort(int[] arr) {
        /**
         * Iterating Through the Unsorted Elements
         */
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            /**
             * Finding the Insertion Point and Shifting Elements
             *
             */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            /**
             * Placing the Key in the Correct Position
             */
            arr[j + 1] = key;
        }
    }

}