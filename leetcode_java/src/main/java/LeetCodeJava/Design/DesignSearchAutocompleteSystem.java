package LeetCodeJava.Design;

// https://leetcode.com/problems/design-search-autocomplete-system/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  642. Design Search Autocomplete System
 *  Hard
 *
 *  Design a search autocomplete system for a search engine. Users may input a sentence
 *  (at least one word and ends with a special character '#').
 *
 *  For each character they type (except '#'), return the top 3 historical hot sentences
 *  that have the same prefix as the part of the sentence already typed:
 *   - The hot degree of a sentence is the number of times a user typed exactly it before.
 *   - Ties are broken by ASCII order (smaller first).
 *   - Fewer than 3 matches -> return as many as exist.
 *   - On '#', the typed sentence is recorded and an empty list is returned.
 *
 *  Implement the AutocompleteSystem class:
 *   - AutocompleteSystem(String[] sentences, int[] times) initializes with history.
 *   - List<String> input(char c) processes the next typed character.
 *
 *  Example:
 *    AutocompleteSystem(["i love you","island","ironman","i love leetcode"], [5,3,2,2])
 *    input('i') -> ["i love you", "island", "i love leetcode"]
 *    input(' ') -> ["i love you", "i love leetcode"]
 *    input('a') -> []
 *    input('#') -> []   // "i a" is now stored with hot degree 1
 *
 *  Constraints:
 *    n == sentences.length == times.length, 1 <= n <= 100
 *    1 <= sentences[i].length <= 100
 *    sentences[i] consists of lowercase letters, '#' and spaces
 *    1 <= times[i] <= 50
 *    c is a lowercase letter, a space, or '#'
 *    At most 5000 calls will be made to input.
 */
public class DesignSearchAutocompleteSystem {

    // V0
    // IDEA: TRIE where every node caches the SET of sentences passing through it.
    //       Typing keeps a pointer to the current trie node, so `input` only has to
    //       sort that node's (small) candidate list by (-hot, lexicographic).
    //       A null node pointer means the prefix died -> keep returning [].
    /**
     * time = O(L) per constructor sentence, O(p log p) per input (p = matching sentences)
     * space = O(total sentence length)
     */
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> sentences = new ArrayList<>();
    }

    private final TrieNode root;
    private final Map<String, Integer> hot;
    private TrieNode cur;
    private StringBuilder buffer;

    public DesignSearchAutocompleteSystem(String[] sentences, int[] times) {
        this.root = new TrieNode();
        this.hot = new HashMap<>();
        this.cur = this.root;
        this.buffer = new StringBuilder();

        for (int i = 0; i < sentences.length; i++) {
            this.hot.put(sentences[i], times[i]);
            addSentence(sentences[i]);
        }
    }

    /**
     * time = O(p log p), p = sentences sharing the current prefix
     * space = O(p)
     */
    public List<String> input(char c) {

        List<String> res = new ArrayList<>();

        if (c == '#') {
            String typed = this.buffer.toString();
            Integer old = this.hot.get(typed);
            if (old == null) {
                this.hot.put(typed, 1);
                addSentence(typed);
            } else {
                this.hot.put(typed, old + 1);
            }
            this.buffer = new StringBuilder();
            this.cur = this.root;
            return res;
        }

        this.buffer.append(c);
        if (this.cur != null) {
            this.cur = this.cur.children.get(c);
        }
        if (this.cur == null) {
            return res;
        }

        List<String> candidates = new ArrayList<>(this.cur.sentences);
        Collections.sort(candidates, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                int ha = hot.get(a);
                int hb = hot.get(b);
                if (ha != hb) {
                    return hb - ha; // hotter first
                }
                return a.compareTo(b); // then ASCII order
            }
        });

        for (int i = 0; i < candidates.size() && i < 3; i++) {
            res.add(candidates.get(i));
        }
        return res;
    }

    private void addSentence(String sentence) {
        TrieNode node = this.root;
        for (int i = 0; i < sentence.length(); i++) {
            char ch = sentence.charAt(i);
            TrieNode child = node.children.get(ch);
            if (child == null) {
                child = new TrieNode();
                node.children.put(ch, child);
            }
            node = child;
            node.sentences.add(sentence);
        }
    }
}
