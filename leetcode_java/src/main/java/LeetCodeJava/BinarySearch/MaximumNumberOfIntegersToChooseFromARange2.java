package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-ii/description/

import java.util.TreeSet;

/**
 * 2557. Maximum Number of Integers to Choose From a Range II
 * Medium
 *
 * You are given an integer array banned and two integers n and maxSum. You are choosing some number of integers following the below rules:
 *
 * - The chosen integers have to be in the range [1, n].
 * - Each integer can be chosen at most once.
 * - The chosen integers should not be in the array banned.
 * - The sum of the chosen integers should not exceed maxSum.
 *
 * Return the maximum number of integers you can choose following the mentioned rules.
 *
 *
 * Example 1:
 *
 * Input: banned = [1,4,6], n = 6, maxSum = 4
 * Output: 1
 * Explanation: You can choose the integer 3.
 * 3 is in the range [1, 6], and do not appear in banned. The sum of the chosen integers is 3, which does not exceed maxSum.
 *
 * Example 2:
 *
 * Input: banned = [4,3,5,6], n = 7, maxSum = 18
 * Output: 3
 * Explanation: You can choose the integers 1, 2, and 7.
 * All these integers are in the range [1, 7], all do not appear in banned, and their sum is 10, which does not exceed maxSum.
 *
 *
 * Constraints:
 *
 * 1 <= banned.length <= 10^5
 * 1 <= banned[i] <= n <= 10^9
 * 1 <= maxSum <= 10^15
 *
 */
public class MaximumNumberOfIntegersToChooseFromARange2 {

    // V0
    // IDEA: GREEDY (SMALLEST FIRST) + BINARY SEARCH INSIDE EACH FREE GAP
    // to maximise the COUNT, always take the smallest allowed integers. n is up to 1e9, so
    // walk the gaps between consecutive (sorted, de-duplicated) banned values instead,
    // with sentinels 0 and n + 1. Gap (low, high) holds low+1 .. high-1, and taking the
    // first t of them costs t * (2*low + 1 + t) / 2, monotone in t -> binary search t.
    /**
     * time = O(M log M + M log N)   (M = banned.length)
     * space = O(M)
     */
    public int maxCount(int[] banned, int n, long maxSum) {
        // sorted, de-duplicated banned values plus the two sentinels
        TreeSet<Integer> bannedSet = new TreeSet<>();
        bannedSet.add(0);
        bannedSet.add(n + 1);
        for (int value : banned) {
            bannedSet.add(value);
        }

        long budget = maxSum;
        int chosen = 0;
        Integer previous = null;
        for (int current : bannedSet) {
            if (previous != null) {
                long low = previous;
                // binary search the largest t with cost(t) <= budget
                long lo = 0;
                long hi = current - low - 1;
                while (lo < hi) {
                    long mid = lo + (hi - lo + 1) / 2;
                    if (rangeSum(low, mid) <= budget) {
                        lo = mid;
                    } else {
                        hi = mid - 1;
                    }
                }
                chosen += (int) lo;
                budget -= rangeSum(low, lo);
                // every later gap only holds larger integers
                if (budget <= 0) {
                    break;
                }
            }
            previous = current;
        }
        return chosen;
    }

    /** Sum of (low+1) + (low+2) + ... + (low+count). */
    private long rangeSum(long low, long count) {
        return count * (2 * low + 1 + count) / 2;
    }
}
