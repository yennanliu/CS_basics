package LeetCodeJava.String;

// https://leetcode.com/problems/generate-a-string-with-characters-that-have-odd-counts/description/

/**
 * 1374. Generate a String With Characters That Have Odd Counts
 * Easy
 *
 * Given an integer n, return a string with n characters such that each character in such string
 * occurs an odd number of times.
 *
 * The returned string must contain only lowercase English letters.
 * If there are multiples valid strings, return any of them.
 *
 *
 * Example 1:
 *
 * Input: n = 4
 * Output: "pppz"
 * Explanation: "pppz" is a valid string since the character 'p' occurs three times and the
 * character 'z' occurs once. Note that there are many other valid strings such as "ohhh" and "love".
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: "xy"
 * Explanation: "xy" is a valid string since the characters 'x' and 'y' occur once.
 * Note that there are many other valid strings such as "ag" and "ur".
 *
 * Example 3:
 *
 * Input: n = 7
 * Output: "holasss"
 *
 *
 * Constraints:
 *
 * 1 <= n <= 500
 *
 */
public class GenerateAStringWithCharactersThatHaveOddCounts {

    // V0
    // IDEA: CONSTRUCTION BY THE PARITY OF n
    //       n odd  -> "a" * n             (one letter, odd count)
    //       n even -> "a" * (n - 1) + "b" (n - 1 is odd, 'b' appears once)
    //       a single letter cannot work for an even n, so two letters are the minimum there.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String generateTheString(int n) {
        StringBuilder sb = new StringBuilder();
        if (n % 2 == 1) {
            for (int i = 0; i < n; i++) {
                sb.append('a');
            }
        } else {
            for (int i = 0; i < n - 1; i++) {
                sb.append('a');
            }
            sb.append('b');
        }
        return sb.toString();
    }
}
