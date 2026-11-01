package LeetCodeJava.String;

// https://leetcode.com/problems/find-the-original-typed-string-i/description/

/**
 * 3330. Find the Original Typed String I
 * Easy
 *
 * Alice is attempting to type a specific string on her computer. However, she tends to be clumsy and may press a key for too long, resulting in a character being typed multiple times.
 *
 * Although Alice tried to focus on her typing, she is aware that she may still have done this at most once.
 *
 * You are given a string word, which represents the final output displayed on Alice's screen.
 *
 * Return the total number of possible original strings that Alice might have intended to type.
 *
 *
 * Example 1:
 *
 * Input: word = "abbcccc"
 * Output: 5
 * Explanation:
 * The possible strings are: "abbcccc", "abbccc", "abbcc", "abbc", and "abcccc".
 *
 * Example 2:
 *
 * Input: word = "abcd"
 * Output: 1
 * Explanation:
 * The only possible string is "abcd".
 *
 * Example 3:
 *
 * Input: word = "aaaa"
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 100
 * word consists only of lowercase English letters.
 *
 */
public class FindTheOriginalTypedStringI {

    // V0
    // IDEA: AT MOST ONE RUN WAS OVER-TYPED -> 1 + SUM OVER RUNS OF (LENGTH - 1)
    //   a run of length L can be shortened to L - 1 shorter lengths; each adjacent equal pair
    //   contributes exactly one of those, so count the equal adjacent pairs.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int possibleStringCount(String word) {
        // the string as typed is always one possibility
        int count = 1;
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) == word.charAt(i - 1)) {
                count++;
            }
        }
        return count;
    }
}
