package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-search-ii/description/?envType=list&envId=xoqag3yj

import java.util.ArrayList;
import java.util.List;

public class WordSearch2 {

    // V0
    // TODO : implement
//    public List<String> findWords(char[][] board, String[] words) {
//        return null;
//    }

    // V1
    // IDEA : BACKTRACK + TRIE
    // https://leetcode.com/problems/word-search-ii/solutions/59780/java-15ms-easiest-solution-100-00/
    public List<String> findWords2(char[][] board, String[] words) {
        List<String> res = new ArrayList<>();
        TrieNode root = buildTrie(words);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                dfs (board, i, j, root, res);
            }
        }
        return res;
    }

    public void dfs(char[][] board, int i, int j, TrieNode p, List<String> res) {
        char c = board[i][j];
        if (c == '#' || p.next[c - 'a'] == null) return;
        p = p.next[c - 'a'];
        if (p.word != null) {   // found one
            res.add(p.word);
            p.word = null;     // de-duplicate
        }

        board[i][j] = '#';
        if (i > 0) dfs(board, i - 1, j ,p, res);
        if (j > 0) dfs(board, i, j - 1, p, res);
        if (i < board.length - 1) dfs(board, i + 1, j, p, res);
        if (j < board[0].length - 1) dfs(board, i, j + 1, p, res);
        board[i][j] = c;
    }

    public TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode p = root;
            for (char c : w.toCharArray()) {
                int i = c - 'a';
                if (p.next[i] == null) p.next[i] = new TrieNode();
                p = p.next[i];
            }
            p.word = w;
        }
        return root;
    }

    class TrieNode {
        TrieNode[] next = new TrieNode[26];
        String word;
    }

    // V2
    // IDEA : TRIE + BACKTRACK
    // https://leetcode.com/problems/word-search-ii/solutions/4707890/backtracking-solution-using-trie-explained/
    class TrieNode2 {
        private TrieNode2[] links;
        private final int LENGTH = 26;
        private boolean isWord = false;

        public TrieNode2() {
            links = new TrieNode2[LENGTH];
        }

        // Check if the node contains a link to the given key
        public boolean containsKey(char key) {
            return links[key - 'a'] != null;
        }

        // Retrieve the next node for the given key
        public TrieNode2 get(char key) {
            return links[key - 'a'];
        }

        // Create a link for the given key to the node
        public void put(char key, TrieNode2 node) {
            links[key - 'a'] = node;
        }

        // Mark this node as representing the end of a word
        public void setWord(boolean isWord) {
            this.isWord = isWord;
        }

        // Check if this node represents the end of a word
        public boolean isWord() {
            return isWord;
        }
    }

    class Trie2 {
        private TrieNode2 root;

        public Trie2() {
            root = new TrieNode2();
        }

        // Insert a word into the trie
        public void insert(String word) {
            TrieNode2 node = root;

            for (char c : word.toCharArray()) {

                if (!node.containsKey(c)) {
                    node.put(c, new TrieNode2());
                }
                node = node.get(c);
            }
            node.setWord(true);
        }

        public TrieNode2 getRoot() {
            return root;
        }
    }

    private final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // Check if the given position is within the board boundaries
    private boolean isValidCell(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Build a trie from a list of words
    private Trie2 buildTrie2(String[] words) {
        Trie2 trie = new Trie2();

        for (String word : words) {
            trie.insert(word);
        }
        return trie;
    }

    // Backtrack to find all words starting from (row, col)
    private void backTrack(char[][] board, boolean[][] visited, int row, int col, TrieNode2 node, StringBuilder word, List<String> result) {
        if (node.isWord()) {
            result.add(word.toString());
            node.setWord(false); // Avoid duplicate entries
        }

        if (!isValidCell(row, col, board.length, board[0].length) || visited[row][col]) {
            return;
        }
        visited[row][col] = true;

        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValidCell(newRow, newCol, board.length, board[0].length) && !visited[newRow][newCol]) {
                char nextChar = board[newRow][newCol];

                if (node.containsKey(nextChar)) {
                    word.append(nextChar);
                    backTrack(board, visited, newRow, newCol, node.get(nextChar), word, result);
                    word.deleteCharAt(word.length() - 1); // Backtrack
                }
            }
        }

        visited[row][col] = false; // Reset visited status for backtracking
    }

    public List<String> findWords2_(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();

        Trie2 trie = buildTrie2(words);
        TrieNode2 root = trie.getRoot();

        boolean[][] visited = new boolean[board.length][board[0].length];

        for (int row = 0; row < board.length; row++) {

            for (int col = 0; col < board[0].length; col++) {

                if (root.containsKey(board[row][col])) {
                    StringBuilder word = new StringBuilder();
                    word.append(board[row][col]);
                    backTrack(board, visited, row, col, root.get(board[row][col]), word, result);
                }
            }
        }
        return result;
    }

}
