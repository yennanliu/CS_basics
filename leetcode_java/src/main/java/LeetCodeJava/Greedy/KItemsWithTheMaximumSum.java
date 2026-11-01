package LeetCodeJava.Greedy;

// https://leetcode.com/problems/k-items-with-the-maximum-sum/description/

/**
 * 2600. K Items With the Maximum Sum
 * Easy
 *
 * There is a bag that consists of items, each item has a number 1, 0, or -1 written on it.
 *
 * You are given four non-negative integers numOnes, numZeros, numNegOnes, and k.
 *
 * The bag initially contains:
 *
 * - numOnes items with 1s written on them.
 * - numZeroes items with 0s written on them.
 * - numNegOnes items with -1s written on them.
 *
 * We want to pick exactly k items among the available items. Return the maximum possible sum of numbers written on the items.
 *
 *
 * Example 1:
 *
 * Input: numOnes = 3, numZeros = 2, numNegOnes = 0, k = 2
 * Output: 2
 * Explanation: We have a bag of items with numbers written on them {1, 1, 1, 0, 0}. We take 2 items with 1 written on them and get a sum in a total of 2.
 * It can be proven that 2 is the maximum possible sum.
 *
 * Example 2:
 *
 * Input: numOnes = 3, numZeros = 2, numNegOnes = 0, k = 4
 * Output: 3
 * Explanation: We have a bag of items with numbers written on them {1, 1, 1, 0, 0}. We take 3 items with 1 written on them, and 1 item with 0 written on it, and get a sum in a total of 3.
 * It can be proven that 3 is the maximum possible sum.
 *
 *
 * Constraints:
 *
 * 0 <= numOnes, numZeros, numNegOnes <= 50
 * 0 <= k <= numOnes + numZeros + numNegOnes
 *
 */
public class KItemsWithTheMaximumSum {

    // V0
    // IDEA: GREEDY (take the biggest labels first: 1s, then 0s, then the forced -1s)
    /**
     * time = O(1)
     * space = O(1)
     */
    public int kItemsWithMaximumSum(int numOnes, int numZeros, int numNegOnes, int k) {
        // enough ones to fill every pick
        if (k <= numOnes) {
            return k;
        }
        // all the ones, padded with zeros
        if (k <= numOnes + numZeros) {
            return numOnes;
        }
        // whatever is still left must be -1s
        int forcedNegOnes = k - numOnes - numZeros;
        return numOnes - forcedNegOnes;
    }
}
