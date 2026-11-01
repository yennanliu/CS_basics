package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-swaps-to-make-strings-equal/description/

/**
 * 1247. Minimum Swaps to Make Strings Equal
 * Medium
 *
 * You are given two strings s1 and s2 of equal length consisting of letters "x" and "y" only.
 * Your task is to make these two strings equal to each other. You can swap any two characters
 * that belong to different strings, which means: swap s1[i] and s2[j].
 *
 * Return the minimum number of swaps required to make s1 and s2 equal, or return -1 if it is
 * impossible to do so.
 *
 *
 * Example 1:
 *
 * Input: s1 = "xx", s2 = "yy"
 * Output: 1
 * Explanation: Swap s1[0] and s2[1], s1 = "yx", s2 = "yx".
 *
 * Example 2:
 *
 * Input: s1 = "xy", s2 = "yx"
 * Output: 2
 * Explanation: Swap s1[0] and s2[0], s1 = "yy", s2 = "xx".
 * Swap s1[0] and s2[1], s1 = "xy", s2 = "xy".
 * Note that you cannot swap s1[0] and s1[1] to make s1 equal to "yx", cause we can only swap
 * chars in different strings.
 *
 * Example 3:
 *
 * Input: s1 = "xx", s2 = "xy"
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= s1.length, s2.length <= 1000
 * s1.length == s2.length
 * s1, s2 only contain 'x' or 'y'.
 *
 */
public class MinimumSwapsToMakeStringsEqual {

    // V0
    // IDEA: GREEDY / COUNTING mismatched columns ("xy" pairs and "yx" pairs)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumSwap(String s1, String s2) {
        // xyCount: columns with s1 = 'x', s2 = 'y'; yxCount: the opposite
        int xyCount = 0;
        int yxCount = 0;
        for (int i = 0; i < s1.length(); i++) {
            char a = s1.charAt(i);
            char b = s2.charAt(i);
            if (a == 'x' && b == 'y') {
                xyCount++;
            } else if (a == 'y' && b == 'x') {
                yxCount++;
            }
        }

        // an odd number of mismatches leaves one column that can never be fixed
        if ((xyCount + yxCount) % 2 == 1) {
            return -1;
        }

        // two "xy" columns are fixed by one swap (same for two "yx");
        // a leftover "xy" + "yx" pair needs two swaps
        int swaps = xyCount / 2 + yxCount / 2;
        if (xyCount % 2 == 1) {
            swaps += 2;
        }
        return swaps;
    }
}
