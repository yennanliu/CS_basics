package LeetCodeJava.Greedy;

// https://leetcode.com/problems/reach-end-of-array-with-max-score/description/

import java.util.List;

/**
 * 3282. Reach End of Array With Max Score
 * Medium
 *
 * You are given an integer array nums of length n.
 *
 * Your goal is to start at index 0 and reach index n - 1. You can only jump to indices greater than your current index.
 *
 * The score for a jump from index i to index j is calculated as (j - i) * nums[i].
 *
 * Return the maximum possible total score by the time you reach the last index.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,1,5]
 * Output: 7
 * Explanation:
 * First, jump to index 1 and then jump to the last index. The final score is 1 * 1 + 2 * 3 = 7.
 *
 * Example 2:
 *
 * Input: nums = [4,3,1,3,2]
 * Output: 16
 * Explanation:
 * Jump directly to the last index. The final score is 4 * 4 = 16.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class ReachEndOfArrayWithMaxScore {

    // V0
    // IDEA: GREEDY — PAY EVERY UNIT STEP AT THE BEST RATE SEEN SO FAR
    //       a jump i -> j earns nums[i] per unit step covered, so the total is
    //       "sum over the n-1 unit steps of the value we were standing on".
    //       jumps can always be split/merged, so the step t -> t+1 is best paid by
    //       max(nums[0..t]). sweep once with a running maximum.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long findMaximumScore(List<Integer> nums) {
        long bestRate = 0;
        long totalScore = 0;

        // the last index is only a destination, never a launch point
        for (int i = 0; i < nums.size() - 1; i++) {
            bestRate = Math.max(bestRate, nums.get(i));
            totalScore += bestRate;
        }
        return totalScore;
    }
}
