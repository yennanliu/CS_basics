package LeetCodeJava.Trie;

// https://leetcode.com/problems/longest-common-suffix-queries/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 3093. Longest Common Suffix Queries
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two arrays of strings wordsContainer and wordsQuery.
 *
 * For each wordsQuery[i], you need to find a string from wordsContainer that has the longest common suffix with wordsQuery[i]. If there are two or more strings in wordsContainer that share the longest common suffix, find the string that is the smallest in length. If there are two or more such strings that have the same smallest length, find the one that occurred earlier in wordsContainer.
 *
 * Return an array of integers ans, where ans[i] is the index of the string in wordsContainer that has the longest common suffix with wordsQuery[i].
 *
 *
 *
 * Example 1:
 *
 * Input: wordsContainer = ["abcd","bcd","xbcd"], wordsQuery = ["cd","bcd","xyz"]
 *
 * Output: [1,1,1]
 *
 * Explanation:
 *
 * Let's look at each wordsQuery[i] separately:
 *
 * For wordsQuery[0] = "cd", strings from wordsContainer that share the longest common suffix "cd" are at indices 0, 1, and 2. Among these, the answer is the string at index 1 because it has the shortest length of 3.
 * For wordsQuery[1] = "bcd", strings from wordsContainer that share the longest common suffix "bcd" are at indices 0, 1, and 2. Among these, the answer is the string at index 1 because it has the shortest length of 3.
 * For wordsQuery[2] = "xyz", there is no string from wordsContainer that shares a common suffix. Hence the longest common suffix is "", that is shared with strings at index 0, 1, and 2. Among these, the answer is the string at index 1 because it has the shortest length of 3.
 * Example 2:
 *
 * Input: wordsContainer = ["abcdefgh","poiuygh","ghghgh"], wordsQuery = ["gh","acbfgh","acbfegh"]
 *
 * Output: [2,0,2]
 *
 * Explanation:
 *
 * Let's look at each wordsQuery[i] separately:
 *
 * For wordsQuery[0] = "gh", strings from wordsContainer that share the longest common suffix "gh" are at indices 0, 1, and 2. Among these, the answer is the string at index 2 because it has the shortest length of 6.
 * For wordsQuery[1] = "acbfgh", only the string at index 0 shares the longest common suffix "fgh". Hence it is the answer, even though the string at index 2 is shorter.
 * For wordsQuery[2] = "acbfegh", strings from wordsContainer that share the longest common suffix "gh" are at indices 0, 1, and 2. Among these, the answer is the string at index 2 because it has the shortest length of 6.
 *
 *
 * Constraints:
 *
 * 1 <= wordsContainer.length, wordsQuery.length <= 104
 * 1 <= wordsContainer[i].length <= 5 * 103
 * 1 <= wordsQuery[i].length <= 5 * 103
 * wordsContainer[i] consists only of lowercase English letters.
 * wordsQuery[i] consists only of lowercase English letters.
 * Sum of wordsContainer[i].length is at most 5 * 105.
 * Sum of wordsQuery[i].length is at most 5 * 105.
 *
 */
public class LongestCommonSuffixQueries {

    // V0
//    public int[] stringIndices(String[] wordsContainer, String[] wordsQuery) {
//
//    }

    // V0-1
    // IDEA: TRIE (gemini)
    class TrieNode_0_1 {
        TrieNode_0_1[] children = new TrieNode_0_1[26];
        int bestIndex = -1; // Stores the index of the best word passing through here
        int minLength = Integer.MAX_VALUE;
    }

    /**
     * time = O(L)
     * space = O(1)
     */
    public int[] stringIndices(String[] wordsContainer, String[] wordsQuery) {
        TrieNode_0_1 root = new TrieNode_0_1();

        // Default best for the root (for when no suffix matches at all)
        int globalBestIndex = 0;
        for (int i = 0; i < wordsContainer.length; i++) {
            if (wordsContainer[i].length() < wordsContainer[globalBestIndex].length()) {
                globalBestIndex = i;
            }
        }
        root.bestIndex = globalBestIndex;
        root.minLength = wordsContainer[globalBestIndex].length();

        // 1. Build the Trie with words from wordsContainer
        for (int i = 0; i < wordsContainer.length; i++) {
            String word = wordsContainer[i];
            int n = word.length();
            TrieNode_0_1 curr = root;

            // Traverse word from back to front (Suffix -> Prefix)
            for (int j = n - 1; j >= 0; j--) {
                int charIdx = word.charAt(j) - 'a';
                if (curr.children[charIdx] == null) {
                    curr.children[charIdx] = new TrieNode_0_1();
                }
                curr = curr.children[charIdx];

                // Update the best candidate at this node
                // Tie-breaking: smaller length, then smaller index
                if (n < curr.minLength) {
                    curr.minLength = n;
                    curr.bestIndex = i;
                }
                // (Index tie-break is implicit because we process in order
                // and only update if n is strictly LESS than minLength)
            }
        }

        // 2. Query the Trie
        int[] result = new int[wordsQuery.length];
        for (int i = 0; i < wordsQuery.length; i++) {
            String query = wordsQuery[i];
            TrieNode_0_1 curr = root;
            int bestSoFar = root.bestIndex;

            for (int j = query.length() - 1; j >= 0; j--) {
                int charIdx = query.charAt(j) - 'a';
                if (curr.children[charIdx] != null) {
                    curr = curr.children[charIdx];
                    bestSoFar = curr.bestIndex;
                } else {
                    break; // No deeper suffix match possible
                }
            }
            result[i] = bestSoFar;
        }

        return result;
    }


    // V0-3
    // IDEA: TRIE
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/64243726
    /**  IDEA:
     *
     * ## 1. English Translation
     *
     * **Q4**
     *
     * * You can refer to session 11; this problem is an application of a **Trie** (Prefix Tree).
     * * Each node in the Trie represents a **prefix**. Each edge represents a **letter**; traversing an edge means adding that letter to the prefix.
     * * "abc" --a--> "abca"
     * * The problem asks about **suffixes**, but after **reversing** them, they actually become prefixes.
     * * On each node of the Trie, record the **smallest index** and the **shortest string** that reaches that node.
     * * Queries involve traversing this Trie until the required edge does not exist or you reach the end.
     *
     * ---
     *
     * ## 2. Traditional Chinese Transcription
     *
     * **Q4**
     *
     * * 可以參考 session 11，這題是有關字典樹（Trie）的應用
     * * 字典樹每個節點代表一個前綴，每一條邊代表一個字母，走過那條邊代表在前綴上加上該字母
     * * "abc" --a--> "abca"
     * * 題目是問後綴，但 reverse 後其實就變前綴了
     * * 字典樹上每個節點就去紀錄，走到這個節點最小的 index，跟最短的字串
     * * 詢問就在這個字典樹上走，直到要走的邊不存在，或是走到底了
     *
     */
    class Node_0_3 {
        Map<Character, Node_0_3> children = new HashMap<>();
        int index;
        int length;

        Node_0_3() {
            index = 0;
            length = 0;
        }
    }

    Node_0_3 root = new Node_0_3();

    private void add(Node_0_3 node, String s, int idx, int len, int pos) {
        if (node == null)
            return;
        if (pos == s.length())
            return;

        char c = s.charAt(pos);
        Node_0_3 next = node.children.get(c);
        if (next == null) {
            next = new Node_0_3();
            next.index = idx;
            next.length = len;
            node.children.put(c, next);
        } else {
            if (len < next.length || (len == next.length && idx < next.index)) {
                next.index = idx;
                next.length = len;
            }
        }

        add(next, s, idx, len, pos + 1);
    }

    private int query(Node_0_3 node, String s, int pos) {
        if (pos == s.length())
            return node.index;

        char c = s.charAt(pos);
        Node_0_3 next = node.children.get(c);
        if (next == null)
            return node.index;

        return query(next, s, pos + 1);
    }

    /**
     * time = O(L)
     * space = O(1)
     */
    public int[] stringIndices_0_3(String[] wordsContainer, String[] wordsQuery) {
        List<String> reversedContainer = new ArrayList<>();
        for (String w : wordsContainer) {
            reversedContainer.add(new StringBuilder(w).reverse().toString());
        }

        root.index = 0;
        root.length = reversedContainer.get(0).length();
        for (int i = 0; i < reversedContainer.size(); i++) {
            if (reversedContainer.get(i).length() < root.length
                    || (reversedContainer.get(i).length() == root.length && i < root.index)) {
                root.index = i;
                root.length = reversedContainer.get(i).length();
            }
            add(root, reversedContainer.get(i), i, reversedContainer.get(i).length(), 0);
        }

        int[] result = new int[wordsQuery.length];
        int index = 0;
        for (String w : wordsQuery) {
            String rev = new StringBuilder(w).reverse().toString();
            result[index++] = query(root, rev, 0);
        }

        return result;
    }


    // V1-1
    // IDEA: TRIE
    // https://leetcode.com/problems/longest-common-suffix-queries/solutions/4917364/c-java-python-2-approaches-trie-dfs-prec-7uee/
    class TrieNode {
        public int index;
        public TrieNode[] children;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public TrieNode() {
            index = Integer.MAX_VALUE;
            children = new TrieNode[26];
        }
    }

    class Trie {
        private TrieNode root;
        private String[] words;

        private boolean change(int i, int j) { // change i to j ?
            int n = words.length;
            return (0 <= j && j < n) && // j should be valid
                    (!(0 <= i && i < n) || // change if i is not valid
                            words[j].length() < words[i].length() || // or if less size
                            (words[j].length() == words[i].length() && j < i)); // or if less index
        }

        private int fillIndices(TrieNode curr) {
            for (TrieNode child : curr.children) {
                int r = child != null ? fillIndices(child) : Integer.MAX_VALUE;
                if (change(curr.index, r)) {
                    curr.index = r;
                }
            }
            return curr.index;
        }

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public Trie(String[] words) {
            int n = words.length;
            root = new TrieNode();
            this.words = words;
            for (int i = 0; i < n; i++) {
                insertSuffix(words[i], i);
            }
            fillIndices(root);
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public void insertSuffix(String s, int index) {
            TrieNode curr = root;
            char[] str = s.toCharArray();
            for (int i = str.length - 1; i >= 0; i--) {
                if (curr.children[str[i] - 'a'] == null) {
                    curr.children[str[i] - 'a'] = new TrieNode();
                }
                curr = curr.children[str[i] - 'a'];
            }
            curr.index = Math.min(curr.index, index);
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public int longestCommonSuffix(String s) {
            TrieNode curr = root;
            char[] str = s.toCharArray();
            for (int i = str.length - 1; i >= 0 && curr.children[str[i] - 'a'] != null; i--) {
                curr = curr.children[str[i] - 'a'];
            }
            return curr.index;
        }
    }

    /**
     * time = O(L)
     * space = O(1)
     */
    public int[] stringIndices_1_1(String[] wordsContainer, String[] wordsQuery) {
        int m = wordsQuery.length;
        Trie trie = new Trie(wordsContainer);
        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            result[i] = trie.longestCommonSuffix(wordsQuery[i]);
        }
        return result;
    }


    // V1-2
    // IDEA: TRIE + DFS Optimization
    // https://leetcode.com/problems/longest-common-suffix-queries/solutions/4917364/c-java-python-2-approaches-trie-dfs-prec-7uee/
    class TrieNode_1_2 {
        public int index;
        public TrieNode_1_2[] children;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public TrieNode_1_2() {
            index = Integer.MAX_VALUE;
            children = new TrieNode_1_2[26];
        }
    }

    class Trie_1_2 {
        private TrieNode_1_2 root;
        private String[] words;

        private boolean change(int i, int j) { // change i to j ?
            int n = words.length;
            return (0 <= j && j < n) && // j should be valid
                    (!(0 <= i && i < n) || // change if i is not valid
                            words[j].length() < words[i].length()); // or if less size
            // or if less index: REMOVED as we are inserting words from container with increasing index
        }

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public Trie_1_2(String[] words) {
            int n = words.length;
            root = new TrieNode_1_2();
            this.words = words;
            for (int i = 0; i < n; i++) {
                insertSuffix(words[i], i);
            }
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public void insertSuffix(String s, int index) {
            TrieNode_1_2 curr = root;
            char[] str = s.toCharArray();
            for (int i = str.length - 1; i >= 0; i--) {
                if (curr.children[str[i] - 'a'] == null) {
                    curr.children[str[i] - 'a'] = new TrieNode_1_2();
                }
                if (change(curr.index, index)) {
                    curr.index = index;
                }
                curr = curr.children[str[i] - 'a'];
            }
            if (change(curr.index, index)) {
                curr.index = index;
            }
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public int longestCommonSuffix(String s) {
            TrieNode_1_2 curr = root;
            char[] str = s.toCharArray();
            for (int i = str.length - 1; i >= 0 && curr.children[str[i] - 'a'] != null; i--) {
                curr = curr.children[str[i] - 'a'];
            }
            return curr.index;
        }
    }


    /**
     * time = O(L)
     * space = O(1)
     */
    public int[] stringIndices_1_2(String[] wordsContainer, String[] wordsQuery) {
        int m = wordsQuery.length;
        Trie trie = new Trie(wordsContainer);
        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            result[i] = trie.longestCommonSuffix(wordsQuery[i]);
        }
        return result;
    }


    // V2
    // IDEA: TRIE
    // https://leetcode.com/problems/longest-common-suffix-queries/solutions/4916802/c-java-explained-using-trie-simple-and-t-uji6/
    public class Node {
        int ind;
        Node[] child;

        Node(int idx) {
            ind = idx;
            child = new Node[26];
        }
    };

    /**
     * time = O(L)
     * space = O(1)
     */
    public void add(Node ptr, String[] wordsContainer, int i) {
        for (int j = wordsContainer[i].length() - 1; j >= 0; --j) {
            int c = wordsContainer[i].charAt(j) - 'a';
            if (ptr.child[c] == null)
                ptr.child[c] = new Node(i);
            ptr = ptr.child[c];
            if (wordsContainer[ptr.ind].length() > wordsContainer[i].length())
                ptr.ind = i;
        }
    }

    /**
     * time = O(L)
     * space = O(1)
     */
    public int search(Node ptr, String s) {
        int ans = ptr.ind;
        for (int i = s.length() - 1; i >= 0; --i) {
            ptr = ptr.child[s.charAt(i) - 'a'];
            if (ptr == null)
                return ans;
            ans = ptr.ind;
        }
        return ans;
    }

    /**
     * time = O(L)
     * space = O(1)
     */
    public int[] stringIndices_2(String[] wordsContainer, String[] wordsQuery) {
        int[] ans = new int[wordsQuery.length];
        Node head = new Node(0);
        for (int i = 0; i < wordsContainer.length; ++i) {
            if (wordsContainer[head.ind].length() > wordsContainer[i].length())
                head.ind = i;
            add(head, wordsContainer, i);
        }
        for (int i = 0; i < wordsQuery.length; ++i)
            ans[i] = search(head, wordsQuery[i]);
        return ans;
    }



}
