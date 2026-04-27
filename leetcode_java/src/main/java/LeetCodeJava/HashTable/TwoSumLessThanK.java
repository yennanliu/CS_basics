package LeetCodeJava.HashTable;

// https://leetcode.com/problems/two-sum-less-than-k/description/
// https://leetcode.ca/all/1099.html

import java.util.Arrays;

/**
 *  1099. Two Sum Less Than K
 * Given an array A of integers and integer K, return the maximum S such that there exists i < j with A[i] + A[j] = S and S < K. If no i, j exist satisfying this equation, return -1.
 *
 *
 *
 * Example 1:
 *
 * Input: A = [34,23,1,24,75,33,54,8], K = 60
 * Output: 58
 * Explanation:
 * We can use 34 and 24 to sum 58 which is less than 60.
 * Example 2:
 *
 * Input: A = [10,20,30], K = 15
 * Output: -1
 * Explanation:
 * In this case it's not possible to get a pair sum less that 15.
 *
 *
 * Note:
 *
 * 1 <= A.length <= 100
 * 1 <= A[i] <= 1000
 * 1 <= K <= 2000
 * Difficulty:
 * Easy
 * Lock:
 * Prime
 *
 */
public class TwoSumLessThanK {

    // V0
//    public int twoSumLessThanK(int[] nums, int k) {
//    }

    // V1-1
    // IDEA: Sorting + Binary Search
    // https://leetcode.ca/2018-12-03-1099-Two-Sum-Less-Than-K/


    // V1-2
    // IDEA: Sorting + Two Pointers
    // https://leetcode.ca/2018-12-03-1099-Two-Sum-Less-Than-K/
    public int twoSumLessThanK_1_2(int[] nums, int k) {
        Arrays.sort(nums);
        int ans = -1;
        int n = nums.length;
        for (int i = 0; i < n; ++i) {
            int j = search(nums, k - nums[i], i + 1, n) - 1;
            if (i < j) {
                ans = Math.max(ans, nums[i] + nums[j]);
            }
        }
        return ans;
    }

    private int search(int[] nums, int x, int l, int r) {
        while (l < r) {
            int mid = (l + r) >> 1;
            if (nums[mid] >= x) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }



}
