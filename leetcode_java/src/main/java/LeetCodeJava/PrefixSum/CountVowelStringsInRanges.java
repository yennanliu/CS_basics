package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/count-vowel-strings-in-ranges/description/

/**
 * 2559. Count Vowel Strings in Ranges
 * Medium
 *
 * You are given a 0-indexed array of strings words and a 2D array of integers queries.
 *
 * Each query queries[i] = [li, ri] asks us to find the number of strings present at the indices ranging from li to ri (both inclusive) of words that start and end with a vowel.
 *
 * Return an array ans of size queries.length, where ans[i] is the answer to the ith query.
 *
 * Note that the vowel letters are 'a', 'e', 'i', 'o', and 'u'.
 *
 *
 * Example 1:
 *
 * Input: words = ["aba","bcb","ece","aa","e"], queries = [[0,2],[1,4],[1,1]]
 * Output: [2,3,0]
 * Explanation: The strings starting and ending with a vowel are "aba", "ece", "aa" and "e".
 * The answer to the query [0,2] is 2 (strings "aba" and "ece").
 * to query [1,4] is 3 (strings "ece", "aa", "e").
 * to query [1,1] is 0.
 * We return [2,3,0].
 *
 * Example 2:
 *
 * Input: words = ["a","e","i"], queries = [[0,2],[0,1],[2,2]]
 * Output: [3,2,1]
 * Explanation: Every string satisfies the conditions, so we return [3,2,1].
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 10^5
 * 1 <= words[i].length <= 40
 * words[i] consists only of lowercase English letters.
 * sum(words[i].length) <= 3 * 10^5
 * 1 <= queries.length <= 10^5
 * 0 <= li <= ri < words.length
 *
 */
public class CountVowelStringsInRanges {

    // V0
    // IDEA: PREFIX SUM
    //       mark each word 1 if it starts and ends with a vowel, else 0, and build
    //       prefix[i] = marked words among words[0 .. i-1]. a query (l, r) is then
    //       prefix[r + 1] - prefix[l] in O(1). a one-letter word like "e" counts.
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public int[] vowelStrings(String[] words, int[][] queries) {
        int n = words.length;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            String word = words[i];
            boolean counts = isVowel(word.charAt(0)) && isVowel(word.charAt(word.length() - 1));
            prefix[i + 1] = prefix[i] + (counts ? 1 : 0);
        }

        int[] answers = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int left = queries[q][0];
            int right = queries[q][1];
            answers[q] = prefix[right + 1] - prefix[left];
        }
        return answers;
    }

    /** True for 'a', 'e', 'i', 'o', 'u'. */
    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }
}
