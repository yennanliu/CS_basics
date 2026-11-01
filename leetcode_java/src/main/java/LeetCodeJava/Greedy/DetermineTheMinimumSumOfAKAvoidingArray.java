package LeetCodeJava.Greedy;

// https://leetcode.com/problems/determine-the-minimum-sum-of-a-k-avoiding-array/description/

/**
 * 2829. Determine the Minimum Sum of a k-avoiding Array
 * Medium
 *
 * You are given two integers, n and k.
 *
 * An array of distinct positive integers is called a k-avoiding array if there does not exist any pair of distinct elements that sum to k.
 *
 * Return the minimum possible sum of a k-avoiding array of length n.
 *
 *
 * Example 1:
 *
 * Input: n = 5, k = 4
 * Output: 18
 * Explanation: Consider the k-avoiding array [1,2,4,5,6], which has a sum of 18.
 * It can be proven that there is no k-avoiding array with a sum less than 18.
 *
 * Example 2:
 *
 * Input: n = 2, k = 6
 * Output: 3
 * Explanation: We can construct the array [1,2], which has a sum of 3.
 * It can be proven that there is no k-avoiding array with a sum less than 3.
 *
 *
 * Constraints:
 *
 * 1 <= n, k <= 50
 *
 */
public class DetermineTheMinimumSumOfAKAvoidingArray {

    // V0
    // IDEA: GREEDY (take the smallest still-legal integer each time)
    //       integers pair up as {1, k-1}, {2, k-2}, ...; at most one of each pair may be used.
    //       scanning upwards, taking i bans its partner k - i, which is always larger than i,
    //       so we never give up a small number for a bigger one. i == k - i is fine: the two
    //       elements of a pair must be distinct.
    /**
     * time = O(N + K)
     * space = O(N + K)
     */
    public int minimumSum(int n, int k) {
        // every chosen value is at most n + k, so this covers every partner we ban
        boolean[] banned = new boolean[n + k + 1];
        int total = 0;
        int candidate = 1;
        for (int taken = 0; taken < n; taken++) {
            while (banned[candidate]) {
                candidate++;
            }
            total += candidate;
            // its partner (if positive) can no longer be used
            int partner = k - candidate;
            if (partner > 0) {
                banned[partner] = true;
            }
            candidate++;
        }
        return total;
    }
}
