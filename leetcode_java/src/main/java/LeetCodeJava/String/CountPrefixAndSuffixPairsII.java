package LeetCodeJava.String;

// https://leetcode.com/problems/count-prefix-and-suffix-pairs-ii/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3045. Count Prefix and Suffix Pairs II
 * Hard
 *
 * You are given a 0-indexed string array words.
 *
 * Let's define a boolean function isPrefixAndSuffix that takes two strings, str1 and str2:
 *
 * isPrefixAndSuffix(str1, str2) returns true if str1 is both a prefix and a suffix of str2, and false otherwise.
 *
 * For example, isPrefixAndSuffix("aba", "ababa") is true because "aba" is a prefix of "ababa" and also a suffix, but isPrefixAndSuffix("abc", "abcd") is false.
 *
 * Return an integer denoting the number of index pairs (i, j) such that i < j, and isPrefixAndSuffix(words[i], words[j]) is true.
 *
 *
 * Example 1:
 *
 * Input: words = ["a","aba","ababa","aa"]
 * Output: 4
 * Explanation: In this example, the counted index pairs are:
 * i = 0 and j = 1 because isPrefixAndSuffix("a", "aba") is true.
 * i = 0 and j = 2 because isPrefixAndSuffix("a", "ababa") is true.
 * i = 0 and j = 3 because isPrefixAndSuffix("a", "aa") is true.
 * i = 1 and j = 2 because isPrefixAndSuffix("aba", "ababa") is true.
 * Therefore, the answer is 4.
 *
 * Example 2:
 *
 * Input: words = ["pa","papa","ma","mama"]
 * Output: 2
 * Explanation: In this example, the counted index pairs are:
 * i = 0 and j = 1 because isPrefixAndSuffix("pa", "papa") is true.
 * i = 2 and j = 3 because isPrefixAndSuffix("ma", "mama") is true.
 * Therefore, the answer is 2.
 *
 * Example 3:
 *
 * Input: words = ["abab","ab"]
 * Output: 0
 * Explanation: In this example, the only valid index pair is i = 0 and j = 1, and isPrefixAndSuffix("abab", "ab") is false.
 * Therefore, the answer is 0.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 10^5
 * 1 <= words[i].length <= 10^5
 * words[i] consists only of lowercase English letters.
 * The sum of the lengths of all words[i] does not exceed 5 * 10^5.
 *
 */
public class CountPrefixAndSuffixPairsII {

    // V0
    // IDEA: TRIE KEYED ON PAIRS (k-th CHAR FROM THE FRONT, k-th FROM THE BACK)
    //       w is a prefix AND a suffix of t  <=>  the pair sequence
    //       (t[0], t[L-1]), (t[1], t[L-2]), ... starts with w's own |w| pairs.
    //       So insert each word's pair sequence into a trie; before a word is
    //       inserted, the end-of-word counters along its path count the
    //       earlier words that qualify (processing left to right gives i < j).
    /**
     * time = O(S)   S = sum of word lengths
     * space = O(S)
     */
    public long countPrefixSuffixPairs(String[] words) {
        PairTrieNode root = new PairTrieNode();
        long pairs = 0;

        for (String word : words) {
            int length = word.length();
            PairTrieNode node = root;
            for (int k = 0; k < length; k++) {
                // encode (front char, back char) as one int key
                int key = word.charAt(k) * 128 + word.charAt(length - 1 - k);
                PairTrieNode child = node.children.get(key);
                if (child == null) {
                    child = new PairTrieNode();
                    node.children.put(key, child);
                }
                node = child;
                // every earlier word ending here is a prefix and a suffix of this one
                pairs += node.wordsEndingHere;
            }
            node.wordsEndingHere++;
        }
        return pairs;
    }

    /** Trie node whose edges are (front char, back char) pairs. */
    private static class PairTrieNode {
        Map<Integer, PairTrieNode> children = new HashMap<>();
        int wordsEndingHere = 0;
    }
}
