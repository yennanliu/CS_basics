package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-permutation/description/

import java.util.ArrayDeque;
import java.util.Deque;
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


    // V1
    // IDEA: STACK -- push the counter, flush on every 'I'
    /**
     *  Push 1, 2, 3, ... onto a stack and pop the whole stack whenever the pattern
     *  says 'I' (and at the very end). Popping reverses, which produces exactly the
     *  descending run a 'D' stretch needs.
     *
     *  The canonical `pattern to permutation` idiom -- it also solves LC 484's
     *  sibling problems (LC 942, LC 2375) unchanged.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int[] findPermutation_1(String s) {
        int n = s.length();
        int[] res = new int[n + 1];
        Deque<Integer> stack = new ArrayDeque<>();
        int idx = 0;

        for (int i = 0; i <= n; i++) {
            stack.push(i + 1);
            // at the end, or wherever the pattern turns upward, flush
            if (i == n || s.charAt(i) == 'I') {
                while (!stack.isEmpty()) {
                    res[idx++] = stack.pop();
                }
            }
        }
        return res;
    }

    // V2
    // IDEA: FILL EACH MAXIMAL 'D' RUN DESCENDING IN ONE GO
    /**
     *  Walk the pattern, find each maximal run of 'D' of length L, and write the
     *  L + 1 values it spans in DESCENDING order directly -- no post-hoc reversal
     *  of an already-built array.
     *
     *  One pass, one write per slot.
     *
     *  time  = O(n)
     *  space = O(n) for the output
     */
    public int[] findPermutation_2(String s) {
        int n = s.length();
        int[] res = new int[n + 1];

        int i = 0;
        while (i <= n) {
            int j = i;
            while (j < n && s.charAt(j) == 'D') {
                j += 1;
            }
            // slots i..j take the values i+1..j+1, written backwards
            for (int t = i; t <= j; t++) {
                res[t] = j + 1 - (t - i);
            }
            i = j + 1;
        }
        return res;
    }

    // V3
    // IDEA: BRUTE FORCE -- next_permutation until the pattern matches
    /**
     *  Start from [1..n+1] (already the lexicographically smallest) and step
     *  through permutations in order until one matches the pattern.
     *
     *  Factorial in the worst case, so unusable beyond tiny n, but it is the
     *  DEFINITION of `lexicographically smallest permutation matching s` and thus
     *  the oracle for the three linear versions.
     *
     *  time  = O(n! * n)
     *  space = O(n)
     */
    public int[] findPermutation_3(String s) {
        int n = s.length();
        int[] perm = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            perm[i] = i + 1;
        }

        while (true) {
            if (matchesPattern(perm, s)) {
                return perm;
            }
            if (!nextPermutation(perm)) {
                return perm; // should be unreachable for a valid pattern
            }
        }
    }

    private boolean matchesPattern(int[] perm, String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'I' && perm[i] > perm[i + 1]) {
                return false;
            }
            if (s.charAt(i) == 'D' && perm[i] < perm[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private boolean nextPermutation(int[] a) {
        int i = a.length - 2;
        while (i >= 0 && a[i] >= a[i + 1]) {
            i -= 1;
        }
        if (i < 0) {
            return false;
        }
        int j = a.length - 1;
        while (a[j] <= a[i]) {
            j -= 1;
        }
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        for (int lo = i + 1, hi = a.length - 1; lo < hi; lo++, hi--) {
            t = a[lo];
            a[lo] = a[hi];
            a[hi] = t;
        }
        return true;
    }

}
