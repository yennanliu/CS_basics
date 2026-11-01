package LeetCodeJava.String;

// https://leetcode.com/problems/find-the-sequence-of-strings-appeared-on-the-screen/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3324. Find the Sequence of Strings Appeared on the Screen
 * Medium
 *
 * You are given a string target.
 *
 * Alice is going to type target on her computer using a special keyboard that has only two keys:
 *
 * Key 1 appends the character "a" to the string on the screen.
 * Key 2 changes the last character of the string on the screen to its next character in the English alphabet. For example, "c" changes to "d" and "z" changes to "a".
 *
 * Note that initially there is an empty string "" on the screen, so she can only press key 1.
 *
 * Return a list of all strings that appear on the screen as Alice types target, in the order they appear, using the minimum key presses.
 *
 *
 * Example 1:
 *
 * Input: target = "abc"
 * Output: ["a","aa","ab","aba","abb","abc"]
 * Explanation:
 * The sequence of key presses done by Alice are:
 * Press key 1, and the string on the screen becomes "a".
 * Press key 1, and the string on the screen becomes "aa".
 * Press key 2, and the string on the screen becomes "ab".
 * Press key 1, and the string on the screen becomes "aba".
 * Press key 2, and the string on the screen becomes "abb".
 * Press key 2, and the string on the screen becomes "abc".
 *
 * Example 2:
 *
 * Input: target = "he"
 * Output: ["a","b","c","d","e","f","g","h","ha","hb","hc","hd","he"]
 *
 *
 * Constraints:
 *
 * 1 <= target.length <= 400
 * target consists only of lowercase English letters.
 *
 */
public class FindTheSequenceOfStringsAppearedOnTheScreen {

    // V0
    // IDEA: SIMULATION -> FOR EACH TARGET CHAR: PRESS KEY 1 ('a'), THEN KEY 2 UNTIL IT MATCHES
    /**
     * time = O(N^2 * 26)  (each recorded screen is a string of length <= N)
     * space = O(N^2 * 26) for the output
     */
    public List<String> stringSequence(String target) {
        List<String> screens = new ArrayList<>();
        // the finished prefix is never revisited
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < target.length(); i++) {
            char goal = target.charAt(i);
            // key 1 appends 'a'; every key 2 press bumps the last letter by one
            for (char current = 'a'; current <= goal; current++) {
                screens.add(prefix.toString() + current);
            }
            prefix.append(goal);
        }
        return screens;
    }
}
