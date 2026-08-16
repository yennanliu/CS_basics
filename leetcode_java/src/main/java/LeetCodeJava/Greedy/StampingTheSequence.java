package LeetCodeJava.Greedy;

// https://leetcode.com/problems/stamping-the-sequence/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 936. Stamping The Sequence
 * Hard
 *
 * You are given two strings stamp and target. Initially, there is a string s of length
 * target.length with all s[i] == '?'.
 *
 * In one turn, you can place stamp over s and replace every letter in the s with the
 * corresponding letter from stamp.
 *
 * For example, if stamp = "abc" and target = "abcba", then s is "?????" initially.
 * In one turn you can:
 *     place stamp at index 0 of s to obtain "abc??",
 *     place stamp at index 1 of s to obtain "?abc?", or
 *     place stamp at index 2 of s to obtain "??abc".
 * Note that stamp must be fully contained in the boundaries of s in order to stamp
 * (i.e., you cannot place stamp at index 3 of s).
 *
 * We want to convert s to target using at most 10 * target.length turns.
 *
 * Return an array of the index of the left-most letter being stamped at each turn.
 * If we cannot obtain target from s within 10 * target.length turns, return an empty
 * array.
 *
 *
 * Example 1:
 *
 * Input: stamp = "abc", target = "ababc"
 * Output: [0,2]
 * Explanation: Initially s = "?????".
 * - Place stamp at index 0 to get "abc??".
 * - Place stamp at index 2 to get "ababc".
 * [1,0,2] would also be accepted as an answer, as well as some other answers.
 *
 * Example 2:
 *
 * Input: stamp = "abca", target = "aabcaca"
 * Output: [3,0,1]
 * Explanation: Initially s = "???????".
 * - Place stamp at index 3 to get "???abca".
 * - Place stamp at index 0 to get "abcabca".
 * - Place stamp at index 1 to get "aabcaca".
 *
 *
 * Constraints:
 *
 * 1 <= stamp.length <= target.length <= 1000
 * stamp and target consist of lowercase English letters.
 *
 */
public class StampingTheSequence {

    // V0
    // IDEA: WORK BACKWARDS (greedy un-stamping) + TOPOLOGICAL SORT
    /**
     *  Stamping FORWARD is hard because later stamps OVERWRITE earlier ones.
     *  So run it in REVERSE: start from `target` and PEEL stamps off, turning the
     *  covered positions into wildcards. The REVERSED order of the peels is the answer.
     *
     *  Model each of the (n - m + 1) windows as a node:
     *
     *     indeg[i] = how many positions inside window i still MISMATCH the stamp.
     *                indeg[i] == 0  ->  window i matches exactly, we can un-stamp it now.
     *     g[p]     = list of windows that are BLOCKED by position p (mismatch at p).
     *
     *  When we un-stamp window i, every position it covers becomes a WILDCARD '?'.
     *  A wildcard matches ANYTHING, so each window that was blocked by that position
     *  loses one mismatch -> decrement its indeg, and enqueue it when it hits 0.
     *
     *  If EVERY position ends up covered, reverse the peel order and return it.
     *
     *  time  = O(n * (n - m + 1))
     *  space = O(n * (n - m + 1))
     */
    public int[] movesToStamp(String stamp, String target) {
        int m = stamp.length();
        int n = target.length();

        int[] indeg = new int[n - m + 1]; // mismatches remaining per window
        List<List<Integer>> g = new ArrayList<>(); // position -> windows blocked by it
        for (int p = 0; p < n; p++) {
            g.add(new ArrayList<>());
        }

        Deque<Integer> q = new ArrayDeque<>();

        for (int i = 0; i + m <= n; i++) {
            indeg[i] = m;
            for (int j = 0; j < m; j++) {
                if (target.charAt(i + j) == stamp.charAt(j)) {
                    indeg[i] -= 1;
                    if (indeg[i] == 0) {
                        q.offer(i);
                    }
                } else {
                    g.get(i + j).add(i);
                }
            }
        }

        List<Integer> res = new ArrayList<>();
        boolean[] covered = new boolean[n];

        while (!q.isEmpty()) {
            int i = q.poll();
            res.add(i);

            for (int j = 0; j < m; j++) {
                int p = i + j;
                if (covered[p]) {
                    continue;
                }
                /** NOTE !!!
                 *
                 *  position p becomes a WILDCARD -> it no longer blocks anyone
                 */
                covered[p] = true;
                for (int w : g.get(p)) {
                    indeg[w] -= 1;
                    if (indeg[w] == 0) {
                        q.offer(w);
                    }
                }
            }
        }

        for (boolean c : covered) {
            if (!c) {
                return new int[0];
            }
        }

        // we PEELED stamps off, so the stamping order is the REVERSE
        int[] out = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            out[i] = res.get(res.size() - 1 - i);
        }
        return out;
    }

}
