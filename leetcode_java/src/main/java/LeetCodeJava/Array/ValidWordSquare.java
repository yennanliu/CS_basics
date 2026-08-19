package LeetCodeJava.Array;

// https://leetcode.com/problems/valid-word-square/

import java.util.List;

/**
 *  422. Valid Word Square
 *  Easy
 *
 *  Given an array of strings words, return true if it forms a valid word square.
 *
 *  A sequence of strings forms a valid word square if the kth row and column read
 *  the exact same string, where 0 <= k < max(numRows, numColumns).
 *
 *  Example 1:
 *  Input: words = ["abcd","bnrt","crmy","dtye"]
 *  Output: true
 *  Explanation:
 *  The 1st row and 1st column both read "abcd".
 *  The 2nd row and 2nd column both read "bnrt".
 *  The 3rd row and 3rd column both read "crmy".
 *  The 4th row and 4th column both read "dtye".
 *  Therefore, it is a valid word square.
 *
 *  Example 2:
 *  Input: words = ["ball","area","read","lady"]
 *  Output: false
 *  Explanation: The 3rd row reads "read" while the 3rd column reads "lead".
 *
 *  Constraints:
 *  1 <= words.length <= 500
 *  1 <= words[i].length <= 500
 *  words[i] consists of only lowercase English letters.
 */
public class ValidWordSquare {

    // V0
    // IDEA: for every char (i, j), the mirrored char (j, i) must exist and be equal
    /**
     * time = O(m * n)   // m = number of words, n = max word length
     * space = O(1)
     */
    public boolean validWordSquare(List<String> words) {
        if (words == null || words.isEmpty()) {
            return true;
        }
        int rows = words.size();
        for (int i = 0; i < rows; i++) {
            String w = words.get(i);
            for (int j = 0; j < w.length(); j++) {
                // the j-th row must exist and be long enough to hold index i
                if (j >= rows) {
                    return false;
                }
                String mirror = words.get(j);
                if (i >= mirror.length()) {
                    return false;
                }
                if (w.charAt(j) != mirror.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }
}
