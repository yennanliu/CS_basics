package LeetCodeJava.DFS;

// https://leetcode.com/problems/remove-methods-from-project/

import java.util.ArrayList;
import java.util.List;

/**
 *  3310. Remove Methods From Project
 *  Medium
 *
 *  You are maintaining a project that has n methods numbered from 0 to n - 1.
 *
 *  You are given two integers n and k, and a 2D integer array invocations, where
 *  invocations[i] = [a_i, b_i] indicates that method a_i invokes method b_i.
 *
 *  There is a known bug in method k. Method k, along with any method invoked by
 *  it, either directly or indirectly, are considered suspicious and we aim to
 *  remove them.
 *
 *  A group of methods can only be removed if no method outside the group invokes
 *  any methods within it.
 *
 *  Return an array containing all the remaining methods after removing all the
 *  suspicious methods. You may return the answer in any order. If it is not
 *  possible to remove all the suspicious methods, none should be removed.
 *
 *  Example 1:
 *    Input: n = 4, k = 1, invocations = [[1,2],[0,1],[3,2]]
 *    Output: [0,1,2,3]
 *    Explanation: methods 1 and 2 are suspicious but are invoked from the clean
 *                 methods 0 and 3, so nothing is removed.
 *
 *  Example 2:
 *    Input: n = 3, k = 2, invocations = [[1,2],[0,1],[2,0]]
 *    Output: []
 *    Explanation: all methods are suspicious and can be removed.
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    0 <= k <= n - 1
 *    0 <= invocations.length <= 2 * 10^5
 *    invocations[i] == [a_i, b_i]
 *    0 <= a_i, b_i <= n - 1
 *    a_i != b_i
 *    invocations[i] != invocations[j]
 */
public class RemoveMethodsFromProject {

    // V0
    // IDEA: MARK THE REACHABLE SET FROM k, THEN CHECK FOR ANY EDGE INTO IT
    //       "suspicious" is exactly the set of methods reachable from k along
    //       the invocation edges - one DFS finds them.
    //       the group may only be removed if nothing OUTSIDE it calls into it,
    //       so scan the edges once: an edge whose caller is clean and whose
    //       callee is suspicious blocks the removal, and then everything stays.
    //       the traversal is ITERATIVE (10^5 methods could form a chain).
    /**
     * time = O(N + E)
     * space = O(N + E)
     */
    public List<Integer> remainingMethods(int n, int k, int[][] invocations) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : invocations) {
            adj.get(e[0]).add(e[1]);
        }

        boolean[] suspicious = new boolean[n];
        suspicious[k] = true;
        int[] stack = new int[n];
        int sp = 0;
        stack[sp++] = k;
        while (sp > 0) {
            int u = stack[--sp];
            for (int v : adj.get(u)) {
                if (!suspicious[v]) {
                    suspicious[v] = true;
                    stack[sp++] = v;
                }
            }
        }

        // a clean caller reaching into the suspicious group blocks the removal
        boolean removable = true;
        for (int[] e : invocations) {
            if (!suspicious[e[0]] && suspicious[e[1]]) {
                removable = false;
                break;
            }
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!removable || !suspicious[i]) {
                res.add(i);
            }
        }
        return res;
    }
}
