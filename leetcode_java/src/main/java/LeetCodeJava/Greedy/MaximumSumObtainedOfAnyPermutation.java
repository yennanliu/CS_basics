package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-sum-obtained-of-any-permutation/description/

import java.util.Arrays;

/**
 * 1589. Maximum Sum Obtained of Any Permutation
 * Medium
 *
 * We have an array of integers, nums, and an array of requests where
 * requests[i] = [starti, endi]. The ith request asks for the sum of
 * nums[starti] + nums[starti + 1] + ... + nums[endi - 1] + nums[endi].
 * Both starti and endi are 0-indexed.
 *
 * Return the maximum total sum of all requests among all permutations of nums.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5], requests = [[1,3],[0,1]]
 * Output: 19
 * Explanation: A permutation with the highest total sum is [3,5,4,2,1]:
 * requests[0] -> 5 + 4 + 2 = 11, requests[1] -> 3 + 5 = 8. Total 19.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5,6], requests = [[0,1]]
 * Output: 11
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,4,5,10], requests = [[0,2],[1,3],[1,1]]
 * Output: 47
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 10^5
 * 0 <= nums[i] <= 10^5
 * 1 <= requests.length <= 10^5
 * requests[i].length == 2
 * 0 <= starti <= endi < n
 *
 */
public class MaximumSumObtainedOfAnyPermutation {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: DIFFERENCE ARRAY + GREEDY (BIGGEST NUMBER ON THE HOTTEST INDEX)
    //       requests only decide HOW OFTEN each index is summed; count that with a
    //       difference array, then pair the largest value with the largest
    //       frequency (rearrangement inequality).
    /**
     * time = O(N log N + Q)
     * space = O(N)
     */
    public int maxSumRangeQuery(int[] nums, int[][] requests) {
        int n = nums.length;

        // diff[a]++ and diff[b + 1]-- marks the range [a, b] as covered once more
        int[] diff = new int[n + 1];
        for (int[] request : requests) {
            diff[request[0]] += 1;
            diff[request[1] + 1] -= 1;
        }

        // prefix sum of the difference array = how many requests cover index i
        int[] frequency = new int[n];
        int running = 0;
        for (int i = 0; i < n; i++) {
            running += diff[i];
            frequency[i] = running;
        }

        // sort both ascending, so index i of each pairs k-th largest with k-th largest
        Arrays.sort(frequency);
        int[] sortedNums = nums.clone();
        Arrays.sort(sortedNums);

        long total = 0;
        for (int i = 0; i < n; i++) {
            total = (total + (long) sortedNums[i] * frequency[i]) % MOD;
        }
        return (int) total;
    }
}
