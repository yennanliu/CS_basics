package LeetCodeJava.String;

// https://leetcode.com/problems/number-of-lines-to-write-string/

/**
 *  806. Number of Lines To Write String
 *  Easy
 *
 *  You are given a string s of lowercase English letters and an array widths
 *  where widths[0] is the width of 'a', widths[1] is the width of 'b', ...,
 *  widths[25] is the width of 'z'.
 *  You are trying to write s across several lines, where each line is no longer
 *  than 100 pixels. Start at the beginning of s and write as many letters as you
 *  can on the current line, then move to the next line when the next letter
 *  would not fit.
 *  Return an array [totalLines, widthOfLastLine].
 *
 *  Example 1:
 *    Input:  widths = [10,10,...,10], s = "abcdefghijklmnopqrstuvwxyz"
 *    Output: [3,60]
 *
 *  Example 2:
 *    Input:  widths = [4,10,10,...,10], s = "bbbcccdddaaa"
 *    Output: [2,4]
 *
 *  Constraints:
 *    widths.length == 26
 *    2 <= widths[i] <= 10
 *    1 <= s.length <= 1000
 *    s contains only lowercase English letters.
 */
public class NumberOfLinesToWriteString {

    // V0
    // IDEA: greedy single pass — start a new line whenever the char overflows 100 px
    /**
     * time = O(n)
     * space = O(1)
     */
    public int[] numberOfLines(int[] widths, String s) {
        int lines = 1;
        int cur = 0;
        if (s == null || s.isEmpty()) {
            return new int[]{1, 0};
        }
        for (char c : s.toCharArray()) {
            int w = widths[c - 'a'];
            if (cur + w > 100) {
                lines++;
                cur = w;
            } else {
                cur += w;
            }
        }
        return new int[]{lines, cur};
    }
}
