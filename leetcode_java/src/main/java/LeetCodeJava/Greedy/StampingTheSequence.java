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


    // V1
    // IDEA: REPEATED FULL PASSES -- peel any window that matches (wildcards allowed)
    /**
     *  Sweep every window; if it matches the stamp treating '?' as a wildcard AND
     *  is not already all wildcards, peel it (write '?' across it) and record the
     *  index. Repeat until a whole pass changes nothing.
     *
     *  No graph, no indegree counters -- just `keep peeling whatever is peelable`,
     *  which is the plainest statement of the reverse-time idea.
     *
     *  time  = O(n^2 * (n - m + 1)) worst case
     *  space = O(n)
     */
    public int[] movesToStamp_1(String stamp, String target) {
        int m = stamp.length();
        int n = target.length();
        char[] cur = target.toCharArray();

        List<Integer> order = new ArrayList<>();
        int peeled = 0;
        boolean changed = true;

        while (changed && peeled < n) {
            changed = false;
            for (int i = 0; i + m <= n; i++) {
                int stamped = peel(cur, stamp, i);
                if (stamped > 0) {
                    order.add(i);
                    peeled += stamped;
                    changed = true;
                }
            }
        }

        if (peeled < n) {
            return new int[0];
        }
        int[] res = new int[order.size()];
        for (int i = 0; i < order.size(); i++) {
            res[i] = order.get(order.size() - 1 - i); // peeling order reversed
        }
        return res;
    }

    /**
     * if window i matches (with '?' wildcards) and is not already blank, blank it
     * and return how many real characters were consumed; otherwise 0
     */
    private int peel(char[] cur, String stamp, int i) {
        int m = stamp.length();
        int real = 0;
        for (int j = 0; j < m; j++) {
            if (cur[i + j] == '?') {
                continue;
            }
            if (cur[i + j] != stamp.charAt(j)) {
                return 0;
            }
            real += 1;
        }
        if (real == 0) {
            return 0; // already fully wildcard -> peeling it achieves nothing
        }
        for (int j = 0; j < m; j++) {
            cur[i + j] = '?';
        }
        return real;
    }

    // V2
    // IDEA: QUEUE-DRIVEN PEELING WITH LOCAL RE-CHECKS
    /**
     *  V1 rescans the whole string after every peel. Peeling window i can only make
     *  the windows OVERLAPPING it newly peelable, so re-checking just
     *  [i - m + 1, i + m - 1] is enough.
     *
     *  -> the total work becomes O(n * m) instead of O(n^2 * m).
     *
     *  time  = O(n * m)
     *  space = O(n)
     */
    public int[] movesToStamp_2(String stamp, String target) {
        int m = stamp.length();
        int n = target.length();
        char[] cur = target.toCharArray();

        List<Integer> order = new ArrayList<>();
        int peeled = 0;

        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i + m <= n; i++) {
            q.offer(i);
        }

        while (!q.isEmpty()) {
            int i = q.poll();
            int stamped = peel(cur, stamp, i);
            if (stamped == 0) {
                continue;
            }
            order.add(i);
            peeled += stamped;
            // only the overlapping windows can have become peelable
            for (int j = Math.max(0, i - m + 1); j <= Math.min(n - m, i + m - 1); j++) {
                q.offer(j);
            }
        }

        if (peeled < n) {
            return new int[0];
        }
        int[] res = new int[order.size()];
        for (int i = 0; i < order.size(); i++) {
            res[i] = order.get(order.size() - 1 - i);
        }
        return res;
    }

    // V3
    // IDEA: GREEDY BY GAIN -- always peel the window that blanks the most characters
    /**
     *  Each round, score every window by how many REAL characters it would turn
     *  into wildcards and peel the best one.
     *
     *  Slower (a full scan per peel) but it produces the SHORTEST stamping
     *  sequence in practice rather than merely a valid one, which is what you want
     *  when the 10 * n move budget is tight.
     *
     *  time  = O(n^2 * m)
     *  space = O(n)
     */
    public int[] movesToStamp_3(String stamp, String target) {
        int m = stamp.length();
        int n = target.length();
        char[] cur = target.toCharArray();

        List<Integer> order = new ArrayList<>();
        int peeled = 0;

        while (peeled < n) {
            int bestIdx = -1;
            int bestGain = 0;

            for (int i = 0; i + m <= n; i++) {
                int gain = gainAt(cur, stamp, i);
                if (gain > bestGain) {
                    bestGain = gain;
                    bestIdx = i;
                }
            }
            if (bestIdx == -1) {
                break; // nothing left to peel
            }

            peel(cur, stamp, bestIdx);
            order.add(bestIdx);
            peeled += bestGain;
        }

        if (peeled < n) {
            return new int[0];
        }
        int[] res = new int[order.size()];
        for (int i = 0; i < order.size(); i++) {
            res[i] = order.get(order.size() - 1 - i);
        }
        return res;
    }

    /** how many real characters window i would blank, or 0 if it does not match */
    private int gainAt(char[] cur, String stamp, int i) {
        int m = stamp.length();
        int real = 0;
        for (int j = 0; j < m; j++) {
            if (cur[i + j] == '?') {
                continue;
            }
            if (cur[i + j] != stamp.charAt(j)) {
                return 0;
            }
            real += 1;
        }
        return real;
    }

}
