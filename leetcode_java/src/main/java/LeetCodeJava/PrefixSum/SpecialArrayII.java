package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/special-array-ii/description/

/**
 * 3152. Special Array II
 * Medium
 *
 * An array is considered special if every pair of its adjacent elements contains two numbers with different parity.
 *
 * You are given an array of integer nums and a 2D integer matrix queries, where for queries[i] = [from_i, to_i] your task is to check that subarray nums[from_i..to_i] is special or not.
 *
 * Return an array of booleans answer such that answer[i] is true if nums[from_i..to_i] is special.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,1,2,6], queries = [[0,4]]
 * Output: [false]
 * Explanation:
 * The subarray is [3,4,1,2,6]. 2 and 6 are both even.
 *
 * Example 2:
 *
 * Input: nums = [4,3,1,6], queries = [[0,2],[2,3]]
 * Output: [false,true]
 * Explanation:
 * 1. The subarray is [4,3,1]. 3 and 1 are both odd. So the answer to this query is false.
 * 2. The subarray is [1,6]. There is only one pair: (1,6) and it contains numbers with different parity. So the answer to this query is true.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 * 1 <= queries.length <= 10^5
 * queries[i].length == 2
 * 0 <= queries[i][0] <= queries[i][1] <= nums.length - 1
 *
 */
public class SpecialArrayII {

    // V0
    // IDEA: PREFIX-COUNT THE BAD ADJACENT PAIRS, THEN EACH QUERY IS O(1)
    // boundary i (between i-1 and i) is bad when both share a parity.
    // badPrefix[i + 1] = number of bad boundaries among 1..i.
    // the boundaries inside nums[from..to] are from+1 .. to, so the query is
    // special exactly when badPrefix[to + 1] - badPrefix[from + 1] == 0
    // (from == to has no boundaries and is always special).
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public boolean[] isArraySpecial(int[] nums, int[][] queries) {
        int n = nums.length;
        int[] badPrefix = new int[n + 1];
        for (int i = 1; i < n; i++) {
            boolean sameParity = nums[i] % 2 == nums[i - 1] % 2;
            badPrefix[i + 1] = badPrefix[i];
            if (sameParity) {
                badPrefix[i + 1]++;
            }
        }

        boolean[] answer = new boolean[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int from = queries[q][0];
            int to = queries[q][1];
            int badInside = badPrefix[to + 1] - badPrefix[from + 1];
            answer[q] = badInside == 0;
        }
        return answer;
    }
}
