package LeetCodeJava.Greedy;

// https://leetcode.com/problems/count-number-of-homogenous-substrings/description/

/**
 * 1759. Count Number of Homogenous Substrings
 * Medium
 *
 * Given a string s, return the number of homogenous substrings of s. Since the
 * answer may be too large, return it modulo 10^9 + 7.
 *
 * A string is homogenous if all the characters of the string are the same.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "abbcccaa"
 * Output: 13
 * Explanation: The homogenous substrings are listed as below:
 * "a"   appears 3 times.
 * "aa"  appears 1 time.
 * "b"   appears 2 times.
 * "bb"  appears 1 time.
 * "c"   appears 3 times.
 * "cc"  appears 2 times.
 * "ccc" appears 1 time.
 * 3 + 1 + 2 + 1 + 3 + 2 + 1 = 13.
 *
 * Example 2:
 *
 * Input: s = "xy"
 * Output: 2
 * Explanation: The homogenous substrings are "x" and "y".
 *
 * Example 3:
 *
 * Input: s = "zzzzz"
 * Output: 15
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase letters.
 *
 */
public class CountNumberOfHomogenousSubstrings {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: GROUP CONSECUTIVE RUNS (a run of length L holds L * (L + 1) / 2 substrings)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countHomogenous(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        long result = 0;
        // length of the run of equal characters that ends at the current index
        int runLength = 0;
        char previous = 0;

        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (current == previous) {
                runLength++;
            } else {
                // a new run starts here
                runLength = 1;
                previous = current;
            }
            // the new character ends exactly `runLength` new homogenous substrings
            result = (result + runLength) % MOD;
        }

        return (int) result;
    }
}
