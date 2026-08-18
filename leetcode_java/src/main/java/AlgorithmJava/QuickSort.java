package AlgorithmJava;

import java.util.Arrays;

/**
 *  QUICK SORT (V1) -- in place, Lomuto partition
 *
 *  Scope: the standard in-place quicksort with the LAST element as
 *         pivot. See QuickSortV2 for the 3-way, non-in-place variant
 *         that handles heavy duplication, and QuickSortV3 for Hoare
 *         partitioning with a randomised pivot.
 *
 *  Divide and conquer, but the work happens BEFORE the recursion (the
 *  mirror image of merge sort, where it happens after):
 *
 *    1) PARTITION around a pivot, so everything smaller ends up left of
 *       it and everything larger ends up right of it
 *    2) the pivot is now in its FINAL position -- it is never moved again
 *    3) recurse on the two sides; no merge step is needed
 *
 *      [3, 7, 8, 5, 2, 1, 9, 4]        pivot = 4 (last)
 *      [3, 2, 1 | 4 | 8, 5, 7, 9]      4 is now final
 *       ------       ----------
 *       sort these two independently
 *
 *  THE LOMUTO SCHEME: `i` marks the boundary of the "smaller than
 *  pivot" region. Walk j across the range; every time a smaller value
 *  is found, swap it to the boundary and push the boundary right. At
 *  the end, swap the pivot into the boundary slot.
 *
 *  WHY THE WORST CASE IS O(N^2): if the pivot is always the smallest or
 *  largest value, one side is empty and the recursion is N deep instead
 *  of log N. Taking the LAST element as pivot makes ALREADY-SORTED
 *  input the worst case -- which is exactly the input you are most
 *  likely to test with. QuickSortV3 fixes that by randomising.
 *
 *  Quick sort is NOT stable, and unlike merge sort it needs no
 *  auxiliary array -- which is why it usually wins on raw arrays.
 *
 *  Time  : Best / Average O(N log N), Worst O(N^2)
 *  Space : O(log N) average recursion depth, O(N) worst
 *
 *  Reference: https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2870/
 */
public class QuickSort {

    /** Sort lst ascending, in place. */
    public void quickSort(int[] lst) {
        if (lst == null || lst.length < 2) {
            return;
        }
        qSort(lst, 0, lst.length - 1);
    }

    /** Sort lst[lo..hi]. */
    private void qSort(int[] lst, int lo, int hi) {
        if (lo >= hi) {
            return;                       // 0 or 1 element is already sorted
        }
        int p = partition(lst, lo, hi);
        qSort(lst, lo, p - 1);            // p itself is final -- excluded from both sides
        qSort(lst, p + 1, hi);
    }

    /**
     *  Partition lst[lo..hi] around lst[hi], and return the pivot's
     *  final index.
     *
     *  Afterwards: lst[lo..p-1] < lst[p] <= lst[p+1..hi]
     */
    private int partition(int[] lst, int lo, int hi) {
        int pivot = lst[hi];
        int i = lo;                       // boundary of the "< pivot" region

        for (int j = lo; j < hi; j++) {
            if (lst[j] < pivot) {
                swap(lst, i, j);
                i++;
            }
        }

        swap(lst, i, hi);                 // put the pivot at the boundary
        return i;
    }

    private void swap(int[] lst, int i, int j) {
        int tmp = lst[i];
        lst[i] = lst[j];
        lst[j] = tmp;
    }

    public static void main(String[] args) {
        QuickSort sorter = new QuickSort();

        int[] mixed = {3, 7, 8, 5, 2, 1, 9, 4};
        sorter.quickSort(mixed);
        assertThat(Arrays.toString(mixed).equals("[1, 2, 3, 4, 5, 7, 8, 9]"), "sorts a mixed array");

        int[] empty = {};
        sorter.quickSort(empty);
        assertThat(empty.length == 0, "empty array");

        int[] single = {1};
        sorter.quickSort(single);
        assertThat(Arrays.toString(single).equals("[1]"), "single element");

        int[] sorted = {1, 2, 3};
        sorter.quickSort(sorted);
        assertThat(Arrays.toString(sorted).equals("[1, 2, 3]"), "already sorted -- the O(N^2) case here");

        int[] reversed = {3, 2, 1};
        sorter.quickSort(reversed);
        assertThat(Arrays.toString(reversed).equals("[1, 2, 3]"), "reversed");

        int[] duplicates = {2, 1, 2, 1};
        sorter.quickSort(duplicates);
        assertThat(Arrays.toString(duplicates).equals("[1, 1, 2, 2]"), "duplicates");

        int[] allSame = {5, 5, 5, 5};
        sorter.quickSort(allSame);
        assertThat(Arrays.toString(allSame).equals("[5, 5, 5, 5]"), "all equal");

        int[] negatives = {0, -3, 5, -1};
        sorter.quickSort(negatives);
        assertThat(Arrays.toString(negatives).equals("[-3, -1, 0, 5]"), "negatives");

        sorter.quickSort(null);   // must not throw

        System.out.println(Arrays.toString(mixed));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
