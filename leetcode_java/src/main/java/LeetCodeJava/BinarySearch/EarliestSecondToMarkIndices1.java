package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/earliest-second-to-mark-indices-i/description/

/**
 * 3048. Earliest Second to Mark Indices I
 * Medium
 *
 * You are given two 1-indexed integer arrays, nums and, changeIndices, having lengths n and m, respectively.
 *
 * Initially, all indices in nums are unmarked. Your task is to mark all indices in nums.
 *
 * In each second, s, in order from 1 to m (inclusive), you can perform one of the following operations:
 *
 * Choose an index i in the range [1, n] and decrement nums[i] by 1.
 * If nums[changeIndices[s]] is equal to 0, mark the index changeIndices[s].
 * Do nothing.
 *
 * Return an integer denoting the earliest second in the range [1, m] when all indices in nums can be marked by choosing operations optimally, or -1 if it is impossible.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,2,0], changeIndices = [2,2,2,2,3,2,2,1]
 * Output: 8
 * Explanation: In this example, we have 8 seconds. The following operations can be performed to mark all indices:
 * Second 1: Choose index 1 and decrement nums[1] by one. nums becomes [1,2,0].
 * Second 2: Choose index 1 and decrement nums[1] by one. nums becomes [0,2,0].
 * Second 3: Choose index 2 and decrement nums[2] by one. nums becomes [0,1,0].
 * Second 4: Choose index 2 and decrement nums[2] by one. nums becomes [0,0,0].
 * Second 5: Mark the index changeIndices[5], which is marking index 3, since nums[3] is equal to 0.
 * Second 6: Mark the index changeIndices[6], which is marking index 2, since nums[2] is equal to 0.
 * Second 7: Do nothing.
 * Second 8: Mark the index changeIndices[8], which is marking index 1, since nums[1] is equal to 0.
 * Now all indices have been marked.
 * It can be shown that it is not possible to mark all indices earlier than the 8th second.
 * Hence, the answer is 8.
 *
 * Example 2:
 *
 * Input: nums = [1,3], changeIndices = [1,1,1,2,1,1,1]
 * Output: 6
 * Explanation: In this example, we have 7 seconds. The following operations can be performed to mark all indices:
 * Second 1: Choose index 2 and decrement nums[2] by one. nums becomes [1,2].
 * Second 2: Choose index 2 and decrement nums[2] by one. nums becomes [1,1].
 * Second 3: Choose index 2 and decrement nums[2] by one. nums becomes [1,0].
 * Second 4: Mark the index changeIndices[4], which is marking index 2, since nums[2] is equal to 0.
 * Second 5: Choose index 1 and decrement nums[1] by one. nums becomes [0,0].
 * Second 6: Mark the index changeIndices[6], which is marking index 1, since nums[1] is equal to 0.
 * Now all indices have been marked.
 * It can be shown that it is not possible to mark all indices earlier than the 6th second.
 * Hence, the answer is 6.
 *
 * Example 3:
 *
 * Input: nums = [0,1], changeIndices = [2,2,2]
 * Output: -1
 * Explanation: In this example, it is impossible to mark all indices because index 1 isn't in changeIndices.
 * Hence, the answer is -1.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 2000
 * 0 <= nums[i] <= 10^9
 * 1 <= m == changeIndices.length <= 2000
 * 1 <= changeIndices[i] <= n
 *
 */
public class EarliestSecondToMarkIndices1 {

    // V0
    // IDEA: BINARY SEARCH THE DEADLINE + GREEDY "MARK AT THE LAST CHANCE"
    // more seconds never hurt, so binary search the smallest feasible s. For a fixed s, each
    // index is marked at its LAST occurrence within the first s seconds (as late as possible,
    // leaving the most room for decrements). Sweep the seconds keeping a pool of spare ones:
    // a second that is nobody's marking second adds to the pool; a marking second must first
    // pay nums[i] spare seconds for the decrements of that index.
    /**
     * time = O(M log M)
     * space = O(N + M)
     */
    public int earliestSecondToMarkIndices(int[] nums, int[] changeIndices) {
        int m = changeIndices.length;
        int left = 1;
        int right = m;
        int answer = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canMarkAll(nums, changeIndices, mid)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return answer;
    }

    /** Whether every index can be marked within the first `seconds` seconds. */
    private boolean canMarkAll(int[] nums, int[] changeIndices, int seconds) {
        int n = nums.length;
        // lastSecond[i] = last 0-based second (< seconds) at which index i (0-based) can be marked
        int[] lastSecond = new int[n];
        for (int i = 0; i < n; i++) {
            lastSecond[i] = -1;
        }
        for (int t = 0; t < seconds; t++) {
            lastSecond[changeIndices[t] - 1] = t;
        }

        // markAt[t] = the index marked at second t, or -1 for a spare second
        int[] markAt = new int[seconds];
        for (int t = 0; t < seconds; t++) {
            markAt[t] = -1;
        }
        for (int i = 0; i < n; i++) {
            if (lastSecond[i] == -1) {
                // index i never shows up, so it can never be marked
                return false;
            }
            markAt[lastSecond[i]] = i;
        }

        long spare = 0;
        for (int t = 0; t < seconds; t++) {
            if (markAt[t] == -1) {
                spare++;
            } else {
                int need = nums[markAt[t]];
                if (spare < need) {
                    return false;
                }
                // spend `need` earlier spare seconds decrementing, then mark now
                spare -= need;
            }
        }
        return true;
    }
}
