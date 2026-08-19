package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-palindrome/

import java.util.HashMap;
import java.util.Map;

/**
 *  409. Longest Palindrome
 *  Easy
 *
 *  Given a string s which consists of lowercase or uppercase letters,
 *  return the length of the longest palindrome that can be built with those letters.
 *
 *  Letters are case sensitive, for example, "Aa" is not considered a palindrome.
 *
 *  Example 1:
 *  Input: s = "abccccdd"
 *  Output: 7
 *  Explanation: One longest palindrome that can be built is "dccaccd", whose length is 7.
 *
 *  Example 2:
 *  Input: s = "a"
 *  Output: 1
 *
 *  Constraints:
 *  1 <= s.length <= 2000
 *  s consists of lowercase and/or uppercase English letters only.
 */
public class LongestPalindrome {

    // V0
    // IDEA: COUNT EACH CHAR, take all even parts, plus 1 if any odd count exists
    /**
     * time = O(n)
     * space = O(1)   // at most 52 distinct letters
     */
    public int longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Map<Character, Integer> cnt = new HashMap<>();
        for (char c : s.toCharArray()) {
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);
        }

        int res = 0;
        boolean hasOdd = false;
        for (int v : cnt.values()) {
            res += (v / 2) * 2;
            if (v % 2 == 1) {
                hasOdd = true;
            }
        }
        return hasOdd ? res + 1 : res;
    }

    // V1
    // IDEA: SET toggle - a char in set means it is currently "unpaired"
    /**
     * time = O(n)
     * space = O(1)
     */
    public int longestPalindrome_1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        java.util.Set<Character> set = new java.util.HashSet<>();
        int pairs = 0;
        for (char c : s.toCharArray()) {
            if (set.contains(c)) {
                set.remove(c);
                pairs++;
            } else {
                set.add(c);
            }
        }
        return pairs * 2 + (set.isEmpty() ? 0 : 1);
    }
}
