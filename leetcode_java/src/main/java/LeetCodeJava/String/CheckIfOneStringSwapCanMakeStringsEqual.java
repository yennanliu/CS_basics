package LeetCodeJava.String;

// https://leetcode.com/problems/check-if-one-string-swap-can-make-strings-equal/description/

/**
 * 1790. Check if One String Swap Can Make Strings Equal
 * Easy
 *
 * You are given two strings s1 and s2 of equal length. A string swap is an operation where you choose two indices in a string (not necessarily different) and swap the characters at these indices.
 *
 * Return true if it is possible to make both strings equal by performing at most one string swap on exactly one of the strings. Otherwise, return false.
 *
 * Example 1:
 *
 * Input: s1 = "bank", s2 = "kanb"
 * Output: true
 * Explanation: For example, swap the first character with the last character of s2 to make "bank".
 *
 * Example 2:
 *
 * Input: s1 = "attack", s2 = "defend"
 * Output: false
 * Explanation: It is impossible to make them equal with one string swap.
 *
 * Example 3:
 *
 * Input: s1 = "kelb", s2 = "kelb"
 * Output: true
 * Explanation: The two strings are already equal, so no string swap operation is required.
 *
 * Constraints:
 *
 * 1 <= s1.length, s2.length <= 100
 * s1.length == s2.length
 * s1 and s2 consist of only lowercase English letters.
 *
 */
public class CheckIfOneStringSwapCanMakeStringsEqual {

    // V0
    // IDEA: COLLECT THE MISMATCHING POSITIONS (THERE MUST BE EXACTLY 0 OR 2)
    //       one swap repairs two positions i, j only when they mirror each other:
    //       s1[i] == s2[j] and s1[j] == s2[i].
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean areAlmostEqual(String s1, String s2) {
        int firstDiff = -1;
        int secondDiff = -1;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                continue;
            }
            if (firstDiff == -1) {
                firstDiff = i;
            } else if (secondDiff == -1) {
                secondDiff = i;
            } else {
                // a third mismatch: one swap cannot fix it
                return false;
            }
        }

        // already equal: the swap is optional
        if (firstDiff == -1) {
            return true;
        }
        // exactly one mismatch cannot be fixed by a swap
        if (secondDiff == -1) {
            return false;
        }
        return s1.charAt(firstDiff) == s2.charAt(secondDiff)
                && s1.charAt(secondDiff) == s2.charAt(firstDiff);
    }
}
