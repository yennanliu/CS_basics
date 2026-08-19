package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-substrings-that-can-be-rearranged-to-contain-a-string-i/

/**
 *  3297. Count Substrings That Can Be Rearranged to Contain a String I
 *  Medium
 *
 *  You are given two strings word1 and word2.
 *
 *  A string x is called valid if x can be rearranged to have word2 as a prefix.
 *
 *  Return the total number of valid substrings of word1.
 *
 *  Example 1:
 *    Input: word1 = "bcca", word2 = "abc"
 *    Output: 1
 *    Explanation: The only valid substring is "bcca", which can be rearranged
 *                 to "abcc" having "abc" as a prefix.
 *
 *  Example 2:
 *    Input: word1 = "abcabc", word2 = "abc"
 *    Output: 10
 *    Explanation: All substrings except those of size 1 and 2 are valid.
 *
 *  Example 3:
 *    Input: word1 = "abcabc", word2 = "aaabc"
 *    Output: 0
 *
 *  Constraints:
 *    1 <= word1.length <= 10^5
 *    1 <= word2.length <= 10^4
 *    word1 and word2 consist only of lowercase English letters.
 */
public class CountSubstringsThatCanBeRearrangedToContainAStringI {

    // V0
    // IDEA: "REARRANGEABLE TO HAVE word2 AS A PREFIX" == COVERS ITS LETTER COUNTS
    //       the substring may be shuffled freely, so it qualifies exactly when
    //       it holds at least as many of every letter as word2 does — the
    //       leftovers go after the prefix. that condition only gets easier as
    //       the substring grows, so a sliding window is exact: for each right
    //       end push `left` past every start whose window still covers word2,
    //       then the starts 0 .. left-1 are exactly the valid ones.
    //       tracking how many letters are still SHORT keeps the check O(1).
    /**
     * time = O(N + M)
     * space = O(1)   // 26 counters
     */
    public long validSubstringCount(String word1, String word2) {
        int[] need = new int[26];
        for (int i = 0; i < word2.length(); i++) {
            need[word2.charAt(i) - 'a']++;
        }
        int missing = 0;
        for (int c = 0; c < 26; c++) {
            if (need[c] > 0) {
                missing++;
            }
        }

        int[] have = new int[26];
        int left = 0;
        long res = 0L;
        for (int right = 0; right < word1.length(); right++) {
            int c = word1.charAt(right) - 'a';
            have[c]++;
            if (have[c] == need[c]) {
                missing--;
            }
            while (missing == 0) {
                int d = word1.charAt(left) - 'a';
                have[d]--;
                if (have[d] == need[d] - 1) {
                    missing++;
                }
                left++;
            }
            res += left;
        }
        return res;
    }
}
