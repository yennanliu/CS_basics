package LeetCodeJava.BFS;

// https://leetcode.com/problems/k-similar-strings/description/

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * 854. K-Similar Strings
 * Hard
 *
 * Strings s1 and s2 are k-similar (for some non-negative integer k) if we can swap
 * the positions of two letters in s1 exactly k times so that the resulting string
 * equals s2.
 *
 * Given two anagrams s1 and s2, return the smallest k for which s1 and s2 are k-similar.
 *
 *
 * Example 1:
 *
 * Input: s1 = "ab", s2 = "ba"
 * Output: 1
 * Explanation: The two string are 1-similar because we can use one swap to change
 * s1 to s2: "ab" --> "ba".
 *
 * Example 2:
 *
 * Input: s1 = "abc", s2 = "bca"
 * Output: 2
 * Explanation: The two strings are 2-similar because we can use two swaps to change
 * s1 to s2: "abc" --> "bac" --> "bca".
 *
 *
 * Constraints:
 *
 * 1 <= s1.length <= 20
 * s2.length == s1.length
 * s1 and s2 contain only lowercase letters from the set {'a','b','c','d','e','f'}.
 * s2 is an anagram of s1.
 *
 */
public class KSimilarStrings {

    // V0
    // IDEA: BFS with GREEDY PRUNING
    /**
     *   BFS over strings, ONE swap = ONE edge, so the first time we reach s2 the
     *   level IS the minimum number of swaps.
     *
     *   The naive branching factor (all C(n,2) swaps) EXPLODES. Two prunings make
     *   it tractable AND stay optimal:
     *
     *     1) Always fix the LEFTMOST mismatching index i.
     *        Some swap must eventually place s2[i] at position i, and doing it now
     *        never costs more, so we only branch on swaps touching i.
     *
     *     2) Only swap in a j with cur[j] == s2[i] AND cur[j] != s2[j].
     *        Moving a character that is ALREADY in its correct place would just
     *        have to be undone later.
     *
     *   time  = O(n * n! / ...) in theory, but pruning keeps it small for n <= 20
     *   space = O(number of visited states)
     */
    public int kSimilarity(String s1, String s2) {
        if (s1.equals(s2)) {
            return 0;
        }

        int n = s1.length();

        Deque<String> queue = new ArrayDeque<>();
        queue.offer(s1);

        Set<String> visited = new HashSet<>();
        visited.add(s1);

        int steps = 0;

        while (!queue.isEmpty()) {
            steps += 1;

            int levelSize = queue.size();
            for (int t = 0; t < levelSize; t++) {
                String cur = queue.poll();

                /** NOTE !!! pruning 1)
                 *
                 *  jump straight to the LEFTMOST position that is still wrong
                 *  -> everything before it is already settled
                 */
                int i = 0;
                while (cur.charAt(i) == s2.charAt(i)) {
                    i += 1;
                }

                for (int j = i + 1; j < n; j++) {
                    /** NOTE !!! pruning 2)
                     *
                     *  bring the RIGHT character to i,
                     *  and DON'T disturb a character already in place
                     */
                    if (cur.charAt(j) != s2.charAt(i) || cur.charAt(j) == s2.charAt(j)) {
                        continue;
                    }

                    char[] arr = cur.toCharArray();
                    char tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                    String nxt = new String(arr);

                    if (nxt.equals(s2)) {
                        return steps;
                    }
                    if (visited.add(nxt)) {
                        queue.offer(nxt);
                    }
                }
            }
        }

        return -1;
    }


    // V1
    // IDEA: MEMOIZED DFS (top-down min swaps instead of BFS levels)
    /**
     *  minSwaps(state) = 1 + min over the swaps that fix the leftmost mismatch.
     *
     *  A top-down recursion with a memo on the string, rather than BFS expanding
     *  level by level. It explores the SAME pruned state graph as V0 but returns
     *  the value bottom-up, so it needs no queue and no visited-set bookkeeping.
     *
     *  NOTE !!! the bidirectional variant is deliberately NOT used here: the
     *           `fix the leftmost mismatch` pruning is direction dependent, so the
     *           two frontiers would canonicalise toward different goals and could
     *           fail to meet.
     *
     *  time  = O(states * n)
     *  space = O(states)
     */
    public int kSimilarity_1(String s1, String s2) {
        return dfsMemo(s1, s2, new HashMap<>());
    }

    private int dfsMemo(String cur, String goal, Map<String, Integer> memo) {
        if (cur.equals(goal)) {
            return 0;
        }
        Integer cached = memo.get(cur);
        if (cached != null) {
            return cached;
        }
        memo.put(cur, Integer.MAX_VALUE / 2); // guard against revisiting mid-recursion

        int best = Integer.MAX_VALUE / 2;
        for (String nxt : neighbours(cur, goal)) {
            best = Math.min(best, 1 + dfsMemo(nxt, goal, memo));
        }

        memo.put(cur, best);
        return best;
    }

    /** every string one swap away that fixes the leftmost mismatch vs `goal` */
    private List<String> neighbours(String cur, String goal) {
        List<String> out = new ArrayList<>();
        int i = 0;
        while (i < cur.length() && cur.charAt(i) == goal.charAt(i)) {
            i += 1;
        }
        if (i == cur.length()) {
            return out;
        }
        for (int j = i + 1; j < cur.length(); j++) {
            if (cur.charAt(j) == goal.charAt(i) && cur.charAt(j) != goal.charAt(j)) {
                char[] c = cur.toCharArray();
                char t = c[i];
                c[i] = c[j];
                c[j] = t;
                out.add(new String(c));
            }
        }
        return out;
    }

    // V2
    // IDEA: A* WITH A CYCLE-COUNT HEURISTIC
    /**
     *  A permutation that decomposes into cycles needs (length - 1) swaps per
     *  cycle, so `mismatches / 2` rounded up is an ADMISSIBLE lower bound on the
     *  remaining swaps.
     *
     *  Feeding that into A* orders the frontier by f = swaps + heuristic and
     *  reaches the goal after touching far fewer states than plain BFS.
     *
     *  time  = O(states log states)
     *  space = O(states)
     */
    public int kSimilarity_2(String s1, String s2) {
        if (s1.equals(s2)) {
            return 0;
        }

        PriorityQueue<Object[]> pq =
                new PriorityQueue<>(Comparator.comparingInt(o -> (Integer) o[0]));
        pq.add(new Object[] { heuristic(s1, s2), 0, s1 });
        Set<String> seen = new HashSet<>();

        while (!pq.isEmpty()) {
            Object[] cur = pq.poll();
            int g = (Integer) cur[1];
            String state = (String) cur[2];

            if (state.equals(s2)) {
                return g;
            }
            if (!seen.add(state)) {
                continue;
            }

            for (String cand : neighbours(state, s2)) {
                if (!seen.contains(cand)) {
                    pq.add(new Object[] { g + 1 + heuristic(cand, s2), g + 1, cand });
                }
            }
        }
        return -1;
    }

    /** ceil(mismatches / 2) -- one swap can fix at most two positions */
    private int heuristic(String a, String b) {
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                diff += 1;
            }
        }
        return (diff + 1) / 2;
    }

    // V3
    // IDEA: DFS WITH ITERATIVE DEEPENING
    /**
     *  Try depth limits 1, 2, 3, ... and DFS to each limit. The first limit that
     *  succeeds IS the answer.
     *
     *  Memory drops from `all visited states` to just the recursion stack O(d),
     *  which is the classic IDDFS trade -- re-exploring cheap shallow levels in
     *  exchange for O(d) space instead of O(b^d).
     *
     *  time  = O(b^d)
     *  space = O(d)
     */
    public int kSimilarity_3(String s1, String s2) {
        if (s1.equals(s2)) {
            return 0;
        }
        for (int limit = 1; ; limit++) {
            if (dfsLimited(s1.toCharArray(), s2, 0, 0, limit)) {
                return limit;
            }
        }
    }

    private boolean dfsLimited(char[] cur, String goal, int start, int depth, int limit) {
        if (depth == limit) {
            return new String(cur).equals(goal);
        }

        int i = start;
        while (i < cur.length && cur[i] == goal.charAt(i)) {
            i += 1;
        }
        if (i == cur.length) {
            return false; // already equal but depth < limit -> this limit is not minimal
        }
        // BOUND: each remaining swap fixes at most 2 positions
        int diff = 0;
        for (int t = i; t < cur.length; t++) {
            if (cur[t] != goal.charAt(t)) {
                diff += 1;
            }
        }
        if ((diff + 1) / 2 > limit - depth) {
            return false;
        }

        for (int j = i + 1; j < cur.length; j++) {
            if (cur[j] != goal.charAt(i) || cur[j] == goal.charAt(j)) {
                continue;
            }
            char t = cur[i];
            cur[i] = cur[j];
            cur[j] = t;
            if (dfsLimited(cur, goal, i + 1, depth + 1, limit)) {
                return true;
            }
            t = cur[i];
            cur[i] = cur[j];
            cur[j] = t;
        }
        return false;
    }

}
