package LeetCodeJava.String;

// https://leetcode.com/problems/replace-words/description/

import java.util.*;

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

    // V0-1
    // IDEA: TRIE (fixed by gpt)
    class MyNode97 {
        Map<Character, MyNode97> child = new HashMap<>();
        boolean isEnd = false;
    }

    class MyTrie97 {
        MyNode97 root;

        MyTrie97() {
            this.root = new MyNode97();
        }

        public void addWord(String word) {
            MyNode97 node = root;
            for (char ch : word.toCharArray()) {
                node.child.putIfAbsent(ch, new MyNode97());
                node = node.child.get(ch);
            }
            node.isEnd = true;
        }

        // Return the shortest root for the given word
        public String findShortestRoot(String word) {
            MyNode97 node = root;
            StringBuilder sb = new StringBuilder();

            for (char ch : word.toCharArray()) {
                if (!node.child.containsKey(ch)) {
                    return word; // no root found
                }
                sb.append(ch);
                node = node.child.get(ch);

                if (node.isEnd) {
                    return sb.toString(); // shortest root found
                }
            }

            return word; // no replacement
        }
    }


    // V0-2
    // IDEA: TRIE (fixed by gemini)
    /** * Represents a single node in the Trie.
     */
    class TrieNode_0_2 {
        // Map to store children: Character -> TrieNode
        Map<Character, TrieNode_0_2> children;
        // Flag to mark the end of a complete root word from the dictionary
        boolean isEndOfWord;

        TrieNode_0_2() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
        }
    }

    // The root of the Trie
    private final TrieNode_0_2 root = new TrieNode_0_2();

    /**
     * Builds the Trie by inserting all dictionary words.
     * @param word The root word to insert.
     */
    public void insert(String word) {
        TrieNode_0_2 node = root;
        for (char ch : word.toCharArray()) {
            // Use computeIfAbsent for concise creation of new nodes
            node = node.children.computeIfAbsent(ch, k -> new TrieNode_0_2());
        }
        node.isEndOfWord = true;
    }

    /**
     * Finds the shortest root word in the Trie that is a prefix of the given word.
     * @param word The word to check against the Trie.
     * @return The shortest root, or the original word if no root is found.
     */
    public String findShortestRoot(String word) {
        TrieNode_0_2 node = root;
        StringBuilder prefixBuilder = new StringBuilder();

        for (char ch : word.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                // No further prefix found in the dictionary.
                // Stop searching and return the original word.
                return word;
            }

            // Move to the next node and append the character
            node = node.children.get(ch);
            prefixBuilder.append(ch);

            // Crucial: Check if the current prefix is a complete root word.
            // Since we traverse character by character, the FIRST 'isEndOfWord'
            // we hit guarantees the SHORTEST possible root (e.g., if "a" and "apple"
            // are roots, hitting 'a' first is the desired shortest result).
            if (node.isEndOfWord) {
                return prefixBuilder.toString();
            }
        }

        // If the entire word was traversed but no root was marked (e.g., word="cat"
        // and "c" and "ca" are roots, but not "cat"), we return the original word
        // because we haven't hit an 'isEndOfWord' flag yet.
        // However, in the context of the problem, we should return the original word
        // if the *entire* word is traversed and no root was found.
        return word;
    }


    public String replaceWords_0_1(List<String> dictionary, String sentence) {

        // 1. Build the Trie from the dictionary
        for (String rootWord : dictionary) {
            insert(rootWord);
        }

        // 2. Split the sentence into words
        String[] words = sentence.split(" ");

        // 3. Process each word, replacing it with the shortest root found in the Trie
        for (int i = 0; i < words.length; i++) {
            words[i] = findShortestRoot(words[i]);
        }

        // 4. Join the resulting words back into a sentence
        return String.join(" ", words);
    }

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
