package LeetCodeJava.String;

// https://leetcode.com/problems/length-of-last-word/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 58. Length of Last Word
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given a string s consisting of words and spaces, return the length of the last word in the string.
 *
 * A word is a maximal substring consisting of non-space characters only.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "Hello World"
 * Output: 5
 * Explanation: The last word is "World" with length 5.
 * Example 2:
 *
 * Input: s = "   fly me   to   the moon  "
 * Output: 4
 * Explanation: The last word is "moon" with length 4.
 * Example 3:
 *
 * Input: s = "luffy is still joyboy"
 * Output: 6
 * Explanation: The last word is "joyboy" with length 6.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * s consists of only English letters and spaces ' '.
 * There will be at least one word in s.
 *
 *
 */
public class LengthOfLastWord {

    // V0
    // IDEA: STRING OP
    public int lengthOfLastWord(String s) {
        // edge
        if (s.isEmpty()) {
            return 0;
        }

        // remove `extra space` ??
        //s.strip();

        List<String> list = new ArrayList<>();
        for (String x : s.split(" ")) {
            if (!x.isEmpty() && x.length() > 0) {
                // System.out.println(">>> x = " + x);
                list.add(x);
            }
        }

        return list.get(list.size() - 1).length();
    }

    // V1
    // https://leetcode.com/problems/length-of-last-word/solutions/5774504/video-2-solutions-bonus-by-niits-saqv/
    public int lengthOfLastWord_1(String s) {
        int end = s.length() - 1;

        while (end >= 0 && s.charAt(end) == ' ') {
            end--;
        }

        int start = end;
        while (start >= 0 && s.charAt(start) != ' ') {
            start--;
        }

        return end - start;
    }

    // V2
    // https://leetcode.com/problems/length-of-last-word/solutions/4954087/9743easy-solutionwith-explanation-by-mra-30jd/
    public int lengthOfLastWord_2(String s) {
        s = s.trim();

        int length = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != ' ') {
                length++;
            } else if (length > 0) {
                break;
            }
        }

        return length;
    }


    // V3




}
