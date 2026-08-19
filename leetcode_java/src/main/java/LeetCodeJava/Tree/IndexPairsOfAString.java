package LeetCodeJava.Tree;

// https://leetcode.com/problems/index-pairs-of-a-string/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  1065. Index Pairs of a String
 *  Easy
 *
 *  Given a string text and an array of strings words, return an array of all
 *  index pairs [i, j] so that the substring text[i...j] is in words.
 *
 *  Return the pairs [i, j] in sorted order (i.e., sort them by their first
 *  coordinate, and in case of ties sort them by their second coordinate).
 *
 *  Example 1:
 *    Input: text = "thestoryofleetcodeandme", words = ["story","fleet","leetcode"]
 *    Output: [[3,7],[9,13],[10,17]]
 *
 *  Example 2:
 *    Input: text = "ababa", words = ["aba","ab"]
 *    Output: [[0,1],[0,2],[2,3],[2,4]]
 *    Explanation: matches can overlap, "aba" is found at [0,2] and [2,4].
 *
 *  Constraints:
 *    1 <= text.length <= 100
 *    1 <= words.length <= 20
 *    1 <= words[i].length <= 50
 *    text and words[i] consist of lowercase English letters.
 *    All the strings of words are unique.
 */
public class IndexPairsOfAString {

    private static class Trie {
        Map<Character, Trie> children = new HashMap<>();
        boolean isEnd = false;

        void insert(String word) {
            Trie node = this;
            for (char c : word.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    node.children.put(c, new Trie());
                }
                node = node.children.get(c);
            }
            node.isEnd = true;
        }
    }

    // V0
    // IDEA: TRIE
    //       build a trie of `words`, then for every start index i walk the trie
    //       along text[i:], recording [i, j] whenever we land on a word end.
    //       scanning i then j left -> right already yields the required sorted
    //       order, and we can break as soon as the prefix leaves the trie.
    /**
     * time = O(sum(len(w)) + N * L)   // N = text length, L = max word length
     * space = O(sum(len(w)))
     */
    public int[][] indexPairs(String text, String[] words) {
        Trie root = new Trie();
        for (String w : words) {
            root.insert(w);
        }

        int n = text.length();
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Trie node = root;
            for (int j = i; j < n; j++) {
                char c = text.charAt(j);
                if (!node.children.containsKey(c)) {
                    break;
                }
                node = node.children.get(c);
                if (node.isEnd) {
                    res.add(new int[]{i, j});
                }
            }
        }
        return res.toArray(new int[res.size()][]);
    }
}
