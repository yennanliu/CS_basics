package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-derangement-of-an-array/

/**
 *  634. Find the Derangement of An Array
 *  Medium
 *
 *  In combinatorial mathematics, a derangement is a permutation of the elements
 *  of a set, such that no element appears in its original position.
 *
 *  You are given an integer n. There is originally an array consisting of n
 *  integers from 1 to n in ascending order, return the number of derangements
 *  it can generate. Since the answer may be huge, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: n = 3
 *    Output: 2
 *    Explanation: The original array is [1,2,3]. The two derangements are
 *                 [2,3,1] and [3,1,2].
 *
 *  Example 2:
 *    Input: n = 2
 *    Output: 1
 *
 *  Constraints:
 *   - 1 <= n <= 10^6
 */
public class FindTheDerangementOfAnArray {

    // V0
    // IDEA: DP with recurrence D(n) = (n - 1) * ( D(n-1) + D(n-2) ),
    //       D(0) = 1, D(1) = 0.
    //       (element n swaps with one of the (n-1) others; that other element
    //        either lands on n's slot -> D(n-2), or not -> D(n-1))
    /**
     * time = O(n)
     * space = O(1)
     */
    public int findDerangement(int n) {

        final long MOD = 1000000007L;

        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return 0;
        }

        long prev2 = 1; // D(0)
        long prev1 = 0; // D(1)

        for (int i = 2; i <= n; i++) {
            long cur = ((long) (i - 1) % MOD) * ((prev1 + prev2) % MOD) % MOD;
            prev2 = prev1;
            prev1 = cur;
        }

        return (int) prev1;
    }
}
