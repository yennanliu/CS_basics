package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/sort-transformed-array/

/**
 *  360. Sort Transformed Array
 *  Medium
 *
 *  Given a sorted integer array nums and three integers a, b and c, apply a
 *  quadratic function of the form f(x) = ax^2 + bx + c to each element nums[i]
 *  in the array, and return the array in a sorted order.
 *
 *  Example 1:
 *    Input: nums = [-4,-2,2,4], a = 1, b = 3, c = 5
 *    Output: [3,9,15,33]
 *
 *  Example 2:
 *    Input: nums = [-4,-2,2,4], a = -1, b = 3, c = 5
 *    Output: [-23,-5,1,7]
 *
 *  Constraints:
 *    1 <= nums.length <= 200
 *    -100 <= nums[i], a, b, c <= 100
 *    nums is sorted in ascending order.
 *
 *  Follow up: Could you solve it in O(n) time?
 */
public class SortTransformedArray {

    // V0
    // IDEA: 2 POINTERS on the parabola shape
    //       a parabola is "U" shaped when a >= 0 (extremes are at the two ends
    //       of the sorted input) and "n" shaped when a < 0 (extremes at the ends
    //       are the smallest). So compare f(nums[l]) vs f(nums[r]) and fill the
    //       result from the back (a >= 0) or from the front (a < 0).
    /**
     * time = O(N)
     * space = O(1)   // ignoring the output array
     */
    public int[] sortTransformedArray(int[] nums, int a, int b, int c) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] res = new int[n];

        int l = 0;
        int r = n - 1;
        int idx = (a >= 0) ? n - 1 : 0;

        while (l <= r) {
            int lv = f(nums[l], a, b, c);
            int rv = f(nums[r], a, b, c);

            if (a >= 0) {
                // fill largest first, from the back
                if (lv >= rv) {
                    res[idx--] = lv;
                    l++;
                } else {
                    res[idx--] = rv;
                    r--;
                }
            } else {
                // fill smallest first, from the front
                if (lv <= rv) {
                    res[idx++] = lv;
                    l++;
                } else {
                    res[idx++] = rv;
                    r--;
                }
            }
        }
        return res;
    }

    private int f(int x, int a, int b, int c) {
        return a * x * x + b * x + c;
    }
}
