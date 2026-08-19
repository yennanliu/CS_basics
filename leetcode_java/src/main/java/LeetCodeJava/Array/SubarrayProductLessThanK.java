package LeetCodeJava.Array;

// https://leetcode.com/problems/subarray-product-less-than-k/

/**
 *  713. Subarray Product Less Than K
 *  Medium
 *
 *  Given an array of integers nums and an integer k, return the number of
 *  contiguous subarrays where the product of all the elements in the subarray
 *  is strictly less than k.
 *
 *  Example 1:
 *    Input: nums = [10,5,2,6], k = 100
 *    Output: 8
 *    Explanation: The 8 subarrays that have product less than 100 are:
 *    [10], [5], [2], [6], [10, 5], [5, 2], [2, 6], [5, 2, 6].
 *    Note that [10, 5, 2] is not included as the product of 100 is not
 *    strictly less than k.
 *
 *  Example 2:
 *    Input: nums = [1,2,3], k = 0
 *    Output: 0
 *
 *  Constraints:
 *    1 <= nums.length <= 3 * 10^4
 *    1 <= nums[i] <= 1000
 *    0 <= k <= 10^6
 */
public class SubarrayProductLessThanK {

    // V0
    // IDEA: SLIDING WINDOW. Keep window product < k; every time we extend the
    //       right end to j, the window [i..j] contributes (j - i + 1) new subarrays.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        if (k <= 1) {
            return 0;
        }
        int res = 0;
        int product = 1;
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            product *= nums[j];
            while (i <= j && product >= k) {
                product /= nums[i];
                i++;
            }
            res += (j - i + 1);
        }
        return res;
    }
}
