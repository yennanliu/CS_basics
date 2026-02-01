package LeetCodeJava.Trie;

// https://leetcode.com/problems/implement-trie-prefix-tree/
/**
 * 208. Implement Trie (Prefix Tree)
 * Solved
 * Medium
 * Topics
 * Companies
 * A trie (pronounced as "try") or prefix tree is a tree data structure used to efficiently store and retrieve keys in a dataset of strings. There are various applications of this data structure, such as autocomplete and spellchecker.
 *
 * Implement the Trie class:
 *
 * Trie() Initializes the trie object.
 * void insert(String word) Inserts the string word into the trie.
 * boolean search(String word) Returns true if the string word is in the trie (i.e., was inserted before), and false otherwise.
 * boolean startsWith(String prefix) Returns true if there is a previously inserted string word that has the prefix prefix, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input
 * ["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
 * [[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
 * Output
 * [null, null, true, false, true, null, true]
 *
 * Explanation
 * Trie trie = new Trie();
 * trie.insert("apple");
 * trie.search("apple");   // return True
 * trie.search("app");     // return False
 * trie.startsWith("app"); // return True
 * trie.insert("app");
 * trie.search("app");     // return True
 *
 *
 * Constraints:
 *
 * 1 <= word.length, prefix.length <= 2000
 * word and prefix consist only of lowercase English letters.
 * At most 3 * 104 calls in total will be made to insert, search, and startsWith.
 *
 *
 */
import java.util.HashMap;
import java.util.Map;

public class ImplementTrie {

    /**
     * Your Trie object will be instantiated and called as such:
     * Trie obj = new Trie();
     * obj.insert(word);
     * boolean param_2 = obj.search(word);
     * boolean param_3 = obj.startsWith(prefix);
     */
    // V0
    // IDEA : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Tree/implement-trie-prefix-tree.py#L49
    // modified by GPT
    /**
     * NOTE !!!
     *
     *   - define TrieNode (or TreeNode) first
     *       - (2 attrs: Map<String, TrieNode> children, boolean isWord)
     *
     *   - then define Trie (ONLY 1 attr: TrieNode root;)
     *
     *
     *   -> and in fact, we MAINLY use `TrieNode` (in below methods)
     *      `Trie` is more like a nominal class
     *         -> we define it, but NOT use it directly
     */
    /**
     *  NOTE !!!  ONLY 2 attr for `custom node`
     *
     *   1. Map<String TrieNode> children;
     *   2. boolean isEnd;
     *
     *
     *  -> NO need to setup `value`, value already included in the `children` attr
     *     e.g. `String` part of the `Map<String, TrieNode>`
     *
     */
    class TrieNode {

        /** NOTE !!!!
         *
         *  Define children as map structure:
         *
         *   Map<String, TrieNode>
         *
         *  -> string : current "element"
         *  -> TrieNode : the child object
         *
         */
        Map<String, TrieNode> children;
        boolean isWord;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public TrieNode() {
            children = new HashMap<>();
            isWord = false;
        }
    }

    /**
     *  NOTE !!!
     *
     *  Define 2 classes
     *
     *   1) TrieNode
     *   2) Trie
     *
     */
    class Trie {
        TrieNode root;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public Trie() {

            root = new TrieNode();
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public void insert(String word) {
            /**
             *  NOTE !!! get current node first
             */
            TrieNode cur = root;
            for (String c : word.split("")) {
                cur.children.putIfAbsent(c, new TrieNode());
                // move node to its child
                cur = cur.children.get(c);
            }
            cur.isWord = true;
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public boolean search(String word) {
            /**
             *  NOTE !!! get current node first
             */
            TrieNode cur = root;
            for (String c : word.split("")) {
                cur = cur.children.get(c);
                if (cur == null) {
                    return false;
                }
            }
            return cur.isWord;
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public boolean startsWith(String prefix) {
            /**
             *  NOTE !!! get current node first
             */
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

    // V1-1
    // https://neetcode.io/problems/implement-prefix-tree
    // IDEA: ARRAY
//    public class TrieNode {
//        TrieNode[] children = new TrieNode[26];
//        boolean endOfWord = false;
//    }
//
//    public class PrefixTree {
//        private TrieNode root;
//
//        public PrefixTree() {
//            root = new TrieNode();
//        }
//
//        public void insert(String word) {
//            TrieNode cur = root;
//            for (char c : word.toCharArray()) {
//                int i = c - 'a';
//                if (cur.children[i] == null) {
//                    cur.children[i] = new TrieNode();
//                }
//                cur = cur.children[i];
//            }
//            cur.endOfWord = true;
//        }
//
//        public boolean search(String word) {
//            TrieNode cur = root;
//            for (char c : word.toCharArray()) {
//                int i = c - 'a';
//                if (cur.children[i] == null) {
//                    return false;
//                }
//                cur = cur.children[i];
//            }
//            return cur.endOfWord;
//        }
//
//        public boolean startsWith(String prefix) {
//            TrieNode cur = root;
//            for (char c : prefix.toCharArray()) {
//                int i = c - 'a';
//                if (cur.children[i] == null) {
//                    return false;
//                }
//                cur = cur.children[i];
//            }
//            return true;
//        }
//    }


    // V1-2
    // https://neetcode.io/problems/implement-prefix-tree
    // IDEA:
//    public class TrieNode {
//        HashMap<Character, TrieNode> children = new HashMap<>();
//        boolean endOfWord = false;
//    }
//
//    public class PrefixTree {
//        private TrieNode root;
//
//        public PrefixTree() {
//            root = new TrieNode();
//        }
//
//        public void insert(String word) {
//            TrieNode cur = root;
//            for (char c : word.toCharArray()) {
//                cur.children.putIfAbsent(c, new TrieNode());
//                cur = cur.children.get(c);
//            }
//            cur.endOfWord = true;
//        }
//
//        public boolean search(String word) {
//            TrieNode cur = root;
//            for (char c : word.toCharArray()) {
//                if (!cur.children.containsKey(c)) {
//                    return false;
//                }
//                cur = cur.children.get(c);
//            }
//            return cur.endOfWord;
//        }
//
//        public boolean startsWith(String prefix) {
//            TrieNode cur = root;
//            for (char c : prefix.toCharArray()) {
//                if (!cur.children.containsKey(c)) {
//                    return false;
//                }
//                cur = cur.children.get(c);
//            }
//            return true;
//        }
//    }

    // V2
    // https://leetcode.com/problems/implement-trie-prefix-tree/editorial/
    class TrieNode2 {

        // R links to node children
        private TrieNode2[] links;

        private final int R = 26;

        private boolean isEnd;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public TrieNode2() {
            links = new TrieNode2[R];
        }

        /**
         * time = O(L)
         * space = O(1)
         */
        public boolean containsKey(char ch) {
            return links[ch -'a'] != null;
        }
        /**
         * time = O(L)
         * space = O(1)
         */
        public TrieNode2 get(char ch) {
            return links[ch -'a'];
        }
        /**
         * time = O(L)
         * space = O(1)
         */
        public void put(char ch, TrieNode2 node) {
            links[ch -'a'] = node;
        }
        /**
         * time = O(L)
         * space = O(1)
         */
        public void setEnd() {
            isEnd = true;
        }
        /**
         * time = O(L)
         * space = O(1)
         */
        public boolean isEnd() {
            return isEnd;
        }
    }


    class Trie2 {
        private TrieNode2 root;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public Trie2() {
            root = new TrieNode2();
        }

        // Inserts a word into the trie.
        /**
         * time = O(L)
         * space = O(1)
         */
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
        /**
         * time = O(L)
         * space = O(1)
         */
        public boolean search(String word) {
            TrieNode2 node = searchPrefix(word);
            return node != null && node.isEnd();
        }

        // Returns if there is any word in the trie
        // that starts with the given prefix.
        /**
         * time = O(L)
         * space = O(1)
         */
        public boolean startsWith(String prefix) {
            TrieNode2 node = searchPrefix(prefix);
            return node != null;
        }

    }


    // V3
    // https://leetcode.com/problems/implement-trie-prefix-tree/solutions/3309950/java-easiest-solution/
    class TrieNode3 {
        boolean isWord;
        TrieNode3[] children;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public TrieNode3() {
            isWord = false;
            children = new TrieNode3[26]; // 26 English lowercase letters
        }
    }

    class Trie3 {
        TrieNode3 root;

        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public Trie3() {
            root = new TrieNode3();
        }

        /**
         * time = O(L)
         * space = O(1)
         */
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

        /**
         * time = O(L)
         * space = O(1)
         */
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

        /**
         * time = O(L)
         * space = O(1)
         */
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

    // V4
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
            /**
             * time = O(L)
             * space = O(ALPHABET_SIZE * L * N)
             */
            public TrieNode() {
                isComplete = false;
                children = new TrieNode[26];
            }
        }

        TrieNode root;  // Declare the root node
        /**
         * time = O(L)
         * space = O(ALPHABET_SIZE * L * N)
         */
        public Trie4() {
            root = new TrieNode();  // initialize the root node
        }

        /**
         * time = O(L)
         * space = O(1)
         */
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

        /**
         * time = O(L)
         * space = O(1)
         */
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

        /**
         * time = O(L)
         * space = O(1)
         */
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
