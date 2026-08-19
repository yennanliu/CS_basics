package LeetCodeJava.Math;

// https://leetcode.com/problems/lexicographical-numbers/

import java.util.ArrayList;
import java.util.List;

/**
 *  386. Lexicographical Numbers
 *  Medium
 *
 *  Given an integer n, return all the numbers in the range [1, n] sorted in
 *  lexicographical order.
 *
 *  You must write an algorithm that runs in O(n) time and uses O(1) extra space.
 *
 *  Example 1:
 *
 *  Input: n = 13
 *  Output: [1,10,11,12,13,2,3,4,5,6,7,8,9]
 *
 *  Example 2:
 *
 *  Input: n = 2
 *  Output: [1,2]
 *
 *  Constraints:
 *
 *  1 <= n <= 5 * 10^4
 */
public class LexicographicalNumbers {

    // V0
    // IDEA: iterative pre-order walk of the 10-ary trie of numbers:
    //       go deeper (cur * 10) if possible, else move to the next sibling (cur + 1),
    //       backing out of trailing 9s / overflow by dividing by 10
    /**
     * time = O(n)
     * space = O(1)  (excluding the output list)
     */
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        int cur = 1;
        for (int i = 0; i < n; i++) {
            res.add(cur);
            if (cur * 10 <= n) {
                cur *= 10;
            } else {
                while (cur % 10 == 9 || cur + 1 > n) {
                    cur /= 10;
                }
                cur++;
            }
        }
        return res;
    }

    // V1
    // IDEA: explicit DFS from each root 1..9
    /**
     * time = O(n)
     * space = O(log n) recursion depth (excluding the output list)
     */
    public List<Integer> lexicalOrder_1(int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            dfs(i, n, res);
        }
        return res;
    }

    private void dfs(int cur, int n, List<Integer> res) {
        if (cur > n) {
            return;
        }
        res.add(cur);
        for (int d = 0; d <= 9; d++) {
            int next = cur * 10 + d;
            if (next > n) {
                return;
            }
            dfs(next, n, res);
        }
    }
}
