package LeetCodeJava.String;

// https://leetcode.com/problems/number-of-good-ways-to-split-a-string/description/

/**
 * 1525. Number of Good Ways to Split a String
 * Medium
 *
 * You are given a string s.
 *
 * A split is called good if you can split s into two non-empty strings s_left and s_right where their concatenation is equal to s (i.e., s_left + s_right = s) and the number of distinct letters in s_left and s_right is the same.
 *
 * Return the number of good splits you can make in s.
 *
 *
 * Example 1:
 *
 * Input: s = "aacaba"
 * Output: 2
 * Explanation: There are 5 ways to split "aacaba" and 2 of them are good.
 * ("a", "acaba") Left string and right string contains 1 and 3 different letters respectively.
 * ("aa", "caba") Left string and right string contains 1 and 3 different letters respectively.
 * ("aac", "aba") Left string and right string contains 2 and 2 different letters respectively (good split).
 * ("aaca", "ba") Left string and right string contains 2 and 2 different letters respectively (good split).
 * ("aacab", "a") Left string and right string contains 3 and 1 different letters respectively.
 *
 * Example 2:
 *
 * Input: s = "abcd"
 * Output: 1
 * Explanation: Split the string as follows ("ab", "cd").
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of only lowercase English letters.
 *
 */
public class NumberOfGoodWaysToSplitAString {

    // V0
    // IDEA: SWEEP THE CUT, MAINTAIN DISTINCT COUNTS ON BOTH SIDES
    //   start with everything on the right, then move the cut one character at a time:
    //   left gains c, right loses one c; a letter whose count drops to 0 leaves the right side.
    //   stop before the last character, since the right half must stay non-empty.
    /**
     * time = O(N)
     * space = O(26)
     */
    public int numSplits(String s) {
        int[] leftCount = new int[26];
        int[] rightCount = new int[26];
        int leftDistinct = 0;
        int rightDistinct = 0;

        for (int i = 0; i < s.length(); i++) {
            int letter = s.charAt(i) - 'a';
            if (rightCount[letter] == 0) {
                rightDistinct++;
            }
            rightCount[letter]++;
        }

        int goodSplits = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            int letter = s.charAt(i) - 'a';

            // move s[i] from the right side to the left side
            if (leftCount[letter] == 0) {
                leftDistinct++;
            }
            leftCount[letter]++;

            rightCount[letter]--;
            if (rightCount[letter] == 0) {
                rightDistinct--;
            }

            if (leftDistinct == rightDistinct) {
                goodSplits++;
            }
        }
        return goodSplits;
    }
}
