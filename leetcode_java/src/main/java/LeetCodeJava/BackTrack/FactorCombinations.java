package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/factor-combinations/

import java.util.*;

/**
 *  254. Factor Combinations
 *  Medium
 *
 *  Numbers can be regarded as the product of their factors.
 *   - For example, 8 = 2 x 2 x 2 = 2 x 4.
 *
 *  Given an integer n, return all possible combinations of its factors. You may
 *  return the answer in any order.
 *
 *  Note that the factors should be in the range [2, n - 1].
 *
 *  Example 1:
 *   Input: n = 1
 *   Output: []
 *
 *  Example 2:
 *   Input: n = 12
 *   Output: [[2,6],[2,2,3],[3,4]]
 *
 *  Example 3:
 *   Input: n = 37
 *   Output: []
 *
 *  Constraints:
 *   1 <= n <= 10^7
 */
public class FactorCombinations {

    // V0
    // IDEA: backtracking, factors kept non-decreasing (start from the last used factor)
    //       so each multiset is produced exactly once; the trivial [n] is excluded
    /**
     * time = O(n * log n)
     * space = O(log n) recursion depth (excluding output)
     */
    public List<List<Integer>> getFactors(int n) {
        List<List<Integer>> res = new ArrayList<>();
        if (n <= 3) {
            return res;
        }
        dfs(n, 2, new ArrayList<Integer>(), res);
        return res;
    }

    private void dfs(int n, int start, List<Integer> cur, List<List<Integer>> res) {
        for (int i = start; i * i <= n; i++) {
            if (n % i != 0) {
                continue;
            }
            // [... , i, n/i] is one complete combination
            cur.add(i);
            cur.add(n / i);
            res.add(new ArrayList<>(cur));
            cur.remove(cur.size() - 1);

            // keep factoring n/i further
            dfs(n / i, i, cur, res);
            cur.remove(cur.size() - 1);
        }
    }
}
