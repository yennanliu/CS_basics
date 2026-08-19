package LeetCodeJava.Math;

// https://leetcode.com/problems/k-th-symbol-in-grammar/

/**
 *  779. K-th Symbol in Grammar
 *  Medium
 *
 *  We build a table of n rows (1-indexed). We start by writing 0 in the 1st
 *  row. Now in every subsequent row, we look at the previous row and replace
 *  each occurrence of 0 with 01, and each occurrence of 1 with 10.
 *
 *  For example, for n = 3, the 1st row is 0, the 2nd row is 01, and the 3rd
 *  row is 0110.
 *
 *  Given two integers n and k, return the kth (1-indexed) symbol in the nth
 *  row of a table of n rows.
 *
 *  Example 1:
 *    Input: n = 1, k = 1
 *    Output: 0
 *
 *  Example 2:
 *    Input: n = 2, k = 1
 *    Output: 0
 *
 *  Example 3:
 *    Input: n = 2, k = 2
 *    Output: 1
 *
 *  Constraints:
 *   - 1 <= n <= 30
 *   - 1 <= k <= 2^(n - 1)
 */
public class KThSymbolInGrammar {

    // V0
    // IDEA: RECURSION on the parent symbol.
    //       symbol k in row n comes from symbol (k + 1) / 2 in row n - 1:
    //         parent 0 -> "01",  parent 1 -> "10"
    //       so an odd k keeps the parent value, an even k flips it.
    /**
     * time = O(n)
     * space = O(n) (recursion stack)
     */
    public int kthGrammar(int n, int k) {

        if (n == 1) {
            return 0;
        }

        int parent = kthGrammar(n - 1, (k + 1) / 2);

        // odd k -> first char of the expansion (== parent)
        // even k -> second char (== flipped parent)
        return (k % 2 == 1) ? parent : (1 - parent);
    }

    // V1
    // IDEA: BIT TRICK -- unrolling the recursion, the answer is just the parity
    //       of the number of 1-bits in (k - 1).
    /**
     * time = O(log k)
     * space = O(1)
     */
    public int kthGrammar_1(int n, int k) {
        return Integer.bitCount(k - 1) % 2;
    }
}
