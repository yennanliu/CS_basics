package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/permutations-iii/

import java.util.ArrayList;
import java.util.List;

/**
 *  3437. Permutations III
 *  Medium
 *
 *  Given an integer n, an alternating permutation is a permutation of the first n
 *  positive integers such that no two adjacent elements are both odd or both even.
 *
 *  Return all such alternating permutations sorted in lexicographical order.
 *
 *  Example 1:
 *    Input: n = 4
 *    Output: [[1,2,3,4],[1,4,3,2],[2,1,4,3],[2,3,4,1],
 *             [3,2,1,4],[3,4,1,2],[4,1,2,3],[4,3,2,1]]
 *
 *  Example 2:
 *    Input: n = 3
 *    Output: [[1,2,3],[3,2,1]]
 *
 *  Constraints:
 *    1 <= n <= 10
 */
public class PermutationsIII {

    private int n;
    private int[] cur;
    private int size;
    private boolean[] used;
    private List<int[]> res;

    // V0
    // IDEA: BACKTRACKING THAT ONLY EVER OFFERS THE OPPOSITE PARITY
    //       "no two adjacent elements share a parity" means the parities must
    //       alternate, so once the first element is placed the parity of every later
    //       slot is fixed. the search therefore never has to test the constraint
    //       after the fact -- at each step it simply iterates over the unused numbers
    //       of the required parity.
    //
    //       trying the candidates in increasing order makes the recursion emit
    //       complete permutations in lexicographic order, so no final sort is needed.
    /**
     * time = O(n * number of alternating permutations)
     * space = O(n)   // excluding the output
     */
    public int[][] permute(int n) {
        this.n = n;
        this.cur = new int[n];
        this.size = 0;
        this.used = new boolean[n + 1];
        this.res = new ArrayList<>();
        dfs();
        int[][] out = new int[res.size()][];
        for (int i = 0; i < res.size(); i++) {
            out[i] = res.get(i);
        }
        return out;
    }

    private void dfs() {
        if (size == n) {
            int[] copy = new int[n];
            System.arraycopy(cur, 0, copy, 0, n);
            res.add(copy);
            return;
        }
        if (size > 0) {
            // the next value must have the OPPOSITE parity of the previous one
            int lo = ((cur[size - 1] & 1) == 1) ? 2 : 1;
            for (int v = lo; v <= n; v += 2) {
                if (!used[v]) {
                    used[v] = true;
                    cur[size++] = v;
                    dfs();
                    size--;
                    used[v] = false;
                }
            }
        } else {
            for (int v = 1; v <= n; v++) {
                used[v] = true;
                cur[size++] = v;
                dfs();
                size--;
                used[v] = false;
            }
        }
    }
}
