package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combination-sum-iii/

import java.util.*;

/**
 *  216. Combination Sum III
 *  Medium
 *
 *  Find all valid combinations of k numbers that sum up to n such that:
 *   - Only numbers 1 through 9 are used.
 *   - Each number is used at most once.
 *
 *  Return a list of all possible valid combinations. The list must not contain
 *  the same combination twice, and the combinations may be returned in any order.
 *
 *  Example 1:
 *   Input: k = 3, n = 7
 *   Output: [[1,2,4]]
 *
 *  Example 2:
 *   Input: k = 3, n = 9
 *   Output: [[1,2,6],[1,3,5],[2,3,4]]
 *
 *  Example 3:
 *   Input: k = 4, n = 1
 *   Output: []
 *
 *  Constraints:
 *   2 <= k <= 9
 *   1 <= n <= 60
 */
public class CombinationSumIII {

    // V0
    // IDEA: backtracking over 1..9, keep candidates strictly increasing so no dup
    /**
     * time = O(k * C(9, k))
     * space = O(k)
     */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        backtrack(1, k, n, new ArrayList<Integer>(), res);
        return res;
    }

    private void backtrack(int start, int k, int remain,
                           List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == k) {
            if (remain == 0) {
                res.add(new ArrayList<>(cur));
            }
            return;
        }
        // prune: remaining sum can never be reached / already exceeded
        if (remain <= 0) {
            return;
        }
        for (int i = start; i <= 9; i++) {
            if (i > remain) {
                break;
            }
            cur.add(i);
            backtrack(i + 1, k, remain - i, cur, res);
            cur.remove(cur.size() - 1);
        }
    }
}
