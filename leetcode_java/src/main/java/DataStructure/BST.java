package DataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 *  BINARY SEARCH TREE -- an ordered symbol table
 *
 *  A BST stores KEY -> VALUE pairs, like a HashMap, but keeps the keys
 *  ORDERED. That ordering is what a hash table cannot give you: min,
 *  max, floor, ceiling, and an in-order (sorted) walk.
 *
 *  THE BST INVARIANT -- for every node:
 *
 *        every key in the        node       every key in the
 *        left subtree is   <--   key  -->   right subtree is
 *        SMALLER                            LARGER
 *
 *             S
 *           /   \
 *          E     X
 *         / \
 *        A   R
 *         \  /
 *          C H
 *
 *  Two consequences worth memorising:
 *    - get/put/delete follow ONE root-to-leaf path, so they cost O(H)
 *      where H is the height
 *    - an IN-ORDER walk emits the keys already SORTED
 *
 *  H is log N only while the tree stays balanced. Inserting keys in
 *  SORTED order degrades the BST into a linked list and every operation
 *  becomes O(N) -- that is exactly what AVL and red-black trees exist to
 *  prevent, and why java.util.TreeMap is a red-black tree rather than a
 *  plain BST.
 *
 *  Time  : get / put / delete / min / max  O(H)
 *          -> O(log N) balanced, O(N) worst
 *  Space : O(N) storage, O(H) recursion stack
 *
 *  See data_structure/python/binary_search_tree.py and
 *      data_structure/js/binary_search_tree.js for the same structure.
 *  Reference: https://www.coursera.org/learn/algorithms-part1/lecture/7An9B/binary-search-trees
 */
public class BST<Key extends Comparable<Key>, Value> {

    private Node root;
    private int size;

    private class Node {
        private final Key key;
        private Value val;
        private Node left;
        private Node right;

        Node(Key key, Value val) {
            this.key = key;
            this.val = val;
        }
    }

    /** Number of key-value pairs. */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    //--- put ------------------------------------------------------

    /** Insert, or overwrite the value if the key is already present. */
    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        root = put(root, key, val);
    }

    /**
     *  Each call returns the subtree that should replace the one it was
     *  given. That is what lets the recursion rewire parent pointers
     *  without ever holding a parent reference.
     */
    private Node put(Node x, Key key, Value val) {
        if (x == null) {                     // fell off the tree -> this is the spot
            size++;
            return new Node(key, val);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;                     // same key -> overwrite, do not grow
        }
        return x;
    }

    //--- get ------------------------------------------------------

    /** The value for `key`, or null. Iterative: no stack needed. */
    public Value get(Key key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                return x.val;
            }
        }
        return null;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    //--- min / max ------------------------------------------------

    /** The smallest key = the leftmost node. */
    public Key min() {
        return root == null ? null : min(root).key;
    }

    /** The largest key = the rightmost node. */
    public Key max() {
        if (root == null) {
            return null;
        }
        Node x = root;
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }

    private Node min(Node x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    //--- delete ---------------------------------------------------

    /**
     *  Remove `key`, keeping the BST invariant intact (Hibbard deletion).
     *
     *  Three cases once the node is found:
     *    0 children  drop it
     *    1 child     splice the child in where the node was
     *    2 children  replace it with its IN-ORDER SUCCESSOR -- the
     *                smallest key in the right subtree, which is the
     *                only key that keeps every comparison valid -- then
     *                delete that successor from the right subtree
     */
    public void delete(Key key) {
        if (contains(key)) {
            root = delete(root, key);
            size--;
        }
    }

    private Node delete(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = delete(x.left, key);
        } else if (cmp > 0) {
            x.right = delete(x.right, key);
        } else {
            if (x.left == null) {            // 0 or 1 child
                return x.right;
            }
            if (x.right == null) {
                return x.left;
            }
            Node successor = min(x.right);   // 2 children
            Node replacement = new Node(successor.key, successor.val);
            replacement.right = deleteMin(x.right);
            replacement.left = x.left;
            return replacement;
        }
        return x;
    }

    private Node deleteMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = deleteMin(x.left);
        return x;
    }

    //--- iteration ------------------------------------------------

    /** All keys in SORTED order -- the property a hash table cannot offer. */
    public Iterable<Key> keys() {
        List<Key> keys = new ArrayList<>();
        inorder(root, keys);
        return keys;
    }

    private void inorder(Node x, List<Key> keys) {
        if (x == null) {
            return;
        }
        inorder(x.left, keys);
        keys.add(x.key);
        inorder(x.right, keys);
    }

    /** Edges on the longest root-to-leaf path. Empty = -1, single node = 0. */
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) {
            return -1;
        }
        return 1 + Math.max(height(x.left), height(x.right));
    }

    public static void main(String[] args) {
        BST<String, Integer> bst = new BST<>();
        assertThat(bst.isEmpty(), "a new tree is empty");
        assertThat(bst.get("A") == null, "get on an empty tree is null");
        assertThat(bst.min() == null && bst.max() == null, "no min or max yet");

        String[] keys = {"S", "E", "A", "R", "C", "H", "X", "M", "P", "L"};
        for (int i = 0; i < keys.length; i++) {
            bst.put(keys[i], i);
        }
        assertThat(bst.size() == 10, "ten pairs");
        assertThat(bst.get("R") == 3, "lookup by key");
        assertThat(bst.contains("H") && !bst.contains("Z"), "contains");

        // an in-order walk of a BST is sorted -- the defining property
        assertThat(bst.keys().toString().equals("[A, C, E, H, L, M, P, R, S, X]"), "keys are sorted");
        assertThat(bst.min().equals("A") && bst.max().equals("X"), "min / max");

        // putting an existing key overwrites instead of growing the tree
        bst.put("R", 99);
        assertThat(bst.get("R") == 99 && bst.size() == 10, "overwrite, not insert");

        bst.delete("C");                     // a leaf
        assertThat(!bst.contains("C") && bst.size() == 9, "leaf removed");

        bst.delete("A");                     // one child
        assertThat(bst.keys().toString().equals("[E, H, L, M, P, R, S, X]"), "one-child node removed");

        bst.delete("E");                     // two children
        assertThat(bst.keys().toString().equals("[H, L, M, P, R, S, X]"), "two-child node removed");

        bst.delete("S");                     // the root, two children
        assertThat(bst.keys().toString().equals("[H, L, M, P, R, X]"), "root removed");

        bst.delete("ZZZ");                   // absent -> no-op
        assertThat(bst.size() == 6, "deleting an absent key changes nothing");

        // sorted input degrades the BST into a chain: height N-1, not log N
        BST<Integer, Integer> degenerate = new BST<>();
        for (int i = 1; i <= 5; i++) {
            degenerate.put(i, i);
        }
        assertThat(degenerate.height() == 4, "sorted input -> a linked list");

        System.out.println("keys  : " + bst.keys());
        System.out.println("height: " + bst.height());
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
