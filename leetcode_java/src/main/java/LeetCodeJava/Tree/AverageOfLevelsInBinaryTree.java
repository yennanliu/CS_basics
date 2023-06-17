package LeetCodeJava.Tree;

// https://leetcode.com/problems/average-of-levels-in-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class AverageOfLevelsInBinaryTree {


    // V0

    // V1
    // IDEA : BFS
    // https://leetcode.com/problems/average-of-levels-in-binary-tree/editorial/
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

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/average-of-levels-in-binary-tree/editorial/
    public List < Double > averageOfLevels_2(TreeNode root) {
        List < Integer > count = new ArrayList < > ();
        List < Double > res = new ArrayList < > ();
        average(root, 0, res, count);
        for (int i = 0; i < res.size(); i++)
            res.set(i, res.get(i) / count.get(i));
        return res;
    }
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
