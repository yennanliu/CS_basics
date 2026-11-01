package LeetCodeJava.String;

// https://leetcode.com/problems/longest-word-with-all-prefixes/description/
// https://leetcode.ca/2020-12-31-1858-Longest-Word-With-All-Prefixes/

import java.util.*;

/**
 * 1858. Longest Word With All Prefixes
 * Medium
 *
 * Given an array of strings words, find the longest string in words such that every prefix of it is also in words.
 *
 * For example, let words = ["a", "app", "ap"]. The string "app" has prefixes "ap" and "a", all of which are in words.
 *
 * Return the string described above. If there is more than one string with the same length, return the lexicographically smallest one, and if no string exists, return "".
 *
 *
 * Example 1:
 *
 * Input: words = ["k","ki","kir","kira", "kiran"]
 * Output: "kiran"
 * Explanation: "kiran" has prefixes "kira", "kir", "ki", and "k", and all of them appear in words.
 *
 * Example 2:
 *
 * Input: words = ["a", "banana", "app", "appl", "ap", "apply", "apple"]
 * Output: "apple"
 * Explanation: Both "apple" and "apply" have all their prefixes in words.
 * However, "apple" is lexicographically smaller, so we return that.
 *
 * Example 3:
 *
 * Input: words = ["abc", "bc", "ab", "qwe"]
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 10^5
 * 1 <= words[i].length <= 10^5
 * 1 <= sum(words[i].length) <= 10^5
 * words[i] consists only of lowercase English letters.
 *
 */
public class LongestWordWithAllPrefixes {

    // V0
    // IDEA: SORT BY LENGTH + "GOOD" SET (INCREMENTAL DP)
    //       a word w has all its prefixes present iff len(w) == 1, or its parent
    //       w[0..len-2] is itself good (which already covers every shorter prefix).
    //       Processing words by increasing length decides each parent first.
    //       Ties: longest, then lexicographically smallest.
    /**
     * time = O(L + N log N)   (L = total length of all words)
     * space = O(L)
     */
    public String longestWord(String[] words) {
        String[] sorted = words.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a.length(), b.length()));

        Set<String> good = new HashSet<>();
        String best = "";
        for (String word : sorted) {
            boolean parentIsGood = word.length() == 1
                    || good.contains(word.substring(0, word.length() - 1));
            if (!parentIsGood) {
                continue;
            }
            good.add(word);
            boolean longer = word.length() > best.length();
            boolean sameLengthSmaller = word.length() == best.length() && word.compareTo(best) < 0;
            if (longer || sameLengthSmaller) {
                best = word;
            }
        }
        return best;
    }

    // V1
    // IDEA: TRIE (INSERT ALL WORDS, THEN CHECK EVERY PREFIX NODE IS A WORD END)
    //       a word qualifies iff every node on its path is marked as the end of a word.
    /**
     * time = O(L)
     * space = O(L)
     */
    public String longestWord_1(String[] words) {
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new TrieNode();
                }
                node = node.children[c - 'a'];
            }
            node.isWord = true;
        }

        String best = "";
        for (String word : words) {
            if (!allPrefixesAreWords(root, word)) {
                continue;
            }
            boolean longer = word.length() > best.length();
            boolean sameLengthSmaller = word.length() == best.length() && word.compareTo(best) < 0;
            if (longer || sameLengthSmaller) {
                best = word;
            }
        }
        return best;
    }

    /** True if every prefix of word (including word itself) is marked as a word in the trie. */
    private boolean allPrefixesAreWords(TrieNode root, String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null || !node.isWord) {
                return false;
            }
        }
        return true;
    }

    /** A trie node over lowercase letters. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isWord;
    }
}
