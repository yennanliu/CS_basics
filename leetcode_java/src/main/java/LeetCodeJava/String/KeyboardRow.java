package LeetCodeJava.String;

// https://leetcode.com/problems/keyboard-row/description/

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 500. Keyboard Row
 * Easy
 *
 * Given an array of strings words, return the words that can be typed using letters of
 * the alphabet on only one row of American keyboard like the image below.
 *
 * In the American keyboard:
 *
 * - the first row consists of the characters "qwertyuiop",
 * - the second row consists of the characters "asdfghjkl", and
 * - the third row consists of the characters "zxcvbnm".
 *
 * Example 1:
 *
 * Input: words = ["Hello","Alaska","Dad","Peace"]
 * Output: ["Alaska","Dad"]
 * Explanation: Both "a" and "A" are in the 2nd row of the American keyboard due to
 * case insensitivity.
 *
 * Example 2:
 *
 * Input: words = ["omk"]
 * Output: []
 *
 * Example 3:
 *
 * Input: words = ["adsdf","sfd"]
 * Output: ["adsdf","sfd"]
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 20
 * 1 <= words[i].length <= 100
 * words[i] consists of English letters (both lowercase and uppercase).
 *
 */
public class KeyboardRow {

    // V0
    // IDEA: HASH TABLE (map every letter -> its keyboard row id)
    /**
     *  a word is valid if ALL of its letters map to the SAME row id
     *
     *  NOTE !!! the check is CASE INSENSITIVE -> lowercase every char first
     *
     *  time  = O(L)   // L = total number of characters over all words
     *  space = O(1)   // the letter -> row map has a fixed size (26)
     */
    public String[] findWords(String[] words) {
        String[] rows = { "qwertyuiop", "asdfghjkl", "zxcvbnm" };

        int[] rowOf = new int[26];
        for (int idx = 0; idx < rows.length; idx++) {
            for (char ch : rows[idx].toCharArray()) {
                rowOf[ch - 'a'] = idx;
            }
        }

        List<String> res = new ArrayList<>();
        for (String w : words) {
            String lower = w.toLowerCase();
            int first = rowOf[lower.charAt(0) - 'a'];

            boolean sameRow = true;
            for (int i = 1; i < lower.length(); i++) {
                if (rowOf[lower.charAt(i) - 'a'] != first) {
                    sameRow = false;
                    break;
                }
            }

            if (sameRow) {
                res.add(w);
            }
        }

        return res.toArray(new String[0]);
    }


    // V1
    // IDEA: ONE BITMASK PER ROW, AND them together
    /**
     *  Give each row a 26-bit mask. A word is typeable on one row iff the AND of
     *  the masks of all its letters is non-zero -- i.e. some row contains every
     *  letter.
     *
     *  Reduces the whole per-word test to a fold of three integers.
     *
     *  time  = O(L)
     *  space = O(1)
     */
    public String[] findWords_1(String[] words) {
        String[] rows = { "qwertyuiop", "asdfghjkl", "zxcvbnm" };
        int[] maskOf = new int[26];
        for (int r = 0; r < rows.length; r++) {
            for (char ch : rows[r].toCharArray()) {
                maskOf[ch - 'a'] |= 1 << r;
            }
        }

        List<String> res = new ArrayList<>();
        for (String w : words) {
            int acc = 0b111; // all three rows still possible
            for (char ch : w.toLowerCase().toCharArray()) {
                acc &= maskOf[ch - 'a'];
            }
            if (acc != 0) {
                res.add(w);
            }
        }
        return res.toArray(new String[0]);
    }

    // V2
    // IDEA: indexOf AGAINST THE THREE ROW STRINGS
    /**
     *  Pick the row that contains the word's FIRST letter, then check that every
     *  other letter is in that same row with String.indexOf.
     *
     *  No precomputation at all -- the row strings themselves are the lookup
     *  table -- which is the shortest correct version.
     *
     *  time  = O(L * 26)
     *  space = O(1)
     */
    public String[] findWords_2(String[] words) {
        String[] rows = { "qwertyuiop", "asdfghjkl", "zxcvbnm" };

        List<String> res = new ArrayList<>();
        for (String w : words) {
            String lower = w.toLowerCase();
            String row = null;
            for (String r : rows) {
                if (r.indexOf(lower.charAt(0)) >= 0) {
                    row = r;
                    break;
                }
            }
            boolean ok = true;
            for (int i = 1; i < lower.length() && ok; i++) {
                ok = row.indexOf(lower.charAt(i)) >= 0;
            }
            if (ok) {
                res.add(w);
            }
        }
        return res.toArray(new String[0]);
    }

    // V3
    // IDEA: REGEX -- one alternation of three character classes
    /**
     *  `^[qwertyuiop]+$|^[asdfghjkl]+$|^[zxcvbnm]+$` with CASE_INSENSITIVE states
     *  the whole rule in one pattern.
     *
     *  The most declarative version: the specification IS the code, and it needs no
     *  case folding step.
     *
     *  time  = O(L)
     *  space = O(1)
     */
    private static final Pattern ROW_PATTERN = Pattern.compile(
            "^[qwertyuiop]+$|^[asdfghjkl]+$|^[zxcvbnm]+$", Pattern.CASE_INSENSITIVE);

    public String[] findWords_3(String[] words) {
        List<String> res = new ArrayList<>();
        for (String w : words) {
            if (ROW_PATTERN.matcher(w).matches()) {
                res.add(w);
            }
        }
        return res.toArray(new String[0]);
    }

}
