package LeetCodeJava.String;

// https://leetcode.com/problems/longest-word-in-dictionary/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 *  720. Longest Word in Dictionary
 * Attempted
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of strings words representing an English Dictionary, return the longest word in words that can be built one character at a time by other words in words.
 *
 * If there is more than one possible answer, return the longest word with the smallest lexicographical order. If there is no answer, return the empty string.
 *
 * Note that the word should be built from left to right with each additional character being added to the end of a previous word.
 *
 *
 *
 * Example 1:
 *
 * Input: words = ["w","wo","wor","worl","world"]
 * Output: "world"
 * Explanation: The word "world" can be built one character at a time by "w", "wo", "wor", and "worl".
 * Example 2:
 *
 * Input: words = ["a","banana","app","appl","ap","apply","apple"]
 * Output: "apple"
 * Explanation: Both "apply" and "apple" can be built from other words in the dictionary. However, "apple" is lexicographically smaller than "apply".
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 1000
 * 1 <= words[i].length <= 30
 * words[i] consists of lowercase English letters.
 *
 */
public class LongestWordInDictionary {

    // V0
//    public String longestWord(String[] words) {
//
//    }

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
