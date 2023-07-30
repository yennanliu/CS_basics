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
    // TODO: fix search method (with "." case, use recursion)
//    class myNode{
//
//        // attr
//        HashMap<String, myNode> child;
//        Boolean isEnd;
//
//        // constructor
//        myNode(){
//            HashMap<String, myNode> child = new HashMap<>();
//            Boolean isEnd = false;
//        }
//
//    }
//
//    class WordDictionary {
//
//        myNode node;
//
//        public WordDictionary() {
//
//            this.node = new myNode();
//        }
//
//        public void addWord(String word) {
//
//            char[] wordArray = word.toCharArray();
//            for (char x: wordArray){
//                String cur = String.valueOf(x);
//                this.node.child = new HashMap<>();
//                this.node.child.put(cur, new myNode());
//                this.node = this.node.child.get(cur);
//            }
//
//            this.node.isEnd = true;
//        }
//
//        public boolean search(String word) {
//
//            char[] wordArray = word.toCharArray();
//            for (char x : wordArray){
//
//                String cur = String.valueOf(x);
//                HashMap<String, myNode> child = this.node.child;
//
//                if (child == null){
//                    return false;
//                }
//                else if (cur.equals(".")){
//                    continue;
//                }else{
//                    if (this.node.child.containsKey(x)){
//                        return false;
//                    }
//                    this.node = this.node.child.get(cur);
//                }
//            }
//
//            return true ? this.node.isEnd: false;
//        }
//
//    }

    // V1
    // IDEA : TRIE
    // https://leetcode.com/problems/design-add-and-search-words-data-structure/editorial/
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap();
        boolean word = false;
        public TrieNode() {}
    }

    class WordDictionary2 {
        TrieNode trie;

        /** Initialize your data structure here. */
        public WordDictionary2() {
            trie = new TrieNode();
        }

        /** Adds a word into the data structure. */
        public void addWord(String word) {
            TrieNode node = trie;

            for (char ch : word.toCharArray()) {
                if (!node.children.containsKey(ch)) {
                    node.children.put(ch, new TrieNode());
                }
                node = node.children.get(ch);
            }
            node.word = true;
        }

        /** Returns if the word is in the node. */
        public boolean searchInNode(String word, TrieNode node) {
            for (int i = 0; i < word.length(); ++i) {
                char ch = word.charAt(i);
                if (!node.children.containsKey(ch)) {
                    // if the current character is '.'
                    // check all possible nodes at this level
                    if (ch == '.') {
                        for (char x : node.children.keySet()) {
                            TrieNode child = node.children.get(x);
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
