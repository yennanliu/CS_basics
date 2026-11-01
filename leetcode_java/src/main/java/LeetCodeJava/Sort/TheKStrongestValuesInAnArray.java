package LeetCodeJava.Sort;

// https://leetcode.com/problems/the-k-strongest-values-in-an-array/description/

import java.util.*;

/**
 * 1471. The k Strongest Values in an Array
 * Medium
 *
 * Given an array of integers arr and an integer k.
 *
 * A value arr[i] is said to be stronger than a value arr[j] if |arr[i] - m| > |arr[j] - m|
 * where m is the centre of the array.
 * If |arr[i] - m| == |arr[j] - m|, then arr[i] is said to be stronger than arr[j] if
 * arr[i] > arr[j].
 *
 * Return a list of the strongest k values in the array. Return the answer in any arbitrary
 * order.
 *
 * The centre is the middle value in an ordered integer list. More formally, if the length
 * of the list is n, the centre is the element in position ((n - 1) / 2) in the sorted list
 * (0-indexed).
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,3,4,5], k = 2
 * Output: [5,1]
 * Explanation: Centre is 3, the elements of the array sorted by the strongest are
 * [5,1,4,2,3]. The strongest 2 elements are [5, 1]. [1, 5] is also accepted answer.
 *
 * Example 2:
 *
 * Input: arr = [1,1,3,5,5], k = 2
 * Output: [5,5]
 *
 * Example 3:
 *
 * Input: arr = [6,7,11,7,6,8], k = 5
 * Output: [11,8,6,6,7]
 * Explanation: Any permutation of [11,8,6,6,7] is accepted.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * -10^5 <= arr[i] <= 10^5
 * 1 <= k <= arr.length
 *
 */
public class TheKStrongestValuesInAnArray {

    // V0
    // IDEA: SORT TWICE — sort once to read the centre m = arr[(n - 1) / 2], then sort by
    //       strength (larger |x - m| first, ties broken by the larger value) and take k
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] getStrongest(int[] arr, int k) {
        int n = arr.length;
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        // the centre is the LOWER median of the sorted array, not the average
        final int centre = sorted[(n - 1) / 2];

        // boxed copy so a custom comparator can be used
        Integer[] values = new Integer[n];
        for (int i = 0; i < n; i++) {
            values[i] = sorted[i];
        }

        Arrays.sort(values, (a, b) -> {
            int distanceA = Math.abs(a - centre);
            int distanceB = Math.abs(b - centre);
            if (distanceA != distanceB) {
                // farther from the centre is stronger -> comes first
                return Integer.compare(distanceB, distanceA);
            }
            // same distance: the larger value is stronger
            return Integer.compare(b, a);
        });

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = values[i];
        }
        return result;
    }

    // V1
    // IDEA: SORT + TWO POINTERS — after sorting, the strongest remaining value is always at
    //       one of the two ends; compare the ends k times and take the stronger one
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] getStrongest_1(int[] arr, int k) {
        int n = arr.length;
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        int centre = sorted[(n - 1) / 2];

        int left = 0;
        int right = n - 1;
        int[] result = new int[k];

        for (int i = 0; i < k; i++) {
            int leftDistance = Math.abs(sorted[left] - centre);
            int rightDistance = Math.abs(sorted[right] - centre);
            // on a tie the right end wins, because it holds the larger value
            if (rightDistance >= leftDistance) {
                result[i] = sorted[right];
                right--;
            } else {
                result[i] = sorted[left];
                left++;
            }
        }
        return result;
    }
}
