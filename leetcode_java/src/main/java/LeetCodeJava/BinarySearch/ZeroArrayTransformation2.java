package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/zero-array-transformation-ii/description/

/**
 * 3356. Zero Array Transformation II
 * Medium
 *
 * You are given an integer array nums of length n and a 2D array queries where queries[i] = [l_i, r_i, val_i].
 *
 * Each queries[i] represents the following action on nums:
 *
 * Decrement the value at each index in the range [l_i, r_i] in nums by at most val_i.
 * The amount by which each value is decremented can be chosen independently for each index.
 *
 * A Zero Array is an array with all its elements equal to 0.
 *
 * Return the minimum possible non-negative value of k, such that after processing the first k queries in sequence, nums becomes a Zero Array. If no such k exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,0,2], queries = [[0,2,1],[0,2,1],[1,1,3]]
 * Output: 2
 * Explanation:
 * For i = 0 (l = 0, r = 2, val = 1): Decrement values at indices [0, 1, 2] by [1, 0, 1] respectively. The array will become [1, 0, 1].
 * For i = 1 (l = 0, r = 2, val = 1): Decrement values at indices [0, 1, 2] by [1, 0, 1] respectively. The array will become [0, 0, 0], which is a Zero Array. Therefore, the minimum value of k is 2.
 *
 * Example 2:
 *
 * Input: nums = [4,3,2,1], queries = [[1,3,2],[0,2,1]]
 * Output: -1
 * Explanation:
 * For i = 0 (l = 1, r = 3, val = 2): Decrement values at indices [1, 2, 3] by [2, 2, 1] respectively. The array will become [4, 1, 0, 0].
 * For i = 1 (l = 0, r = 2, val = 1): Decrement values at indices [0, 1, 2] by [1, 1, 0] respectively. The array will become [3, 0, 0, 0], which is not a Zero Array.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 5 * 10^5
 * 1 <= queries.length <= 10^5
 * queries[i].length == 3
 * 0 <= l_i <= r_i < nums.length
 * 1 <= val_i <= 5
 *
 */
public class ZeroArrayTransformation2 {

    // V0
    // IDEA: BINARY SEARCH ON THE NUMBER OF QUERIES + DIFFERENCE ARRAY CHECK
    // using MORE queries never hurts (each may decrement by anything from 0 to val), so
    // feasibility is monotone in k. For a fixed k the indices are independent: index i is
    // zeroable iff the total val of the first k queries covering it reaches nums[i], and a
    // difference array sums those vals in one pass.
    /**
     * time = O((N + Q) log Q)
     * space = O(N)
     */
    public int minZeroArray(int[] nums, int[][] queries) {
        // already a zero array: no query needed
        boolean allZero = true;
        for (int num : nums) {
            if (num != 0) {
                allZero = false;
                break;
            }
        }
        if (allZero) {
            return 0;
        }

        int left = 1;
        int right = queries.length;
        int answer = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canZero(nums, queries, mid)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return answer;
    }

    /** Whether the first k queries can bring every element to zero. */
    private boolean canZero(int[] nums, int[][] queries, int k) {
        int n = nums.length;
        long[] diff = new long[n + 1];
        for (int t = 0; t < k; t++) {
            int from = queries[t][0];
            int to = queries[t][1];
            int value = queries[t][2];
            diff[from] += value;
            diff[to + 1] -= value;
        }
        long coverage = 0;
        for (int i = 0; i < n; i++) {
            coverage += diff[i];
            if (coverage < nums[i]) {
                return false;
            }
        }
        return true;
    }
}
