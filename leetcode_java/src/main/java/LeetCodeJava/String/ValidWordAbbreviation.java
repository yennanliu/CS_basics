package LeetCodeJava.String;

// https://leetcode.com/problems/valid-word-abbreviation/

/**
 *  408. Valid Word Abbreviation
 *  Easy
 *
 *  A string can be abbreviated by replacing any number of non-adjacent,
 *  non-empty substrings with their lengths. The lengths should not have
 *  leading zeros.
 *
 *  Given a string word and an abbreviation abbr, return whether the string
 *  matches the given abbreviation.
 *
 *  Example 1:
 *    Input: word = "internationalization", abbr = "i12iz4n"   Output: true
 *  Example 2:
 *    Input: word = "apple", abbr = "a2e"                      Output: false
 *
 *  Constraints:
 *    1 <= word.length <= 20, word consists of only lowercase English letters.
 *    1 <= abbr.length <= 10, abbr consists of lowercase English letters and digits.
 *    All the integers in abbr will fit in a 32-bit integer.
 */
public class ValidWordAbbreviation {

    // V0
    // IDEA: two pointers - on a digit run parse the count and jump, on a letter compare directly
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean validWordAbbreviation(String word, String abbr) {
        if (word == null || abbr == null) {
            return false;
        }

        int i = 0; // pointer on word
        int j = 0; // pointer on abbr

        while (i < word.length() && j < abbr.length()) {
            char c = abbr.charAt(j);

            if (Character.isDigit(c)) {
                // leading zero is invalid
                if (c == '0') {
                    return false;
                }
                int num = 0;
                while (j < abbr.length() && Character.isDigit(abbr.charAt(j))) {
                    num = num * 10 + (abbr.charAt(j) - '0');
                    j++;
                }
                i += num;
            } else {
                if (word.charAt(i) != c) {
                    return false;
                }
                i++;
                j++;
            }
        }

        return i == word.length() && j == abbr.length();
    }
}
