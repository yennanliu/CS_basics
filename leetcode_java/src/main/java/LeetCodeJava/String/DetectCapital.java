package LeetCodeJava.String;

// https://leetcode.com/problems/detect-capital/

/**
 *  520. Detect Capital
 *  Easy
 *
 *  We define the usage of capitals in a word to be right when one of the
 *  following cases holds:
 *    - All letters in this word are capitals, like "USA".
 *    - All letters in this word are not capitals, like "leetcode".
 *    - Only the first letter in this word is capital, like "Google".
 *
 *  Given a string word, return true if the usage of capitals in it is right.
 *
 *  Example 1:
 *    Input: word = "USA"    Output: true
 *  Example 2:
 *    Input: word = "FlaG"   Output: false
 *
 *  Constraints:
 *    1 <= word.length <= 100
 *    word consists of lowercase and uppercase English letters.
 */
public class DetectCapital {

    // V0
    // IDEA: count uppercase letters - valid iff all upper, none upper, or exactly one at index 0
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean detectCapitalUse(String word) {
        if (word == null || word.isEmpty()) {
            return true;
        }

        int upper = 0;
        for (int i = 0; i < word.length(); i++) {
            if (Character.isUpperCase(word.charAt(i))) {
                upper++;
            }
        }

        if (upper == word.length() || upper == 0) {
            return true;
        }
        return upper == 1 && Character.isUpperCase(word.charAt(0));
    }
}
