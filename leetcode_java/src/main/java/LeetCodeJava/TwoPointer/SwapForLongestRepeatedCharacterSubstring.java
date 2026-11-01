package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/swap-for-longest-repeated-character-substring/description/

/**
 * 1156. Swap For Longest Repeated Character Substring
 * Medium
 *
 * You are given a string text. You can swap two of the characters in the text.
 *
 * Return the length of the longest substring with repeated characters.
 *
 *
 * Example 1:
 *
 * Input: text = "ababa"
 * Output: 3
 * Explanation: We can swap the first 'b' with the last 'a', or the last 'b' with the first 'a'.
 * Then, the longest repeated character substring is "aaa" with length 3.
 *
 * Example 2:
 *
 * Input: text = "aaabaaa"
 * Output: 6
 * Explanation: Swap 'b' with the last 'a' (or the first 'a'),
 * and we get longest repeated character substring "aaaaaa" with length 6.
 *
 * Example 3:
 *
 * Input: text = "aaaaa"
 * Output: 5
 * Explanation: No need to swap, longest repeated character substring is "aaaaa"
 * with length is 5.
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 2 * 10^4
 * text consist of lowercase English characters only.
 *
 */
public class SwapForLongestRepeatedCharacterSubstring {

    // V0
    // IDEA: TWO POINTERS OVER RUNS + CHARACTER COUNT
    //       for each run text[i..j-1] of one char c (length leftLen):
    //       - skip the ONE blocking char at index j
    //       - take the next run of c right after it (length rightLen)
    //       - a swap can join them : leftLen + rightLen + 1
    //       but we can never use more c's than the string owns -> cap by count[c]
    //       answer = max over all runs of min(leftLen + rightLen + 1, count[c])
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxRepOpt1(String text) {
        int n = text.length();

        // how many of each letter exist in total
        int[] count = new int[26];
        for (int i = 0; i < n; i++) {
            count[text.charAt(i) - 'a']++;
        }

        int best = 0;
        int i = 0;
        while (i < n) {
            char c = text.charAt(i);

            // run of c : text[i .. j-1]
            int j = i;
            while (j < n && text.charAt(j) == c) {
                j++;
            }
            int leftLen = j - i;

            // skip the blocker at j, then extend through the next run of c
            int k = j + 1;
            while (k < n && text.charAt(k) == c) {
                k++;
            }
            int rightLen = k - j - 1;

            // +1 for the c swapped in (from elsewhere), capped by how many c's exist
            int candidate = Math.min(leftLen + rightLen + 1, count[c - 'a']);
            best = Math.max(best, candidate);

            // the next run starts right after this one
            i = j;
        }
        return best;
    }
}
