package LeetCodeJava.String;

// https://leetcode.com/problems/longest-word-in-dictionary/description/

import java.util.*;

/**
 * 720. Longest Word in Dictionary
 * Attempted
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of strings words representing an English Dictionary, return the longest word in words that can be built one character at a time by other words in words.
 * <p>
 * If there is more than one possible answer, return the longest word with the smallest lexicographical order. If there is no answer, return the empty string.
 * <p>
 * Note that the word should be built from left to right with each additional character being added to the end of a previous word.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: words = ["w","wo","wor","worl","world"]
 * Output: "world"
 * Explanation: The word "world" can be built one character at a time by "w", "wo", "wor", and "worl".
 * Example 2:
 * <p>
 * Input: words = ["a","banana","app","appl","ap","apply","apple"]
 * Output: "apple"
 * Explanation: Both "apply" and "apple" can be built from other words in the dictionary. However, "apple" is lexicographically smaller than "apply".
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= words.length <= 1000
 * 1 <= words[i].length <= 30
 * words[i] consists of lowercase English letters.
 */
public class LongestWordInDictionary {

    // V0
//    public String longestWord(String[] words) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (gemini)

    /**
     * NOTE !!!
     * <p>
     * Core idea:
     * <p>
     * For LeetCode 720 – Longest Word in Dictionary, a
     * <p>
     * word is valid if:
     * <p>
     * -> `Every prefix of the word exists in the dictionary.`
     * <p>
     * <p>
     * Example:
     * <p>
     * ["w","wo","wor","worl","world"]
     * <p>
     * -> "world" is valid because:
     * <p>
     * "w" exists
     * "wo" exists
     * "wor" exists
     * "worl" exists
     * "world" exists
     */
    public String longestWord_0_1(String[] words) {
        // 1. Sort words:
        // Primary: by length (small to large)
        // Secondary: lexicographical (alphabetical)
        /**  NOTE !!!  why need to sort ??
         *
         *
         *       * # 📌 Why Sorting Is Critical
         *              *
         *              * We sort first:
         *              *
         *              * ```java
         *              * Arrays.sort(words);
         *              * ```
         *              *
         *              * That ensures:
         *              *
         *              * ```
         *              * ["w","wo","wor","worl","world"]
         *              * ```
         *              *
         *              * comes in this order.
         *              *
         *              * So when we reach `"world"`:
         *              *
         *              * * `"worl"` has already been processed
         *              * * If `"worl"` wasn’t valid, it wouldn’t be in `builtWords`
         *
         */
        Arrays.sort(words);

        Set<String> builtWords = new HashSet<>();
        String result = "";

        // 2. Iterate through sorted words
        for (String w : words) {
            // Base case: words of length 1 are always "buildable"
            // Recursive case: word is buildable if its prefix (all but last char) exists in set
            /**  NOTE !!!  why `builtWords.contains(w.substring(0, w.length() - 1))` ?
             *
             *
             *  - 1) Short answer
             *
             *   - given ["w","wo","wor","worl","world"]
             *
             *   -> If:
             *
             *     "w" is valid
             *     "wo" is valid
             *     "wor" is valid
             *     "worl" is valid
             *
             *   -> Then to verify "world"
             *      -> we only need to check:
             *         "worl"
             *
             *
             *  - 2) Long answer
             *
             *  Great question 👍 — this line is the **core trick** of LC 720.
             *
             * ```java
             * builtWords.contains(w.substring(0, w.length() - 1))
             * ```
             *
             * Let’s break it down clearly.
             *
             * ---
             *
             * # 🎯 What the problem requires
             *
             * A word is valid only if:
             *
             * > **All its prefixes exist in the dictionary.**
             *
             * Example:
             *
             * ```
             * ["w","wo","wor","worl","world"]
             * ```
             *
             * To accept `"world"`:
             *
             * * `"w"` must exist
             * * `"wo"` must exist
             * * `"wor"` must exist
             * * `"worl"` must exist
             *
             * ---
             *
             * # 🔥 Why Only Check `length - 1` Prefix?
             *
             * You might think we need to check *all* prefixes.
             *
             * But we **don’t**.
             *
             * Because we build words **incrementally**.
             *
             * ---
             *
             * ## 🧠 Key Idea (Inductive Logic)
             *
             * If:
             *
             * * `"w"` is valid
             * * `"wo"` is valid
             * * `"wor"` is valid
             * * `"worl"` is valid
             *
             * Then to verify `"world"`
             * we only need to check:
             *
             * ```
             * "worl"
             * ```
             *
             * Because:
             *
             * * If `"worl"` was valid,
             * * Then `"wor"` must already have been valid,
             * * And `"wo"`,
             * * And `"w"`.
             *
             * So checking the immediate shorter prefix is enough.
             *
             * ---
             *
             * # 📌 Why Sorting Is Critical
             *
             * We sort first:
             *
             * ```java
             * Arrays.sort(words);
             * ```
             *
             * That ensures:
             *
             * ```
             * ["w","wo","wor","worl","world"]
             * ```
             *
             * comes in this order.
             *
             * So when we reach `"world"`:
             *
             * * `"worl"` has already been processed
             * * If `"worl"` wasn’t valid, it wouldn’t be in `builtWords`
             *
             * So this line:
             *
             * ```java
             * builtWords.contains(w.substring(0, w.length() - 1))
             * ```
             *
             * means:
             *
             * > “Was the word formed by removing the last character already buildable?”
             *
             * ---
             *
             * # 📎 Concrete Example
             *
             * Input:
             *
             * ```
             * ["a", "banana", "app", "appl", "ap", "apply", "apple"]
             * ```
             *
             * After sorting:
             *
             * ```
             * ["a","ap","app","appl","apple","apply","banana"]
             * ```
             *
             * Now iteration:
             *
             * | Word     | Prefix Checked | Exists? | Add? |
             * | -------- | -------------- | ------- | ---- |
             * | "a"      | —              | —       | ✅    |
             * | "ap"     | "a"            | ✅       | ✅    |
             * | "app"    | "ap"           | ✅       | ✅    |
             * | "appl"   | "app"          | ✅       | ✅    |
             * | "apple"  | "appl"         | ✅       | ✅    |
             * | "apply"  | "appl"         | ✅       | ✅    |
             * | "banana" | "banan"        | ❌       | ❌    |
             *
             * We never needed to check all prefixes manually.
             *
             * ---
             *
             * # ❓ Why Not Check All Prefixes?
             *
             * We could do:
             *
             * ```java
             * for (int i = 1; i < w.length(); i++) {
             *     if (!set.contains(w.substring(0, i))) return false;
             * }
             * ```
             *
             * But that’s:
             *
             * * More code
             * * Less efficient
             * * Unnecessary
             *
             * Because the sorted + incremental build already guarantees correctness.
             *
             * ---
             *
             * # ⚡ Final Summary
             *
             * We check:
             *
             * ```java
             * w.substring(0, w.length() - 1)
             * ```
             *
             * because:
             *
             * 1. Sorting ensures smaller prefixes are processed first
             * 2. If the immediate prefix exists, all smaller prefixes must already exist
             * 3. That makes this a clean O(n log n) solution
             *
             *
             *
             */

            if (w.length() == 1 || builtWords.contains(w.substring(0, w.length() - 1))) {
                builtWords.add(w);

                // 3. Update result if current word is longer
                // (Sorting ensures alphabetical order is handled naturally)
                if (w.length() > result.length()) {
                    result = w;
                }
            }
        }

        return result;
    }

    // V0-2
    // IDEA: BRUTE FORCE (GPT)
    public String longestWord_0_2(String[] words) {

        if (words == null || words.length == 0) {
            return "";
        }

        Arrays.sort(words); // lexicographical order

        Set<String> built = new HashSet<>();
        String result = "";

        for (String word : words) {

            // word is valid if:
            // length == 1 OR its prefix exists
            if (word.length() == 1 || built.contains(word.substring(0, word.length() - 1))) {

                built.add(word);

                if (word.length() > result.length()) {
                    result = word;
                }
            }
        }

        return result;
    }


    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/longest-word-in-dictionary/editorial/
    public String longestWord_1_1(String[] words) {
        String ans = "";
        Set<String> wordset = new HashSet();
        for (String word : words) {
            wordset.add(word);
        }
        for (String word : words) {
            if (word.length() > ans.length() ||
                    word.length() == ans.length() && word.compareTo(ans) < 0) {
                boolean good = true;
                for (int k = 1; k < word.length(); ++k) {
                    if (!wordset.contains(word.substring(0, k))) {
                        good = false;
                        break;
                    }
                }
                if (good) {
                    ans = word;
                }
            }
        }
        return ans;
    }


    // V1-2
    // IDEA: TRIE + DFS
    // https://leetcode.com/problems/longest-word-in-dictionary/editorial/
    public String longestWord_1_2(String[] words) {
        Trie trie = new Trie();
        int index = 0;
        for (String word : words) {
            trie.insert(word, ++index); //indexed by 1
        }
        trie.words = words;
        return trie.dfs();
    }
}

class Node {
    char c;
    HashMap<Character, Node> children = new HashMap();
    int end;

    public Node(char c) {
        this.c = c;
    }
}

class Trie {
    Node root;
    String[] words;

    public Trie() {
        root = new Node('0');
    }

    public void insert(String word, int index) {
        Node cur = root;
        for (char c : word.toCharArray()) {
            cur.children.putIfAbsent(c, new Node(c));
            cur = cur.children.get(c);
        }
        cur.end = index;
    }

    public String dfs() {
        String ans = "";
        Stack<Node> stack = new Stack();
        stack.push(root);
        while (!stack.empty()) {
            Node node = stack.pop();
            if (node.end > 0 || node == root) {
                if (node != root) {
                    String word = words[node.end - 1];
                    if (word.length() > ans.length() ||
                            word.length() == ans.length() && word.compareTo(ans) < 0) {
                        ans = word;
                    }
                }
                for (Node nei : node.children.values()) {
                    stack.push(nei);
                }
            }
        }
        return ans;
    }


    // V2
    // IDEA: TRIE + DFS
    // https://leetcode.com/problems/longest-word-in-dictionary/solutions/634793/java-the-easiest-to-understand-trie-solu-qbqi/
    private TreeNode root_2;
    private String result = "";

    public String longestWord(String[] words) {
        root_2 = new TreeNode();

        for (String w : words)
            insert(w);

        dfs(root_2);

        return result;
    }

    private void dfs(TreeNode node) {
        if (node == null)
            return;

        if (node.word != null) {
            if (node.word.length() > result.length())
                result = node.word;
            else if (node.word.length() == result.length() && node.word.compareTo(result) < 0)
                result = node.word;
        }

        for (TreeNode child : node.children)
            if (child != null && child.word != null)
                dfs(child);
    }

    private void insert(String word) {
        TreeNode current = root_2;
        for (char c : word.toCharArray()) {
            if (current.children[c - 'a'] == null)
                current.children[c - 'a'] = new TreeNode();
            current = current.children[c - 'a'];
        }
        current.word = word;
        }

    }

    class TreeNode {
        TreeNode[] children = new TreeNode[26];
        String word;

        TreeNode() {
        }


    // V3
    // IDEA: PREFIX
    // https://leetcode.com/problems/longest-word-in-dictionary/solutions/3652066/easy-python-solution-prefix-clean-code-b-xx7p/




}
