package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/two-sum-bsts/description/
// https://leetcode.ca/2019-03-28-1214-Two-Sum-BSTs/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 1214. Two Sum BSTs
 * Medium
 *
 * Given the roots of two binary search trees, root1 and root2, return true if and only if
 * there is a node in the first tree and a node in the second tree whose values sum up to a
 * given integer target.
 *
 * Example 1:
 *
 * Input: root1 = [2,1,4], root2 = [1,0,3], target = 5
 * Output: true
 * Explanation: 2 and 3 sum up to 5.
 *
 * Example 2:
 *
 * Input: root1 = [0,-10,10], root2 = [5,1,7,0,2], target = 18
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in each tree is in the range [1, 5000].
 * -10^9 <= Node.val, target <= 10^9
 *
 */
public class TwoSumBSTs {

    // V0
    // IDEA: IN-ORDER TRAVERSAL (-> sorted list) + TWO POINTERS
    //       an in-order walk of a BST yields its values sorted, so this becomes
    //       "two sum on two sorted arrays": one pointer climbs tree1's ascending
    //       list, the other descends tree2's list from its largest value.
    /**
     * time = O(M + N)
     * space = O(M + N)
     */
    public boolean twoSumBSTs(TreeNode root1, TreeNode root2, int target) {
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        inorder(root1, first);
        inorder(root2, second);

        int i = 0;
        int j = second.size() - 1;
        while (i < first.size() && j >= 0) {
            // long : two values of up to 1e9 overflow an int
            long sum = (long) first.get(i) + second.get(j);
            if (sum == target) {
                return true;
            } else if (sum < target) {
                // need a bigger sum -> next larger value from tree1
                i++;
            } else {
                // need a smaller sum -> next smaller value from tree2
                j--;
            }
        }
        return false;
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

    // V1
    // IDEA: HASH SET
    //       put every value of tree1 into a set, then for each value v of tree2
    //       check whether (target - v) is in the set.
    /**
     * time = O(M + N)
     * space = O(M)
     */
    public boolean twoSumBSTs_1(TreeNode root1, TreeNode root2, int target) {
        Set<Long> seen = new HashSet<>();
        collect(root1, seen);
        return search(root2, seen, target);
    }

    /** Adds every value in the subtree to seen. */
    private void collect(TreeNode node, Set<Long> seen) {
        if (node == null) {
            return;
        }
        seen.add((long) node.val);
        collect(node.left, seen);
        collect(node.right, seen);
    }

    /** True if some node's complement (target - val) is in seen. */
    private boolean search(TreeNode node, Set<Long> seen, int target) {
        if (node == null) {
            return false;
        }
        if (seen.contains((long) target - node.val)) {
            return true;
        }
        return search(node.left, seen, target) || search(node.right, seen, target);
    }
}
