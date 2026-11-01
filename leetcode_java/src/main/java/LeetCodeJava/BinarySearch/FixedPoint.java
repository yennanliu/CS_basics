package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/fixed-point/description/

/**
 * 1064. Fixed Point
 * Easy
 *
 * Given an array of distinct integers arr, where arr is sorted in ascending order,
 * return the smallest index i that satisfies arr[i] == i. If there is no such index,
 * return -1.
 *
 *
 * Example 1:
 *
 * Input: arr = [-10,-5,0,3,7]
 * Output: 3
 * Explanation: For the given array, arr[0] = -10, arr[1] = -5, arr[2] = 0, arr[3] = 3, thus the output is 3.
 *
 * Example 2:
 *
 * Input: arr = [0,2,5,8,17]
 * Output: 0
 * Explanation: arr[0] = 0, thus the output is 0.
 *
 * Example 3:
 *
 * Input: arr = [-10,-5,3,4,7,9]
 * Output: -1
 * Explanation: There is no such i that arr[i] == i, thus the output is -1.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length < 10^4
 * -10^9 <= arr[i] <= 10^9
 *
 * Follow up: The O(n) solution is very straightforward. Can we do better?
 *
 */
public class FixedPoint {

    // V0
    // IDEA: BINARY SEARCH (FIRST INDEX WITH arr[i] - i >= 0)
    /**
     *  arr is sorted and its values are distinct integers, so arr[i + 1] >= arr[i] + 1,
     *  which makes f(i) = arr[i] - i non-decreasing. Binary search the leftmost i with
     *  f(i) >= 0, then check whether it is exactly 0.
     */
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int fixedPoint(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] >= mid) {
                // mid may be the answer; everything right of it is too far
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if (arr[left] == left) {
            return left;
        }
        return -1;
    }
}
