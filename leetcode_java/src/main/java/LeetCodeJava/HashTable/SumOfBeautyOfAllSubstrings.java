package LeetCodeJava.HashTable;

// https://leetcode.com/problems/sum-of-beauty-of-all-substrings/description/

/**
 * 1781. Sum of Beauty of All Substrings
 * Medium
 *
 * The beauty of a string is the difference in frequencies between the most frequent and least
 * frequent characters.
 *
 * For example, the beauty of "abaacc" is 3 - 1 = 2.
 *
 * Given a string s, return the sum of beauty of all of its substrings.
 *
 *
 * Example 1:
 *
 * Input: s = "aabcb"
 * Output: 5
 * Explanation: The substrings with non-zero beauty are ["aab","aabc","aabcb","abcb","bcb"], each with beauty equal to 1.
 *
 * Example 2:
 *
 * Input: s = "aabcbaa"
 * Output: 17
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of only lowercase English letters.
 *
 */
public class SumOfBeautyOfAllSubstrings {

    // V0
    // IDEA: FIX THE LEFT END, GROW THE RIGHT END, KEEP A 26-SLOT COUNTER UPDATED INCREMENTALLY
    /**
     * time = O(N^2 * 26)
     * space = O(1)
     */
    public int beautySum(String s) {
        int n = s.length();
        int total = 0;

        for (int start = 0; start < n; start++) {
            // counts for the substring s[start..end], extended one character at a time
            int[] count = new int[26];
            for (int end = start; end < n; end++) {
                count[s.charAt(end) - 'a']++;

                int most = 0;
                int least = Integer.MAX_VALUE;
                for (int freq : count) {
                    // letters absent from the substring must not count as the minimum
                    if (freq == 0) {
                        continue;
                    }
                    most = Math.max(most, freq);
                    least = Math.min(least, freq);
                }
                total += most - least;
            }
        }
        return total;
    }
}
