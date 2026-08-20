package LeetCodeJava.Design;

// https://leetcode.com/problems/implement-trie-ii-prefix-tree/

/**
 *  1804. Implement Trie II (Prefix Tree)
 *  Medium
 *
 *  A trie (pronounced as "try") or prefix tree is a tree data structure used to
 *  efficiently store and retrieve keys in a dataset of strings.
 *
 *  Implement the Trie class:
 *    Trie() Initializes the trie object.
 *    void insert(String word) Inserts the string word into the trie.
 *    int countWordsEqualTo(String word) Returns the number of instances of the
 *      string word in the trie.
 *    int countWordsStartingWith(String prefix) Returns the number of strings in the
 *      trie that have the string prefix as a prefix.
 *    void erase(String word) Erases the string word from the trie.
 *
 *  Example 1:
 *    Input
 *      ["Trie","insert","insert","countWordsEqualTo","countWordsStartingWith",
 *       "erase","countWordsEqualTo","countWordsStartingWith","erase",
 *       "countWordsStartingWith"]
 *      [[],["apple"],["apple"],["apple"],["app"],["apple"],["apple"],["app"],
 *       ["apple"],["app"]]
 *    Output
 *      [null,null,null,2,2,null,1,1,null,0]
 *
 *  Constraints:
 *    1 <= word.length, prefix.length <= 2000
 *    word and prefix consist only of lowercase English letters.
 *    At most 3 * 10^4 calls in total will be made to insert, countWordsEqualTo,
 *      countWordsStartingWith and erase.
 *    It is guaranteed that for any call to erase, the string word exists in the trie.
 */
public class ImplementTrieIIPrefixTree {

    // V0
    // IDEA: TRIE WITH *TWO COUNTERS* PER NODE (word count + prefix count)
    //
    //       each node keeps
    //         wordCnt   : how many inserted words END here    -> countWordsEqualTo
    //         prefixCnt : how many inserted words PASS through -> countWordsStartingWith
    //
    //       insert : ++prefixCnt on every node along the path, ++wordCnt on the last.
    //       erase  : the mirror image, --prefixCnt along the path, --wordCnt on the last.
    //
    //       erase is guaranteed to be called only for a word that exists, so a node
    //       on the path can never be missing and no counter can go negative; the now
    //       empty nodes can simply be left in place -- prefixCnt == 0 answers 0 anyway.
    /**
     * time = O(L) per operation, L = word / prefix length
     * space = O(total inserted characters)
     */
    private static class TrieNode {
        TrieNode[] next = new TrieNode[26];
        int wordCnt;
        int prefixCnt;
    }

    private final TrieNode root;

    public ImplementTrieIIPrefixTree() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode cur = root;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'a';
            if (cur.next[c] == null) {
                cur.next[c] = new TrieNode();
            }
            cur = cur.next[c];
            cur.prefixCnt++;
        }
        cur.wordCnt++;
    }

    public int countWordsEqualTo(String word) {
        TrieNode node = find(word);
        return node == null ? 0 : node.wordCnt;
    }

    public int countWordsStartingWith(String prefix) {
        TrieNode node = find(prefix);
        return node == null ? 0 : node.prefixCnt;
    }

    public void erase(String word) {
        TrieNode cur = root;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'a';
            cur = cur.next[c]; // guaranteed to exist
            cur.prefixCnt--;
        }
        cur.wordCnt--;
    }

    private TrieNode find(String s) {
        TrieNode cur = root;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            if (cur.next[c] == null) {
                return null;
            }
            cur = cur.next[c];
        }
        return cur;
    }
}
