package DataStructure;

// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Trie/ImplementTrie.java

public class Trie {

    /**  NOTE !!!
     *
     *  we define `sub class` TrieNode
     */
    class TrieNode{

        TrieNode[] children;
        boolean isEnd;

        public TrieNode(){
            children = new TrieNode[26]; // NOTE !!! this
            isEnd = false;
        }
    }

    // attr
    TrieNode root;

    // constructor
    public Trie(){
        root = new TrieNode();
    }

    // method
    public void insert(String word) {
        TrieNode node  = root;  // start traversing from the root
        for (char c : word.toCharArray()) {
            // if the node index for the character is empty
            if (node.children[c - 'a'] == null) {
                // create a new node at that index
                node.children[c - 'a'] = new TrieNode();
            }
            // jump to that node
            node = node.children[c - 'a'];
        }
        // mark the last letter as true
        // means a word ends at this letter
        node.isEnd = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            // if the node index for the character is empty
            if (node.children[c - 'a'] == null) {
                return false;   // means word doesn't exist
            }
            // jump to the character node
            node = node.children[c - 'a'];
        }
        // return if the last letter is marked as true/false
        // if true -> a word ends at this character otherwise not
        // if this exact word were inserted, we would have marked
        // the last character as true for sure
        return node.isEnd;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            // if the node index for the character is empty
            if (node.children[c - 'a'] == null) {
                return false;
            }
            node = node.children[c - 'a'];
        }
        // all the character nodes were found in the tree
        return true;    // means prefix exists
    }


}
