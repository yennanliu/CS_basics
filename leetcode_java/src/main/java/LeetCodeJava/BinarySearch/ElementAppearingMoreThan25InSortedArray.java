package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/element-appearing-more-than-25-in-sorted-array/description/

/**
 * 1287. Element Appearing More Than 25% In Sorted Array
 * Easy
 *
 * Given an integer array sorted in non-decreasing order, there is exactly one
 * integer in the array that occurs more than 25% of the time, return that integer.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,2,6,6,6,6,7,10]
 * Output: 6
 *
 * Example 2:
 *
 * Input: arr = [1,1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^4
 * 0 <= arr[i] <= 10^5
 *
 */
public class ElementAppearingMoreThan25InSortedArray {

    // V0
    // IDEA: SORTED ARRAY PROPERTY + FIXED OFFSET CHECK
    //
    //   If x fills more than n/4 of a SORTED array, its run is longer than n/4,
    //   so arr[i] == arr[i + n/4] holds at the run's start index i.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findSpecialInteger(int[] arr) {
        int n = arr.length;
        int step = n / 4;
        for (int i = 0; i + step < n; i++) {
            // same value step positions apart -> its run is longer than n/4
            if (arr[i] == arr[i + step]) {
                return arr[i];
            }
        }
        // unreachable for valid input (exactly one such element exists)
        return arr[0];
    }

    // V1
    // IDEA: BINARY SEARCH ON THE 3 QUARTER POSITIONS
    //
    //   A run longer than n/4 must cover at least one of the indices n/4, n/2, 3n/4.
    //   For each candidate, binary search its first and last index and check the
    //   run length. Better bound than V0; the one to mention as the follow-up.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int findSpecialInteger_1(int[] arr) {
        int n = arr.length;
        int[] candidateIndexes = {n / 4, n / 2, (3 * n) / 4};
        for (int index : candidateIndexes) {
            int value = arr[index];
            int first = lowerBound(arr, value);
            // first index holding a value greater than `value`
            int afterLast = lowerBound(arr, value + 1);
            // run length > n/4, written without division
            if ((afterLast - first) * 4 > n) {
                return value;
            }
        }
        return arr[0];
    }

    /** First index i with arr[i] >= target (arr.length if none). */
    private int lowerBound(int[] arr, int target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
