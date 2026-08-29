package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-length-of-subarray-with-positive-product/

/**
 *  1567. Maximum Length of Subarray With Positive Product
 *  Medium
 *
 *  Given an array of integers nums, find the maximum length of a subarray where
 *  the product of all its elements is positive.
 *
 *  A subarray of an array is a consecutive sequence of zero or more values taken
 *  out of that array.
 *
 *  Return the maximum length of a subarray with positive product.
 *
 *
 *  Example 1:
 *
 *  Input: nums = [1,-2,-3,4]
 *  Output: 4
 *  Explanation: The array nums already has a positive product of 24.
 *
 *  Example 2:
 *
 *  Input: nums = [0,1,-2,-3,-4]
 *  Output: 3
 *  Explanation: The longest subarray with positive product is [1,-2,-3].
 *
 *  Example 3:
 *
 *  Input: nums = [-1,-2,-3,0,1]
 *  Output: 2
 *
 *
 *  Constraints:
 *
 *  1 <= nums.length <= 10^5
 *  -10^9 <= nums[i] <= 10^9
 */
public class MaximumLengthOfSubarrayWithPositiveProduct {

    // V0
    // IDEA: DP — track longest subarray ending at i with positive / negative product
    /**
     * time = O(n)
     * space = O(1)
     */
    public int getMaxLen(int[] nums) {
        int pos = 0; // len of longest subarray ending here with positive product
        int neg = 0; // len of longest subarray ending here with negative product
        int res = 0;

        for (int x : nums) {
            if (x == 0) {
                pos = 0;
                neg = 0;
            } else if (x > 0) {
                pos = pos + 1;
                neg = (neg > 0) ? neg + 1 : 0;
            } else {
                int newPos = (neg > 0) ? neg + 1 : 0;
                int newNeg = pos + 1;
                pos = newPos;
                neg = newNeg;
            }
            res = Math.max(res, pos);
        }
        return res;
    }

    // V1
    // IDEA: 2 POINTERS — per zero-free segment, cut at the first / last negative
    /**
     * time = O(n)
     * space = O(1)
     */
    public int getMaxLen_1(int[] nums) {
        int n = nums.length;
        int res = 0;
        int i = 0;
        while (i < n) {
            if (nums[i] == 0) {
                i++;
                continue;
            }
            int start = i;
            int negCnt = 0;
            int firstNeg = -1;
            int lastNeg = -1;
            while (i < n && nums[i] != 0) {
                if (nums[i] < 0) {
                    negCnt++;
                    if (firstNeg < 0) {
                        firstNeg = i;
                    }
                    lastNeg = i;
                }
                i++;
            }
            int end = i - 1; // inclusive
            if (negCnt % 2 == 0) {
                res = Math.max(res, end - start + 1);
            } else {
                // drop the prefix up to firstNeg, or the suffix from lastNeg
                res = Math.max(res, end - firstNeg);
                res = Math.max(res, lastNeg - start);
            }
        }
        return res;
    }

    // V2
    // IDEA: brute force O(n^2) — expand every start index and track the running
    //       sign; kept as a readable correctness reference
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public int getMaxLen_2(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            int sign = 1;
            for (int j = i; j < nums.length; j++) {
                if (nums[j] == 0) {
                    break;
                }
                sign = (nums[j] > 0) ? sign : -sign;
                if (sign > 0) {
                    res = Math.max(res, j - i + 1);
                }
            }
        }
        return res;
    }
}
