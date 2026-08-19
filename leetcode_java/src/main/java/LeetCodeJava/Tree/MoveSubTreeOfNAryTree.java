package LeetCodeJava.Tree;

// https://leetcode.com/problems/move-sub-tree-of-n-ary-tree/

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  1516. Move Sub-Tree of N-Ary Tree
 *  Hard
 *
 *  Given the root of an N-ary tree of unique values, and two nodes of the tree
 *  p and q.
 *
 *  You should move the subtree of the node p to become a direct child of node
 *  q. If p is already a direct child of q, do not change anything. Node p must
 *  be the last child in the children list of node q.
 *
 *  Return the root of the tree after adjusting it.
 *
 *  There are 3 cases for nodes p and q:
 *    1. Node q is in the sub-tree of node p.
 *    2. Node p is in the sub-tree of node q.
 *    3. Neither p is in the sub-tree of q nor q is in the sub-tree of p.
 *
 *  In cases 2 and 3 you just move p (with its sub-tree) to be a child of q,
 *  but in case 1 the tree may be disconnected, so you need to reconnect it.
 *
 *  Example 1:
 *    Input: root = [1,null,2,3,null,4,5,null,6,null,7,8], p = 4, q = 1
 *    Output: [1,null,2,3,4,null,5,null,6,null,7,8]
 *    Explanation: case 2 - p is in the sub-tree of q, so p just moves under q
 *                 and becomes its LAST child.
 *
 *  Example 4:
 *    Input: root = [1,null,2,3,null,4], p = 1, q = 4
 *    Output: [4,null,1,null,2,3]
 *    Explanation: case 1 - q is in the sub-tree of p, so q is detached from
 *                 its parent, takes p's old slot (here: becomes the new root)
 *                 and p is appended as q's last child.
 *
 *  Constraints:
 *    The total number of nodes is between [2, 1000].
 *    Each node has a unique value.
 *    p != null, q != null, p != q
 */
public class MoveSubTreeOfNAryTree {

    // Definition for a Node (N-ary tree).
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
            this.children = children != null ? children : new ArrayList<Node>();
        }
    }

    // V0
    // IDEA: PARENT MAP + 3-CASE SURGERY
    //       build a parent pointer for every node with one iterative DFS, then
    //       walk up from q: if we ever hit p then q lives inside p's subtree
    //       (case 1).
    //         case 0 : p is already a direct child of q -> nothing to do.
    //         case 1 (q inside p's subtree) : cutting p out would strand the
    //                part of the tree above p, so first LIFT q into p's old
    //                slot -> detach q from its own parent, put q where p used
    //                to be (or make q the new root if p was the root), then
    //                append p as q's last child.
    //         case 2/3 : p's subtree does not contain q, so a plain move is
    //                safe -> detach p from its parent, append it to q.children.
    //       NOTE: in cases 2/3 p can never be the root (the root's subtree is
    //             the whole tree, which would put q inside it -> case 1), so p
    //             always has a parent there.
    /**
     * time = O(N)
     * space = O(N)
     */
    public Node moveSubTree(Node root, Node p, Node q) {
        if (q.children.contains(p)) {
            return root;
        }

        // iterative DFS -> parent of every node
        Map<Node, Node> parent = new HashMap<>();
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            for (Node child : node.children) {
                parent.put(child, node);
                stack.push(child);
            }
        }

        // is q inside p's subtree ?
        boolean qUnderP = false;
        Node cur = parent.get(q);
        while (cur != null) {
            if (cur == p) {
                qUnderP = true;
                break;
            }
            cur = parent.get(cur);
        }

        if (qUnderP) {
            parent.get(q).children.remove(q);
            if (p == root) {
                q.children.add(p);
                return q;
            }
            Node pp = parent.get(p);
            pp.children.set(pp.children.indexOf(p), q);
            q.children.add(p);
            return root;
        }

        parent.get(p).children.remove(p);
        q.children.add(p);
        return root;
    }
}
