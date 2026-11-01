package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-vowels-permutation/description/

/**
 * 1220. Count Vowels Permutation
 * Hard
 *
 * Given an integer n, your task is to count how many strings of length n can be formed
 * under the following rules:
 *
 * Each character is a lower case vowel ('a', 'e', 'i', 'o', 'u')
 * Each vowel 'a' may only be followed by an 'e'.
 * Each vowel 'e' may only be followed by an 'a' or an 'i'.
 * Each vowel 'i' may not be followed by another 'i'.
 * Each vowel 'o' may only be followed by an 'i' or a 'u'.
 * Each vowel 'u' may only be followed by an 'a'.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 5
 * Explanation: All possible strings are: "a", "e", "i" , "o" and "u".
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 10
 * Explanation: All possible strings are: "ae", "ea", "ei", "ia", "ie", "io", "iu", "oi",
 * "ou" and "ua".
 *
 * Example 3:
 *
 * Input: n = 5
 * Output: 68
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 10^4
 *
 */
public class CountVowelsPermutation {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: 1D DP (COUNT STRINGS BY THEIR LAST VOWEL)
    /**
     * successors (from the statement):     predecessors (inverted):
     *   a -> e                               a <- e, i, u
     *   e -> a, i                            e <- a, i
     *   i -> a, e, o, u                      i <- e, o
     *   o -> i, u                            o <- i
     *   u -> a                               u <- i, o
     *
     * endA .. endU = number of valid strings of the current length ending with that vowel
     *
     * one more character:
     *   endA' = endE + endI + endU
     *   endE' = endA + endI
     *   endI' = endE + endO
     *   endO' = endI
     *   endU' = endI + endO
     *
     * base: length 1 -> one string per vowel
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countVowelPermutation(int n) {
        long endA = 1;
        long endE = 1;
        long endI = 1;
        long endO = 1;
        long endU = 1;

        for (int length = 2; length <= n; length++) {
            // compute all five from the previous length before overwriting any
            long nextA = (endE + endI + endU) % MOD;
            long nextE = (endA + endI) % MOD;
            long nextI = (endE + endO) % MOD;
            long nextO = endI % MOD;
            long nextU = (endI + endO) % MOD;

            endA = nextA;
            endE = nextE;
            endI = nextI;
            endO = nextO;
            endU = nextU;
        }
        return (int) ((endA + endE + endI + endO + endU) % MOD);
    }
}
