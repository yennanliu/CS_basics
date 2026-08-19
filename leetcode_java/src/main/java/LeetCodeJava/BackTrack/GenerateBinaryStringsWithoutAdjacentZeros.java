package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/generate-binary-strings-without-adjacent-zeros/

import java.util.ArrayList;
import java.util.List;

/**
 *  3211. Generate Binary Strings Without Adjacent Zeros
 *  Medium
 *
 *  You are given a positive integer n.
 *
 *  A binary string x is valid if all substrings of x of length 2 contain at
 *  least one "1".
 *
 *  Return all valid strings with length n, in any order.
 *
 *  Example 1:
 *    Input: n = 3
 *    Output: ["010","011","101","110","111"]
 *
 *  Example 2:
 *    Input: n = 1
 *    Output: ["0","1"]
 *
 *  Constraints:
 *    1 <= n <= 18
 */
public class GenerateBinaryStringsWithoutAdjacentZeros {

    // V0
    // IDEA: BACKTRACK, REFUSING TO PLACE A '0' RIGHT AFTER A '0'
    //       "every length-2 substring holds a 1" == "no two adjacent zeros",
    //       so building left to right the only rule is: a '0' may follow
    //       anything except another '0'.
    //       the count of such strings is a Fibonacci number, so n = 18 yields
    //       only a few thousand results.
    /**
     * time = O(n * F(n)), F(n) = number of valid strings (Fibonacci)
     * space = O(n) recursion (excluding the output)
     */
    public List<String> validStrings(int n) {
        List<String> res = new ArrayList<>();
        build(0, n, new StringBuilder(), res);
        return res;
    }

    private void build(int i, int n, StringBuilder cur, List<String> res) {
        if (i == n) {
            res.add(cur.toString());
            return;
        }
        // '0' only when the previous char is not '0'
        if (cur.length() == 0 || cur.charAt(cur.length() - 1) == '1') {
            cur.append('0');
            build(i + 1, n, cur, res);
            cur.deleteCharAt(cur.length() - 1);
        }
        cur.append('1');
        build(i + 1, n, cur, res);
        cur.deleteCharAt(cur.length() - 1);
    }
}
