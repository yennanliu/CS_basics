package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-number-of-operations-to-sort-a-binary-tree-by-level/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 2471. Minimum Number of Operations to Sort a Binary Tree by Level
 * Medium
 *
 * You are given the root of a binary tree with unique values.
 *
 * In one operation, you can choose any two nodes at the same level and swap their values.
 *
 * Return the minimum number of operations needed to make the values at each level sorted in a strictly increasing order.
 *
 * The level of a node is the number of edges along the path between it and the root node.
 *
 *
 * Example 1:
 *
 * Input: root = [1,4,3,7,6,8,5,null,null,null,null,9,null,10]
 * Output: 3
 * Explanation:
 * - Swap 4 and 3. The 2nd level becomes [3,4].
 * - Swap 7 and 5. The 3rd level becomes [5,6,8,7].
 * - Swap 8 and 7. The 3rd level becomes [5,6,7,8].
 * We used 3 operations so return 3.
 * It can be proven that 3 is the minimum number of operations needed.
 *
 * Example 2:
 *
 * Input: root = [1,3,2,7,6,5,4]
 * Output: 3
 * Explanation:
 * - Swap 3 and 2. The 2nd level becomes [2,3].
 * - Swap 7 and 4. The 3rd level becomes [4,6,5,7].
 * - Swap 6 and 5. The 3rd level becomes [4,5,6,7].
 * We used 3 operations so return 3.
 * It can be proven that 3 is the minimum number of operations needed.
 *
 * Example 3:
 *
 * Input: root = [1,2,3,4,5,6]
 * Output: 0
 * Explanation: Each level is already sorted in increasing order so return 0.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 10^5].
 * 1 <= Node.val <= 10^5
 * All the values of the tree are unique.
 *
 */
public class MinimumNumberOfOperationsToSortABinaryTreeByLevel {

    // V0
    // IDEA: LEVEL-ORDER BFS; PER LEVEL, MIN SWAPS TO SORT = size - (number of cycles)
    //   swaps stay within a level, so each level is an independent
    //   "sort a permutation by swaps" problem: a cycle of length L needs L - 1
    //   swaps, so the level costs size - cycles. The permutation maps each
    //   position to the index its value occupies in sorted order.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minimumOperations(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int totalSwaps = 0;
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            // collect this level's values, left to right
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            totalSwaps += minSwapsToSort(level);
        }
        return totalSwaps;
    }

    /** Minimum swaps to sort distinct values = size minus the number of permutation cycles. */
    private int minSwapsToSort(List<Integer> values) {
        int size = values.size();

        // order[rank] = original index of the rank-th smallest value
        Integer[] order = new Integer[size];
        for (int i = 0; i < size; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(values.get(a), values.get(b)));

        // target[i] = the index values[i] must move to
        int[] target = new int[size];
        for (int rank = 0; rank < size; rank++) {
            target[order[rank]] = rank;
        }

        // count the cycles of the permutation i -> target[i]
        boolean[] visited = new boolean[size];
        int cycles = 0;
        for (int i = 0; i < size; i++) {
            if (visited[i]) {
                continue;
            }
            cycles++;
            int j = i;
            while (!visited[j]) {
                visited[j] = true;
                j = target[j];
            }
        }
        return size - cycles;
    }
}
