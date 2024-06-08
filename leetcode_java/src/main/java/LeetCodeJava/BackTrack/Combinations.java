package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combinations/description/

import java.util.ArrayList;
import java.util.List;

public class Combinations {

    // V0
    // IDEA : BACKTRACK (modified by gpt)
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
            cur.add(i);
            getCombine(n, k, i + 1, cur, res);
            cur.remove(cur.size() - 1); // undo the move
        }
    }

    // V0'
    // IDEA : BACKTRACK
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Backtracking/combinations.py

    // V1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/combinations/submissions/1281420720/
    public List<List<Integer>> combine_1(int n, int k) {
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

    // V2
    // IDEA : BACKTRACK (interactive)
    // https://leetcode.com/problems/combinations/solutions/3845249/iterative-backtracking-video-100-efficient-combinatorial-generation/
    public List<List<Integer>> combine_2_1(int n, int k) {
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

    // V2
    // IDEA : BACKTRACK (recursive)
    // https://leetcode.com/problems/combinations/solutions/3845249/iterative-backtracking-video-100-efficient-combinatorial-generation/
    public List<List<Integer>> combine_2_2(int n, int k) {
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
