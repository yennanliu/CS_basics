package LeetCodeJava.HashTable;

// https://leetcode.com/problems/minimum-number-of-steps-to-make-two-strings-anagram/description/

/**
 * 1347. Minimum Number of Steps to Make Two Strings Anagram
 * Medium
 *
 * You are given two strings of the same length s and t.
 * In one step you can choose any character of t and replace it with
 * another character.
 *
 * Return the minimum number of steps to make t an anagram of s.
 *
 * An Anagram of a string is a string that contains the same characters with a
 * different (or the same) ordering.
 *
 *
 * Example 1:
 *
 * Input: s = "bab", t = "aba"
 * Output: 1
 * Explanation: Replace the first 'a' in t with b, t = "bba" which is anagram of s.
 *
 * Example 2:
 *
 * Input: s = "leetcode", t = "practice"
 * Output: 5
 * Explanation: Replace 'p', 'r', 'a', 'i' and 'c' from t with proper characters
 * to make t anagram of s.
 *
 * Example 3:
 *
 * Input: s = "anagram", t = "mangaar"
 * Output: 0
 * Explanation: "anagram" and "mangaar" are anagrams.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 5 * 10^4
 * s.length == t.length
 * s and t consist of lowercase English letters only.
 *
 */
public class MinimumNumberOfStepsToMakeTwoStringsAnagram {

    // V0
    // IDEA: COUNT THE SURPLUS CHARS IN t
    //       s and t have the same length, so every char t has too many of is replaced by
    //       exactly one char t is missing -> answer = total surplus in t.
    /**
     * time = O(N)
     * space = O(1)   (26 counters)
     */
    public int minSteps(String s, String t) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        int steps = 0;
        for (char c : t.toCharArray()) {
            count[c - 'a']--;
            // this c is not needed by s -> it has to be replaced
            if (count[c - 'a'] < 0) {
                steps++;
            }
        }
        return steps;
    }
}
