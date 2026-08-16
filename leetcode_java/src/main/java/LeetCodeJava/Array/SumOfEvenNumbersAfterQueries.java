package LeetCodeJava.Array;

// https://leetcode.com/problems/sum-of-even-numbers-after-queries/description/
/**
 * 985. Sum of Even Numbers After Queries
 * Medium
 *
 * You are given an integer array nums and an array queries where queries[i] = [val_i, index_i].
 *
 * For each query i, first, apply nums[index_i] = nums[index_i] + val_i, then print the sum of
 * the even values of nums.
 *
 * Return an integer array answer where answer[i] is the answer to the ith query.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4], queries = [[1,0],[-3,1],[-4,0],[2,3]]
 * Output: [8,6,2,4]
 * Explanation: At the beginning, the array is [1,2,3,4].
 * After adding 1 to nums[0], the array is [2,2,3,4], and the sum of even values is 2 + 2 + 4 = 8.
 * After adding -3 to nums[1], the array is [2,-1,3,4], and the sum of even values is 2 + 4 = 6.
 * After adding -4 to nums[0], the array is [-2,-1,3,4], and the sum of even values is -2 + 4 = 2.
 * After adding 2 to nums[3], the array is [-2,-1,3,6], and the sum of even values is -2 + 6 = 4.
 *
 * Example 2:
 *
 * Input: nums = [1], queries = [[4,0]]
 * Output: [0]
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^4
 * -10^4 <= nums[i] <= 10^4
 * 1 <= queries.length <= 10^4
 * -10^4 <= val_i <= 10^4
 * 0 <= index_i < nums.length
 *
 */
public class SumOfEvenNumbersAfterQueries {

    // V0
    // IDEA: RUNNING SUM (only the touched element can change parity)
    /**
     *  Keep `evenSum` = sum of all even values. For each query only ONE index
     *  changes, so:
     *     1) if the OLD value was even, remove it from evenSum
     *     2) apply the delta
     *     3) if the NEW value is even, add it back
     *
     *  -> recomputing the whole sum per query would be O(n * m) instead.
     *
     *  NOTE: values can go NEGATIVE. `x % 2 == 0` is still a correct evenness test
     *        in java for negatives (e.g. -2 % 2 == 0), but `x % 2 == 1` would NOT be
     *        (java gives -1, not 1, for odd negatives).
     *
     *  time  = O(n + m), n = nums.length, m = queries.length
     *  space = O(1) excluding the output array
     */
    public int[] sumEvenAfterQueries(int[] nums, int[][] queries) {
        int evenSum = 0;
        for (int x : nums) {
            if (x % 2 == 0) {
                evenSum += x;
            }
        }

        int[] res = new int[queries.length];

        for (int q = 0; q < queries.length; q++) {
            int val = queries[q][0];
            int idx = queries[q][1];

            /** NOTE !!!
             *
             *  remove the OLD value FIRST (if it was even),
             *  then mutate, then add the NEW value back (if it is even)
             */
            if (nums[idx] % 2 == 0) {
                evenSum -= nums[idx];
            }

            nums[idx] += val;

            if (nums[idx] % 2 == 0) {
                evenSum += nums[idx];
            }

            res[q] = evenSum;
        }

        return res;
    }

}
