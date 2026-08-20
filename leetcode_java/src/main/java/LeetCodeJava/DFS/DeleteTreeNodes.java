package LeetCodeJava.DFS;

// https://leetcode.com/problems/delete-tree-nodes/

import java.util.ArrayList;
import java.util.List;

/**
 *  1273. Delete Tree Nodes
 *  Medium
 *
 *  A tree rooted at node 0 is given as follows:
 *    The number of nodes is nodes;
 *    The value of the ith node is value[i];
 *    The parent of the ith node is parent[i].
 *
 *  Remove every subtree whose sum of values of nodes is zero.
 *
 *  Return the number of the remaining nodes in the tree.
 *
 *  Example 1:
 *    Input: nodes = 7, parent = [-1,0,0,1,2,2,2], value = [1,-2,4,0,-2,-1,-1]
 *    Output: 2
 *
 *  Example 2:
 *    Input: nodes = 7, parent = [-1,0,0,1,2,2,2], value = [1,-2,4,0,-2,-1,-2]
 *    Output: 6
 *
 *  Constraints:
 *    1 <= nodes <= 10^4
 *    parent.length == nodes
 *    0 <= parent[i] <= nodes - 1
 *    parent[0] == -1 which indicates that 0 is the root.
 *    value.length == nodes
 *    -10^5 <= value[i] <= 10^5
 *    The given input is guaranteed to represent a valid tree.
 */
public class DeleteTreeNodes {

    // V0
    // IDEA: POST-ORDER DFS -- carry (subtree sum, subtree node count) upward
    //       if a subtree's sum is 0 the WHOLE subtree disappears, so its surviving
    //       count becomes 0 and the parent inherits nothing from it. Deleting a
    //       subtree can therefore never resurrect its descendants, which is why one
    //       bottom-up pass is enough.
    //       written ITERATIVELY (a 10^4-deep chain is allowed): a top-down order has
    //       every parent before its children, so walking it BACKWARDS is bottom-up.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int deleteTreeNodes(int nodes, int[] parent, int[] value) {

        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < nodes; i++) {
            children.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < nodes; i++) {
            children.get(parent[i]).add(i);
        }

        // top-down order (parents first)
        int[] order = new int[nodes];
        int head = 0;
        int tail = 0;
        order[tail++] = 0;
        while (head < tail) {
            int cur = order[head++];
            for (Integer c : children.get(cur)) {
                order[tail++] = c;
            }
        }

        long[] sum = new long[nodes];
        int[] cnt = new int[nodes];

        for (int i = nodes - 1; i >= 0; i--) {
            int cur = order[i];
            sum[cur] += value[cur];
            cnt[cur] += 1;
            if (sum[cur] == 0) {
                cnt[cur] = 0; // the whole subtree is removed
            }
            if (cur != 0) {
                sum[parent[cur]] += sum[cur];
                cnt[parent[cur]] += cnt[cur];
            }
        }

        return cnt[0];
    }
}
