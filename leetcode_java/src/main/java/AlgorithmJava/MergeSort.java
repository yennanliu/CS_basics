package AlgorithmJava;

import java.util.Arrays;

/**
 *  MERGE SORT -- in place, index-based
 *
 *  Scope: the version that sorts an existing array by index range. See
 *         MergeSortTopDown for the functional variant that returns new
 *         arrays, and algorithm/python/merge_sort_bottomup.py for the
 *         iterative bottom-up form.
 *
 *  Divide and conquer:
 *    1) SPLIT the range in half
 *    2) sort each half recursively
 *    3) MERGE the two sorted halves back into place
 *
 *          [38, 27, 43, 3, 9, 82, 10]
 *         /                          \
 *    [38, 27, 43]              [3, 9, 82, 10]
 *       ...                         ...
 *         \                          /
 *        [3, 9, 10, 27, 38, 43, 82]
 *
 *  The merge is the whole algorithm: two sorted runs combine in linear
 *  time by repeatedly taking the smaller of the two front values. There
 *  are log N levels and each merges N items, hence O(N log N) -- and
 *  unlike quicksort that bound holds in the WORST case too.
 *
 *  Merge sort is STABLE, which is why it is the standard choice for
 *  sorting objects by a key. The price is O(N) scratch space, which is
 *  also why quicksort usually wins on raw arrays.
 *
 *  Time  : O(N log N) in all cases
 *  Space : O(N)
 *
 *  Reference: https://neetcode.io/courses/lessons/sorting-algorithms
 */
public class MergeSort {

    /** Sort the whole array ascending, in place. */
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    /** Sort arr[left..right] ascending, in place. */
    public static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;                              // 0 or 1 element is already sorted
        }
        int mid = left + (right - left) / 2;     // not (left + right) / 2, which can overflow
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    /** Merge the sorted runs arr[left..mid] and arr[mid+1..right]. */
    private static void merge(int[] arr, int left, int mid, int right) {
        int[] leftPart = Arrays.copyOfRange(arr, left, mid + 1);
        int[] rightPart = Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0;          // cursor into leftPart
        int j = 0;          // cursor into rightPart
        int k = left;       // write cursor into arr

        while (i < leftPart.length && j < rightPart.length) {
            // `<=` and not `<`: on a TIE take from the LEFT run, which is
            // what makes the sort stable
            if (leftPart[i] <= rightPart[j]) {
                arr[k++] = leftPart[i++];
            } else {
                arr[k++] = rightPart[j++];
            }
        }

        // exactly one run still has values; copy whatever is left
        while (i < leftPart.length) {
            arr[k++] = leftPart[i++];
        }
        while (j < rightPart.length) {
            arr[k++] = rightPart[j++];
        }
    }

    public static void main(String[] args) {
        int[] mixed = {38, 27, 43, 3, 9, 82, 10};
        mergeSort(mixed);
        assertThat(Arrays.toString(mixed).equals("[3, 9, 10, 27, 38, 43, 82]"), "sorts a mixed array");

        int[] empty = {};
        mergeSort(empty);
        assertThat(empty.length == 0, "empty array");

        int[] single = {1};
        mergeSort(single);
        assertThat(Arrays.toString(single).equals("[1]"), "single element");

        int[] sorted = {1, 2, 3};
        mergeSort(sorted);
        assertThat(Arrays.toString(sorted).equals("[1, 2, 3]"), "already sorted");

        int[] reversed = {3, 2, 1};
        mergeSort(reversed);
        assertThat(Arrays.toString(reversed).equals("[1, 2, 3]"), "reversed");

        int[] duplicates = {2, 1, 2, 1};
        mergeSort(duplicates);
        assertThat(Arrays.toString(duplicates).equals("[1, 1, 2, 2]"), "duplicates");

        int[] negatives = {0, -3, 5, -1};
        mergeSort(negatives);
        assertThat(Arrays.toString(negatives).equals("[-3, -1, 0, 5]"), "negatives");

        mergeSort(null);   // must not throw

        // sorting a sub-range leaves the rest of the array alone
        int[] partial = {9, 8, 3, 2, 1};
        mergeSort(partial, 0, 2);
        assertThat(Arrays.toString(partial).equals("[3, 8, 9, 2, 1]"), "only arr[0..2] was touched");

        // O(N log N) even on the input that makes quicksort quadratic
        int[] large = new int[200_000];
        for (int i = 0; i < large.length; i++) {
            large[i] = large.length - i;
        }
        mergeSort(large);
        assertThat(large[0] == 1 && large[199_999] == 200_000, "200k reversed elements");

        System.out.println(Arrays.toString(mixed));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
