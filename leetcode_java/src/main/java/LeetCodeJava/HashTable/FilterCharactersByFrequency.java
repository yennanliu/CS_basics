package LeetCodeJava.HashTable;

// https://leetcode.com/problems/filter-characters-by-frequency/description/
// https://leetcode.ca/2025-10-02-3662-Filter-Characters-by-Frequency/

/**
 * 3662. Filter Characters by Frequency
 * Easy
 *
 * You are given a string s consisting of lowercase English letters and an
 * integer k.
 *
 * Your task is to construct a new string that contains only those characters
 * from s which appear fewer than k times in the entire string. The order of
 * characters in the new string must be the same as their order in s.
 *
 * Return the resulting string. If no characters qualify, return an empty
 * string.
 *
 * Note: Every occurrence of a character that occurs fewer than k times is
 * kept.
 *
 *
 * Example 1:
 *
 * Input: s = "aadbbcccca", k = 3
 * Output: "dbb"
 * Explanation:
 * Character frequencies in s:
 * 'a' appears 3 times
 * 'd' appears 1 time
 * 'b' appears 2 times
 * 'c' appears 4 times
 * Only 'd' and 'b' appear fewer than 3 times. Preserving their order, the
 * result is "dbb".
 *
 * Example 2:
 *
 * Input: s = "xyz", k = 2
 * Output: "xyz"
 * Explanation:
 * All characters ('x', 'y', 'z') appear exactly once, which is fewer than 2.
 * Thus the whole string is returned.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s consists of lowercase English letters.
 * 1 <= k <= s.length
 *
 */
public class FilterCharactersByFrequency {

    // V0
    // IDEA: COUNT THEN FILTER (TWO PASSES)
    //   keeping a character depends on its GLOBAL frequency, unknowable while
    //   streaming left to right. pass 1 builds the counts, pass 2 walks s again
    //   and keeps every character seen fewer than k times, which preserves order.
    /**
     * time = O(N)
     * space = O(1)
     */
    public String filterCharacters(String s, int k) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (count[c - 'a'] < k) {
                result.append(c);
            }
        }
        return result.toString();
    }
}
