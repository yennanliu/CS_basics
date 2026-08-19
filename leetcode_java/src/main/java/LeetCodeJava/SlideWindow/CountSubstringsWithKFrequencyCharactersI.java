package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-substrings-with-k-frequency-characters-i/

/**
 *  3325. Count Substrings With K-Frequency Characters I
 *  Medium
 *
 *  Given a string s and an integer k, return the total number of substrings of
 *  s where at least one character appears at least k times.
 *
 *  Example 1:
 *    Input: s = "abacb", k = 2
 *    Output: 4
 *    Explanation: The valid substrings are "aba", "abac", "abacb" ('a' twice)
 *                 and "bacb" ('b' twice).
 *
 *  Example 2:
 *    Input: s = "abcde", k = 1
 *    Output: 15
 *    Explanation: All substrings are valid because every character appears at
 *                 least once.
 *
 *  Constraints:
 *    1 <= s.length <= 3000
 *    1 <= k <= s.length
 *    s consists only of lowercase English letters.
 */
public class CountSubstringsWithKFrequencyCharactersI {

    // V0
    // IDEA: SLIDING WINDOW — "SOME CHARACTER HITS k" ONLY GETS EASIER AS THE
    //       WINDOW GROWS
    //       a valid substring stays valid when extended, so for each right end
    //       there is a threshold: every start at or before some index gives a
    //       valid substring and every later start does not. so push `left`
    //       forward while the window still has a character reaching k; the
    //       count of valid substrings ending here is exactly `left`.
    //       only the character just added can newly reach k, so the test is
    //       a single array lookup.
    /**
     * time = O(N)
     * space = O(1)   // 26 counters
     */
    public int numberOfSubstrings(String s, int k) {
        int[] cnt = new int[26];
        int left = 0;
        int res = 0;
        for (int right = 0; right < s.length(); right++) {
            int c = s.charAt(right) - 'a';
            cnt[c]++;
            while (cnt[c] >= k) {
                int d = s.charAt(left) - 'a';
                cnt[d]--;
                left++;
            }
            res += left;
        }
        return res;
    }
}
