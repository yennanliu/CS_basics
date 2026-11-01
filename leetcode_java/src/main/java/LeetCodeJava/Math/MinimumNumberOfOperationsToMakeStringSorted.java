package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-number-of-operations-to-make-string-sorted/description/

/**
 * 1830. Minimum Number of Operations to Make String Sorted
 * Hard
 *
 * You are given a string s (0-indexed). You are asked to perform the following operation on s until you get a sorted string:
 *
 * Find the largest index i such that 1 <= i < s.length and s[i] < s[i - 1].
 * Find the largest index j such that i <= j < s.length and s[k] < s[i - 1] for all the possible values of k in the range [i, j] inclusive.
 * Swap the two characters at indices i - 1 and j.
 * Reverse the suffix starting at index i.
 *
 * Return the number of operations needed to make the string sorted. Since the answer can be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "cba"
 * Output: 5
 * Explanation: The simulation goes as follows:
 * Operation 1: i=2, j=2. Swap s[1] and s[2] to get s="cab", then reverse the suffix starting at 2. Now, s="cab".
 * Operation 2: i=1, j=2. Swap s[0] and s[2] to get s="bac", then reverse the suffix starting at 1. Now, s="bca".
 * Operation 3: i=2, j=2. Swap s[1] and s[2] to get s="bac", then reverse the suffix starting at 2. Now, s="bac".
 * Operation 4: i=1, j=1. Swap s[0] and s[1] to get s="abc", then reverse the suffix starting at 1. Now, s="acb".
 * Operation 5: i=2, j=2. Swap s[1] and s[2] to get s="abc", then reverse the suffix starting at 2. Now, s="abc".
 *
 * Example 2:
 *
 * Input: s = "aabaa"
 * Output: 2
 * Explanation: The simulation goes as follows:
 * Operation 1: i=3, j=4. Swap s[2] and s[4] to get s="aaaab", then reverse the substring starting at 3. Now, s="aaaba".
 * Operation 2: i=4, j=4. Swap s[3] and s[4] to get s="aaaab", then reverse the substring starting at 4. Now, s="aaaab".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 3000
 * s consists only of lowercase English letters.
 *
 */
public class MinimumNumberOfOperationsToMakeStringSorted {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: THE OPERATION IS "PREVIOUS PERMUTATION" -> ANSWER = LEXICOGRAPHIC RANK OF s
    //  (count permutations smaller than s position by position, with modular inverse factorials)
    /**
     * time = O(N * 26)
     * space = O(N)
     */
    public int makeStringSorted(String s) {
        int n = s.length();

        // fact[i] = i! mod MOD, invFact[i] = (i!)^-1 mod MOD
        long[] fact = new long[n + 1];
        long[] invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        // Fermat: x^(MOD-2) is the inverse of x, since MOD is prime
        invFact[n] = modPow(fact[n], MOD - 2);
        for (int i = n; i > 0; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }

        // counts of the characters NOT yet fixed (the suffix s[i..])
        int[] count = new int[26];
        for (int i = 0; i < n; i++) {
            count[s.charAt(i) - 'a']++;
        }

        long rank = 0;
        for (int i = 0; i < n; i++) {
            int current = s.charAt(i) - 'a';

            // how many remaining chars are smaller than s[i] -> each can sit at position i
            int smaller = 0;
            for (int c = 0; c < current; c++) {
                smaller += count[c];
            }

            if (smaller > 0) {
                // permutations of the tail with a smaller char at i:
                //   smaller * (n-i-1)! / prod(count[x]!)
                long ways = fact[n - i - 1] * smaller % MOD;
                for (int c = 0; c < 26; c++) {
                    ways = ways * invFact[count[c]] % MOD;
                }
                rank = (rank + ways) % MOD;
            }

            // s[i] is now fixed, remove it from the pool
            count[current]--;
        }
        return (int) rank;
    }

    /** Computes base^exp mod MOD by binary exponentiation. */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            // lowest bit of exp set -> this power of base is part of the product
            if (exp % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp /= 2;
        }
        return result;
    }
}
