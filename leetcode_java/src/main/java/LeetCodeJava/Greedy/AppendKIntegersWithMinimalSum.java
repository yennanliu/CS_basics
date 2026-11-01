package LeetCodeJava.Greedy;

// https://leetcode.com/problems/append-k-integers-with-minimal-sum/description/

import java.util.Arrays;

/**
 * 2195. Append K Integers With Minimal Sum
 * Medium
 *
 * You are given an integer array nums and an integer k. Append k unique positive integers that do not
 * appear in nums to nums such that the resulting total sum is minimum.
 *
 * Return the sum of the k integers appended to nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,25,10,25], k = 2
 * Output: 5
 *
 * Example 2:
 *
 * Input: nums = [5,6], k = 6
 * Output: 25
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= k <= 10^8
 *
 */
public class AppendKIntegersWithMinimalSum {

    // V0
    // IDEA: SORT, WALK THE GAPS BETWEEN DISTINCT VALUES, SUM EACH GAP IN BULK WITH THE ARITHMETIC-SERIES FORMULA
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long minimalKSum(int[] nums, int k) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        long sum = 0;
        long needed = k;
        // the largest value already "accounted for" (present in nums, or taken)
        long previous = 0;

        for (int value : sorted) {
            if (needed == 0) {
                break;
            }
            // the open gap [previous + 1, value - 1] holds missing numbers; duplicates give an empty gap
            if (value > previous + 1) {
                long low = previous + 1;
                long high = Math.min(value - 1, low + needed - 1);
                sum += seriesSum(low, high);
                needed -= high - low + 1;
            }
            previous = Math.max(previous, value);
        }

        // still short: take the run directly above the maximum
        if (needed > 0) {
            sum += seriesSum(previous + 1, previous + needed);
        }
        return sum;
    }

    /** Sum of the integers low..high inclusive. */
    private long seriesSum(long low, long high) {
        return (low + high) * (high - low + 1) / 2;
    }
}
