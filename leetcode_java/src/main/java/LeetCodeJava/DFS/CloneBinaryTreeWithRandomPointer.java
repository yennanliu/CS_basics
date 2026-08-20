package LeetCodeJava.DFS;

// https://leetcode.com/problems/clone-binary-tree-with-random-pointer/

import java.util.HashMap;
import java.util.Map;

/**
 *  1485. Clone Binary Tree With Random Pointer
 *  Medium
 *
 *  A binary tree is given such that each node contains an additional random pointer
 *  which could point to any node in the tree or null.
 *
 *  Return a deep copy of the tree.
 *
 *  The tree is represented in the same input/output way as normal binary trees where
 *  each node is represented as a pair of [val, random_index] where:
 *    val: an integer representing Node.val
 *    random_index: the index of the node (in the input) where the random pointer
 *                  points to, or null if it does not point to any node.
 *
 *  You will be given the tree in class Node and you should return the cloned tree in
 *  class NodeCopy. NodeCopy class is just a clone of Node class with the same
 *  attributes and constructors.
 *
 *  Example 1:
 *    Input: root = [[1,null],null,[4,3],[7,0]]
 *    Output: [[1,null],null,[4,3],[7,0]]
 *    Explanation: The original binary tree is [1,null,4,7]. The random pointer of
 *                 node 4 is node 7, and the random pointer of node 7 is node 1.
 *
 *  Example 2:
 *    Input: root = [[1,4],null,[1,0],null,[1,5],[1,5]]
 *    Output: [[1,4],null,[1,0],null,[1,5],[1,5]]
 *    Explanation: The random pointer of a node can be the node itself.
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [0, 1000].
 *    1 <= Node.val <= 10^6
 */
public class CloneBinaryTreeWithRandomPointer {

    // problem-specific node shapes (LC gives these as separate classes)
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node random;

        public Node() {
        }

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, Node left, Node right, Node random) {
            this.val = val;
            this.left = left;
            this.right = right;
            this.random = random;
        }
    }

    public static class NodeCopy {
        public int val;
        public NodeCopy left;
        public NodeCopy right;
        public NodeCopy random;

        public NodeCopy() {
        }

        public NodeCopy(int val) {
            this.val = val;
        }

        public NodeCopy(int val, NodeCopy left, NodeCopy right, NodeCopy random) {
            this.val = val;
            this.left = left;
            this.right = right;
            this.random = random;
        }
    }

    // V0
    // IDEA: DFS + OLD->NEW MAP (create the copy BEFORE recursing)
    //       the random pointers turn the structure into a general graph, so a plain
    //       tree copy would loop forever or duplicate nodes.
    //       keep mp[original] = copy and register the copy the MOMENT it is created,
    //       then wire up left / right / random recursively.
    //       NOTE: registering first is what breaks the cycles - a random pointer back
    //             into an ancestor (or into the node itself) finds the copy in mp
    //             instead of recursing again.
    /**
     * time = O(n)
     * space = O(n)
     */
    public NodeCopy copyRandomBinaryTree(Node root) {
        Map<Node, NodeCopy> mp = new HashMap<>();
        return dfs(root, mp);
    }

    private NodeCopy dfs(Node node, Map<Node, NodeCopy> mp) {
        if (node == null) {
            return null;
        }
        if (mp.containsKey(node)) {
            return mp.get(node);
        }
        NodeCopy copy = new NodeCopy(node.val);
        mp.put(node, copy); // register BEFORE recursing -> cycles terminate
        copy.left = dfs(node.left, mp);
        copy.right = dfs(node.right, mp);
        copy.random = dfs(node.random, mp);
        return copy;
    }
}
