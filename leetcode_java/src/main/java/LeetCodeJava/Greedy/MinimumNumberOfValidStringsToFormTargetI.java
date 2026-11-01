package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-valid-strings-to-form-target-i/description/

import java.util.*;

/**
 * 3291. Minimum Number of Valid Strings to Form Target I
 * Medium
 *
 * You are given an array of strings words and a string target.
 *
 * A string x is called valid if x is a prefix of any string in words.
 *
 * Return the minimum number of valid strings that can be concatenated to form target. If it is not possible to form target, return -1.
 *
 *
 * Example 1:
 *
 * Input: words = ["abc","aaaaa","bcdef"], target = "aabcdabc"
 * Output: 3
 * Explanation:
 * The target string can be formed by concatenating:
 * Prefix of length 2 of words[1], i.e. "aa".
 * Prefix of length 3 of words[2], i.e. "bcd".
 * Prefix of length 3 of words[0], i.e. "abc".
 *
 * Example 2:
 *
 * Input: words = ["abababab","ab"], target = "ababaababa"
 * Output: 2
 * Explanation:
 * The target string can be formed by concatenating:
 * Prefix of length 5 of words[0], i.e. "ababa".
 * Prefix of length 5 of words[0], i.e. "ababa".
 *
 * Example 3:
 *
 * Input: words = ["abcdef"], target = "xyz"
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 5 * 10^3
 * The input is generated such that sum(words[i].length) <= 10^5.
 * words[i] consists only of lowercase English letters.
 * 1 <= target.length <= 5 * 10^3
 * target consists only of lowercase English letters.
 *
 */
public class MinimumNumberOfValidStringsToFormTargetI {

    // V0
    // IDEA: TRIE FOR LONGEST REACH PER START + JUMP GAME II GREEDY
    //       if a prefix of length L matches target at i, every shorter prefix does too,
    //       so only reach[i] = i + longest matching word-prefix at i matters
    //       (walk the trie from target[i] until it runs out).
    //       then it is exactly Jump Game II: extend the frontier with the farthest
    //       reach seen, counting one piece each time the frontier advances.
    /**
     * time = O(T * L + W)   T = target length, L = max word length, W = total word length
     * space = O(W)
     */
    public int minValidStrings(String[] words, String target) {
        int n = target.length();

        // build the trie of all words
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                int c = word.charAt(i) - 'a';
                if (node.children[c] == null) {
                    node.children[c] = new TrieNode();
                }
                node = node.children[c];
            }
        }

        // reach[i] = exclusive end of the longest valid piece starting at i
        int[] reach = new int[n];
        for (int i = 0; i < n; i++) {
            TrieNode node = root;
            int j = i;
            while (j < n && node.children[target.charAt(j) - 'a'] != null) {
                node = node.children[target.charAt(j) - 'a'];
                j++;
            }
            reach[i] = j;
        }

        // Jump Game II: pieces needed to cover [0, n)
        int pieces = 0;
        int currentEnd = 0; // target[0 .. currentEnd) is covered with `pieces` pieces
        int farthest = 0;
        int i = 0;
        while (currentEnd < n) {
            // any piece can start inside the covered region (including exactly at its end)
            while (i < n && i <= currentEnd) {
                farthest = Math.max(farthest, reach[i]);
                i++;
            }
            if (farthest <= currentEnd) {
                return -1; // stuck: no piece starts at currentEnd
            }
            currentEnd = farthest;
            pieces++;
        }
        return pieces;
    }

    /** Trie node over lowercase letters. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
    }
}
