package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/all-elements-in-two-binary-search-trees/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 1305. All Elements in Two Binary Search Trees
 * Medium
 *
 * Given two binary search trees root1 and root2, return a list containing all the integers
 * from both trees sorted in ascending order.
 *
 *
 * Example 1:
 *
 * Input: root1 = [2,1,4], root2 = [1,0,3]
 * Output: [0,1,1,2,3,4]
 *
 * Example 2:
 *
 * Input: root1 = [1,null,8], root2 = [8,1]
 * Output: [1,1,8,8]
 *
 *
 * Constraints:
 *
 * The number of nodes in each tree is in the range [0, 5000].
 * -10^5 <= Node.val <= 10^5
 *
 */
public class AllElementsInTwoBinarySearchTrees {

    // V0
    // IDEA: INORDER + TWO POINTERS MERGE (never re-sort)
    //       an inorder walk of a BST already yields a sorted list, so walk both
    //       trees, then merge the two sorted lists like merge sort's merge step.
    //       (sorting the concatenation would cost O((M+N) log(M+N)).)
    /**
     * time = O(M + N)
     * space = O(M + N)
     */
    public List<Integer> getAllElements(TreeNode root1, TreeNode root2) {
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        inorder(root1, first);
        inorder(root2, second);

        List<Integer> merged = new ArrayList<>(first.size() + second.size());
        int i = 0;
        int j = 0;
        // take the smaller head each time
        while (i < first.size() && j < second.size()) {
            if (first.get(i) <= second.get(j)) {
                merged.add(first.get(i));
                i++;
            } else {
                merged.add(second.get(j));
                j++;
            }
        }
        // one list is exhausted; append whatever is left of the other
        while (i < first.size()) {
            merged.add(first.get(i));
            i++;
        }
        while (j < second.size()) {
            merged.add(second.get(j));
            j++;
        }
        return merged;
    }

    /** Appends the subtree's values to out in ascending order. */
    private void inorder(TreeNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        inorder(node.left, out);
        out.add(node.val);
        inorder(node.right, out);
    }
}
