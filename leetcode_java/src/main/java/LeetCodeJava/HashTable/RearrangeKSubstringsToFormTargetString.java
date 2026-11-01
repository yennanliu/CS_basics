package LeetCodeJava.HashTable;

// https://leetcode.com/problems/rearrange-k-substrings-to-form-target-string/description/

import java.util.*;

/**
 * 3365. Rearrange K Substrings to Form Target String
 * Medium
 *
 * You are given two strings s and t, both of which are anagrams of each other, and an integer k.
 *
 * Your task is to determine whether it is possible to split the string s into k equal-sized substrings, rearrange the substrings, and concatenate them in any order to create a new string equal to t.
 *
 * Return true if this is possible, otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: s = "abcd", t = "cdab", k = 2
 * Output: true
 * Explanation:
 * Split s into 2 substrings of length 2: ["ab", "cd"].
 * Rearranging these substrings as ["cd", "ab"], and connecting them results in "cdab", which matches t.
 *
 * Example 2:
 *
 * Input: s = "aabbcc", t = "bbaacc", k = 3
 * Output: true
 * Explanation:
 * Split s into 3 substrings of length 2: ["aa", "bb", "cc"].
 * Rearranging these substrings as ["bb", "aa", "cc"], and connecting them results in "bbaacc", which matches t.
 *
 * Example 3:
 *
 * Input: s = "aabbcc", t = "bbaacc", k = 2
 * Output: false
 * Explanation:
 * Split s into 2 substrings of length 3: ["aab", "bcc"].
 * These substrings cannot be rearranged to form t = "bbaacc", so the output is false.
 *
 *
 * Constraints:
 *
 * 1 <= s.length == t.length <= 2 * 10^5
 * 1 <= k <= s.length
 * s.length is divisible by k.
 * s and t consist only of lowercase English letters.
 *
 */
public class RearrangeKSubstringsToFormTargetString {

    // V0
    // IDEA: THE k BLOCKS ARE ATOMIC - COMPARE THE TWO MULTISETS OF BLOCKS
    //   the cut points are fixed and pieces are only reordered, so t is
    //   reachable exactly when cutting t at the same offsets yields the same
    //   multiset of pieces as s. count s's blocks up, t's blocks down.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPossibleToRearrange(String s, String t, int k) {
        int n = s.length();
        int size = n / k;
        Map<String, Integer> balance = new HashMap<>();

        for (int i = 0; i < n; i += size) {
            String blockS = s.substring(i, i + size);
            String blockT = t.substring(i, i + size);
            balance.put(blockS, balance.getOrDefault(blockS, 0) + 1);
            balance.put(blockT, balance.getOrDefault(blockT, 0) - 1);
        }

        // equal multisets leave every block's balance at zero
        for (int diff : balance.values()) {
            if (diff != 0) {
                return false;
            }
        }
        return true;
    }
}
