package AlgorithmJava;

import java.util.Arrays;

/**
 *  INSERTION SORT
 *
 *  Build the sorted part one value at a time, the way you sort a hand
 *  of cards: pick up the next value and slide it left past everything
 *  bigger than it.
 *
 *      [5 | 1, 4, 2]   take 1, shift 5 right, drop 1 at the front
 *      [1, 5 | 4, 2]   take 4, shift 5 right, drop 4
 *      [1, 4, 5 | 2]   take 2, shift 5 and 4 right, drop 2
 *      [1, 2, 4, 5]
 *
 *  The inner loop SHIFTS rather than swaps: each bigger value is copied
 *  one slot right and `key` is written once at the end -- half the
 *  writes of the swap-based version.
 *
 *  WHY IT IS THE ONE O(N^2) SORT WORTH KNOWING:
 *    - STABLE: the `>` comparison stops at an equal value, so equal
 *      keys never cross
 *    - in place, O(1) extra memory
 *    - O(N) on nearly-sorted input -- each new value stops immediately
 *    - very low constant factor, which is why production sorts
 *      (Timsort, introsort) switch to it for small subarrays
 *
 *  Time  : Best O(N) (already sorted), Average / Worst O(N^2)
 *  Space : O(1)
 *
 *  Reference: https://neetcode.io/courses/lessons/sorting-algorithms
 */
public class InsertionSort {

    /** Sort arr ascending, in place. */
    public static void insertionSort(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];   // the value being placed
            int j = i - 1;

            // shift everything greater than key one slot to the right.
            // `>` and not `>=` is what keeps the sort stable.
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;   // the hole left behind is where key belongs
        }
    }

    public static void main(String[] args) {
        int[] mixed = {12, 11, 13, 5, 6};
        insertionSort(mixed);
        assertThat(Arrays.toString(mixed).equals("[5, 6, 11, 12, 13]"), "sorts a mixed array");

        int[] empty = {};
        insertionSort(empty);
        assertThat(empty.length == 0, "empty array");

        int[] single = {1};
        insertionSort(single);
        assertThat(Arrays.toString(single).equals("[1]"), "single element");

        int[] sorted = {1, 2, 3};
        insertionSort(sorted);
        assertThat(Arrays.toString(sorted).equals("[1, 2, 3]"), "already sorted -- the O(N) case");

        int[] reversed = {3, 2, 1};
        insertionSort(reversed);
        assertThat(Arrays.toString(reversed).equals("[1, 2, 3]"), "reversed -- the O(N^2) case");

        int[] duplicates = {2, 1, 2, 1};
        insertionSort(duplicates);
        assertThat(Arrays.toString(duplicates).equals("[1, 1, 2, 2]"), "duplicates");

        int[] negatives = {0, -3, 5, -1};
        insertionSort(negatives);
        assertThat(Arrays.toString(negatives).equals("[-3, -1, 0, 5]"), "negatives");

        insertionSort(null);   // must not throw

        System.out.println(Arrays.toString(mixed));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
