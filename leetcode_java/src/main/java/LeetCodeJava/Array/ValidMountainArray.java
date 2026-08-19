package LeetCodeJava.Array;

// https://leetcode.com/problems/valid-mountain-array/

/**
 *  941. Valid Mountain Array
 *  Easy
 *
 *  Given an array of integers arr, return true if and only if it is a valid
 *  mountain array.
 *
 *  Recall that arr is a mountain array if and only if:
 *   - arr.length >= 3
 *   - There exists some i with 0 < i < arr.length - 1 such that:
 *       - arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
 *       - arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
 *
 *  Example 1:
 *  Input: arr = [2,1]
 *  Output: false
 *
 *  Example 2:
 *  Input: arr = [3,5,5]
 *  Output: false
 *
 *  Example 3:
 *  Input: arr = [0,3,2,1]
 *  Output: true
 *
 *  Constraints:
 *   - 1 <= arr.length <= 10^4
 *   - 0 <= arr[i] <= 10^4
 */
public class ValidMountainArray {

    // V0
    // IDEA: walk strictly up from the left, then require a strict descent that
    //       reaches the last index; the peak must be neither endpoint
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean validMountainArray(int[] arr) {
        if (arr == null || arr.length < 3) {
            return false;
        }
        int n = arr.length;
        int i = 0;
        while (i + 1 < n && arr[i] < arr[i + 1]) {
            i++;
        }
        // peak can't be the first or the last element
        if (i == 0 || i == n - 1) {
            return false;
        }
        while (i + 1 < n && arr[i] > arr[i + 1]) {
            i++;
        }
        return i == n - 1;
    }
}
