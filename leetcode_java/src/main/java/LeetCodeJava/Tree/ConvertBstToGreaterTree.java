package LeetCodeJava.Tree;

// https://leetcode.com/problems/convert-bst-to-greater-tree/submissions/1274802003/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 538. Convert BST to Greater Tree
 * Medium
 *
 * Given the root of a Binary Search Tree (BST), convert it to a Greater Tree such that every key of the original BST is changed to the original key plus the sum of all keys greater than the original key in BST.
 *
 * As a reminder, a binary search tree is a tree that satisfies these constraints:
 *
 * The left subtree of a node contains only nodes with keys less than the node's key.
 * The right subtree of a node contains only nodes with keys greater than the node's key.
 * Both the left and right subtrees must also be binary search trees.
 *
 * Example 1:
 *
 * https://assets.leetcode.com/uploads/2019/05/02/tree.png
 *
 * Input: root = [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
 * Output: [30,36,21,36,35,26,15,null,null,null,33,null,null,null,8]
 *
 * Example 2:
 *
 * Input: root = [0,null,1]
 * Output: [1,null,1]
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 10^4].
 * -10^4 <= Node.val <= 10^4
 * All the values in the tree are unique.
 * root is guaranteed to be a valid binary search tree.
 *
 * Note: This question is the same as 1038: https://leetcode.com/problems/binary-search-tree-to-greater-sum-tree/
 *
 */
public class ConvertBstToGreaterTree {

    // V0
    // IDEA : DFS + `reverse in-order` traverse (right -> node -> left)
    /**
     *  NOTE !!!
     *
     *   a BST's reverse in-order traverse (right -> node -> left)
     *   visits nodes in `descending` order,
     *   so when we reach a node, `runningSum` already holds the sum of
     *   all values GREATER than it -> we simply add it to the node val.
     */
    int runningSum = 0;

    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode convertBST(TreeNode root) {
        reverseInOrder(root);
        return root;
    }

    private void reverseInOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        // 1) visit `bigger` (right) sub tree first
        reverseInOrder(node.right);
        // 2) update current node with the accumulated `greater` sum
        runningSum += node.val;
        node.val = runningSum;
        // 3) then visit `smaller` (left) sub tree
        reverseInOrder(node.left);
    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/convert-bst-to-greater-tree/solutions/2823307/java-recursive-solution/
    static Set<Integer> set;
    static int sum;
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode convertBST_1(TreeNode root) {
        set = new TreeSet<>();
        sum = 0;
        traverseBST(root);
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer i : set) {
            map.put(i, sum);
            sum -= i;
        }
        traverseBSTAndPutNewValues(root, map);
        return root;
    }

    private void traverseBSTAndPutNewValues(TreeNode node, Map<Integer, Integer> map) {
        if (node == null) {
            return;
        }
        if (map.containsKey(node.val)) {
            node.val = map.get(node.val);
        }
        traverseBSTAndPutNewValues(node.left, map);
        traverseBSTAndPutNewValues(node.right, map);
    }

    private void traverseBST(TreeNode node) {
        if (node == null) {
            return;
        }

        if (set.add(node.val)) {
            sum += node.val;
        }
        traverseBST(node.left);
        traverseBST(node.right);
    }

    // V2
    // https://leetcode.com/problems/convert-bst-to-greater-tree/solutions/4932808/java-dept-first-search-beats-100-0ms/
    int store=0;
    /**
     * time = O(N)
     * space = O(H)
     */
    public void helper(TreeNode root,boolean flag){
        if(root==null){
            return ;
        }
        helper(root.right,false);
        if(!flag){
            int last=root.val;
            root.val+=store;
            store+=last;
            flag=true;
        }
        helper(root.left,false);

    }
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode convertBST_2(TreeNode root) {
        helper(root,false);
        return root;
    }


}
