package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/construct-string-with-minimum-cost-easy/description/
// https://leetcode.ca/2024-08-31-3253-Construct-String-with-Minimum-Cost-(Easy)/

import java.util.*;

/**
 * 3253. Construct String with Minimum Cost (Easy)
 * Medium
 *
 * You are given a string target, an array of strings words, and an integer array costs, both arrays of the same length.
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
 * 1 <= target.length <= 2000
 * 1 <= words.length == costs.length <= 50
 * 1 <= words[i].length <= target.length
 * target and words[i] consist only of lowercase English letters.
 * 1 <= costs[i] <= 10^4
 *
 */
public class ConstructStringWithMinimumCostEasy {

    // V0
    // IDEA: DP OVER PREFIXES, TRYING EACH WORD AS THE LAST PIECE
    //   dp[i] = cheapest cost to build target[0 .. i-1]
    //   dp[i] = min over words w with target ending at i equal to w of dp[i - len(w)] + cost(w)
    //   Only 50 words and |target| <= 2000, so a direct comparison per word is affordable
    //   (LC 3213, the hard version, needs hashing).
    /**
     * time = O(N * W * L), W = number of words, L = word length
     * space = O(N)
     */
    public int minimumCost(String target, String[] words, int[] costs) {
        int n = target.length();
        final int INF = Integer.MAX_VALUE;
        int[] dp = new int[n + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w < words.length; w++) {
                String word = words[w];
                int length = word.length();
                int start = i - length;
                // the word must fit and the prefix before it must be buildable
                if (start < 0 || dp[start] == INF) {
                    continue;
                }
                if (target.startsWith(word, start)) {
                    dp[i] = Math.min(dp[i], dp[start] + costs[w]);
                }
            }
        }
        return dp[n] == INF ? -1 : dp[n];
    }
}
