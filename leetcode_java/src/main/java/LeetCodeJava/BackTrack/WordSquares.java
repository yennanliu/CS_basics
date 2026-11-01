package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-squares/description/
// https://leetcode.ca/2017-01-28-425-Word-Squares/

import java.util.*;

/**
 * 425. Word Squares
 * Hard
 *
 * Given an array of unique strings words, return all the word squares you can build
 * from words. The same word from words can be used multiple times. You can return
 * the answer in any order.
 *
 * A sequence of strings forms a valid word square if the kth row and column read the
 * same string, where 0 <= k < max(numRows, numColumns).
 *
 * For example, the word sequence ["ball","area","lead","lady"] forms a word square
 * because each word reads the same both horizontally and vertically.
 *
 *
 * Example 1:
 *
 * Input: words = ["area","lead","wall","lady","ball"]
 * Output: [["ball","area","lead","lady"],["wall","area","lead","lady"]]
 * Explanation:
 * The output consists of two word squares. The order of output does not matter
 * (just the order of words in each word square matters).
 *
 * Example 2:
 *
 * Input: words = ["abat","baba","atan","atal"]
 * Output: [["baba","abat","baba","atal"],["baba","abat","baba","atan"]]
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 1000
 * 1 <= words[i].length <= 4
 * All words[i] have the same length.
 * words[i] consists of only lowercase English letters.
 * All words[i] are unique.
 *
 */
public class WordSquares {

    // V0
    // IDEA: PREFIX MAP + BACKTRACKING (ROW k IS FORCED TO MATCH COLUMN k)
    //
    //   Once rows 0..k-1 are placed, row k is not free: reading down column k of the
    //   rows already placed spells the prefix row k must start with.
    //
    //     ["ball", "area"]  ->  row 2 must start with  b[2] a[2] = "le"
    //
    //   A map prefix -> words, built once, turns that lookup into O(1).
    //   NOTE: the map includes the empty prefix "" (-> all words), so row 0 is picked
    //         by the same lookup as every other row.
    /**
     * time = O(M * 26^L * L)
     * space = O(M * L)
     */
    public List<List<String>> wordSquares(String[] words) {
        List<List<String>> result = new ArrayList<>();
        // edge case: no words, no squares
        if (words == null || words.length == 0) {
            return result;
        }

        int wordLength = words[0].length();

        // every prefix (including "") -> the words that start with it
        Map<String, List<String>> wordsByPrefix = new HashMap<>();
        for (String word : words) {
            for (int len = 0; len <= wordLength; len++) {
                String prefix = word.substring(0, len);
                wordsByPrefix.computeIfAbsent(prefix, key -> new ArrayList<>()).add(word);
            }
        }

        backtrack(new ArrayList<>(), wordLength, wordsByPrefix, result);
        return result;
    }

    /** Fills row square.size(), trying only words with the forced prefix. */
    private void backtrack(List<String> square, int wordLength,
                           Map<String, List<String>> wordsByPrefix, List<List<String>> result) {
        int row = square.size();
        if (row == wordLength) {
            result.add(new ArrayList<>(square));
            return;
        }

        // column `row` of the rows placed so far == the prefix this row needs
        StringBuilder needed = new StringBuilder();
        for (String placed : square) {
            needed.append(placed.charAt(row));
        }

        List<String> candidates = wordsByPrefix.getOrDefault(needed.toString(), new ArrayList<>());
        for (String candidate : candidates) {
            square.add(candidate);
            backtrack(square, wordLength, wordsByPrefix, result);
            square.remove(square.size() - 1);
        }
    }

    // V1
    // IDEA: TRIE + BACKTRACKING
    //
    //   Same search as V0, but the prefix lookup walks a trie whose every node keeps
    //   the words passing through it. Uses O(M * L) nodes instead of O(M * L) prefix
    //   strings; the classic answer when an interviewer asks "can you do it with a trie?".
    /**
     * time = O(M * 26^L * L)
     * space = O(M * L)
     */
    public List<List<String>> wordSquares_1(String[] words) {
        List<List<String>> result = new ArrayList<>();
        if (words == null || words.length == 0) {
            return result;
        }

        int wordLength = words[0].length();

        // build the trie; each node remembers which words pass through it
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode node = root;
            node.wordsWithPrefix.add(word);
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
                node.wordsWithPrefix.add(word);
            }
        }

        backtrackWithTrie(new ArrayList<>(), wordLength, root, result);
        return result;
    }

    /** One trie node: 26 children plus the words whose prefix ends here. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        List<String> wordsWithPrefix = new ArrayList<>();
    }

    /** Fills the next row with every word the trie says carries the forced prefix. */
    private void backtrackWithTrie(List<String> square, int wordLength, TrieNode root,
                                   List<List<String>> result) {
        int row = square.size();
        if (row == wordLength) {
            result.add(new ArrayList<>(square));
            return;
        }

        // walk the trie along column `row` of the rows placed so far
        TrieNode node = root;
        for (String placed : square) {
            int index = placed.charAt(row) - 'a';
            node = node.children[index];
            if (node == null) {
                // no word has this prefix: dead end
                return;
            }
        }

        for (String candidate : node.wordsWithPrefix) {
            square.add(candidate);
            backtrackWithTrie(square, wordLength, root, result);
            square.remove(square.size() - 1);
        }
    }
}
