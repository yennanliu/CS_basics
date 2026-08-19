package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximal-score-after-applying-k-operations/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  2530. Maximal Score After Applying K Operations
 *  Medium
 *
 *  You are given a 0-indexed integer array nums and an integer k. You have a
 *  starting score of 0.
 *
 *  In one operation:
 *    1. choose an index i such that 0 <= i < nums.length,
 *    2. increase your score by nums[i], and
 *    3. replace nums[i] with ceil(nums[i] / 3).
 *
 *  Return the maximum possible score you can attain after applying exactly k
 *  operations.
 *
 *  Example 1:
 *    Input: nums = [10,10,10,10,10], k = 5
 *    Output: 50
 *
 *  Example 2:
 *    Input: nums = [1,10,3,3,3], k = 3
 *    Output: 17
 *    Explanation: pick 10 -> 4 -> then 3. score = 10 + 4 + 3 = 17.
 *
 *  Constraints:
 *    1 <= nums.length, k <= 10^5
 *    1 <= nums[i] <= 10^9
 */
public class MaximalScoreAfterApplyingKOperations {

    // V0
    // IDEA: GREEDY + MAX HEAP
    //       every operation is independent : taking the current maximum can never
    //       hurt, since the value left behind (ceil(v/3)) is monotonic in v.
    //       NOTE: ceil(v/3) with INTEGER math -> (v + 2) / 3 (float division would
    //             lose precision for v up to 10^9).
    /**
     * time = O(n + k log n)
     * space = O(n)
     */
    public long maxKelements(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int x : nums) {
            pq.add(x);
        }

        long score = 0L;
        for (int i = 0; i < k; i++) {
            int v = pq.poll();
            score += v;
            pq.add((v + 2) / 3); // ceil(v / 3)
        }
        return score;
    }
}
