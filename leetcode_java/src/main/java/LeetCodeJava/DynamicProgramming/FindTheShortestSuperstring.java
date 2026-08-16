package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-shortest-superstring/description/

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

}
