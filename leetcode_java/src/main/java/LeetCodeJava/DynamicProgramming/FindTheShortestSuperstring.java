package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-shortest-superstring/description/

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 943. Find the Shortest Superstring
 * Hard
 *
 * Given an array of strings words, return the smallest string that contains each string
 * in words as a substring. If there are multiple valid strings of the smallest length,
 * return any of them.
 *
 * You may assume that no string in words is a substring of another string in words.
 *
 * Example 1:
 *
 * Input: words = ["alex","loves","leetcode"]
 * Output: "alexlovesleetcode"
 * Explanation: All permutations of "alex", "loves", "leetcode" would also be accepted.
 *
 * Example 2:
 *
 * Input: words = ["catg","ctaagt","gcta","ttca","atgcatc"]
 * Output: "gctaagttcatgcatc"
 *
 * Constraints:
 *
 * 1 <= words.length <= 12
 * 1 <= words[i].length <= 20
 * words[i] consists of lowercase English letters.
 * All the strings of words are unique.
 *
 */
public class FindTheShortestSuperstring {

    // V0
    // IDEA: BITMASK DP (Travelling Salesman style) + PATH RECONSTRUCTION
    /**
     *  - Since NO word is a substring of another, the answer is simply some
     *    PERMUTATION of the words glued together, sharing the MAXIMUM overlap
     *    between neighbours.
     *
     *  - overlap[i][j] = length of the longest SUFFIX of words[i] that is also
     *    a PREFIX of words[j].
     *
     *  - dp[mask][i] = the MAXIMUM total overlap achievable when the set of used
     *    words is `mask` and the LAST word placed is `i`.
     *
     *  NOTE !!! total length = sum(len(w)) - max total overlap, so MAXIMISING
     *           overlap MINIMISES the superstring -- that is why the DP maximises.
     *
     *  time  = O(n^2 * 2^n + n^2 * L), n = words.length, L = max word length
     *  space = O(n * 2^n)
     */
    public String shortestSuperstring(String[] words) {
        int n = words.length;

        // overlap[i][j] : longest suffix of words[i] == prefix of words[j]
        int[][] overlap = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                int maxK = Math.min(words[i].length(), words[j].length());
                for (int k = maxK; k > 0; k--) {
                    if (words[i].endsWith(words[j].substring(0, k))) {
                        overlap[i][j] = k;
                        break;
                    }
                }
            }
        }

        // dp[mask][i] = max overlap, -1 means `state not reachable`
        int[][] dp = new int[1 << n][n];
        int[][] parent = new int[1 << n][n];
        for (int mask = 0; mask < (1 << n); mask++) {
            for (int i = 0; i < n; i++) {
                dp[mask][i] = -1;
                parent[mask][i] = -1;
            }
        }
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 0;
        }

        for (int mask = 0; mask < (1 << n); mask++) {
            for (int i = 0; i < n; i++) {
                if (dp[mask][i] < 0 || ((mask >> i) & 1) == 0) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (((mask >> j) & 1) == 1) {
                        continue;
                    }
                    int nxtMask = mask | (1 << j);
                    int cand = dp[mask][i] + overlap[i][j];
                    if (cand > dp[nxtMask][j]) {
                        dp[nxtMask][j] = cand;
                        parent[nxtMask][j] = i;
                    }
                }
            }
        }

        int full = (1 << n) - 1;
        int last = 0;
        for (int i = 1; i < n; i++) {
            if (dp[full][i] > dp[full][last]) {
                last = i;
            }
        }

        /** NOTE !!!
         *
         *  walk the PARENT pointers backwards to recover the word order
         */
        List<Integer> order = new ArrayList<>();
        int mask = full;
        int cur = last;
        while (cur != -1) {
            order.add(cur);
            int prev = parent[mask][cur];
            mask ^= (1 << cur);
            cur = prev;
        }
        Collections.reverse(order);

        StringBuilder res = new StringBuilder(words[order.get(0)]);
        for (int t = 1; t < order.size(); t++) {
            int a = order.get(t - 1);
            int b = order.get(t);
            // DROP the shared prefix of the next word
            res.append(words[b].substring(overlap[a][b]));
        }
        return res.toString();
    }


    // V1
    // IDEA: SAME TSP DP, but MINIMISING TOTAL LENGTH instead of maximising overlap
    /**
     *  dp[mask][i] = the shortest superstring LENGTH covering `mask` ending at i.
     *
     *  Equivalent to V0 (length = total - overlap), but stating the objective as
     *  the thing actually being minimised removes the mental step of inverting it
     *  -- and it generalises if the words ever carry per-word costs.
     *
     *  time  = O(n^2 * 2^n)
     *  space = O(n * 2^n)
     */
    public String shortestSuperstring_1(String[] words) {
        int n = words.length;
        int[][] overlap = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                for (int k = Math.min(words[i].length(), words[j].length()); k > 0; k--) {
                    if (words[i].endsWith(words[j].substring(0, k))) {
                        overlap[i][j] = k;
                        break;
                    }
                }
            }
        }

        final int INF = Integer.MAX_VALUE / 4;
        int[][] dp = new int[1 << n][n];
        int[][] parent = new int[1 << n][n];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        for (int[] row : parent) {
            Arrays.fill(row, -1);
        }
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = words[i].length();
        }

        for (int mask = 1; mask < (1 << n); mask++) {
            for (int i = 0; i < n; i++) {
                if (dp[mask][i] >= INF || ((mask >> i) & 1) == 0) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (((mask >> j) & 1) == 1) {
                        continue;
                    }
                    int nm = mask | (1 << j);
                    int cand = dp[mask][i] + words[j].length() - overlap[i][j];
                    if (cand < dp[nm][j]) {
                        dp[nm][j] = cand;
                        parent[nm][j] = i;
                    }
                }
            }
        }

        int full = (1 << n) - 1;
        int last = 0;
        for (int i = 1; i < n; i++) {
            if (dp[full][i] < dp[full][last]) {
                last = i;
            }
        }

        List<Integer> order = new ArrayList<>();
        int mask = full;
        int cur = last;
        while (cur != -1) {
            order.add(cur);
            int prev = parent[mask][cur];
            mask ^= (1 << cur);
            cur = prev;
        }
        Collections.reverse(order);

        StringBuilder sb = new StringBuilder(words[order.get(0)]);
        for (int t = 1; t < order.size(); t++) {
            sb.append(words[order.get(t)].substring(overlap[order.get(t - 1)][order.get(t)]));
        }
        return sb.toString();
    }

    // V2
    // IDEA: BRUTE FORCE over all n! orderings
    /**
     *  Try every permutation, glue with maximum overlap, keep the shortest result.
     *
     *  n! rather than 2^n * n^2, so it only runs up to n = 8 or so -- but it makes
     *  no DP claim at all, which is what validates the bitmask formulation.
     *
     *  time  = O(n! * n * L)
     *  space = O(n)
     */
    public String shortestSuperstring_2(String[] words) {
        int n = words.length;
        int[][] overlap = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                for (int k = Math.min(words[i].length(), words[j].length()); k > 0; k--) {
                    if (words[i].endsWith(words[j].substring(0, k))) {
                        overlap[i][j] = k;
                        break;
                    }
                }
            }
        }

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        String[] best = { null };
        permuteSuper(words, overlap, perm, 0, best);
        return best[0];
    }

    private void permuteSuper(String[] words, int[][] overlap, int[] perm, int pos,
                              String[] best) {
        if (pos == perm.length) {
            StringBuilder sb = new StringBuilder(words[perm[0]]);
            for (int t = 1; t < perm.length; t++) {
                sb.append(words[perm[t]].substring(overlap[perm[t - 1]][perm[t]]));
            }
            String s = sb.toString();
            if (best[0] == null || s.length() < best[0].length()) {
                best[0] = s;
            }
            return;
        }
        for (int i = pos; i < perm.length; i++) {
            int t = perm[pos];
            perm[pos] = perm[i];
            perm[i] = t;
            permuteSuper(words, overlap, perm, pos + 1, best);
            t = perm[pos];
            perm[pos] = perm[i];
            perm[i] = t;
        }
    }

    // V3
    // IDEA: TOP-DOWN MEMOISED TSP
    /**
     *  best(mask, last) = the shortest completion from `last` covering the words
     *  still missing from `mask`.
     *
     *  Recursive Held-Karp: only the reachable (mask, last) pairs are expanded,
     *  which on many inputs is far fewer than the full 2^n * n table.
     *
     *  time  = O(n^2 * 2^n)
     *  space = O(n * 2^n)
     */
    private Integer[][] memoSup;
    private int[][] overlapSup;
    private String[] wordsSup;

    public String shortestSuperstring_3(String[] words) {
        int n = words.length;
        wordsSup = words;
        overlapSup = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                for (int k = Math.min(words[i].length(), words[j].length()); k > 0; k--) {
                    if (words[i].endsWith(words[j].substring(0, k))) {
                        overlapSup[i][j] = k;
                        break;
                    }
                }
            }
        }
        memoSup = new Integer[1 << n][n];

        int full = (1 << n) - 1;
        int bestStart = 0;
        int bestCost = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int cost = words[i].length() + remaining(1 << i, i, full);
            if (cost < bestCost) {
                bestCost = cost;
                bestStart = i;
            }
        }

        // rebuild by replaying the same choices
        StringBuilder sb = new StringBuilder(words[bestStart]);
        int mask = 1 << bestStart;
        int last = bestStart;
        while (mask != full) {
            int pick = -1;
            int want = remaining(mask, last, full);
            for (int j = 0; j < n; j++) {
                if (((mask >> j) & 1) == 1) {
                    continue;
                }
                int cand = words[j].length() - overlapSup[last][j]
                        + remaining(mask | (1 << j), j, full);
                if (cand == want) {
                    pick = j;
                    break;
                }
            }
            sb.append(words[pick].substring(overlapSup[last][pick]));
            mask |= 1 << pick;
            last = pick;
        }
        return sb.toString();
    }

    private int remaining(int mask, int last, int full) {
        if (mask == full) {
            return 0;
        }
        if (memoSup[mask][last] != null) {
            return memoSup[mask][last];
        }
        int best = Integer.MAX_VALUE / 4;
        for (int j = 0; j < wordsSup.length; j++) {
            if (((mask >> j) & 1) == 1) {
                continue;
            }
            best = Math.min(best, wordsSup[j].length() - overlapSup[last][j]
                    + remaining(mask | (1 << j), j, full));
        }
        memoSup[mask][last] = best;
        return best;
    }

}
