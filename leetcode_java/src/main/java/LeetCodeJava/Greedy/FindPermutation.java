package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-permutation/description/
/**
 * 484. Find Permutation
 * Medium
 * Lock: Prime
 *
 * A permutation perm of n integers of all the integers in the range [1, n] can be
 * represented as a string s of length n - 1 where:
 *
 * - s[i] == 'I' if perm[i] < perm[i + 1], and
 * - s[i] == 'D' if perm[i] > perm[i + 1].
 *
 * Given a string s, reconstruct the lexicographically smallest permutation perm
 * and return it.
 *
 * Example 1:
 *
 * Input: s = "I"
 * Output: [1,2]
 * Explanation: [1,2] is the only legal permutation that can represented by s,
 * where the number 1 and 2 construct an increasing relationship.
 *
 * Example 2:
 *
 * Input: s = "DI"
 * Output: [2,1,3]
 * Explanation: Both [2,1,3] and [3,1,2] can be represented as "DI", but since we
 * want to find the smallest lexicographical permutation, you should return [2,1,3]
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is either 'I' or 'D'.
 *
 */
public class FindPermutation {

    // V0
    // IDEA: GREEDY - START FROM [1..n] AND REVERSE EVERY 'D' RUN
    /**
     *  [1, 2, ..., n] is the lexicographically SMALLEST sequence overall and it
     *  already satisfies every 'I'. The only thing we must fix are the 'D' stretches.
     *
     *  A MAXIMAL run s[i..j-1] == "DD...D" forces perm[i] > perm[i+1] > ... > perm[j],
     *  and the smallest way to make a strictly decreasing block out of the numbers
     *  sitting there is to simply REVERSE that slice - it keeps the SAME value set
     *  (so the surrounding 'I' relations still hold) and puts the smallest possible
     *  number at the earliest position that allows the descent.
     *
     *      s   = D  D  I  D
     *      init= 1  2  3  4  5
     *      run s[0..1] -> reverse perm[0..2] -> 3 2 1 4 5
     *      run s[3]    -> reverse perm[3..4] -> 3 2 1 5 4
     *
     *  time  = O(n)
     *  space = O(n)   // the output
     */
    public int[] findPermutation(String s) {
        int n = s.length();

        int[] perm = new int[n + 1]; // [1, 2, ..., n+1]
        for (int i = 0; i <= n; i++) {
            perm[i] = i + 1;
        }

        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && s.charAt(j) == 'D') {
                j += 1;
            }
            if (j > i) {
                /** NOTE !!!
                 *
                 *  s[i..j-1] are all 'D' -> reverse perm[i..j] (INCLUSIVE on both ends,
                 *  a run of k 'D's touches k+1 permutation slots)
                 */
                reverse(perm, i, j);
            }
            i = Math.max(i + 1, j);
        }

        return perm;
    }

    private void reverse(int[] arr, int lo, int hi) {
        while (lo < hi) {
            int tmp = arr[lo];
            arr[lo] = arr[hi];
            arr[hi] = tmp;
            lo += 1;
            hi -= 1;
        }
    }

}
