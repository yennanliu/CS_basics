package LeetCodeJava.Array;

// https://leetcode.com/problems/split-array-into-fibonacci-sequence/

import java.util.ArrayList;
import java.util.List;

/**
 *  842. Split Array into Fibonacci Sequence
 *  Medium
 *
 *  You are given a string of digits num, such as "123456579". We can split it
 *  into a Fibonacci-like sequence [123, 456, 579].
 *
 *  Formally, a Fibonacci-like sequence is a list f of non-negative integers such
 *  that:
 *   - 0 <= f[i] < 2^31 (that is, each integer fits in a 32-bit signed integer),
 *   - f.length >= 3, and
 *   - f[i] + f[i + 1] == f[i + 2] for all 0 <= i < f.length - 2.
 *
 *  Note that when you split the string into pieces, each piece must not have
 *  extra leading zeros, except if the piece is the number 0 itself.
 *
 *  Return any Fibonacci-like sequence split from num, or return [] if it cannot
 *  be done.
 *
 *  Example 1:
 *  Input: num = "1101111"
 *  Output: [11,0,11,11]
 *
 *  Example 2:
 *  Input: num = "112358130"
 *  Output: []
 *
 *  Example 3:
 *  Input: num = "0123"
 *  Output: []
 *
 *  Constraints:
 *   - 1 <= num.length <= 200
 *   - num contains only digits.
 */
public class SplitArrayIntoFibonacciSequence {

    // V0
    // IDEA: backtracking - try every prefix as the next number; prune on leading
    //       zeros, int overflow, and (once we have >= 2 numbers) the required sum
    /**
     * time = O(n^3) worst case (first two picks are free, the rest is forced)
     * space = O(n)  recursion depth + output
     */
    public List<Integer> splitIntoFibonacci(String num) {
        List<Integer> res = new ArrayList<>();
        if (num == null || num.length() < 3) {
            return res;
        }
        backtrack(num, 0, res);
        return res;
    }

    private boolean backtrack(String num, int idx, List<Integer> path) {
        if (idx == num.length()) {
            return path.size() >= 3;
        }
        for (int end = idx + 1; end <= num.length(); end++) {
            // no leading zero unless the piece is exactly "0"
            if (num.charAt(idx) == '0' && end > idx + 1) {
                break;
            }
            // each piece must fit in a 32-bit signed integer
            if (end - idx > 10) {
                break;
            }
            long val = Long.parseLong(num.substring(idx, end));
            if (val > Integer.MAX_VALUE) {
                break;
            }
            int size = path.size();
            if (size >= 2) {
                long expected = (long) path.get(size - 2) + (long) path.get(size - 1);
                if (val < expected) {
                    continue; // piece too small -> extend it
                }
                if (val > expected) {
                    break;    // piece too big -> longer pieces only get bigger
                }
            }
            path.add((int) val);
            if (backtrack(num, end, path)) {
                return true;
            }
            path.remove(path.size() - 1);
        }
        return false;
    }
}
