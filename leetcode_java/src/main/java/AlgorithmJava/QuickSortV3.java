package AlgorithmJava;

import java.util.Arrays;
import java.util.Random;

/**
 *  QUICK SORT (V3) -- Hoare partition with a randomised pivot
 *
 *  Scope: the two fixes that make quicksort safe on real input. See
 *         QuickSort for the plain Lomuto version this improves on, and
 *         QuickSortV2 for the 3-way non-in-place variant.
 *
 *  FIX 1 -- RANDOM PIVOT
 *  QuickSort takes the LAST element as pivot, which makes an
 *  already-sorted array the O(N^2) worst case. Swapping a random
 *  element into the pivot slot first means no fixed input can trigger
 *  that: the O(N^2) case still exists, but an adversary cannot aim for
 *  it and sorted input is no longer special.
 *
 *  FIX 2 -- HOARE PARTITION
 *  Two pointers close in from both ends, swapping any out-of-place pair
 *  they meet:
 *
 *      [3, 7, 8, 5, 2, 1, 9, 4]     pivot value = 5
 *       i-->              <--j
 *      i stops at 7 (>= 5), j stops at 1 (<= 5)  ->  swap
 *      [3, 1, 8, 5, 2, 7, 9, 4]
 *      ... continue until the pointers cross
 *
 *  Hoare does roughly THREE TIMES FEWER SWAPS than Lomuto, and on
 *  all-equal input it splits down the middle instead of degrading --
 *  which is why it is the scheme real libraries use.
 *
 *  THE CATCH: Hoare's return value is a SPLIT POINT, not the pivot's
 *  final index. The pivot is not necessarily in place, so the recursive
 *  calls must be `(lo, p)` and `(p + 1, hi)` -- NOT `(p + 1, ...)` on
 *  the left as with Lomuto. Getting this wrong drops an element, which
 *  is the classic Hoare bug.
 *
 *  Time  : Best / Average O(N log N), Worst O(N^2) (now unreachable by
 *          a fixed input)
 *  Space : O(log N) average recursion depth, O(N) worst
 *
 *  Reference: https://neetcode.io/courses/lessons/sorting-algorithms
 */
public class QuickSortV3 {

    private static final Random RANDOM = new Random();

    /** Sort the whole array ascending, in place. */
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    /** Sort arr[low..high] ascending, in place. */
    public static void quickSort(int[] arr, int low, int high) {
        if (low >= high) {
            return;
        }
        int split = partition(arr, low, high);
        // NOTE: `split` is included on the LEFT. Hoare does not place the
        // pivot, so excluding it here would silently drop an element.
        quickSort(arr, low, split);
        quickSort(arr, split + 1, high);
    }

    /**
     *  Hoare partition of arr[low..high].
     *
     *  Returns an index p such that everything in arr[low..p] is <= every
     *  element of arr[p+1..high]. The pivot itself may sit on either side.
     */
    private static int partition(int[] arr, int low, int high) {
        // randomise: move a random element into the pivot slot first
        swap(arr, low + RANDOM.nextInt(high - low + 1), low);
        int pivot = arr[low];

        int i = low - 1;
        int j = high + 1;

        while (true) {
            // advance from the left until a value belonging on the right
            do {
                i++;
            } while (arr[i] < pivot);

            // advance from the right until a value belonging on the left
            do {
                j--;
            } while (arr[j] > pivot);

            if (i >= j) {
                return j;       // pointers crossed -> this is the split point
            }
            swap(arr, i, j);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] mixed = {3, 7, 8, 5, 2, 1, 9, 4};
        quickSort(mixed);
        assertThat(Arrays.toString(mixed).equals("[1, 2, 3, 4, 5, 7, 8, 9]"), "sorts a mixed array");

        int[] empty = {};
        quickSort(empty);
        assertThat(empty.length == 0, "empty array");

        int[] single = {1};
        quickSort(single);
        assertThat(Arrays.toString(single).equals("[1]"), "single element");

        int[] two = {2, 1};
        quickSort(two);
        assertThat(Arrays.toString(two).equals("[1, 2]"), "two elements");

        int[] sorted = {1, 2, 3, 4, 5};
        quickSort(sorted);
        assertThat(Arrays.toString(sorted).equals("[1, 2, 3, 4, 5]"), "already sorted");

        int[] reversed = {5, 4, 3, 2, 1};
        quickSort(reversed);
        assertThat(Arrays.toString(reversed).equals("[1, 2, 3, 4, 5]"), "reversed");

        int[] duplicates = {2, 1, 2, 1};
        quickSort(duplicates);
        assertThat(Arrays.toString(duplicates).equals("[1, 1, 2, 2]"), "duplicates");

        int[] allSame = {5, 5, 5, 5, 5};
        quickSort(allSame);
        assertThat(Arrays.toString(allSame).equals("[5, 5, 5, 5, 5]"), "all equal -- Hoare splits evenly");

        int[] negatives = {0, -3, 5, -1};
        quickSort(negatives);
        assertThat(Arrays.toString(negatives).equals("[-3, -1, 0, 5]"), "negatives");

        quickSort(null);   // must not throw

        // a randomised pivot must still be correct on every run, and no
        // element may be lost -- the classic Hoare off-by-one
        for (int trial = 0; trial < 200; trial++) {
            int[] random = RANDOM.ints(50, -100, 100).toArray();
            int[] expected = random.clone();
            Arrays.sort(expected);
            quickSort(random);
            assertThat(Arrays.equals(random, expected), "random trial " + trial);
        }

        // sorted input is no longer the worst case: 100k elements would
        // blow the stack with a last-element pivot
        int[] large = new int[100_000];
        for (int i = 0; i < large.length; i++) {
            large[i] = i;
        }
        quickSort(large);
        assertThat(large[0] == 0 && large[99_999] == 99_999, "100k sorted elements, no stack overflow");

        System.out.println(Arrays.toString(mixed));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
