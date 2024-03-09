package LeetCodeJava.Trie;

// https://leetcode.com/problems/implement-trie-prefix-tree/

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */
import java.util.HashMap;
import java.util.Map;

public class ImplementTrie {

    // V0
    // IDEA : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Tree/implement-trie-prefix-tree.py#L49
    // modified by GPT
    class TrieNode {
        Map<String, TrieNode> children;
        boolean isWord;

        public TrieNode() {
            children = new HashMap<>();
            isWord = false;
        }
    }

    class Trie {
        TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode cur = root;
            for (String c : word.split("")) {
                cur.children.putIfAbsent(c, new TrieNode());
                cur = cur.children.get(c);
            }
            cur.isWord = true;
        }

        public boolean search(String word) {
            TrieNode cur = root;
            for (String c : word.split("")) {
                cur = cur.children.get(c);
                if (cur == null) {
                    return false;
                }
            }
            return cur.isWord;
        }

        public boolean startsWith(String prefix) {
            TrieNode cur = root;
            for (String c : prefix.split("")) {
                cur = cur.children.get(c);
                if (cur == null) {
                    return false;
                }
            }
            return true;
        }
    }

    // V1
    // https://leetcode.com/problems/implement-trie-prefix-tree/editorial/
    class TrieNode2 {

        // R links to node children
        private TrieNode2[] links;

        private final int R = 26;

        private boolean isEnd;

        public TrieNode2() {
            links = new TrieNode2[R];
        }

        public boolean containsKey(char ch) {
            return links[ch -'a'] != null;
        }
        public TrieNode2 get(char ch) {
            return links[ch -'a'];
        }
        public void put(char ch, TrieNode2 node) {
            links[ch -'a'] = node;
        }
        public void setEnd() {
            isEnd = true;
        }
        public boolean isEnd() {
            return isEnd;
        }
    }


    class Trie2 {
        private TrieNode2 root;

        public Trie2() {
            root = new TrieNode2();
        }

        // Inserts a word into the trie.
        public void insert(String word) {
            TrieNode2 node = root;
            for (int i = 0; i < word.length(); i++) {
                char currentChar = word.charAt(i);
                if (!node.containsKey(currentChar)) {
                    node.put(currentChar, new TrieNode2());
                }
                node = node.get(currentChar);
            }
            node.setEnd();
        }


        // search a prefix or whole key in trie and
        // returns the node where search ends
        private TrieNode2 searchPrefix(String word) {
            TrieNode2 node = root;
            for (int i = 0; i < word.length(); i++) {
                char curLetter = word.charAt(i);
                if (node.containsKey(curLetter)) {
                    node = node.get(curLetter);
                } else {
                    return null;
                }
            }
            return node;
        }

        // Returns if the word is in the trie.
        public boolean search(String word) {
            TrieNode2 node = searchPrefix(word);
            return node != null && node.isEnd();
        }

        // Returns if there is any word in the trie
        // that starts with the given prefix.
        public boolean startsWith(String prefix) {
            TrieNode2 node = searchPrefix(prefix);
            return node != null;
        }

    }


    // V2
    // https://leetcode.com/problems/implement-trie-prefix-tree/solutions/3309950/java-easiest-solution/
    class TrieNode3 {
        boolean isWord;
        TrieNode3[] children;

        public TrieNode3() {
            isWord = false;
            children = new TrieNode3[26]; // 26 English lowercase letters
        }
    }

    class Trie3 {
        TrieNode3 root;

        public Trie3() {
            root = new TrieNode3();
        }

        public void insert(String word) {
            TrieNode3 node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode3();
                }
                node = node.children[index];
            }
            node.isWord = true;
        }

        public boolean search(String word) {
            TrieNode3 node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return false;
                }
                node = node.children[index];
            }
            return node.isWord;
        }

        public boolean startsWith(String prefix) {
            TrieNode3 node = root;
            for (char c : prefix.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return false;
                }
                node = node.children[index];
            }
            return true;
        }
    }

    // V3
    // https://leetcode.com/problems/implement-trie-prefix-tree/solutions/3308019/simple-implementation-in-java/
    class Trie4 {
        // Trie Node class
        class TrieNode {
            // to mark the last letter of a string as true
            // that yes, a word ends at this letter
            boolean isComplete;
            // a node array of size 26
            // to store nodes for 26 lowercase alphabets
            TrieNode[] children;
            public TrieNode() {
                isComplete = false;
                children = new TrieNode[26];
            }
        }

        TrieNode root;  // Declare the root node
        public Trie4() {
            root = new TrieNode();  // initialize the root node
        }

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
            node.isComplete = true;
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
            return node.isComplete;
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

}
