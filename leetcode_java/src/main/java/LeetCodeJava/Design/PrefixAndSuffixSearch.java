package LeetCodeJava.Design;

// https://leetcode.com/problems/prefix-and-suffix-search/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  745. Prefix and Suffix Search
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Design a special dictionary that searches the words in it by a prefix and a suffix.
 *
 * Implement the WordFilter class:
 *
 * WordFilter(string[] words) Initializes the object with the words in the dictionary.
 * f(string pref, string suff) Returns the index of the word in the dictionary, which has the prefix pref and the suffix suff. If there is more than one valid index, return the largest of them. If there is no such word in the dictionary, return -1.
 *
 *
 * Example 1:
 *
 * Input
 * ["WordFilter", "f"]
 * [[["apple"]], ["a", "e"]]
 * Output
 * [null, 0]
 * Explanation
 * WordFilter wordFilter = new WordFilter(["apple"]);
 * wordFilter.f("a", "e"); // return 0, because the word at index 0 has prefix = "a" and suffix = "e".
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 104
 * 1 <= words[i].length <= 7
 * 1 <= pref.length, suff.length <= 7
 * words[i], pref and suff consist of lowercase English letters only.
 * At most 104 calls will be made to the function f.
 *
 */
public class PrefixAndSuffixSearch {

    // V0
//    class WordFilter {
//
//        public WordFilter(String[] words) {
//
//        }
//
//        public int f(String pref, String suff) {
//
//        }
//    }

    // V0-1
    // IDEA: (fixed by gemini)
    // --- Trie Node Class ---
    class TrieNode_0_1 {
        // Store the largest weight (index) of the word that passes through this node.
        int maxWeight;
        Map<Character, TrieNode_0_1> children;

        TrieNode_0_1() {
            this.maxWeight = 0;
            this.children = new HashMap<>();
        }
    }

    // --- WordFilter Class ---
    class WordFilter_0_1 {

        private final TrieNode_0_1 root;

        /**
         * Constructor: Preprocessing step to build the Trie.
         * Inserts all combinations of (suffix + "#" + word) for every word.
         * Time Complexity: O(N * L^2), where N is number of words, L is max word length.
         */
        public WordFilter_0_1(String[] words) {
            this.root = new TrieNode_0_1();

            for (int weight = 0; weight < words.length; weight++) {
                String word = words[weight];
                int length = word.length();

                // Loop through all possible suffixes of the current word
                // The combined key is: suffix + "#" + word
                for (int i = 0; i <= length; i++) {
                    String combinedKey = word.substring(i) + "#" + word;
                    insert(combinedKey, weight);
                }
            }
        }

        // Helper method to insert a string into the Trie and update maxWeight along the path.
        private void insert(String key, int weight) {
            TrieNode_0_1 node = root;

            for (char ch : key.toCharArray()) {
                node.children.putIfAbsent(ch, new TrieNode_0_1());
                node = node.children.get(ch);

                // Update the maxWeight at the current node
                node.maxWeight = Math.max(node.maxWeight, weight);
            }
        }

        /**
         * Query method: Finds the word with the largest weight that matches pref and suff.
         * Time Complexity: O(L), where L is the length of the query string.
         */
        public int f(String pref, String suff) {
            // Search for the combined key: suff + "#" + pref
            String query = suff + "#" + pref;
            TrieNode_0_1 node = root;

            for (char ch : query.toCharArray()) {
                if (!node.children.containsKey(ch)) {
                    // If any character in the query path is missing, no matching word exists.
                    return -1;
                }
                node = node.children.get(ch);
            }

            // The maxWeight at the final node represents the largest index (weight)
            // of a word that contains the required suffix and prefix.
            return node.maxWeight;
        }
    }

    /**
     * Your MyNode101, MyTrie101 classes are not needed in this fixed solution,
     * as the logic is integrated directly into the final WordFilter class.
     */


    // V0-2
    // IDEA: (gpt)
    class WordFilter_0_2 {

        class TrieNode {
            TrieNode[] child = new TrieNode[27]; // 26 chars + '{'
            int weight = -1;
        }

        TrieNode root = new TrieNode();

        public WordFilter_0_2(String[] words) {
            for (int weight = 0; weight < words.length; weight++) {
                String w = words[weight] + "{"; // add separator

                // For suffix-prefix combinations
                for (int i = 0; i < w.length(); i++) {
                    insert(w.substring(i) + words[weight], weight);
                }
            }
        }

        private void insert(String word, int weight) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int idx = (c == '{') ? 26 : c - 'a';
                if (node.child[idx] == null)
                    node.child[idx] = new TrieNode();
                node = node.child[idx];
                node.weight = weight;
            }
        }

        public int f(String pref, String suff) {
            String query = suff + "{" + pref;
            TrieNode node = root;

            for (char c : query.toCharArray()) {
                int idx = (c == '{') ? 26 : c - 'a';
                if (node.child[idx] == null)
                    return -1;
                node = node.child[idx];
            }

            return node.weight;
        }
    }


    // V1-1
    // IDEA: Trie + Set Intersection [Time Limit Exceeded] (TLE)
    // https://leetcode.com/problems/prefix-and-suffix-search/editorial/
    class WordFilter_1_1 {
        TrieNode trie1, trie2;

        public WordFilter_1_1(String[] words) {
            trie1 = new TrieNode();
            trie2 = new TrieNode();
            int wt = 0;
            for (String word : words) {
                char[] ca = word.toCharArray();

                TrieNode cur = trie1;
                cur.weight.add(wt);
                for (char letter : ca) {
                    if (cur.children[letter - 'a'] == null) {
                        cur.children[letter - 'a'] = new TrieNode();
                    }
                    cur = cur.children[letter - 'a'];
                    cur.weight.add(wt);
                }

                cur = trie2;
                cur.weight.add(wt);
                for (int j = ca.length - 1; j >= 0; --j) {
                    char letter = ca[j];
                    if (cur.children[letter - 'a'] == null) {
                        cur.children[letter - 'a'] = new TrieNode();
                    }
                    cur = cur.children[letter - 'a'];
                    cur.weight.add(wt);
                }
                wt++;
            }
        }

        public int f(String prefix, String suffix) {
            TrieNode cur1 = trie1, cur2 = trie2;
            for (char letter : prefix.toCharArray()) {
                if (cur1.children[letter - 'a'] == null) {
                    return -1;
                }
                cur1 = cur1.children[letter - 'a'];
            }
            char[] ca = suffix.toCharArray();
            for (int j = ca.length - 1; j >= 0; --j) {
                char letter = ca[j];
                if (cur2.children[letter - 'a'] == null) {
                    return -1;
                }
                cur2 = cur2.children[letter - 'a'];
            }

            int ans = -1;
            for (int w1 : cur1.weight) {
                if (w1 > ans && cur2.weight.contains(w1)) {
                    ans = w1;
                }
            }

            return ans;
        }
    }

    class TrieNode {
        TrieNode[] children;
        Set<Integer> weight;

        public TrieNode() {
            children = new TrieNode[26];
            weight = new HashSet();
        }
    }



    // V1-2
    // IDEA: Paired Trie [Accepted]
    // https://leetcode.com/problems/prefix-and-suffix-search/editorial/
    class WordFilter_1_2 {
        TrieNode_1_2 trie;

        public WordFilter_1_2(String[] words) {
            trie = new TrieNode_1_2();
            int wt = 0;
            for (String word : words) {
                TrieNode_1_2 cur = trie;
                cur.weight = wt;
                int L = word.length();
                char[] chars = word.toCharArray();
                for (int i = 0; i < L; ++i) {

                    TrieNode_1_2 tmp = cur;
                    for (int j = i; j < L; ++j) {
                        int code = (chars[j] - '`') * 27;
                        if (tmp.children.get(code) == null) {
                            tmp.children.put(code, new TrieNode_1_2());
                        }
                        tmp = tmp.children.get(code);
                        tmp.weight = wt;
                    }

                    tmp = cur;
                    for (int k = L - 1 - i; k >= 0; --k) {
                        int code = (chars[k] - '`');
                        if (tmp.children.get(code) == null) {
                            tmp.children.put(code, new TrieNode_1_2());
                        }
                        tmp = tmp.children.get(code);
                        tmp.weight = wt;
                    }

                    int code = (chars[i] - '`') * 27 + (chars[L - 1 - i] - '`');
                    if (cur.children.get(code) == null) {
                        cur.children.put(code, new TrieNode_1_2());
                    }
                    cur = cur.children.get(code);
                    cur.weight = wt;

                }
                wt++;
            }
        }

        public int f(String prefix, String suffix) {
            TrieNode_1_2 cur = trie;
            int i = 0, j = suffix.length() - 1;
            while (i < prefix.length() || j >= 0) {
                char c1 = i < prefix.length() ? prefix.charAt(i) : '`';
                char c2 = j >= 0 ? suffix.charAt(j) : '`';
                int code = (c1 - '`') * 27 + (c2 - '`');
                cur = cur.children.get(code);
                if (cur == null) {
                    return -1;
                }
                i++;
                j--;
            }
            return cur.weight;
        }
    }

    class TrieNode_1_2 {
        Map<Integer, TrieNode_1_2> children;
        int weight;

        public TrieNode_1_2() {
            children = new HashMap();
            weight = 0;
        }
    }


    // V1-3
    // IDEA: Trie of Suffix Wrapped Words
    // https://leetcode.com/problems/prefix-and-suffix-search/editorial/
    class WordFilter_1_3 {
        TrieNode_1_3 trie;

        public WordFilter_1_3(String[] words) {
            trie = new TrieNode_1_3();
            for (int weight = 0; weight < words.length; ++weight) {
                String word = words[weight] + "{";
                for (int i = 0; i < word.length(); ++i) {
                    TrieNode_1_3 cur = trie;
                    cur.weight = weight;
                    for (int j = i; j < 2 * word.length() - 1; ++j) {
                        int k = word.charAt(j % word.length()) - 'a';
                        if (cur.children[k] == null) {
                            cur.children[k] = new TrieNode_1_3();
                        }
                        cur = cur.children[k];
                        cur.weight = weight;
                    }
                }
            }
        }

        public int f(String prefix, String suffix) {
            TrieNode_1_3 cur = trie;
            for (char letter : (suffix + '{' + prefix).toCharArray()) {
                if (cur.children[letter - 'a'] == null) {
                    return -1;
                }
                cur = cur.children[letter - 'a'];
            }
            return cur.weight;
        }
    }

    class TrieNode_1_3 {
        TrieNode_1_3[] children;
        int weight;

        public TrieNode_1_3() {
            children = new TrieNode_1_3[27];
            weight = 0;
        }
    }


    // V2


    // V3



}
