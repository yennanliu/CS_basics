package LeetCodeJava.String;

// https://leetcode.com/problems/longest-common-prefix-between-adjacent-strings-after-removals/description/

/**
 * 3598. Longest Common Prefix Between Adjacent Strings After Removals
 * Medium
 *
 * You are given an array of strings words. For each index i in the range [0, words.length - 1], perform the following steps:
 *
 * Remove the element at index i from the words array.
 * Compute the length of the longest common prefix among all adjacent pairs in the modified array.
 *
 * Return an array answer, where answer[i] denotes the length of the longest common prefix between the adjacent pairs after removing the element at index i. If the words array contains fewer than 2 strings after removing element i, or if no adjacent pair exists, then answer[i] should be 0.
 *
 *
 * Example 1:
 *
 * Input: words = ["jump","run","run","jump","run"]
 * Output: [3,0,0,3,3]
 * Explanation:
 * Removing index 0:
 * words becomes ["run", "run", "jump", "run"]
 * Longest adjacent pair prefix: "run" and "run" -> 3
 * Removing index 1:
 * words becomes ["jump", "run", "jump", "run"]
 * Longest adjacent pair prefix: none -> 0
 * Removing index 2:
 * words becomes ["jump", "run", "jump", "run"]
 * Longest adjacent pair prefix: none -> 0
 * Removing index 3:
 * words becomes ["jump", "run", "run", "run"]
 * Longest adjacent pair prefix: "run" and "run" -> 3
 * Removing index 4:
 * words becomes ["jump", "run", "run", "jump"]
 * Longest adjacent pair prefix: "run" and "run" -> 3
 *
 * Example 2:
 *
 * Input: words = ["dog","racer","car"]
 * Output: [0,0,0]
 * Explanation:
 * Removing any index results in an answer of 0.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 10^5
 * 1 <= words[i].length <= 10^4
 * words[i] consists of lowercase English letters.
 * The sum of words[i].length is smaller than or equal 10^5.
 *
 */
public class LongestCommonPrefixBetweenAdjacentStringsAfterRemovals {

    // V0
    // IDEA: PREFIX / SUFFIX MAXIMA OVER THE ORIGINAL ADJACENT-PAIR LCPs + THE ONE STITCHED PAIR
    //   removing index i kills pairs (i-1, i) and (i, i+1) and creates (i-1, i+1); every other
    //   pair survives. answer[i] = max(best pair left of i, best pair right of i, lcp(i-1, i+1)).
    /**
     * time = O(total length of words)
     * space = O(N)
     */
    public int[] longestCommonPrefix(String[] words) {
        int n = words.length;
        int[] answer = new int[n];
        // with <= 2 words, removing one leaves no adjacent pair
        if (n <= 2) {
            return answer;
        }

        // pairLcp[j] = lcp(words[j], words[j + 1])
        int pairCount = n - 1;
        int[] pairLcp = new int[pairCount];
        for (int j = 0; j < pairCount; j++) {
            pairLcp[j] = lcp(words[j], words[j + 1]);
        }
        // prefixMax[j] = max(pairLcp[0..j]), suffixMax[j] = max(pairLcp[j..pairCount-1])
        int[] prefixMax = new int[pairCount];
        int[] suffixMax = new int[pairCount];
        prefixMax[0] = pairLcp[0];
        for (int j = 1; j < pairCount; j++) {
            prefixMax[j] = Math.max(prefixMax[j - 1], pairLcp[j]);
        }
        suffixMax[pairCount - 1] = pairLcp[pairCount - 1];
        for (int j = pairCount - 2; j >= 0; j--) {
            suffixMax[j] = Math.max(suffixMax[j + 1], pairLcp[j]);
        }

        for (int i = 0; i < n; i++) {
            int best = 0;
            // pairs entirely left of i end at index i - 1, i.e. pair indices 0..i-2
            if (i - 2 >= 0) {
                best = Math.max(best, prefixMax[i - 2]);
            }
            // pairs entirely right of i start at index i + 1
            if (i + 1 <= pairCount - 1) {
                best = Math.max(best, suffixMax[i + 1]);
            }
            // the freshly stitched pair (i - 1, i + 1)
            if (i > 0 && i < n - 1) {
                best = Math.max(best, lcp(words[i - 1], words[i + 1]));
            }
            answer[i] = best;
        }
        return answer;
    }

    /** Length of the longest common prefix of a and b. */
    private int lcp(String a, String b) {
        int limit = Math.min(a.length(), b.length());
        int i = 0;
        while (i < limit && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return i;
    }
}
