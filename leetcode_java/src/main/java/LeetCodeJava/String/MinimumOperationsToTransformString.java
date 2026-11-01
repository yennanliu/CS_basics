package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-operations-to-transform-string/description/

/**
 * 3675. Minimum Operations to Transform String
 * Medium
 *
 * You are given a string s consisting only of lowercase English letters.
 *
 * You can perform the following operation any number of times (including zero):
 *
 * Choose any character c in the string and replace every occurrence of c with
 * the next lowercase letter in the English alphabet.
 *
 * Return the minimum number of operations required to transform s into a
 * string consisting of only 'a' characters.
 *
 * Note: Consider the alphabet as circular, thus 'a' comes after 'z'.
 *
 * Example 1:
 *
 * Input: s = "yz"
 * Output: 2
 * Explanation:
 * Change 'y' to 'z' to get "zz".
 * Change 'z' to 'a' to get "aa".
 * Thus, the answer is 2.
 *
 * Example 2:
 *
 * Input: s = "a"
 * Output: 0
 * Explanation:
 * The string "a" only consists of 'a' characters. Thus, the answer is 0.
 *
 * Constraints:
 *
 * 1 <= s.length <= 5 * 10^5
 * s consists only of lowercase English letters.
 *
 */
public class MinimumOperationsToTransformString {

    // V0
    // IDEA: THE ANSWER IS THE LARGEST WRAP DISTANCE TO 'a' OVER THE LETTERS PRESENT
    //   letter c needs 26 - (c - 'a') steps to wrap to 'a'. The hardest letter passes through
    //   every letter between it and 'z' on the way, sweeping them along for free, so the total
    //   is the maximum single distance, not a sum.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations(String s) {
        int maxDistance = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch != 'a') {
                int distance = 26 - (ch - 'a');
                maxDistance = Math.max(maxDistance, distance);
            }
        }
        return maxDistance;
    }
}
