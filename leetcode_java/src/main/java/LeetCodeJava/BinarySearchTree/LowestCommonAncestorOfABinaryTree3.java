package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/
// https://leetcode.ca/all/1650.html

import java.util.HashSet;
import java.util.Set;

/**
 * 1650. Lowest Common Ancestor of a Binary Tree III
 * Given two nodes of a binary tree p and q, return their lowest common ancestor (LCA).
 *
 * Each node will have a reference to its parent node. The definition for Node is below:
 *
 * class Node {
 *     public int val;
 *     public Node left;
 *     public Node right;
 *     public Node parent;
 * }
 * According to the definition of LCA on Wikipedia: "The lowest common ancestor of two nodes p and q in a tree T is the lowest node that has both p and q as descendants (where we allow a node to be a descendant of itself)."
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * Output: 3
 * Explanation: The LCA of nodes 5 and 1 is 3.
 * Example 2:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
 * Output: 5
 * Explanation: The LCA of nodes 5 and 4 is 5 since a node can be a descendant of itself according to the LCA definition.
 * Example 3:
 *
 * Input: root = [1,2], p = 1, q = 2
 * Output: 1
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 105].
 * -109 <= Node.val <= 109
 * All Node.val are unique.
 * p != q
 * p and q exist in the tree.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Facebook LinkedIn Microsoft
 * Problem Solution
 * 1650-Lowest-Common-Ancestor-of-a-Binary-Tree-III
 * All Problems:
 *
 *
 */
public class LowestCommonAncestorOfABinaryTree3 {

    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
    };

    // V0
    // HASHSET + `2 POINTER`
    // e.g.
    //      setup a pointer traverse and collect all p's parent
    //      the other pointer traverse and collect all q's parent
    public Node lowestCommonAncestor(Node p, Node q) {
        // Step 1: Traverse both paths
        Set<Node> ancestors = new HashSet<>();

        // Traverse from node p to the root, storing all ancestors
        while (p != null) {
            ancestors.add(p);
            p = p.parent;
        }

        // Traverse from node q upwards, the first common ancestor is the LCA
        while (q != null) {
            if (ancestors.contains(q)) {
                return q;  // First common ancestor found
            }
            q = q.parent;
        }

        return null;  // In case there's no common ancestor (shouldn't happen in this problem)
    }

    // V0-1
    // IDEA: 2 POINTERS (gpt)
    public Node lowestCommonAncestor_0_1(Node p, Node q) {
        Node a = p;
        Node b = q;

        // Loop until both pointers meet
        while (a != b) {
            // Move a to its parent, or switch to q if null
            a = (a == null) ? q : a.parent;

            // Move b to its parent, or switch to p if null
            b = (b == null) ? p : b.parent;
        }

        // Either both are null or both are at LCA
        return a;
    }

    // V0-2
    // IDEA: SET + 2 POINTERS (gpt)
    public Node lowestCommonAncestor_0_2(Node p, Node q) {
        // Step 1: Traverse both paths
        Set<Node> ancestors = new HashSet<>();

        // Traverse from node p to the root, storing all ancestors
        while (p != null) {
            ancestors.add(p);
            p = p.parent;
        }

        // Traverse from node q upwards, the first common ancestor is the LCA
        while (q != null) {
            if (ancestors.contains(q)) {
                return q;  // First common ancestor found
            }
            q = q.parent;
        }

        return null;  // In case there's no common ancestor (shouldn't happen in this problem)
    }

    // V1-1
    // https://leetcode.ca/2020-06-06-1650-Lowest-Common-Ancestor-of-a-Binary-Tree-III/
    // IDEA: SET
    /** NOTE !!
     *
     *   -> NO recursive call in below approach
     */
    public Node lowestCommonAncestor_1_1(Node p, Node q) {

        // NOTE !!! we init set
        Set<Node> set = new HashSet<Node>();

        /**  NOTE !!!
         *
         *  we start from `p` node, and keep finding its parent,
         *  and save node to set as well
         */
        Node temp = p;
        while (temp != null) {
            set.add(temp);
            temp = temp.parent;
        }


        /**  NOTE !!!
         *
         *  we start from `q` node, and keep finding its parent,
         *  and save node to set as well
         */
        temp = q;
        while (temp != null) {
            /**  NOTE !!!
             *
             *
             *    if `set` already contain current node,
             *    -> means we already found a LCA,
             *    -> break the while loop directly, and return current node as answer
             */
            if (set.contains(temp))
                break;
            else
                temp = temp.parent;
        }
        return temp;
    }

    // V1-2
    // https://leetcode.ca/2020-06-06-1650-Lowest-Common-Ancestor-of-a-Binary-Tree-III/
    // IDEA: 2 POINTERS
    public Node lowestCommonAncestor_1_2(Node p, Node q) {
        Node a = p, b = q;
        while (a != b) {
            a = a.parent == null ? q : a.parent;
            b = b.parent == null ? p : b.parent;
        }
        return a;
    }


    // V2

}
