package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/extract-kth-character-from-the-rope-tree/

/**
 *  2689. Extract Kth Character From The Rope Tree
 *  Easy
 *
 *  You are given the root of a binary tree and an integer k. Besides the left and
 *  right children, every node of this tree has two other properties, a string
 *  node.val containing only lowercase English letters (possibly empty) and a
 *  non-negative integer node.len. There are two types of nodes in this tree:
 *   - Leaf: no children, node.len = 0, and node.val is some non-empty string.
 *   - Internal: at least one child (at most two), node.len > 0, node.val is empty.
 *
 *  The tree described above is called a Rope binary tree. We define S[node]
 *  recursively as follows:
 *   - If node is a leaf, S[node] = node.val
 *   - Otherwise S[node] = concat(S[node.left], S[node.right]) and
 *     S[node].length = node.len
 *
 *  Return the k-th character of the string S[root].
 *
 *  Example 1:
 *    Input: root = [10,4,"abcpoe","g","rta"], k = 6
 *    Output: "b"
 *    Explanation: S[root] = concat(concat("g","rta"), "abcpoe") = "grtaabcpoe",
 *                 so the 6th character is "b".
 *
 *  Example 2:
 *    Input: root = [12,6,6,"abc","efg","hij","klm"], k = 3
 *    Output: "c"
 *    Explanation: S[root] = "abcefghijklm", so the 3rd character is "c".
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^3]
 *    node.val contains only lowercase English letters
 *    0 <= node.val.length <= 50
 *    0 <= node.len <= 10^4
 *    for leaf nodes, node.len = 0 and node.val is non-empty
 *    for internal nodes, node.len > 0 and node.val is empty
 *    1 <= k <= S[root].length
 */
public class ExtractKthCharacterFromTheRopeTree {

    // Definition for a rope tree node (problem specific shape).
    public static class RopeTreeNode {
        public int len;
        public String val;
        public RopeTreeNode left;
        public RopeTreeNode right;

        public RopeTreeNode() {
            this.val = "";
        }

        public RopeTreeNode(String val) {
            this.val = val;
        }

        public RopeTreeNode(int len) {
            this.len = len;
            this.val = "";
        }

        public RopeTreeNode(int len, RopeTreeNode left, RopeTreeNode right) {
            this.len = len;
            this.val = "";
            this.left = left;
            this.right = right;
        }
    }

    // V0
    // IDEA: BINARY-SEARCH STYLE DESCENT (NEVER BUILD THE WHOLE STRING)
    //       every node already knows the length of the piece it produces:
    //         - internal node -> node.len
    //         - leaf node     -> node.val.length()   (its node.len is 0)
    //       so from an internal node we decide where the k-th char lives by only
    //       looking at the SIZE OF THE LEFT SUBTREE:
    //         k <= size(left) -> go left, k unchanged
    //         otherwise       -> go right with k -= size(left)
    //       NOTE: k is 1-indexed, so the answer is node.val.charAt(k - 1).
    //       NOTE: an internal node may have only ONE child - size(null) = 0 keeps
    //             that case working with no special branch.
    //       NOTE: written as a LOOP, so a degenerate chain-like tree can never
    //             blow the stack.
    /**
     * time = O(H)     // H = tree height
     * space = O(1)
     */
    public char getKthCharacter(RopeTreeNode root, int k) {
        RopeTreeNode node = root;
        while (node.len > 0) {          // internal -> keep descending
            int leftSize = size(node.left);
            if (k <= leftSize) {
                node = node.left;
            } else {
                k -= leftSize;
                node = node.right;
            }
        }
        return node.val.charAt(k - 1);
    }

    private int size(RopeTreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.len > 0 ? node.len : node.val.length();
    }
}
