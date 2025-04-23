package LeetCodeJava.Trie;

// https://leetcode.com/problems/design-add-and-search-words-data-structure/
/**
 *  211. Design Add and Search Words Data Structure
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Design a data structure that supports adding new words and finding if a string matches any previously added string.
 *
 * Implement the WordDictionary class:
 *
 * WordDictionary() Initializes the object.
 * void addWord(word) Adds word to the data structure, it can be matched later.
 * bool search(word) Returns true if there is any string in the data structure that matches word or false otherwise. word may contain dots '.' where dots can be matched with any letter.
 *
 *
 * Example:
 *
 * Input
 * ["WordDictionary","addWord","addWord","addWord","search","search","search","search"]
 * [[],["bad"],["dad"],["mad"],["pad"],["bad"],[".ad"],["b.."]]
 * Output
 * [null,null,null,null,false,true,true,true]
 *
 * Explanation
 * WordDictionary wordDictionary = new WordDictionary();
 * wordDictionary.addWord("bad");
 * wordDictionary.addWord("dad");
 * wordDictionary.addWord("mad");
 * wordDictionary.search("pad"); // return False
 * wordDictionary.search("bad"); // return True
 * wordDictionary.search(".ad"); // return True
 * wordDictionary.search("b.."); // return True
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 25
 * word in addWord consists of lowercase English letters.
 * word in search consist of '.' or lowercase English letters.
 * There will be at most 2 dots in word for search queries.
 * At most 104 calls will be made to addWord and search.
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DesignAddAndSearchWordsDataStructure {

    // V0
    // IDEA : TRIE (LC 208) + RECURSION + SUB STRING
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

            /**
             *  NOTE !!!
             *
             *   we loop over `idx` of word
             *
             *   (instead of word)
             *
             *   -> then we know how to sub string the string (via idx)
             *
             */
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
                            /**
                             *
                             *  NOTE !!!!
                             *
                             *   Reason why can't use  if(!this.search(_word)){return false} ....
                             *
                             *
                             * ```java
                             * if (!this.search(_word)) {
                             *     return false;
                             * }
                             * this.search(_word); // ???
                             * ```
                             *
                             * …not functionally the same as just this:
                             *
                             * ```java
                             * if (this.search(_word)) {
                             *     return true;
                             * }
                             * ```
                             *
                             *   ### 🔍 What’s the actual difference?
                             *
                             * Let’s walk through it step-by-step:
                             *
                             * -------------------------------------------------
                             * ## 🔁 Original block:
                             * -------------------------------------------------
                             *
                             * ```java
                             * if (!this.search(_word)) {
                             *     return false;
                             * }
                             * this.search(_word); // ????
                             * ```
                             *
                             *   ### What happens?
                             *
                             * - You call `this.search(_word)`.
                             * - If it's **false**, you `return false` immediately.
                             * - But if it’s **true**, you… call it again?
                             *     ➤ That second call is **completely unnecessary**, and **does nothing** — it doesn’t store, return, or use the result.
                             *
                             * ### ❌ Problems:
                             * - It repeats work.
                             * - It adds cognitive overhead.
                             * - It suggests a misunderstanding of how `if` statements + recursion should flow.
                             *
                             *
                             *
                             * -------------------------------------------------
                             *  ## ✅ Correct logic (used in your current code):
                             * -------------------------------------------------
                             *
                             *
                             *   if (searchInNode(word.substring(i + 1), child)) {
                             *                                 return true;
                             *     }
                             *
                             * ### What happens?
                             *
                             * - You call `search(_word)`.
                             * - If **any path** from the wildcard returns `true`, you short-circuit and return early.
                             * - If **none** match, loop continues or returns `false` after the loop.
                             *
                             * This is the **correct backtracking pattern** for wildcard matching
                             * like in regex or trie DFS.
                             *
                             *
                             *  ### 💡 Summary Table:
                             *
                             * | Version | Behavior | Outcome |
                             * |--------|----------|---------|
                             * | `if (!search()) return false; search();` | Redundant extra call | Inefficient, unclear |
                             * | `if (search()) return true;` | Early exit on match | ✅ Correct backtracking |
                             * | `search();` alone | No effect unless used | ❌ Ineffective |
                             *
                             *
                             *
                             *
                             * ### 🛠 Recommendation:
                             *
                             * Always **return early** inside wildcard DFS-style logic when a path matches.
                             * Avoid calling `search()` multiple times on the same substring
                             * unless you're branching or exploring alternative states.
                             */
                            /**
                             *  NOTE !!!
                             *
                             *   we get sub string of word via `word.substring(i + 1)`
                             *
                             */
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