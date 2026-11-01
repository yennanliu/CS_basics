package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/construct-string-with-minimum-cost/description/

import java.util.*;

/**
 * 3213. Construct String with Minimum Cost
 * Hard
 *
 * You are given a string target, an array of strings words, and an integer array costs,
 * both arrays of the same length.
 *
 * Imagine an empty string s.
 *
 * You can perform the following operation any number of times (including zero):
 *
 * Choose an index i in the range [0, words.length - 1].
 * Append words[i] to s.
 * The cost of operation is costs[i].
 *
 * Return the minimum cost to make s equal to target. If it's not possible, return -1.
 *
 *
 * Example 1:
 *
 * Input: target = "abcdef", words = ["abdef","abc","d","def","ef"], costs = [100,1,1,10,5]
 * Output: 7
 * Explanation:
 * The minimum cost can be achieved by performing the following operations:
 * Select index 1 and append "abc" to s at a cost of 1, resulting in s = "abc".
 * Select index 2 and append "d" to s at a cost of 1, resulting in s = "abcd".
 * Select index 4 and append "ef" to s at a cost of 5, resulting in s = "abcdef".
 *
 * Example 2:
 *
 * Input: target = "aaaa", words = ["z","zz","zzz"], costs = [1,10,100]
 * Output: -1
 * Explanation:
 * It is impossible to make s equal to target, so we return -1.
 *
 *
 * Constraints:
 *
 * 1 <= target.length <= 5 * 10^4
 * 1 <= words.length == costs.length <= 5 * 10^4
 * 1 <= words[i].length <= target.length
 * The total sum of words[i].length is less than or equal to 5 * 10^4
 * target and words[i] consist only of lowercase English letters.
 * 1 <= costs[i] <= 10^4
 *
 */
public class ConstructStringWithMinimumCost {

    // V0
    // IDEA: DP OVER PREFIXES + ROLLING HASH, TRYING ONLY THE DISTINCT WORD LENGTHS
    //
    //   dp[i] = min cost to build the prefix target[0 : i]  (INF = impossible)
    //   dp[0] = 0
    //   dp[i] = min over words w ending at i of  dp[i - len(w)] + cost(w)
    //
    //   Speed-up: total word length <= 5*10^4, so there are at most ~316 DISTINCT
    //   lengths (1 + 2 + ... + L <= 5*10^4). Per position we only try those lengths,
    //   and for each length a map {hash of word -> cheapest cost} answers
    //   "is target[i-L : i] a word?" in O(1) via prefix hashes of target.
    //   Two moduli are combined into one long key so a collision is negligible.
    //
    //   (Trying every word at every position is O(n * total word length) and times out.)
    private static final long MOD1 = 1_000_000_007L;
    private static final long MOD2 = 1_000_000_009L;
    private static final long BASE = 131L;
    private static final int INF = Integer.MAX_VALUE;

    /**
     * time = O(N * D + W)   N = target length, D = distinct word lengths (<= ~316), W = total word length
     * space = O(N + W)
     */
    public int minimumCost(String target, String[] words, int[] costs) {
        int n = target.length();

        // prefix hashes of target under both moduli, plus powers of BASE
        long[] prefix1 = new long[n + 1];
        long[] prefix2 = new long[n + 1];
        long[] power1 = new long[n + 1];
        long[] power2 = new long[n + 1];
        power1[0] = 1;
        power2[0] = 1;
        for (int i = 0; i < n; i++) {
            int ch = target.charAt(i);
            prefix1[i + 1] = (prefix1[i] * BASE + ch) % MOD1;
            prefix2[i + 1] = (prefix2[i] * BASE + ch) % MOD2;
            power1[i + 1] = (power1[i] * BASE) % MOD1;
            power2[i + 1] = (power2[i] * BASE) % MOD2;
        }

        // length -> {combined hash of a word of that length -> cheapest cost}
        // duplicate words collapse to their minimum cost here
        Map<Integer, Map<Long, Integer>> costByLength = new HashMap<>();
        for (int k = 0; k < words.length; k++) {
            String word = words[k];
            int length = word.length();
            if (length > n) {
                continue; // can never fit inside target
            }
            long hash1 = 0;
            long hash2 = 0;
            for (int j = 0; j < length; j++) {
                int ch = word.charAt(j);
                hash1 = (hash1 * BASE + ch) % MOD1;
                hash2 = (hash2 * BASE + ch) % MOD2;
            }
            long key = combine(hash1, hash2);
            Map<Long, Integer> table = costByLength.computeIfAbsent(length, x -> new HashMap<>());
            Integer best = table.get(key);
            if (best == null || costs[k] < best) {
                table.put(key, costs[k]);
            }
        }

        // the distinct lengths, ascending, so the inner loop can stop once L > i
        List<Integer> lengths = new ArrayList<>(costByLength.keySet());
        Collections.sort(lengths);

        int[] dp = new int[n + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int length : lengths) {
                if (length > i) {
                    break;
                }
                int start = i - length;
                // the prefix before this piece must itself be buildable
                if (dp[start] == INF) {
                    continue;
                }
                // hash of target[start : i] = prefix[i] - prefix[start] * BASE^length
                long sub1 = ((prefix1[i] - prefix1[start] * power1[length]) % MOD1 + MOD1) % MOD1;
                long sub2 = ((prefix2[i] - prefix2[start] * power2[length]) % MOD2 + MOD2) % MOD2;
                Integer wordCost = costByLength.get(length).get(combine(sub1, sub2));
                if (wordCost != null && dp[start] + wordCost < dp[i]) {
                    dp[i] = dp[start] + wordCost;
                }
            }
        }

        if (dp[n] == INF) {
            return -1;
        }
        return dp[n];
    }

    /** Packs the two 30-bit hashes into one long key (high 32 bits, low 32 bits). */
    private long combine(long hash1, long hash2) {
        return (hash1 << 32) | hash2;
    }
}
