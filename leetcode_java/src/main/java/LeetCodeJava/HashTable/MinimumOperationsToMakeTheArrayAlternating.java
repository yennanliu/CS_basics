package LeetCodeJava.HashTable;

// https://leetcode.com/problems/minimum-operations-to-make-the-array-alternating/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2170. Minimum Operations to Make the Array Alternating
 * Medium
 *
 * You are given a 0-indexed array nums consisting of n positive integers.
 *
 * The array nums is called alternating if:
 *
 * nums[i - 2] == nums[i], where 2 <= i <= n - 1.
 * nums[i - 1] != nums[i], where 1 <= i <= n - 1.
 *
 * In one operation, you can choose an index i and change nums[i] into any positive integer.
 *
 * Return the minimum number of operations required to make the array alternating.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1,3,2,4,3]
 * Output: 3
 * Explanation:
 * One way to make the array alternating is by converting it to [3,1,3,1,3,1].
 * The number of operations required in this case is 3.
 * It can be proven that it is not possible to make the array alternating in less than 3 operations.
 *
 * Example 2:
 *
 * Input: nums = [1,2,2,2,2]
 * Output: 2
 * Explanation:
 * One way to make the array alternating is by converting it to [1,2,1,2,1].
 * The number of operations required in this case is 2.
 * Note that the array cannot be converted to [2,2,2,2,2] because in this case nums[0] == nums[1] which violates the constraint that nums[i - 1] != nums[i].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class MinimumOperationsToMakeTheArrayAlternating {

    // V0
    // IDEA: COUNT EVEN / ODD SLOTS SEPARATELY, KEEP EACH SIDE'S TOP TWO VALUES
    //   the final array is a, b, a, b, ... with a != b, so the cost is
    //   (even slots not equal to a) + (odd slots not equal to b).
    //   take each side's most frequent value; if the two winners collide, the best is
    //   min(even 1st + odd 2nd, even 2nd + odd 1st) - which is why the runner-up is kept.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumOperations(int[] nums) {
        int n = nums.length;

        Map<Integer, Integer> evenCount = new HashMap<>();
        Map<Integer, Integer> oddCount = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                evenCount.put(nums[i], evenCount.getOrDefault(nums[i], 0) + 1);
            } else {
                oddCount.put(nums[i], oddCount.getOrDefault(nums[i], 0) + 1);
            }
        }

        int evenSlots = (n + 1) / 2;
        int oddSlots = n / 2;

        // each is {bestValue, bestCount, secondCount}
        int[] evenTop = topTwo(evenCount);
        int[] oddTop = topTwo(oddCount);

        // winners differ -> keep both of them
        if (evenTop[0] != oddTop[0]) {
            return (evenSlots - evenTop[1]) + (oddSlots - oddTop[1]);
        }

        // winners collide -> one side has to fall back to its runner-up
        int keepEvenBest = (evenSlots - evenTop[1]) + (oddSlots - oddTop[2]);
        int keepOddBest = (evenSlots - evenTop[2]) + (oddSlots - oddTop[1]);
        return Math.min(keepEvenBest, keepOddBest);
    }

    /** Returns {most frequent value, its count, second-highest count}; value 0 (invalid) when empty. */
    private int[] topTwo(Map<Integer, Integer> count) {
        int bestValue = 0;
        int bestCount = 0;
        int secondCount = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int frequency = entry.getValue();
            if (frequency > bestCount) {
                secondCount = bestCount;
                bestCount = frequency;
                bestValue = entry.getKey();
            } else if (frequency > secondCount) {
                secondCount = frequency;
            }
        }
        return new int[] {bestValue, bestCount, secondCount};
    }
}
