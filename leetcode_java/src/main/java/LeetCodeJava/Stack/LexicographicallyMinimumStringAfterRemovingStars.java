package LeetCodeJava.Stack;

// https://leetcode.com/problems/lexicographically-minimum-string-after-removing-stars/

/**
 *  3170. Lexicographically Minimum String After Removing Stars
 *  Medium
 *
 *  You are given a string s. It may contain any number of '*' characters. Your
 *  task is to remove all '*' characters.
 *
 *  While there is a '*', do the following operation: delete the leftmost '*'
 *  and the smallest non-'*' character to its left. If there are several
 *  smallest characters, you can delete any of them.
 *
 *  Return the lexicographically smallest resulting string after removing all
 *  '*' characters.
 *
 *  Example 1:
 *    Input: s = "aaba*"
 *    Output: "aab"
 *    Explanation: deleting the 'b' at index 3 gives the smallest result.
 *
 *  Example 2:
 *    Input: s = "abc"
 *    Output: "abc"
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s consists only of lowercase English letters and '*'.
 *    The input is generated such that it is possible to delete all '*'.
 */
public class LexicographicallyMinimumStringAfterRemovingStars {

    // V0
    // IDEA: ONE STACK OF POSITIONS PER LETTER - DELETE THE *LATEST* SMALLEST
    //       Each '*' must remove the smallest surviving letter to its left, and
    //       when several copies exist the choice is ours. Removing the
    //       RIGHTMOST copy is best: it leaves the earlier copies in place, and
    //       a smaller letter earlier in the string is what lexicographic order
    //       rewards.
    //       So keep 26 stacks of indices, one per letter. A '*' pops from the
    //       lowest non-empty stack -> that letter's latest position in O(1).
    /**
     * time = O(26 * N)
     * space = O(N)
     */
    public String clearStars(String s) {
        int n = s.length();
        int[][] stacks = new int[26][];
        int[] tops = new int[26];
        for (int c = 0; c < 26; c++) {
            stacks[c] = new int[n];
            tops[c] = -1;
        }
        boolean[] removed = new boolean[n];

        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (ch == '*') {
                removed[i] = true;
                for (int c = 0; c < 26; c++) {
                    if (tops[c] >= 0) {
                        removed[stacks[c][tops[c]--]] = true;
                        break;
                    }
                }
            } else {
                int c = ch - 'a';
                stacks[c][++tops[c]] = i;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (!removed[i]) {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
}
