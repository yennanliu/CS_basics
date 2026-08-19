package LeetCodeJava.Math;

// https://leetcode.com/problems/permutation-sequence/

import java.util.ArrayList;
import java.util.List;

/**
 *  60. Permutation Sequence
 *  Hard
 *
 *  The set [1, 2, 3, ..., n] contains a total of n! unique permutations.
 *  By listing and labeling all of the permutations in order, we get the following
 *  sequence for n = 3:
 *    "123", "132", "213", "231", "312", "321"
 *
 *  Given n and k, return the kth permutation sequence.
 *
 *  Example 1:
 *    Input: n = 3, k = 3
 *    Output: "213"
 *
 *  Example 2:
 *    Input: n = 4, k = 9
 *    Output: "2314"
 *
 *  Example 3:
 *    Input: n = 3, k = 1
 *    Output: "123"
 *
 *  Constraints:
 *    1 <= n <= 9
 *    1 <= k <= n!
 */
public class PermutationSequence {

    // V0
    // IDEA: factorial number system - pick each digit by index k / (remaining-1)!
    /**
     * time = O(n^2)   (list removal is O(n))
     * space = O(n)
     */
    public String getPermutation(int n, int k) {

        // factorials: fact[i] = i!
        int[] fact = new int[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i;
        }

        List<Integer> candidates = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            candidates.add(i);
        }

        k -= 1;   // switch to 0-based index

        StringBuilder sb = new StringBuilder();
        for (int i = n; i >= 1; i--) {
            int blockSize = fact[i - 1];
            int idx = k / blockSize;
            k %= blockSize;
            sb.append(candidates.remove(idx));
        }

        return sb.toString();
    }
}
