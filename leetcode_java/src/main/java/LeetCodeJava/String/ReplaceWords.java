package LeetCodeJava.String;

// https://leetcode.com/problems/replace-words/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 648. Replace Words
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * In English, we have a concept called root, which can be followed by some other word to form another longer word - let's call this word derivative. For example, when the root "help" is followed by the word "ful", we can form a derivative "helpful".
 *
 * Given a dictionary consisting of many roots and a sentence consisting of words separated by spaces, replace all the derivatives in the sentence with the root forming it. If a derivative can be replaced by more than one root, replace it with the root that has the shortest length.
 *
 * Return the sentence after the replacement.
 *
 *
 *
 * Example 1:
 *
 * Input: dictionary = ["cat","bat","rat"], sentence = "the cattle was rattled by the battery"
 * Output: "the cat was rat by the bat"
 * Example 2:
 *
 * Input: dictionary = ["a","b","c"], sentence = "aadsfasf absbs bbab cadsfafs"
 * Output: "a a b c"
 *
 *
 * Constraints:
 *
 * 1 <= dictionary.length <= 1000
 * 1 <= dictionary[i].length <= 100
 * dictionary[i] consists of only lower-case letters.
 * 1 <= sentence.length <= 106
 * sentence consists of only lower-case letters and spaces.
 * The number of words in sentence is in the range [1, 1000]
 * The length of each word in sentence is in the range [1, 1000]
 * Every two consecutive words in sentence will be separated by exactly one space.
 * sentence does not have leading or trailing spaces.
 *
 */
public class ReplaceWords {

    // V0
//    public String replaceWords(List<String> dictionary, String sentence) {
//
//    }

    // V1-1
    // IDEA: HASHSET
    // https://leetcode.com/problems/replace-words/editorial/
    public String replaceWords_1_1(List<String> dictionary, String sentence) {
        String[] wordArray = sentence.split(" ");
        Set<String> dictSet = new HashSet<>(dictionary);

        // Replace each word in sentence with the corresponding shortest root
        for (int i = 0; i < wordArray.length; i++) {
            wordArray[i] = shortestRoot(wordArray[i], dictSet);
        }

        return String.join(" ", wordArray);
    }

    private String shortestRoot(String word, Set<String> dictSet) {
        // Find the shortest root of the word in the dictionary
        for (int i = 1; i <= word.length(); i++) {
            String root = word.substring(0, i);
            if (dictSet.contains(root)) {
                return root;
            }
        }
        // There is not a corresponding root in the dictionary
        return word;
    }


    // V1-2
    // IDEA: PREFIX TRIE
    // https://leetcode.com/problems/replace-words/editorial/
    class TrieNode {

        boolean isEnd;
        TrieNode[] children;

        TrieNode() {
            isEnd = false;
            children = new TrieNode[26];
        }
    }

    class Trie {

        private TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                if (current.children[c - 'a'] == null) {
                    current.children[c - 'a'] = new TrieNode();
                }
                current = current.children[c - 'a'];
            }
            current.isEnd = true;
        }

        // Find the shortest root of the word in the trie
        public String shortestRoot(String word) {
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (current.children[c - 'a'] == null) {
                    // There is not a corresponding root in the trie
                    return word;
                }
                current = current.children[c - 'a'];
                if (current.isEnd) {
                    return word.substring(0, i + 1);
                }
            }
            // There is not a corresponding root in the trie
            return word;
        }
    }

    public String replaceWords_1_2(List<String> dictionary, String sentence) {
        String wordArray[] = sentence.split(" ");

        Trie dictTrie = new Trie();
        for (String word : dictionary) {
            dictTrie.insert(word);
        }

        // Replace each word in the sentence with the corresponding shortest root
        for (int word = 0; word < wordArray.length; word++) {
            wordArray[word] = dictTrie.shortestRoot(wordArray[word]);
        }

        return String.join(" ", wordArray);
    }

    // V2-1
    // IDEA: TRIE
    // https://leetcode.ca/2017-09-08-648-Replace-Words/
    class Trie_2_1 {
        private Trie_2_1[] children = new Trie_2_1[26];
        private int ref = -1;

        public void insert(String w, int i) {
            Trie_2_1 node = this;
            for (int j = 0; j < w.length(); ++j) {
                int idx = w.charAt(j) - 'a';
                if (node.children[idx] == null) {
                    node.children[idx] = new Trie_2_1();
                }
                node = node.children[idx];
            }
            node.ref = i;
        }

        public int search(String w) {
            Trie_2_1 node = this;
            for (int j = 0; j < w.length(); ++j) {
                int idx = w.charAt(j) - 'a';
                if (node.children[idx] == null) {
                    return -1;
                }
                node = node.children[idx];
                if (node.ref != -1) {
                    return node.ref;
                }
            }
            return -1;
        }
    }

    public String replaceWords_2_1(List<String> dictionary, String sentence) {
        Trie_2_1 trie = new Trie_2_1();
        for (int i = 0; i < dictionary.size(); ++i) {
            trie.insert(dictionary.get(i), i);
        }
        List<String> ans = new ArrayList<>();
        for (String w : sentence.split("\\s")) {
            int idx = trie.search(w);
            ans.add(idx == -1 ? w : dictionary.get(idx));
        }
        return String.join(" ", ans);
    }


    // V3


}
