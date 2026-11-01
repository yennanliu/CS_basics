package LeetCodeJava.String;

// https://leetcode.com/problems/check-if-dfs-strings-are-palindromes/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 3327. Check if DFS Strings Are Palindromes
 * Hard
 *
 * You are given a tree rooted at node 0, consisting of n nodes numbered from 0 to n - 1. The tree is represented by an array parent, where parent[i] is the parent of node i. Since node 0 is the root, parent[0] == -1.
 *
 * You are also given a string s of length n, where s[i] is the character assigned to node i.
 *
 * Consider an empty string dfsStr, and define a recursive function dfs(int x) that takes a node x as a parameter and performs the following steps in order:
 *
 * Iterate over each child y of x in increasing order of their numbers, and call dfs(y).
 * Add the character s[x] to the end of the string dfsStr.
 *
 * Note that dfsStr is shared across all recursive calls of dfs.
 *
 * You need to find a boolean array answer of size n, where for every index i from 0 to n - 1, you do the following:
 *
 * Empty the string dfsStr and call dfs(i).
 * If the resulting string dfsStr is a palindrome, then set answer[i] to true. Otherwise, set answer[i] to false.
 *
 * Return the array answer.
 *
 *
 * Example 1:
 *
 * Input: parent = [-1,0,0,1,1,2], s = "aababa"
 * Output: [true,true,false,true,true,true]
 * Explanation:
 * Calling dfs(0) results in the string dfsStr = "abaaba", which is a palindrome.
 * Calling dfs(1) results in the string dfsStr = "aba", which is a palindrome.
 * Calling dfs(2) results in the string dfsStr = "ab", which is not a palindrome.
 * Calling dfs(3) results in the string dfsStr = "a", which is a palindrome.
 * Calling dfs(4) results in the string dfsStr = "b", which is a palindrome.
 * Calling dfs(5) results in the string dfsStr = "a", which is a palindrome.
 *
 * Example 2:
 *
 * Input: parent = [-1,0,0,0,0], s = "aabcb"
 * Output: [true,true,true,true,true]
 * Explanation:
 * Every call on dfs(x) results in a palindrome string.
 *
 *
 * Constraints:
 *
 * n == parent.length == s.length
 * 1 <= n <= 10^5
 * 0 <= parent[i] <= n - 1 for all i >= 1.
 * parent[0] == -1
 * parent represents a valid tree.
 * s consists only of lowercase English letters.
 *
 */
public class CheckIfDfsStringsArePalindromes {

    private static final long MOD_1 = 1_000_000_007L;
    private static final long MOD_2 = 998_244_353L;
    private static final long BASE = 131L;

    // V0
    // IDEA: ONE GLOBAL POST-ORDER DFS STRING (EVERY SUBTREE IS A CONTIGUOUS SLICE) + ROLLING HASH
    //   dfs appends a node's char after all its children (visited in index order), so running it
    //   once from the root makes each node's own dfs string the slice [start[x], end[x]].
    //   Each slice is a palindrome iff its forward hash equals its backward hash (double mod
    //   to make collisions negligible). Iterative dfs: a 10^5-node path would overflow the stack.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean[] findAnswer(int[] parent, String s) {
        int n = parent.length;
        int[] start = new int[n];
        int[] end = new int[n];
        String text = buildDfsString(parent, s, start, end);
        int m = text.length();

        // power[i] = BASE^i, forward[i] = hash of text[0, i), backward[i] = hash of text[i, m) reversed
        long[] power1 = new long[m + 1];
        long[] power2 = new long[m + 1];
        long[] forward1 = new long[m + 1];
        long[] forward2 = new long[m + 1];
        long[] backward1 = new long[m + 1];
        long[] backward2 = new long[m + 1];
        power1[0] = 1;
        power2[0] = 1;
        for (int i = 0; i < m; i++) {
            power1[i + 1] = power1[i] * BASE % MOD_1;
            power2[i + 1] = power2[i] * BASE % MOD_2;
            forward1[i + 1] = (forward1[i] * BASE + text.charAt(i)) % MOD_1;
            forward2[i + 1] = (forward2[i] * BASE + text.charAt(i)) % MOD_2;
        }
        for (int i = m - 1; i >= 0; i--) {
            backward1[i] = (backward1[i + 1] * BASE + text.charAt(i)) % MOD_1;
            backward2[i] = (backward2[i + 1] * BASE + text.charAt(i)) % MOD_2;
        }

        boolean[] answer = new boolean[n];
        for (int x = 0; x < n; x++) {
            int lo = start[x];
            int hi = end[x] + 1; // exclusive
            int length = hi - lo;
            // hash of text[lo, hi) read forwards
            long f1 = mod(forward1[hi] - forward1[lo] * power1[length] % MOD_1, MOD_1);
            long f2 = mod(forward2[hi] - forward2[lo] * power2[length] % MOD_2, MOD_2);
            // hash of text[lo, hi) read backwards
            long b1 = mod(backward1[lo] - backward1[hi] * power1[length] % MOD_1, MOD_1);
            long b2 = mod(backward2[lo] - backward2[hi] * power2[length] % MOD_2, MOD_2);
            answer[x] = (f1 == b1) && (f2 == b2);
        }
        return answer;
    }

    // V1
    // IDEA: SAME GLOBAL DFS STRING + MANACHER (DETERMINISTIC, NO HASH COLLISIONS)
    //   Manacher on "#c#c#...#" gives radius[i] = length (in the original string) of the
    //   longest palindrome centred at i. The slice [lo, hi] is centred at lo + hi + 1 in the
    //   separated string, so it is a palindrome iff radius[lo + hi + 1] >= hi - lo + 1.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean[] findAnswer_1(int[] parent, String s) {
        int n = parent.length;
        int[] start = new int[n];
        int[] end = new int[n];
        String text = buildDfsString(parent, s, start, end);

        // separated string: '#' between and around every character
        StringBuilder separated = new StringBuilder("#");
        for (int i = 0; i < text.length(); i++) {
            separated.append(text.charAt(i)).append('#');
        }
        int[] radius = manacher(separated.toString());

        boolean[] answer = new boolean[n];
        for (int x = 0; x < n; x++) {
            int lo = start[x];
            int hi = end[x];
            answer[x] = radius[lo + hi + 1] >= hi - lo + 1;
        }
        return answer;
    }

    /**
     * Iterative post-order dfs from node 0, children in increasing index order.
     * Fills start[x] / end[x] with the inclusive range of x's subtree in the returned string.
     */
    private String buildDfsString(int[] parent, String s, int[] start, int[] end) {
        int n = parent.length;
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }
        // adding i in increasing order keeps every child list sorted
        for (int i = 1; i < n; i++) {
            children.get(parent[i]).add(i);
        }

        StringBuilder text = new StringBuilder();
        // stack entry = {node, 0 if entering, 1 if all children are already done}
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {0, 0});
        while (!stack.isEmpty()) {
            int[] top = stack.pop();
            int node = top[0];
            if (top[1] == 1) {
                // post-order: the node's own character comes after its subtree
                text.append(s.charAt(node));
                end[node] = text.length() - 1;
                continue;
            }
            start[node] = text.length();
            stack.push(new int[] {node, 1});
            // push children in reverse so the smallest index is visited first
            List<Integer> kids = children.get(node);
            for (int i = kids.size() - 1; i >= 0; i--) {
                stack.push(new int[] {kids.get(i), 0});
            }
        }
        return text.toString();
    }

    /** Manacher: radius[i] = how far the palindrome centred at i extends on each side. */
    private int[] manacher(String t) {
        int len = t.length();
        int[] radius = new int[len];
        // [center - radius[center], right] is the rightmost palindrome found so far
        int center = 0;
        int right = 0;
        for (int i = 0; i < len; i++) {
            if (i < right) {
                // reuse the mirror position's radius, capped by the known boundary
                radius[i] = Math.min(right - i, radius[2 * center - i]);
            }
            while (i - radius[i] - 1 >= 0 && i + radius[i] + 1 < len
                    && t.charAt(i - radius[i] - 1) == t.charAt(i + radius[i] + 1)) {
                radius[i]++;
            }
            if (i + radius[i] > right) {
                center = i;
                right = i + radius[i];
            }
        }
        return radius;
    }

    /** Non-negative remainder. */
    private long mod(long value, long modulus) {
        long result = value % modulus;
        if (result < 0) {
            result += modulus;
        }
        return result;
    }
}
