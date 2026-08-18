package AlgorithmJava;

import java.util.Arrays;

/**
 *  MERGE SORT -- top down, functional style
 *
 *  Scope: the variant that RETURNS a new sorted array instead of
 *         rearranging one. See MergeSort for the in-place, index-based
 *         version you would actually ship.
 *
 *  Same algorithm, expressed as a pure function:
 *
 *      merge_sort(input) = merge( merge_sort(left half),
 *                                 merge_sort(right half) )
 *
 *          [38, 27, 43, 3]
 *         /               \
 *    [38, 27]           [43, 3]
 *      /    \            /    \
 *   [38]   [27]       [43]    [3]
 *      \    /            \    /
 *     [27, 38]          [3, 43]
 *          \               /
 *        [3, 27, 38, 43]
 *
 *  WHY BOTHER WITH THIS FORM: the recursion mirrors the definition
 *  exactly, with no index bookkeeping, which makes the algorithm easier
 *  to see. It is also how you would write it in a language without
 *  mutable arrays.
 *
 *  WHAT IT COSTS: Arrays.copyOfRange allocates at every level, so the
 *  extra memory is O(N log N) rather than O(N), and the input is never
 *  modified. Use MergeSort when that matters.
 *
 *  Time  : O(N log N) in all cases
 *  Space : O(N log N) -- new arrays at every level
 *
 *  Reference: https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2868/
 */
public class MergeSortTopDown {

    /** Returns a NEW sorted array; the input is left untouched. */
    public int[] merge_sort(int[] input) {
        if (input == null) {
            return new int[0];
        }
        if (input.length <= 1) {
            return input;                       // 0 or 1 element is already sorted
        }

        int pivot = input.length / 2;
        int[] leftList = merge_sort(Arrays.copyOfRange(input, 0, pivot));
        int[] rightList = merge_sort(Arrays.copyOfRange(input, pivot, input.length));
        return merge(leftList, rightList);
    }

    /** Combine two SORTED arrays into one sorted array, in O(n + m). */
    public int[] merge(int[] leftList, int[] rightList) {
        int[] ret = new int[leftList.length + rightList.length];
        int leftCursor = 0;
        int rightCursor = 0;
        int retCursor = 0;

        while (leftCursor < leftList.length && rightCursor < rightList.length) {
            // `<=` and not `<`: on a TIE take from the LEFT run, which is
            // what makes the sort stable
            if (leftList[leftCursor] <= rightList[rightCursor]) {
                ret[retCursor++] = leftList[leftCursor++];
            } else {
                ret[retCursor++] = rightList[rightCursor++];
            }
        }

        // exactly one side still has values; append whatever is left
        while (leftCursor < leftList.length) {
            ret[retCursor++] = leftList[leftCursor++];
        }
        while (rightCursor < rightList.length) {
            ret[retCursor++] = rightList[rightCursor++];
        }
        return ret;
    }

    public static void main(String[] args) {
        MergeSortTopDown sorter = new MergeSortTopDown();

        assertThat(Arrays.toString(sorter.merge_sort(new int[] {38, 27, 43, 3, 9, 82, 10}))
                        .equals("[3, 9, 10, 27, 38, 43, 82]"), "sorts a mixed array");
        assertThat(sorter.merge_sort(new int[] {}).length == 0, "empty array");
        assertThat(sorter.merge_sort(null).length == 0, "null input");
        assertThat(Arrays.toString(sorter.merge_sort(new int[] {1})).equals("[1]"), "single element");
        assertThat(Arrays.toString(sorter.merge_sort(new int[] {1, 2, 3})).equals("[1, 2, 3]"),
                "already sorted");
        assertThat(Arrays.toString(sorter.merge_sort(new int[] {3, 2, 1})).equals("[1, 2, 3]"),
                "reversed");
        assertThat(Arrays.toString(sorter.merge_sort(new int[] {2, 1, 2, 1})).equals("[1, 1, 2, 2]"),
                "duplicates");
        assertThat(Arrays.toString(sorter.merge_sort(new int[] {0, -3, 5, -1})).equals("[-3, -1, 0, 5]"),
                "negatives");

        // merge() on its own
        assertThat(Arrays.toString(sorter.merge(new int[] {1, 4, 7}, new int[] {2, 5}))
                        .equals("[1, 2, 4, 5, 7]"), "merge two sorted arrays");
        assertThat(Arrays.toString(sorter.merge(new int[] {}, new int[] {1, 2})).equals("[1, 2]"),
                "merge with an empty side");

        // the input array is not mutated
        int[] input = {3, 1, 2};
        sorter.merge_sort(input);
        assertThat(Arrays.toString(input).equals("[3, 1, 2]"), "input is left untouched");

        System.out.println(Arrays.toString(sorter.merge_sort(new int[] {38, 27, 43, 3, 9, 82, 10})));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
