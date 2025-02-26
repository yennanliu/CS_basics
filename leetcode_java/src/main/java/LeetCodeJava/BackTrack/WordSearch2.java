package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-search-ii/description/?envType=list&envId=xoqag3yj

import java.util.*;

/**
 * 212. Word Search II
 * Solved
 * Hard
 * Topics
 * Companies
 * Hint
 * Given an m x n board of characters and a list of strings words, return all words on the board.
 *
 * Each word must be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once in a word.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], words = ["oath","pea","eat","rain"]
 * Output: ["eat","oath"]
 * Example 2:
 *
 *
 * Input: board = [["a","b"],["c","d"]], words = ["abcb"]
 * Output: []
 *
 *
 * Constraints:
 *
 * m == board.length
 * n == board[i].length
 * 1 <= m, n <= 12
 * board[i][j] is a lowercase English letter.
 * 1 <= words.length <= 3 * 104
 * 1 <= words[i].length <= 10
 * words[i] consists of lowercase English letters.
 * All the strings of words are unique.
 *
 */
public class WordSearch2 {

    // V0
    // TODO : implement
//    public List<String> findWords(char[][] board, String[] words) {
//        return null;
//    }

    // V0-1
    // IDEA:  for loop + LC 79 (TLE)
    public List<String> findWords_0_1(char[][] board, String[] words) {

        // edge
        if (board.length == 0 || board[0].length == 0) {
            // return false;
            return null;
        }
        if (words == null || words.length == 0) {
            // return true; // ???
            return null;
        }

        int l = board.length;
        int w = board[0].length;

        List<String> finalRes = new ArrayList<>();

        // dfs
        // TODO: optimize this O(N ^ 3) time complexity ???
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                for (String word : words) {
                    boolean[][] visited = new boolean[l][w];
                    //System.out.println(">>> word " + word);
                    if (board[i][j] == word.charAt(0)) {
                        if (canBuild(board, word, j, i, visited, 0)) {
                            if (!finalRes.contains(word)) {
                                finalRes.add(word);
                            }
                        }
                    }
                }
            }
        }

        return finalRes;
    }

    private boolean canBuild(char[][] board, String word, int x, int y, boolean[][] visited, int idx) {

        int l = board.length;
        int w = board[0].length;

        if (idx == word.length()) {
            return true;
        }

        if (idx > word.length()) {
            return false;
        }

        // NOTE !!! we validate condition before go into `for loop and recursive call`
        if (x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || board[y][x] != word.charAt(idx)) {
            return false;
        }

        // int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        visited[y][x] = true;

        /**
         * NOTE !!!
         *
         * 1) we use below structure
         * if ( recursive_call_1 or recursive_call_2 ..) {
         * return true
         * }
         *
         * 2) since we need to `undo` visited record
         * so after above logic, we modify visited[y][x] back to false (e.g.
         * non-visited)
         *
         * 3) RETURN `false` at the final of recursive call
         * -> since it can reach this point,
         * -> means NOT POSSIBLE to find a solution
         * -> return false
         */

        if (canBuild(board, word, x + 1, y, visited, idx + 1) ||
                canBuild(board, word, x - 1, y, visited, idx + 1) ||
                canBuild(board, word, x, y + 1, visited, idx + 1) ||
                canBuild(board, word, x, y - 1, visited, idx + 1)) {
            return true;
        }

        // undo
        visited[y][x] = false;

        /**
         * 3) RETURN `false` at the final of recursive call
         */
        return false;
    }

    // V0-2
    // IDEA: TRIE + DFS + BACKTRACK + MAP (gpt)
    // Trie Node Class
//    class TrieNode {
//        Map<Character, TrieNode> children = new HashMap<>();
//        boolean isWord = false;
//    }
//
//    public List<String> findWords(char[][] board, String[] words) {
//        // Edge case: if the board is empty or no words to search for
//        if (board.length == 0 || board[0].length == 0 || words == null || words.length == 0) {
//            return new ArrayList<>();
//        }
//
//        int l = board.length;
//        int w = board[0].length;
//
//        // Step 1: Build the Trie
//        TrieNode root = buildTrie(words);
//
//        Set<String> result = new HashSet<>();
//        boolean[][] visited = new boolean[l][w];
//
//        // Step 2: DFS to search for words in the board
//        for (int i = 0; i < l; i++) {
//            for (int j = 0; j < w; j++) {
//                if (root.children.containsKey(board[i][j])) {
//                    dfs(board, i, j, root, visited, "", result);
//                }
//            }
//        }
//
//        return new ArrayList<>(result);
//    }
//
//    // Build Trie from the list of words
//    private TrieNode buildTrie(String[] words) {
//        TrieNode root = new TrieNode();
//        for (String word : words) {
//            TrieNode node = root;
//            for (char c : word.toCharArray()) {
//                node = node.children.computeIfAbsent(c, k -> new TrieNode());
//            }
//            node.isWord = true;
//        }
//        return root;
//    }
//
//    // Depth-First Search (DFS) to explore all possible paths
//    private void dfs(char[][] board, int i, int j, TrieNode node, boolean[][] visited, String currentWord, Set<String> result) {
//        // Check if out of bounds or already visited
//        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) {
//            return;
//        }
//
//        char c = board[i][j];
//        TrieNode nextNode = node.children.get(c);
//
//        // If the character is not in the Trie, stop
//        if (nextNode == null) {
//            return;
//        }
//
//        // Build the current word
//        currentWord += c;
//
//        // If we found a valid word, add it to the result
//        if (nextNode.isWord) {
//            result.add(currentWord);
//        }
//
//        // Mark as visited and explore the four possible directions (up, down, left, right)
//        visited[i][j] = true;
//        dfs(board, i + 1, j, nextNode, visited, currentWord, result);
//        dfs(board, i - 1, j, nextNode, visited, currentWord, result);
//        dfs(board, i, j + 1, nextNode, visited, currentWord, result);
//        dfs(board, i, j - 1, nextNode, visited, currentWord, result);
//
//        // Backtrack, mark the cell as unvisited
//        visited[i][j] = false;
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
