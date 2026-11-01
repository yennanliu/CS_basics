package LeetCodeJava.Math;

// https://leetcode.com/problems/prime-pairs-with-target-sum/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 2761. Prime Pairs With Target Sum
 * Medium
 *
 * You are given an integer n. We say that two integers x and y form a prime number pair if:
 *
 * 1 <= x <= y <= n
 * x + y == n
 * x and y are prime numbers
 *
 * Return the 2D sorted list of prime number pairs [xi, yi]. The list should be sorted in increasing order of xi. If there are no prime number pairs at all, return an empty array.
 *
 * Note: A prime number is a natural number greater than 1 with only two factors, itself and 1.
 *
 *
 * Example 1:
 *
 * Input: n = 10
 * Output: [[3,7],[5,5]]
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: []
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^6
 *
 */
public class PrimePairsWithTargetSum {

    // V0
    // IDEA: SIEVE OF ERATOSTHENES + ENUMERATE THE SMALLER PRIME x in [2, n / 2]; y = n - x is then fixed
    /**
     * time = O(N log log N)
     * space = O(N)
     */
    public List<List<Integer>> findPrimePairs(int n) {
        List<List<Integer>> pairs = new ArrayList<>();
        // smallest pair is 2 + 2 = 4
        if (n < 4) {
            return pairs;
        }

        boolean[] isComposite = new boolean[n + 1];
        isComposite[0] = true;
        isComposite[1] = true;
        for (int i = 2; (long) i * i <= n; i++) {
            if (!isComposite[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isComposite[j] = true;
                }
            }
        }

        // x only increases, so the pairs come out already sorted
        for (int x = 2; x <= n / 2; x++) {
            int y = n - x;
            if (!isComposite[x] && !isComposite[y]) {
                pairs.add(Arrays.asList(x, y));
            }
        }
        return pairs;
    }
}
