package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/find-the-punishment-number-of-an-integer/

/**
 *  2698. Find the Punishment Number of an Integer
 *  Medium
 *
 *  Given a positive integer n, return the punishment number of n.
 *
 *  The punishment number of n is defined as the sum of the squares of all
 *  integers i such that:
 *    - 1 <= i <= n
 *    - The decimal representation of i * i can be partitioned into contiguous
 *      substrings such that the sum of the integer values of these substrings
 *      equals i.
 *
 *  Example 1:
 *    Input: n = 10
 *    Output: 182
 *    Explanation: 1 (1*1=1), 9 (81 -> 8+1), 10 (100 -> 10+0)
 *                 -> 1 + 81 + 100 = 182
 *
 *  Example 2:
 *    Input: n = 37
 *    Output: 1478
 *    Explanation: 1, 9, 10 and 36 (1296 -> 1+29+6)
 *                 -> 1 + 81 + 100 + 1296 = 1478
 *
 *  Constraints:
 *    1 <= n <= 1000
 */
public class FindThePunishmentNumberOfAnInteger {

    // V0
    // IDEA: BACKTRACKING OVER THE SPLIT POINTS OF str(i * i)
    //       for each i walk the digits of i*i and try every possible length for
    //       the next chunk, carrying `rest` = how much of i is still unmatched.
    //         - consumed the whole string -> success iff rest == 0
    //         - otherwise grow the chunk digit by digit (y = y*10 + d) and
    //           recurse with rest - y
    //       NOTE: PRUNING - once y > rest, that chunk and every longer one
    //             (appending digits only grows y) can never fit -> break.
    //       NOTE: chunks may be "0" / carry leading zeros ("100" -> 10 + 0),
    //             which the running y = y*10 + d construction handles naturally.
    /**
     * time = O(n * 2^d), d = digits of n^2 (<= 7 for n <= 1000)
     * space = O(d)
     */
    public int punishmentNumber(int n) {
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            int sq = i * i;
            if (canSplit(String.valueOf(sq), 0, i)) {
                ans += sq;
            }
        }
        return ans;
    }

    private boolean canSplit(String s, int start, int rest) {
        if (start == s.length()) {
            return rest == 0;
        }
        int y = 0;
        for (int j = start; j < s.length(); j++) {
            y = y * 10 + (s.charAt(j) - '0');
            if (y > rest) {
                break; // pruning: a longer chunk is only bigger
            }
            if (canSplit(s, j + 1, rest - y)) {
                return true;
            }
        }
        return false;
    }
}
