package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-substrings-that-can-be-rearranged-to-contain-a-string-ii/

/**
 *  3298. Count Substrings That Can Be Rearranged to Contain a String II
 *  Hard
 *
 *  You are given two strings word1 and word2.
 *
 *  A string x is called valid if x can be rearranged to have word2 as a prefix.
 *
 *  Return the total number of valid substrings of word1.
 *
 *  Note that the memory limits in this problem are smaller than usual, so you
 *  must implement a solution with a linear runtime complexity.
 *
 *  Example 1:
 *    Input: word1 = "bcca", word2 = "abc"
 *    Output: 1
 *
 *  Example 2:
 *    Input: word1 = "abcabc", word2 = "abc"
 *    Output: 10
 *
 *  Example 3:
 *    Input: word1 = "abcabc", word2 = "aaabc"
 *    Output: 0
 *
 *  Constraints:
 *    1 <= word1.length <= 10^6
 *    1 <= word2.length <= 10^4
 *    word1 and word2 consist only of lowercase English letters.
 */
public class CountSubstringsThatCanBeRearrangedToContainAStringII {

    // V0
    // IDEA: SAME SLIDING WINDOW AS LC 3297 — IT WAS ALREADY LINEAR
    //       a substring is valid iff it holds at least as many of every letter
    //       as word2 (the surplus sits after the prefix once rearranged), and
    //       that property is monotone in the window, so two pointers apply.
    //       for each right end advance `left` while the window still covers
    //       word2; the starts 0 .. left-1 are the valid ones, contributing
    //       `left`. the only thing this sequel changes is the scale (10^6 with
    //       a tighter memory limit), so the state stays two 26-entry arrays
    //       plus a counter of still-short letters, each char visited twice.
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
