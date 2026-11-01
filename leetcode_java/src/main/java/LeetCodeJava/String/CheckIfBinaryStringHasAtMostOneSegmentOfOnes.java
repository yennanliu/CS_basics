package LeetCodeJava.String;

// https://leetcode.com/problems/check-if-binary-string-has-at-most-one-segment-of-ones/description/

/**
 * 1784. Check if Binary String Has at Most One Segment of Ones
 * Easy
 *
 * Given a binary string s without leading zeros, return true if s contains at most one contiguous segment of ones. Otherwise, return false.
 *
 * Example 1:
 *
 * Input: s = "1001"
 * Output: false
 * Explanation: The string has two segments of size 1.
 *
 * Example 2:
 *
 * Input: s = "110"
 * Output: true
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s[i] is either '0' or '1'.
 * s[0] is '1'.
 *
 */
public class CheckIfBinaryStringHasAtMostOneSegmentOfOnes {

    // V0
    // IDEA: LOOK FOR THE PATTERN "01"
    //       s starts with '1', so a second segment of ones can only begin right
    //       after a zero -- exactly when "01" occurs.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean checkOnesSegment(String s) {
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i - 1) == '0' && s.charAt(i) == '1') {
                return false;
            }
        }
        return true;
    }
}
