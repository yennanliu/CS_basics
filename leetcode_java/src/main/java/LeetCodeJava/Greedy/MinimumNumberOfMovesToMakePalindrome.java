package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-moves-to-make-palindrome/description/

/**
 * 2193. Minimum Number of Moves to Make Palindrome
 * Hard
 *
 * You are given a string s consisting only of lowercase English letters.
 *
 * In one move, you can select any two adjacent characters of s and swap them.
 *
 * Return the minimum number of moves needed to make s a palindrome.
 *
 * Note that the input will be generated such that s can always be converted to a palindrome.
 *
 *
 * Example 1:
 *
 * Input: s = "aabb"
 * Output: 2
 *
 * Example 2:
 *
 * Input: s = "letelt"
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 2000
 * s consists only of lowercase English letters.
 * s can be converted to a palindrome using a finite number of moves.
 *
 */
public class MinimumNumberOfMovesToMakePalindrome {

    // V0
    // IDEA: GREEDY FROM THE OUTSIDE IN - MATCH chars[left] WITH ITS RIGHTMOST TWIN, SWAP IT INTO PLACE
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minMovesToMakePalindrome(String s) {
        char[] chars = s.toCharArray();
        int moves = 0;
        int left = 0;
        int right = chars.length - 1;

        while (left < right) {
            // the cheapest partner for chars[left] is its rightmost occurrence in [left + 1, right]
            int twin = right;
            while (twin > left && chars[twin] != chars[left]) {
                twin--;
            }

            if (twin == left) {
                // no twin: this is the single odd character and belongs in the centre.
                // nudge it one step right and retry the same `left`
                swap(chars, left, left + 1);
                moves++;
            } else {
                // bubble the twin rightwards into position `right`, one adjacent swap at a time
                for (int k = twin; k < right; k++) {
                    swap(chars, k, k + 1);
                    moves++;
                }
                left++;
                right--;
            }
        }
        return moves;
    }

    /** Swaps chars[i] and chars[j]. */
    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }
}
