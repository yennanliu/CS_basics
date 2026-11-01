package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/replace-the-substring-for-balanced-string/description/

/**
 * 1234. Replace the Substring for Balanced String
 * Medium
 *
 * You are given a string s of length n containing only four kinds of characters:
 * 'Q', 'W', 'E', and 'R'.
 *
 * A string is said to be balanced if each of its characters appears n / 4 times where n is
 * the length of the string.
 *
 * Return the minimum length of the substring that can be replaced with any other string of
 * the same length to make s balanced. If s is already balanced, return 0.
 *
 * Example 1:
 *
 * Input: s = "QWER"
 * Output: 0
 * Explanation: s is already balanced.
 *
 * Example 2:
 *
 * Input: s = "QQWE"
 * Output: 1
 * Explanation: We need to replace a 'Q' to 'R', so that "RQWE" (or "QRWE") is balanced.
 *
 * Example 3:
 *
 * Input: s = "QQQW"
 * Output: 2
 * Explanation: We can replace the first "QQ" to "ER".
 *
 *
 * Constraints:
 *
 * n == s.length
 * 4 <= n <= 10^5
 * n is a multiple of 4.
 * s contains only 'Q', 'W', 'E', and 'R'.
 *
 */
public class ReplaceTheSubstringForBalancedString {

    // V0
    // IDEA: SLIDING WINDOW / TWO POINTERS (shrinkable window)
    //       KEY INSIGHT : a window [left..right] can be rewritten into anything,
    //       so it works iff every char OUTSIDE the window occurs at most n/4
    //       times (whatever is missing is then written inside the window).
    //       count everything, move `right` forward (s[right] leaves "outside"),
    //       and shrink from the left while the window is still valid, recording
    //       the smallest width.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int balancedString(String s) {
        int n = s.length();
        int target = n / 4;

        // outside[c] = how many c's sit outside the current window
        int[] outside = new int[128];
        for (int i = 0; i < n; i++) {
            outside[s.charAt(i)]++;
        }

        // already balanced -> nothing to replace
        if (isValid(outside, target)) {
            return 0;
        }

        int best = n;
        int left = 0;
        for (int right = 0; right < n; right++) {
            // s[right] moves INTO the window
            outside[s.charAt(right)]--;

            // shrink while the window still covers every surplus char
            while (left <= right && isValid(outside, target)) {
                best = Math.min(best, right - left + 1);
                outside[s.charAt(left)]++;
                left++;
            }
        }
        return best;
    }

    /** True if no char outside the window exceeds the balanced count. */
    private boolean isValid(int[] outside, int target) {
        return outside['Q'] <= target
                && outside['W'] <= target
                && outside['E'] <= target
                && outside['R'] <= target;
    }
}
