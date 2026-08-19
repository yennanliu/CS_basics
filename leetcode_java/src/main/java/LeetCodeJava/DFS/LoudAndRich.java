package LeetCodeJava.DFS;

// https://leetcode.com/problems/loud-and-rich/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  851. Loud and Rich
 *  Medium
 *
 *  There is a group of n people labeled from 0 to n - 1 where each person has a different amount
 *  of money and a different level of quietness.
 *
 *  You are given an array richer where richer[i] = [ai, bi] indicates that ai has more money than
 *  bi and an integer array quiet where quiet[i] is the quietness of the ith person. All the given
 *  data in richer are logically correct.
 *
 *  Return an integer array answer where answer[x] = y if y is the least quiet person (that is,
 *  the person y with the smallest value of quiet[y]) among all people who definitely have equal
 *  to or more money than the person x.
 *
 *  Example 1:
 *  Input: richer = [[1,0],[2,1],[3,1],[3,7],[4,3],[5,3],[6,3]], quiet = [3,2,5,4,6,1,7,0]
 *  Output: [5,5,2,5,4,5,6,7]
 *
 *  Example 2:
 *  Input: richer = [], quiet = [0]
 *  Output: [0]
 *
 *  Constraints:
 *  n == quiet.length
 *  1 <= n <= 500
 *  0 <= quiet[i] < n, all values of quiet are unique
 *  0 <= richer.length <= n * (n - 1) / 2
 *  0 <= ai, bi < n, ai != bi, all pairs of richer are unique
 */
public class LoudAndRich {

    // V0
    // IDEA: DFS + MEMO on a "who is richer than me" graph (edge b -> a when a is richer than b),
    //       answer[x] = quietest among x and every node reachable from x
    /**
     * time = O(n + e)
     * space = O(n + e)
     */
    public int[] loudAndRich(int[][] richer, int[] quiet) {
        int n = quiet.length;
        List<List<Integer>> richerThan = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            richerThan.add(new ArrayList<Integer>());
        }
        for (int[] pair : richer) {
            // pair[0] richer than pair[1] -> from pair[1] we can walk up to pair[0]
            richerThan.get(pair[1]).add(pair[0]);
        }

        int[] res = new int[n];
        Arrays.fill(res, -1);
        for (int i = 0; i < n; i++) {
            dfs(i, richerThan, quiet, res);
        }
        return res;
    }

    private int dfs(int cur, List<List<Integer>> richerThan, int[] quiet, int[] res) {
        if (res[cur] != -1) {
            return res[cur];
        }
        res[cur] = cur;
        for (Integer next : richerThan.get(cur)) {
            int cand = dfs(next, richerThan, quiet, res);
            if (quiet[cand] < quiet[res[cur]]) {
                res[cur] = cand;
            }
        }
        return res[cur];
    }
}
