package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combinations/description/
/**
 * 77. Combinations
 * Solved
 * Medium
 * Topics
 * Companies
 * Given two integers n and k, return all possible combinations of k numbers chosen from the range [1, n].
 *
 * You may return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 4, k = 2
 * Output: [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 * Explanation: There are 4 choose 2 = 6 total combinations.
 * Note that combinations are unordered, i.e., [1,2] and [2,1] are considered to be the same combination.
 * Example 2:
 *
 * Input: n = 1, k = 1
 * Output: [[1]]
 * Explanation: There is 1 choose 1 = 1 total combination.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 20
 * 1 <= k <= n
 *
 */
import java.util.ArrayList;
import java.util.List;

public class Combinations {

    // V0
    // IDEA: BACKTRACK + START_IDX (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *   -> for combine, we need "start_idx"
     *
     */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();

        if (n == 1 && k == 1) {
            List<Integer> cur = new ArrayList<>();
            cur.add(1);
            res.add(cur);
            return res;
        }

        // backtrack
        List<Integer> cur = new ArrayList<>();
        /** NOTE !!! start_idx start from 1 */
        getCombine(n, k, 1, cur, res);
        return res;
    }

    private void getCombine(int n, int k, int start_idx, List<Integer> cur, List<List<Integer>> res) {

        if (cur.size() == k) {
            res.add(new ArrayList<>(cur));
            return;
        }

        /** NOTE !!! i = start, and i <= n, since idx starts from 1 */
        for (int i = start_idx; i <= n; i++) {
            /** NOTE !!!
             *
             *  NO NEED to maintain a `candidate` array,
             *  since the `to add val` is simply a value between [1,n].
             *  So, we can just loop over start_idx - n, then use idx as val
             */
            cur.add(i);
            getCombine(n, k, i + 1, cur, res);
            cur.remove(cur.size() - 1); // undo the move
        }
    }

    // V0-1
    // IDEA: BACKTRACK + START_IDX (fixed by gpt)
    List<List<Integer>> combineRes = new ArrayList<>();

    public List<List<Integer>> combine_0_1(int n, int k) {
        combineRes.clear(); // Clear previous results
        if (n == 0 || k == 0 || k > n)
            return new ArrayList<>();
        combineHelper(n, k, 1, new ArrayList<>());
        return combineRes;
    }

    private void combineHelper(int n, int k, int start, List<Integer> cur) {
        if (cur.size() == k) {
            combineRes.add(new ArrayList<>(cur));
            return;
        }

        // Optimization: loop only until n - (k - cur.size()) + 1
        for (int i = start; i <= n - (k - cur.size()) + 1; i++) {
            /** NOTE !!!
             *
             *  NO NEED to maintain a `candidate` array,
             *  since the `to add val` is simply a value between [1,n].
             *  So, we can just loop over start_idx - n, then use idx as val
             */
            cur.add(i);
            combineHelper(n, k, i + 1, cur);
            cur.remove(cur.size() - 1); // backtrack
        }
    }

    // V0-2
    // IDEA : BACKTRACK
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Backtracking/combinations.py

    // V1
    // https://www.youtube.com/watch?v=q0s6m7AiM7o
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0077-combinations.java
    List<List<Integer>> res_1;
    public List<List<Integer>> combine_1(int n, int k) {
        res_1 = new ArrayList<>();
        backtrack(1, new ArrayList<Integer>(), n, k);
        return res_1;
    }

    private void backtrack(int start, ArrayList<Integer> comb, int n, int k) {
        if (comb.size() == k){
            res_1.add(new ArrayList<>(comb));
            return;
        }

        for (int i = start; i <= n; i++) {
            comb.add(i);
            backtrack(i+1, comb, n, k);
            comb.remove((Integer) i);
        }
    }

    // V2
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/combinations/submissions/1281420720/
    public List<List<Integer>> combine_2(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> comb = new ArrayList<>();

        backtrack(1, comb, res, n, k);
        return res;
    }

    private void backtrack(int start, List<Integer> comb, List<List<Integer>> res, int n, int k) {
        if (comb.size() == k) {
            res.add(new ArrayList<>(comb));
            return;
        }

        for (int num = start; num <= n; num++) {
            comb.add(num);
            backtrack(num + 1, comb, res, n, k);
            comb.remove(comb.size() - 1);
        }
    }

    // V3
    // IDEA : BACKTRACK (interactive)
    // https://leetcode.com/problems/combinations/solutions/3845249/iterative-backtracking-video-100-efficient-combinatorial-generation/
    public List<List<Integer>> combine_3(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        generateCombinations(1, n, k, new ArrayList<Integer>(), result);
        return result;
    }

    private void generateCombinations(int start, int n, int k, List<Integer> combination, List<List<Integer>> result) {
        if (k == 0) {
            result.add(new ArrayList<>(combination));
            return;
        }
        for (int i = start; i <= n - k + 1; i++) {
            combination.add(i);
            generateCombinations(i + 1, n, k - 1, combination, result);
            combination.remove(combination.size() - 1);
        }
    }

    // V4
    // IDEA : BACKTRACK (recursive)
    // https://leetcode.com/problems/combinations/solutions/3845249/iterative-backtracking-video-100-efficient-combinatorial-generation/
    public List<List<Integer>> combine_4(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack_2(n, k, 1, new ArrayList<>(), result);
        return result;
    }

    private void backtrack_2(int n, int k, int start, List<Integer> combination, List<List<Integer>> result) {
        if (combination.size() == k) {
            result.add(new ArrayList<>(combination));
            return;
        }
        for (int i = start; i <= n; i++) {
            combination.add(i);
            backtrack_2(n, k, i + 1, combination, result);
            combination.remove(combination.size() - 1);
        }
    }

}
