package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/shortest-subarray-to-be-removed-to-make-array-sorted/description/

/**
 * 1574. Shortest Subarray to be Removed to Make Array Sorted
 * Medium
 *
 * Given an integer array arr, remove a subarray (can be empty) from arr such that the
 * remaining elements in arr are non-decreasing.
 *
 * Return the length of the shortest subarray to remove.
 *
 * A subarray is a contiguous subsequence of the array.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,3,10,4,2,3,5]
 * Output: 3
 * Explanation: The shortest subarray we can remove is [10,4,2] of length 3. The remaining
 * elements after that will be [1,2,3,3,5] which are sorted.
 * Another correct solution is to remove the subarray [3,10,4].
 *
 * Example 2:
 *
 * Input: arr = [5,4,3,2,1]
 * Output: 4
 *
 * Example 3:
 *
 * Input: arr = [1,2,3]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 0 <= arr[i] <= 10^9
 *
 */
public class ShortestSubarrayToBeRemovedToMakeArraySorted {

    // V0
    // IDEA: TWO POINTERS (keep a non-decreasing prefix + a non-decreasing suffix)
    //       The kept part is arr[0..left] + arr[right..n-1], both pieces non-decreasing and
    //       arr[left] <= arr[right]. Find the longest sorted suffix first, then grow the
    //       prefix; for each left, push right forward until arr[right] >= arr[left].
    //       Both pointers only move forward -> linear.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findLengthOfShortestSubarray(int[] arr) {
        int n = arr.length;

        // right = start of the longest non-decreasing suffix
        int right = n - 1;
        while (right > 0 && arr[right - 1] <= arr[right]) {
            right--;
        }
        if (right == 0) {
            // already sorted, nothing to remove
            return 0;
        }

        // option: keep only the suffix, remove arr[0 .. right-1]
        int shortest = right;

        // left walks the non-decreasing prefix
        for (int left = 0; left < n; left++) {
            // the prefix must stay non-decreasing up to left
            if (left > 0 && arr[left - 1] > arr[left]) {
                break;
            }
            // skip suffix elements too small to follow arr[left]
            while (right < n && arr[right] < arr[left]) {
                right++;
            }
            // remove everything strictly between left and right
            shortest = Math.min(shortest, right - left - 1);
        }
        return shortest;
    }
}
