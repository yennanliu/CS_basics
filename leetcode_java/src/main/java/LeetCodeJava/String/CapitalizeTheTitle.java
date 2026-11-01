package LeetCodeJava.String;

// https://leetcode.com/problems/capitalize-the-title/description/

/**
 * 2129. Capitalize the Title
 * Easy
 *
 * You are given a string title consisting of one or more words separated by a single space, where each word consists of English letters. Capitalize the string by changing the capitalization of each word such that:
 *
 * If the length of the word is 1 or 2 letters, change all letters to lowercase.
 * Otherwise, change the first letter to uppercase and the remaining letters to lowercase.
 *
 * Return the capitalized title.
 *
 *
 * Example 1:
 *
 * Input: title = "capiTalIze tHe titLe"
 * Output: "Capitalize The Title"
 * Explanation:
 * Since all the words have a length of at least 3, the first letter of each word is uppercase, and the remaining letters are lowercase.
 *
 * Example 2:
 *
 * Input: title = "First leTTeR of EACH Word"
 * Output: "First Letter of Each Word"
 * Explanation:
 * The word "of" has length 2, so it is all lowercase.
 * The remaining words have a length of at least 3, so the first letter of each remaining word is uppercase, and the remaining letters are lowercase.
 *
 * Example 3:
 *
 * Input: title = "i lOve leetcode"
 * Output: "i Love Leetcode"
 * Explanation:
 * The word "i" has length 1, so it is lowercase.
 * The word "lOve" has length 4, so it is capitalized.
 * The word "leetcode" has length 8, so it is capitalized.
 *
 *
 * Constraints:
 *
 * 1 <= title.length <= 100
 * title consists of words separated by a single space without any leading or trailing spaces.
 * Each word consists of uppercase and lowercase English letters and is non-empty.
 *
 */
public class CapitalizeTheTitle {

    // V0
    // IDEA: SPLIT, APPLY THE LENGTH RULE PER WORD, JOIN BACK
    /**
     * time = O(N)
     * space = O(N)
     */
    public String capitalizeTitle(String title) {
        String[] words = title.split(" ");
        StringBuilder sb = new StringBuilder(title.length());
        for (int i = 0; i < words.length; i++) {
            String lower = words[i].toLowerCase();
            if (i > 0) {
                sb.append(' ');
            }
            if (lower.length() <= 2) {
                // short words stay all lowercase
                sb.append(lower);
            } else {
                // longer words: first letter upper, the rest lower
                sb.append(Character.toUpperCase(lower.charAt(0)));
                sb.append(lower, 1, lower.length());
            }
        }
        return sb.toString();
    }
}
