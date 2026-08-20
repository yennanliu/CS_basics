package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-paths-that-can-form-a-palindrome-in-a-tree/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  2791. Count Paths That Can Form a Palindrome in a Tree
 *  Hard
 *
 *  You are given a tree (i.e. a connected, undirected graph that has no cycles) rooted
 *  at node 0 consisting of n nodes numbered from 0 to n - 1. The tree is represented by
 *  a 0-indexed array parent of size n, where parent[i] is the parent of node i. Since
 *  node 0 is the root, parent[0] == -1.
 *
 *  You are also given a string s of length n, where s[i] is the character assigned to
 *  the edge between i and parent[i]. s[0] can be ignored.
 *
 *  Return the number of pairs of nodes (u, v) such that u < v and the characters
 *  assigned to edges on the path from u to v can be rearranged to form a palindrome.
 *
 *  Example 1:
 *    Input: parent = [-1,0,0,1,1,2], s = "acaabc"
 *    Output: 8
 *    Explanation: the single-character paths (0,1),(0,2),(1,3),(1,4),(2,5) plus
 *                 (2,3) -> "aca", (1,5) -> "cac", (3,5) -> "acac" -> "acca".
 *
 *  Example 2:
 *    Input: parent = [-1,0,0,0,0], s = "aaaaa"
 *    Output: 10
 *
 *  Constraints:
 *    n == parent.length == s.length
 *    1 <= n <= 10^5
 *    0 <= parent[i] <= n - 1 for all i >= 1
 *    parent[0] == -1
 *    parent represents a valid tree.
 *    s consists of only lowercase English letters.
 */
public class CountPathsThatCanFormAPalindromeInATree {

    // V0
    // IDEA: ROOT-TO-NODE XOR BITMASK + PAIR COUNTING
    //       a multiset of letters can be rearranged into a palindrome iff AT MOST ONE
    //       letter has an odd count. Encode "parity of each of the 26 letters" as a
    //       26-bit mask -> the condition becomes popcount(mask) <= 1.
    //       let mask[v] = xor of the edge letters on the path root -> v. For a pair
    //       (u, v) the shared prefix down to their LCA cancels out, so
    //           mask(path u..v) = mask[u] ^ mask[v]
    //       and the LCA never has to be found.
    //       so: count pairs with popcount(mask[u] ^ mask[v]) <= 1, i.e. mask[v] equals
    //       mask[u] or mask[u] ^ (1 << k) for one of the 26 letters.
    //       NOTE: a running counter avoids double counting - for each node we look up
    //             the 27 candidate masks among the nodes ALREADY inserted, then insert.
    //       NOTE: parent[i] is NOT guaranteed to be < i, so masks come from an
    //             explicit top-down ITERATIVE traversal (n reaches 10^5, may be a path).
    /**
     * time = O(26 * n)
     * space = O(n)
     */
    public long countPalindromePaths(List<Integer> parent, String s) {

        int n = parent.size();

        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < n; i++) {
            children.get(parent.get(i)).add(i);
        }

        // top-down sweep : mask[child] = mask[parent] ^ (1 << letter of the edge)
        int[] mask = new int[n];
        int[] stack = new int[n];
        int top = 0;
        stack[top++] = 0;
        mask[0] = 0;

        Map<Integer, Integer> cnt = new HashMap<>();
        long res = 0;

        while (top > 0) {
            int cur = stack[--top];

            // count pairs against everything inserted so far
            Integer same = cnt.get(mask[cur]);
            if (same != null) {
                res += same;
            }
            for (int k = 0; k < 26; k++) {
                Integer c = cnt.get(mask[cur] ^ (1 << k));
                if (c != null) {
                    res += c;
                }
            }
            Integer old = cnt.get(mask[cur]);
            cnt.put(mask[cur], old == null ? 1 : old + 1);

            for (Integer child : children.get(cur)) {
                mask[child] = mask[cur] ^ (1 << (s.charAt(child) - 'a'));
                stack[top++] = child;
            }
        }

        return res;
    }
}
