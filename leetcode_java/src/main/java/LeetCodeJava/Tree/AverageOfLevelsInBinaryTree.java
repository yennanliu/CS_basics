package LeetCodeJava.Tree;

// https://leetcode.com/problems/average-of-levels-in-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * 637. Average of Levels in Binary Tree
 * Easy
 *
 * Given the root of a binary tree, return the average value of the nodes on each level in the form of an array. Answers within 10^-5 of the actual answer will be accepted.
 *
 * Example 1:
 *
 * https://assets.leetcode.com/uploads/2021/03/09/avg1-tree.jpg
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [3.00000,14.50000,11.00000]
 * Explanation: The average value of nodes on level 0 is 3, on level 1 is 14.5, and on level 2 is 11.
 * Hence return [3, 14.5, 11].
 *
 * Example 2:
 *
 * https://assets.leetcode.com/uploads/2021/03/09/avg2-tree.jpg
 *
 * Input: root = [3,9,20,15,7]
 * Output: [3.00000,14.50000,11.00000]
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 10^4].
 * -2^31 <= Node.val <= 2^31 - 1
 *
 */
public class AverageOfLevelsInBinaryTree {


    // V0

    // V1
    // IDEA : BFS
    // https://leetcode.com/problems/average-of-levels-in-binary-tree/editorial/
    /**
     * time = O(N)
     * space = O(H)
     */
    public List < Double > averageOfLevels(TreeNode root) {
        List < Double > res = new ArrayList < > ();
        Queue < TreeNode > queue = new LinkedList < > ();
        queue.add(root);

        // NOTE here !!!
        while (!queue.isEmpty()) {
            long sum = 0, count = 0;
            Queue < TreeNode > temp = new LinkedList < > ();
            // NOTE here !!!
            while (!queue.isEmpty()) {
                TreeNode n = queue.poll();
                sum += n.val;
                count++;
                if (n.left != null)
                    temp.add(n.left);
                if (n.right != null)
                    temp.add(n.right);
            }
            // NOTE !!! we go through all elements in same layer via 2nd while loop, then calculate avg via below
            queue = temp;
            // NOTE !!! we get average in double via below
            res.add(sum * 1.0 / count);
        }
        return res;
    }

    // V1-1
    // IDEA : DFS
    // https://leetcode.com/problems/average-of-levels-in-binary-tree/editorial/
    /**
     * time = O(N)
     * space = O(H)
     */
    public List < Double > averageOfLevels_2(TreeNode root) {
        List < Integer > count = new ArrayList < > ();
        List < Double > res = new ArrayList < > ();
        average(root, 0, res, count);
        for (int i = 0; i < res.size(); i++)
            res.set(i, res.get(i) / count.get(i));
        return res;
    }
    /**
     * time = O(N)
     * space = O(H)
     */
    public void average(TreeNode t, int i, List < Double > sum, List < Integer > count) {
        if (t == null)
            return;
        if (i < sum.size()) {
            sum.set(i, sum.get(i) + t.val);
            count.set(i, count.get(i) + 1);
        } else {
            sum.add(1.0 * t.val);
            count.add(1);
        }
        average(t.left, i + 1, sum, count);
        average(t.right, i + 1, sum, count);
    }

}
