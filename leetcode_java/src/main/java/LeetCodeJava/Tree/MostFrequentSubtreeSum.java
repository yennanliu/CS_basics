package LeetCodeJava.Tree;

// https://leetcode.com/problems/most-frequent-subtree-sum/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  508. Most Frequent Subtree Sum
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, return the most frequent subtree sum. If there is a tie, return all the values with the highest frequency in any order.
 *
 * The subtree sum of a node is defined as the sum of all the node values formed by the subtree rooted at that node (including the node itself).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [5,2,-3]
 * Output: [2,-3,4]
 * Example 2:
 *
 *
 * Input: root = [5,2,-5]
 * Output: [2]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * -105 <= Node.val <= 105
 */
public class MostFrequentSubtreeSum {

    // V0
//    public int[] findFrequentTreeSum(TreeNode root) {
//
//    }

    // V1-1
    // IDEA: Pre-Order Traversal
    // https://leetcode.com/problems/most-frequent-subtree-sum/editorial/
    private HashMap<Integer, Integer> sumFreq = new HashMap<Integer, Integer>();
    private Integer maxFreq = 0;

    private int findTreeSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // Current root's tree's sum.
        return root.val + findTreeSum(root.left) + findTreeSum(root.right);
    }

    private void preOrderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }

        // Find current node's tree's sum.
        int currSum = findTreeSum(root);
        sumFreq.put(currSum, sumFreq.getOrDefault(currSum, 0) + 1);
        maxFreq = Math.max(maxFreq, sumFreq.get(currSum));

        // Iterate on left and right subtrees and find their sums.
        preOrderTraversal(root.left);
        preOrderTraversal(root.right);
    }

    public int[] findFrequentTreeSum_1_1(TreeNode root) {
        // Traverse on all nodes one by one, and find it's tree's sum.
        preOrderTraversal(root);

        List<Integer> ansList = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer> mapElement : sumFreq.entrySet()) {
            Integer sum = mapElement.getKey();
            Integer freq = mapElement.getValue();

            if (freq == maxFreq) {
                ansList.add(sum);
            }
        }

        int maxFreqSums[] = new int[ansList.size()];
        for (int i = 0; i < ansList.size(); i++) {
            maxFreqSums[i] = ansList.get(i).intValue();
        }

        return maxFreqSums;
    }



    // V1-2
    // IDEA: Post-Order Traversal
    // https://leetcode.com/problems/most-frequent-subtree-sum/editorial/
    private HashMap<Integer, Integer> sumFreq_1_2 = new HashMap<Integer, Integer>();
    private Integer maxFreq_1_2 = 0;

    private int subtreeSum(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Get left and right subtree's sum.
        int leftSubtreeSum = subtreeSum(root.left);
        int rightSubtreeSum = subtreeSum(root.right);

        // Use child's tree's sums to get current root's tree's sum
        int currSum = root.val + leftSubtreeSum + rightSubtreeSum;

        sumFreq_1_2.put(currSum, sumFreq_1_2.getOrDefault(currSum, 0) + 1);
        maxFreq_1_2 = Math.max(maxFreq_1_2, sumFreq_1_2.get(currSum));
        return currSum;
    }

    public int[] findFrequentTreeSum_1_2(TreeNode root) {
        // Traverse on all nodes one by one, and find it's tree's sum.
        subtreeSum(root);

        List<Integer> ansList = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer> mapElement : sumFreq_1_2.entrySet()) {
            Integer sum = mapElement.getKey();
            Integer freq = mapElement.getValue();

            if (freq == maxFreq_1_2) {
                ansList.add(sum);
            }
        }

        int maxFreqSums[] = new int[ansList.size()];
        for (int i = 0; i < ansList.size(); i++) {
            maxFreqSums[i] = ansList.get(i).intValue();
        }

        return maxFreqSums;
    }


    // V2


    // V3



}
