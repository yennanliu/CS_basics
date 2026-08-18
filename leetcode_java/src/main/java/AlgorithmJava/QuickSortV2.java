package AlgorithmJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  QUICK SORT (V2) -- 3-way partition, not in place
 *
 *  Scope: the "readable" quicksort -- build three lists instead of
 *         swapping in place. See QuickSort for the classic in-place
 *         Lomuto version and QuickSortV3 for Hoare partitioning.
 *
 *  Same divide-and-conquer idea, but the partition splits into THREE
 *  groups rather than two:
 *
 *      [3, 6, 8, 10, 1, 2, 1]          pivot = middle element = 10
 *
 *      left   [3, 6, 8, 1, 2, 1]   < pivot
 *      equal  [10]                 == pivot   <- already final, never recursed on
 *      right  []                   > pivot
 *
 *      result = sort(left) + equal + sort(right)
 *
 *  WHY THE THIRD BUCKET MATTERS: with a 2-way partition, an array of N
 *  identical values still recurses N deep -- the classic O(N^2) trap.
 *  Pulling equals out into their own bucket means duplicates are placed
 *  in ONE pass, so `[5,5,5,...,5]` sorts in O(N). That is the whole
 *  point of Dijkstra's Dutch-national-flag partition.
 *
 *  THE TRADE-OFF: it allocates new lists at every level, so it uses
 *  O(N log N) extra memory and loses quicksort's main advantage over
 *  merge sort. Use it to understand the algorithm, not to ship it.
 *
 *  Time  : Best / Average O(N log N), Worst O(N^2)
 *  Space : O(N log N) -- new lists at every level of the recursion
 */
public class QuickSortV2 {

    /** Returns a NEW sorted list; the input array is left untouched. */
    public List<Integer> quickSort(Integer[] input) {
        if (input == null || input.length == 0) {
            return new ArrayList<>();
        }
        if (input.length == 1) {
            return new ArrayList<>(Collections.singletonList(input[0]));
        }

        // middle element as pivot: unlike "last element", this does not
        // turn already-sorted input into the worst case
        int pivot = input[input.length / 2];

        List<Integer> left = new ArrayList<>();
        List<Integer> equal = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (int num : input) {
            if (num < pivot) {
                left.add(num);
            } else if (num == pivot) {
                equal.add(num);       // every copy of the pivot is placed at once
            } else {
                right.add(num);
            }
        }

        // `equal` is already in its final position, so it is not recursed on.
        // That guarantee is what stops all-duplicate input from degrading.
        List<Integer> result = new ArrayList<>(input.length);
        result.addAll(quickSort(left.toArray(new Integer[0])));
        result.addAll(equal);
        result.addAll(quickSort(right.toArray(new Integer[0])));
        return result;
    }

    public static void main(String[] args) {
        QuickSortV2 sorter = new QuickSortV2();

        assertThat(sorter.quickSort(new Integer[] {3, 6, 8, 10, 1, 2, 1}).toString()
                        .equals("[1, 1, 2, 3, 6, 8, 10]"), "sorts a mixed array");
        assertThat(sorter.quickSort(new Integer[] {5, 4, 3, 2, 1, 0, -1, -2}).toString()
                        .equals("[-2, -1, 0, 1, 2, 3, 4, 5]"), "reversed, with negatives");

        assertThat(sorter.quickSort(new Integer[] {}).isEmpty(), "empty array");
        assertThat(sorter.quickSort(null).isEmpty(), "null input");
        assertThat(sorter.quickSort(new Integer[] {1}).toString().equals("[1]"), "single element");
        assertThat(sorter.quickSort(new Integer[] {1, 2, 3}).toString().equals("[1, 2, 3]"),
                "already sorted");
        assertThat(sorter.quickSort(new Integer[] {2, 1, 2, 1}).toString().equals("[1, 1, 2, 2]"),
                "duplicates");

        // all-equal input: the 3-way split places everything in ONE pass
        Integer[] allSame = new Integer[1000];
        Arrays.fill(allSame, 5);
        assertThat(sorter.quickSort(allSame).size() == 1000, "1000 identical values, no blow-up");

        // the input array is not mutated
        Integer[] input = {3, 1, 2};
        sorter.quickSort(input);
        assertThat(Arrays.toString(input).equals("[3, 1, 2]"), "input is left untouched");

        System.out.println(sorter.quickSort(new Integer[] {3, 6, 8, 10, 1, 2, 1}));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
