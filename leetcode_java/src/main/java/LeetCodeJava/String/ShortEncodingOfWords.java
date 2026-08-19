package LeetCodeJava.String;

// https://leetcode.com/problems/short-encoding-of-words/

import java.util.HashSet;
import java.util.Set;

/**
 *  820. Short Encoding of Words
 *  Medium
 *
 *  A valid encoding of an array of words is any reference string s and array of
 *  indices such that:
 *    - words.length == indices.length
 *    - s ends with the '#' character
 *    - for each index, the substring of s starting from indices[i] and up to
 *      (but not including) the next '#' character is equal to words[i]
 *  Given an array of words, return the length of the shortest reference string s
 *  possible of any valid encoding of words.
 *
 *  Example 1:
 *    Input:  words = ["time","me","bell"]
 *    Output: 10
 *    (s = "time#bell#", indices = [0, 2, 5])
 *
 *  Example 2:
 *    Input:  words = ["t"]
 *    Output: 2
 *
 *  Constraints:
 *    1 <= words.length <= 2000
 *    1 <= words[i].length <= 7
 *    words[i] consists of only lowercase letters.
 */
public class ShortEncodingOfWords {

    // V0
    // IDEA: a word is free iff it is a proper suffix of another word; keep the
    //       set of words and delete every proper suffix of every word
    /**
     * time = O(sum of word_len^2)
     * space = O(sum of word_len)
     */
    public int minimumLengthEncoding(String[] words) {
        Set<String> set = new HashSet<>();
        for (String w : words) {
            set.add(w);
        }
        for (String w : words) {
            for (int i = 1; i < w.length(); i++) {
                set.remove(w.substring(i));
            }
        }
        int res = 0;
        for (String w : set) {
            res += w.length() + 1;
        }
        return res;
    }

    // V1
    // IDEA: trie over the reversed words — only leaf nodes contribute (depth + 1)
    /**
     * time = O(sum of word_len)
     * space = O(number of trie nodes)
     */
    public int minimumLengthEncoding_1(String[] words) {
        TrieNode root = new TrieNode();
        Set<String> uniq = new HashSet<>();
        for (String w : words) {
            uniq.add(w);
        }

        int res = 0;
        for (String w : uniq) {
            TrieNode node = root;
            for (int i = w.length() - 1; i >= 0; i--) {
                int idx = w.charAt(i) - 'a';
                if (node.children[idx] == null) {
                    node.children[idx] = new TrieNode();
                }
                node = node.children[idx];
            }
            node.word = w;
        }

        res = countLeaves(root);
        return res;
    }

    private int countLeaves(TrieNode node) {
        boolean leaf = true;
        int sum = 0;
        for (TrieNode child : node.children) {
            if (child != null) {
                leaf = false;
                sum += countLeaves(child);
            }
        }
        if (leaf) {
            return node.word == null ? 0 : node.word.length() + 1;
        }
        return sum;
    }

    public static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word = null;
    }
}
