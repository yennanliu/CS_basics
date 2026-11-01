package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-increments-on-subarrays-to-form-a-target-array/description/

/**
 * 1526. Minimum Number of Increments on Subarrays to Form a Target Array
 * Hard
 *
 * You are given an integer array target. You have an integer array initial of the same size
 * as target with all elements initially zeros.
 *
 * In one operation you can choose any subarray from initial and increment each value by one.
 *
 * Return the minimum number of operations to form a target array from initial.
 *
 * The test cases are generated so that the answer fits in a 32-bit integer.
 *
 *
 * Example 1:
 *
 * Input: target = [1,2,3,2,1]
 * Output: 3
 * Explanation: We need at least 3 operations to form the target array from the initial array.
 * [0,0,0,0,0] increment 1 from index 0 to 4 (inclusive).
 * [1,1,1,1,1] increment 1 from index 1 to 3 (inclusive).
 * [1,2,2,2,1] increment 1 at index 2.
 * [1,2,3,2,1] target array is formed.
 *
 * Example 2:
 *
 * Input: target = [3,1,1,2]
 * Output: 4
 * Explanation: [0,0,0,0] -> [1,1,1,1] -> [1,1,1,2] -> [2,1,1,2] -> [3,1,1,2]
 *
 * Example 3:
 *
 * Input: target = [3,1,5,4,2]
 * Output: 7
 * Explanation: [0,0,0,0,0] -> [1,1,1,1,1] -> [2,1,1,1,1] -> [3,1,1,1,1] -> [3,1,2,2,2]
 * -> [3,1,3,3,2] -> [3,1,4,4,2] -> [3,1,5,4,2].
 *
 *
 * Constraints:
 *
 * 1 <= target.length <= 10^5
 * 1 <= target[i] <= 10^5
 * The input is generated such that the answer fits inside a 32 bit integer.
 *
 */
public class MinimumNumberOfIncrementsOnSubarraysToFormATargetArray {

    // V0
    // IDEA: GREEDY / SUM OF POSITIVE RISES (answer = target[0] + sum of max(0, target[i] - target[i-1]))
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minNumberOperations(int[] target) {
        // every operation is a "+1 bar" over a contiguous range.
        // the first element needs target[0] bars to start at index 0
        int operations = target[0];

        for (int i = 1; i < target.length; i++) {
            // bars covering i-1 extend to i for free up to height target[i-1];
            // only the extra height must come from brand-new bars.
            // a drop costs nothing: some bars simply end there
            if (target[i] > target[i - 1]) {
                operations += target[i] - target[i - 1];
            }
        }
        return operations;
    }
}
