package LeetCodeJava.Trie;

// https://leetcode.com/problems/design-add-and-search-words-data-structure/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DesignAddAndSearchWordsDataStructure {

    // V0
    // IDEA : TRIE
    class TrieNode {
        Map<String, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        public TrieNode() {}
    }

    class WordDictionary {
        TrieNode trie;

        /** Initialize your data structure here. */
        public WordDictionary() {
            trie = new TrieNode();
        }

        /** Adds a word into the data structure. */
        public void addWord(String word) {

            TrieNode node = trie;

            for (char ch : word.toCharArray()) {
                String key = String.valueOf(ch);
                if (!node.children.containsKey(key)) {
                    node.children.put(key, new TrieNode());
                }
                node = node.children.get(key);
            }
            node.isEnd = true;
        }

        /** Returns if the word is in the node. */
        public boolean searchInNode(String word, TrieNode node) {

            for (int i = 0; i < word.length(); ++i) {
                char ch = word.charAt(i);
                String key = String.valueOf(ch);
                /** CASE 1 : node has NO such child */
                if (!node.children.containsKey(key)) {
                    // if the current character is '.'
                    // check all possible nodes at this level
                    /** CASE 1-1 : element is "." */
                    if (ch == '.') {
                        for (String x : node.children.keySet()) {
                            TrieNode child = node.children.get(x);
                            /** NOTE !!!
                             *  -> if ".", we HAVE to go through all nodes in next levels
                             *  -> and check if any of them is valid
                             *  -> so we need to RECURSIVELY call searchInNode method with "i+1" sub string
                             */

                            /** if can find remain elements in word */
                            if (searchInNode(word.substring(i + 1), child)) {
                                return true;
                            }
                        }
                    }
                    /** if can NOT find remain elements in word */
                    // if no nodes lead to answer
                    // or the current character != '.'
                    return false;
                /** CASE 2 : node has such child */
                } else {
                    // if the character is found
                    // go down to the next level in trie
                    node = node.children.get(key);
                }
            }
            // check if is "end", so to-search word is 100% match word Trie
            return node.isEnd;
        }

        /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
        public boolean search(String word) {
            return searchInNode(word, trie);
        }

    }

    // V1
    // IDEA : TRIE
    // https://leetcode.com/problems/design-add-and-search-words-data-structure/editorial/
    class TrieNode2 {
        Map<Character, TrieNode2> children = new HashMap();
        boolean word = false;
        public TrieNode2() {}
    }

    class WordDictionary2 {
        TrieNode2 trie;

        /** Initialize your data structure here. */
        public WordDictionary2() {
            trie = new TrieNode2();
        }

        /** Adds a word into the data structure. */
        public void addWord(String word) {
            TrieNode2 node = trie;

            for (char ch : word.toCharArray()) {
                if (!node.children.containsKey(ch)) {
                    node.children.put(ch, new TrieNode2());
                }
                node = node.children.get(ch);
            }
            node.word = true;
        }

        /** Returns if the word is in the node. */
        public boolean searchInNode(String word, TrieNode2 node) {
            for (int i = 0; i < word.length(); ++i) {
                char ch = word.charAt(i);
                if (!node.children.containsKey(ch)) {
                    // if the current character is '.'
                    // check all possible nodes at this level
                    if (ch == '.') {
                        for (char x : node.children.keySet()) {
                            TrieNode2 child = node.children.get(x);
                            /** NOTE !!!
                             *  -> if ".", we HAVE to go through all nodes in next levels
                             *  -> and check if any of them is valid
                             *  -> so we need to RECURSIVELY call searchInNode method with "i+1" sub string
                             */
                            if (searchInNode(word.substring(i + 1), child)) {
                                return true;
                            }
                        }
                    }
                    // if no nodes lead to answer
                    // or the current character != '.'
                    return false;
                } else {
                    // if the character is found
                    // go down to the next level in trie
                    node = node.children.get(ch);
                }
            }
            return node.word;
        }

        /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
        public boolean search(String word) {
            return searchInNode(word, trie);
        }
    }

    // V2
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/design-add-and-search-words-data-structure/editorial/
    class WordDictionary4 {
        Map<Integer, Set<String>> d;

        /** Initialize your data structure here. */
        public WordDictionary4() {
            d = new HashMap();
        }

        /** Adds a word into the data structure. */
        public void addWord(String word) {
            int m = word.length();
            if (!d.containsKey(m)) {
                d.put(m, new HashSet());
            }
            d.get(m).add(word);
        }

        /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
        public boolean search(String word) {
            int m = word.length();
            if (d.containsKey(m)) {
                for (String w : d.get(m)) {
                    int i = 0;
                    while ((i < m) && (w.charAt(i) == word.charAt(i) || word.charAt(i) == '.')) {
                        i++;
                    }
                    if (i == m) return true;
                }
            }
            return false;
        }
    }

}
