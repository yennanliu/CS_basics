package DataStructure;

// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Trie/ImplementTrie.java

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

import java.util.HashMap;
import java.util.Map;

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
    /**  NOTE !!!
     *
     *  `Map<String, TrieNode>` structure for children
     *
     *  (but NOT Map<String, List<TrieNode>))
     */
    Map<String, TrieNode> children;
    boolean isEnd;

    public TrieNode() {
        children = new HashMap<>();
        isEnd = false;
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
class Trie2 {
    TrieNode root;

    public Trie2() {

        root = new TrieNode();
    }

    public void insert(String word) {
        /**
         *  NOTE !!! get current node first
         */
        TrieNode cur = root;
        for (String c : word.split("")) {
            /**
             *  NOTE !!!
             *
             *   we put `k-v` to `children` attr,
             *   instead of cur itself, so it's OK
             *   that the `Map<String, TrieNode> children` structure
             *   to save same `parent` but different child
             *   e.g.
             *    a-b-c
             *    a-d-j
             *
             *  Example:
             *
             *  ⸻
             *
             * 🧠 Example Words:
             *
             * abc
             * akg
             *
             * ----------------
             *
             * Let’s insert "abc" first.
             *
             * Step 1 — Insert “abc”:
             * 	•	Start at root
             * 	•	Insert 'a' → create a new node for 'a'
             * 	•	Insert 'b' → under 'a', create a child for 'b'
             * 	•	Insert 'c' → under 'b', create a child for 'c'
             * 	•	Mark 'c' node’s isWord = true
             *
             * 👉 Trie after first insert:
             *
             * (root)
             *   └── "a"
             *        └── "b"
             *             └── "c"  (isWord=true)
             *
             *
             * ⸻
             *
             * Step 2 — Insert “akg”:
             * 	•	Start at root
             * 	•	'a': already exists, reuse it
             * 	•	'k': under 'a', insert a new child 'k'
             * 	•	'g': under 'k', insert a new child 'g'
             * 	•	mark 'g' node’s isWord = true
             *
             * 👉 Trie after inserting "akg":
             *
             * (root)
             *   └── "a"
             *        ├── "b"
             *        │     └── "c"  (isWord=true)
             *        └── "k"
             *              └── "g"  (isWord=true)
             *
             */
            cur.children.putIfAbsent(c, new TrieNode());
            // move node to its child
            cur = cur.children.get(c);
        }
        cur.isEnd = true;
    }

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
        return cur.isEnd;
    }

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
