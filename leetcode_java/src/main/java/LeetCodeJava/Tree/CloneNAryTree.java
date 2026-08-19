package LeetCodeJava.Tree;

// https://leetcode.com/problems/clone-n-ary-tree/

import java.util.ArrayList;
import java.util.List;

/**
 *  1490. Clone N-ary Tree
 *  Medium
 *
 *  Given a root of an N-ary tree, return a deep copy (clone) of the tree.
 *
 *  Each node in the n-ary tree contains a val (int) and a list (List[Node])
 *  of its children.
 *
 *  Example 1:
 *    Input: root = [1,null,3,2,4,null,5,6]
 *    Output: [1,null,3,2,4,null,5,6]
 *
 *  Example 2:
 *    Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 *    Output: [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 *
 *  Constraints:
 *    The depth of the n-ary tree is less than or equal to 1000.
 *    The total number of nodes is between [0, 10^4].
 *
 *  Follow up: Can your solution work for the graph problem?
 */
public class CloneNAryTree {

    // N-ary tree node (problem specific shape)
    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {
            this.children = new ArrayList<>();
        }

        public Node(int val) {
            this.val = val;
            this.children = new ArrayList<>();
        }

        public Node(int val, List<Node> children) {
            this.val = val;
            this.children = children;
        }
    }

    // V0
    // IDEA: POST-ORDER DFS (clone the children first, then build the parent)
    //       a tree has no shared nodes and no cycles, so no visited map is
    //       needed. NOTE: build a NEW children list -- reusing root.children
    //       would leave the clone pointing at the ORIGINAL nodes.
    //       for the graph follow-up an old -> new map is required so a
    //       revisited node returns its existing copy instead of recursing.
    /**
     * time = O(N)
     * space = O(H)   // H = depth (recursion stack)
     */
    public Node cloneTree(Node root) {
        if (root == null) {
            return null;
        }
        List<Node> children = new ArrayList<>();
        for (Node child : root.children) {
            children.add(cloneTree(child));
        }
        return new Node(root.val, children);
    }
}
