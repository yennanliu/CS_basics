package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-odd-letters-from-number/description/
// https://leetcode.ca/2025-07-13-3581-Count-Odd-Letters-from-Number/

/**
 * 3581. Count Odd Letters from Number
 * Easy
 *
 * You are given an integer n perform the following steps:
 *
 * Convert each digit of n into its lowercase English word (e.g., 4 -> "four",
 * 1 -> "one").
 * Concatenate those words in the original digit order to form a string s.
 *
 * Return the number of distinct characters in s that appear an odd number of
 * times.
 *
 *
 * Example 1:
 *
 * Input: n = 41
 * Output: 5
 * Explanation:
 * 41 -> "fourone"
 * Characters with odd frequencies: 'f', 'u', 'r', 'n', 'e'. Thus, the answer
 * is 5.
 *
 * Example 2:
 *
 * Input: n = 20
 * Output: 5
 * Explanation:
 * 20 -> "twozero"
 * Characters with odd frequencies: 't', 'w', 'z', 'e', 'r'. Thus, the answer
 * is 5.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class CountOddLettersFromNumber {

    // V0
    // IDEA: TRACK EACH LETTER'S PARITY WHILE SPELLING THE DIGITS
    //   only whether a letter's count is odd matters, so flip a per-letter flag
    //   on every occurrence. digit order is irrelevant to the counts, so the
    //   digits can be peeled straight off n without building the string.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int countOddLetters(int n) {
        String[] words = {
            "zero", "one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine"
        };

        boolean[] odd = new boolean[26];
        int value = n;
        while (value > 0) {
            String word = words[value % 10];
            for (int i = 0; i < word.length(); i++) {
                int letter = word.charAt(i) - 'a';
                odd[letter] = !odd[letter];
            }
            value /= 10;
        }

        int oddLetters = 0;
        for (boolean isOdd : odd) {
            if (isOdd) {
                oddLetters++;
            }
        }
        return oddLetters;
    }
}
