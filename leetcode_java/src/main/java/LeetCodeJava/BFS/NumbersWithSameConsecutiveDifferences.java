package LeetCodeJava.BFS;

// https://leetcode.com/problems/numbers-with-same-consecutive-differences/

import java.util.*;

/**
 *  967. Numbers With Same Consecutive Differences
 *  Medium
 *
 *  Given two integers n and k, return an array of all the integers of length n
 *  where the difference between every two consecutive digits is k. You may
 *  return the answer in any order.
 *
 *  Note that the integers should not have leading zeros. Integers as 02 and 043
 *  are not allowed.
 *
 *  Example 1:
 *   Input: n = 3, k = 7
 *   Output: [181,292,707,818,929]
 *   Explanation: Note that 070 is not a valid number, because of the leading zero.
 *
 *  Example 2:
 *   Input: n = 2, k = 1
 *   Output: [10,12,21,23,32,34,43,45,54,56,65,67,76,78,87,89,98]
 *
 *  Constraints:
 *   2 <= n <= 9
 *   0 <= k <= 9
 */
public class NumbersWithSameConsecutiveDifferences {

    // V0
    // IDEA: BFS level by level, each level appends one more digit
    /**
     * time = O(2^n)
     * space = O(2^n)
     */
    public int[] numsSameConsecDiff(int n, int k) {
        // start from 1..9 (no leading zero)
        List<Integer> cur = new ArrayList<>();
        for (int d = 1; d <= 9; d++) {
            cur.add(d);
        }

        for (int step = 1; step < n; step++) {
            List<Integer> next = new ArrayList<>();
            for (Integer num : cur) {
                int last = num % 10;
                if (last + k <= 9) {
                    next.add(num * 10 + last + k);
                }
                // when k == 0 both branches are identical, so only take one
                if (k != 0 && last - k >= 0) {
                    next.add(num * 10 + last - k);
                }
            }
            cur = next;
        }

        int[] res = new int[cur.size()];
        for (int i = 0; i < cur.size(); i++) {
            res[i] = cur.get(i);
        }
        return res;
    }

    // V1
    // IDEA: DFS / backtracking building the number digit by digit
    /**
     * time = O(2^n)
     * space = O(n) (excluding output)
     */
    public int[] numsSameConsecDiff_1(int n, int k) {
        List<Integer> res = new ArrayList<>();
        for (int d = 1; d <= 9; d++) {
            dfs(d, 1, n, k, res);
        }
        int[] out = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            out[i] = res.get(i);
        }
        return out;
    }

    private void dfs(int cur, int len, int n, int k, List<Integer> res) {
        if (len == n) {
            res.add(cur);
            return;
        }
        int last = cur % 10;
        if (last + k <= 9) {
            dfs(cur * 10 + last + k, len + 1, n, k, res);
        }
        if (k != 0 && last - k >= 0) {
            dfs(cur * 10 + last - k, len + 1, n, k, res);
        }
    }
}
