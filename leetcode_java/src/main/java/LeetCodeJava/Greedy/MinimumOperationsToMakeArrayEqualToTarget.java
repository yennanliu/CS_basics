package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-operations-to-make-array-equal-to-target/description/

/**
 * 3229. Minimum Operations to Make Array Equal to Target
 * Hard
 *
 * You are given two positive integer arrays nums and target, of the same length.
 *
 * In a single operation, you can select any subarray of nums and increment or decrement each element within that subarray by 1.
 *
 * Return the minimum number of operations required to make nums equal to the array target.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,5,1,2], target = [4,6,2,4]
 * Output: 2
 * Explanation:
 * We will perform the following operations to make nums equal to target:
 * - Increment nums[0..3] by 1, nums = [4,6,2,3].
 * - Increment nums[3..3] by 1, nums = [4,6,2,4].
 *
 * Example 2:
 *
 * Input: nums = [1,3,2], target = [2,1,4]
 * Output: 5
 * Explanation:
 * We will perform the following operations to make nums equal to target:
 * - Increment nums[0..0] by 1, nums = [2,3,2].
 * - Decrement nums[1..1] by 1, nums = [2,2,2].
 * - Decrement nums[1..1] by 1, nums = [2,1,2].
 * - Increment nums[2..2] by 1, nums = [2,1,3].
 * - Increment nums[2..2] by 1, nums = [2,1,4].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length == target.length <= 10^5
 * 1 <= nums[i], target[i] <= 10^8
 *
 */
public class MinimumOperationsToMakeArrayEqualToTarget {

    // V0
    // IDEA: GREEDY ON THE DIFFERENCE ARRAY — ONLY THE *NEW* DEMAND COSTS
    //       diff[i] = target[i] - nums[i]. an operation on a subarray can serve
    //       neighbouring positions whose demand points the same way.
    //       - first position pays |diff[0]|
    //       - same sign as previous: only the excess max(0, |cur| - |prev|) is new
    //       - sign flips (or zero): nothing can be reused, pay |cur|
    //       (same idea as LC 1526 Minimum Number of Increments on Subarrays)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long minimumOperations(int[] nums, int[] target) {
        int n = nums.length;
        long prevDiff = (long) target[0] - nums[0];
        long operations = Math.abs(prevDiff);

        for (int i = 1; i < n; i++) {
            long curDiff = (long) target[i] - nums[i];
            boolean sameSign = (curDiff > 0 && prevDiff > 0) || (curDiff < 0 && prevDiff < 0);
            if (sameSign) {
                // the operations covering i - 1 extend to i; only the extra demand costs
                if (Math.abs(curDiff) > Math.abs(prevDiff)) {
                    operations += Math.abs(curDiff) - Math.abs(prevDiff);
                }
            } else {
                // direction changed: every unit here needs a fresh operation
                operations += Math.abs(curDiff);
            }
            prevDiff = curDiff;
        }
        return operations;
    }
}
