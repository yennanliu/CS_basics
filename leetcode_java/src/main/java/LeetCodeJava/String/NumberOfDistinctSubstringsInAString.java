package LeetCodeJava.String;

// https://leetcode.com/problems/number-of-distinct-substrings-in-a-string/description/
// https://leetcode.ca/2020-07-24-1698-Number-of-Distinct-Substrings-in-a-String/

import java.util.*;

/**
 * 1698. Number of Distinct Substrings in a String
 * Medium
 *
 * Given a string s, return the number of distinct substrings of s.
 *
 * A substring of a string is obtained by deleting any number of characters (possibly zero) from the
 * front of the string and any number (possibly zero) from the back of the string.
 *
 *
 * Example 1:
 *
 * Input: s = "aabbaba"
 * Output: 21
 * Explanation: The set of distinct strings is ["a","b","aa","bb","ab","ba","aab","abb","bab","bba",
 * "aba","aabb","abba","bbab","baba","aabba","abbab","bbaba","aabbab","abbaba","aabbaba"]
 *
 * Example 2:
 *
 * Input: s = "abcdefg"
 * Output: 28
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of lowercase English letters.
 *
 *
 * Follow up: Can you solve this problem in O(n) time complexity?
 *
 */
public class NumberOfDistinctSubstringsInAString {

    private static final int ALPHABET = 26;

    // V0
    // IDEA: SUFFIX AUTOMATON (ANSWERS THE O(N) FOLLOW-UP; V1's TRIE IS THE EASIER ONE TO SAY)
    //       each state v represents the substrings ending at the same set of
    //       positions, with lengths in (len[link[v]], len[v]]. So state v adds
    //       len[v] - len[link[v]] distinct substrings, and every non-empty
    //       substring is counted by exactly one state.
    //       The "clone" branch keeps the suffix links consistent when an existing
    //       transition is not a direct extension.
    /**
     * time = O(N * 26)
     * space = O(N * 26)
     */
    public int countDistinct(String s) {
        int maxStates = 2 * s.length() + 1;
        int[] length = new int[maxStates];
        int[] link = new int[maxStates];
        int[][] next = new int[maxStates][ALPHABET];
        for (int[] row : next) {
            Arrays.fill(row, -1);
        }

        // state 0 is the root (the empty string)
        link[0] = -1;
        int stateCount = 1;
        int last = 0;

        for (int idx = 0; idx < s.length(); idx++) {
            int letter = s.charAt(idx) - 'a';

            // the new state represents the whole prefix s[0..idx]
            int current = stateCount++;
            length[current] = length[last] + 1;

            // add the transition on every suffix of the old prefix that lacks it
            int p = last;
            while (p != -1 && next[p][letter] == -1) {
                next[p][letter] = current;
                p = link[p];
            }

            if (p == -1) {
                link[current] = 0;
            } else {
                int q = next[p][letter];
                if (length[p] + 1 == length[q]) {
                    // q is a direct extension of p: it is the suffix link
                    link[current] = q;
                } else {
                    // split q: the clone takes the shorter strings of q
                    int clone = stateCount++;
                    length[clone] = length[p] + 1;
                    link[clone] = link[q];
                    next[clone] = next[q].clone();
                    while (p != -1 && next[p][letter] == q) {
                        next[p][letter] = clone;
                        p = link[p];
                    }
                    link[q] = clone;
                    link[current] = clone;
                }
            }
            last = current;
        }

        int distinct = 0;
        for (int v = 1; v < stateCount; v++) {
            distinct += length[v] - length[link[v]];
        }
        return distinct;
    }

    // V1
    // IDEA: TRIE OF ALL SUFFIXES (EVERY NEW TRIE NODE IS A NEW DISTINCT SUBSTRING)
    //       every substring is a prefix of some suffix. Insert each suffix s[i..]
    //       into a trie; a path from the root spells a substring, so the number
    //       of nodes created (root excluded) is the number of distinct substrings.
    //       Easiest to explain in an interview; the suffix automaton (V0) is the
    //       O(N) follow-up.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int countDistinct_1(String s) {
        TrieNode root = new TrieNode();
        int distinct = 0;

        for (int start = 0; start < s.length(); start++) {
            TrieNode node = root;
            // walk / extend the path for s[start..end]
            for (int end = start; end < s.length(); end++) {
                int letter = s.charAt(end) - 'a';
                if (node.children[letter] == null) {
                    node.children[letter] = new TrieNode();
                    distinct++;
                }
                node = node.children[letter];
            }
        }
        return distinct;
    }

    /** A trie node over lowercase letters. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET];
    }
}
