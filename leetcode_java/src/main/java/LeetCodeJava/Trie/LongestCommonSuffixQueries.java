package LeetCodeJava.Trie;

// https://leetcode.com/problems/longest-common-suffix-queries/description/
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

    // V1-1
    // IDEA: TRIE
    // https://leetcode.com/problems/longest-common-suffix-queries/solutions/4917364/c-java-python-2-approaches-trie-dfs-prec-7uee/
    class TrieNode {
        public int index;
        public TrieNode[] children;

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

        public Trie(String[] words) {
            int n = words.length;
            root = new TrieNode();
            this.words = words;
            for (int i = 0; i < n; i++) {
                insertSuffix(words[i], i);
            }
            fillIndices(root);
        }

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

        public int longestCommonSuffix(String s) {
            TrieNode curr = root;
            char[] str = s.toCharArray();
            for (int i = str.length - 1; i >= 0 && curr.children[str[i] - 'a'] != null; i--) {
                curr = curr.children[str[i] - 'a'];
            }
            return curr.index;
        }
    }

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

        public Trie_1_2(String[] words) {
            int n = words.length;
            root = new TrieNode_1_2();
            this.words = words;
            for (int i = 0; i < n; i++) {
                insertSuffix(words[i], i);
            }
        }

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

        public int longestCommonSuffix(String s) {
            TrieNode_1_2 curr = root;
            char[] str = s.toCharArray();
            for (int i = str.length - 1; i >= 0 && curr.children[str[i] - 'a'] != null; i--) {
                curr = curr.children[str[i] - 'a'];
            }
            return curr.index;
        }
    }


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
