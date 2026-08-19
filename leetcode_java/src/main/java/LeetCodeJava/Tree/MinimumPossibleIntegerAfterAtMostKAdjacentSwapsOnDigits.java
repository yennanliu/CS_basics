package LeetCodeJava.Tree;

// https://leetcode.com/problems/minimum-possible-integer-after-at-most-k-adjacent-swaps-on-digits/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1505. Minimum Possible Integer After at Most K Adjacent Swaps On Digits
 *  Hard
 *
 *  You are given a string num representing the digits of a very large integer
 *  and an integer k. You are allowed to swap any two adjacent digits of the
 *  integer at most k times.
 *
 *  Return the minimum integer you can obtain also as a string.
 *
 *  Example 1:
 *    Input: num = "4321", k = 4
 *    Output: "1342"
 *
 *  Example 2:
 *    Input: num = "100", k = 1
 *    Output: "010"
 *    Explanation: It's ok for the output to have leading zeros, but the input
 *                 is guaranteed not to have any.
 *
 *  Example 3:
 *    Input: num = "36789", k = 1000
 *    Output: "36789"
 *
 *  Constraints:
 *    1 <= num.length <= 3 * 10^4
 *    num consists of only digits and does not contain leading zeros.
 *    1 <= k <= 10^9
 */
public class MinimumPossibleIntegerAfterAtMostKAdjacentSwapsOnDigits {

    // V0
    // IDEA: GREEDY + BINARY INDEXED TREE (Fenwick)
    //       greedy: fill the output left to right; for each output slot try the
    //       digits 0..9 in order and take the SMALLEST one still affordable.
    //       the cost of pulling the digit at original index i to the front is
    //       the number of digits BEFORE i that have NOT been taken out yet:
    //           cost = (i - 1) - (# already-taken indices in [1 .. i])
    //       a BIT over the "taken" flags answers that in O(log n), and per
    //       digit value a queue of its original indices gives the earliest
    //       untaken occurrence in O(1).
    /**
     * time = O(N * 10 * log N)
     * space = O(N)
     */
    public String minInteger(String num, int k) {
        int n = num.length();

        // pos[d] = ascending queue of 1-indexed positions of digit d
        Deque<Integer>[] pos = new ArrayDeque[10];
        for (int d = 0; d < 10; d++) {
            pos[d] = new ArrayDeque<>();
        }
        for (int i = 0; i < n; i++) {
            pos[num.charAt(i) - '0'].addLast(i + 1);
        }

        int[] tree = new int[n + 1];
        StringBuilder res = new StringBuilder();

        for (int step = 0; step < n; step++) {
            for (int d = 0; d < 10; d++) {
                if (pos[d].isEmpty()) {
                    continue;
                }
                int i = pos[d].peekFirst();
                /*
                 * NOTE !!!
                 *   query(i) counts the ALREADY TAKEN indices in [1..i];
                 *   index i itself is still untaken, so this is exactly the
                 *   taken count in [1..i-1].
                 */
                int cost = (i - 1) - query(tree, i);
                if (cost <= k) {
                    k -= cost;
                    pos[d].pollFirst();
                    update(tree, n, i, 1);
                    res.append((char) ('0' + d));
                    break;
                }
            }
        }
        return res.toString();
    }

    private void update(int[] tree, int n, int i, int delta) {
        while (i <= n) {
            tree[i] += delta;
            i += i & (-i);
        }
    }

    private int query(int[] tree, int i) {
        int s = 0;
        while (i > 0) {
            s += tree[i];
            i -= i & (-i);
        }
        return s;
    }
}
