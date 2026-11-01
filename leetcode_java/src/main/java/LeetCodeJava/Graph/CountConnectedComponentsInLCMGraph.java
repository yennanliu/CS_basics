package LeetCodeJava.Graph;

// https://leetcode.com/problems/count-connected-components-in-lcm-graph/description/

import java.util.*;

/**
 * 3378. Count Connected Components in LCM Graph
 * Hard
 *
 * You are given an array of integers nums of size n and a positive integer threshold.
 *
 * There is a graph consisting of n nodes with the ith node having a value of nums[i]. Two nodes i and j in the graph are connected via an undirected edge if lcm(nums[i], nums[j]) <= threshold.
 *
 * Return the number of connected components in this graph.
 *
 * A connected component is a subgraph of a graph in which there exists a path between any two vertices, and no vertex of the subgraph shares an edge with a vertex outside of the subgraph.
 *
 * The term lcm(a, b) denotes the least common multiple of a and b.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4,8,3,9], threshold = 5
 * Output: 4
 * Explanation:
 * The four connected components are (2, 4), (3), (8), (9).
 *
 * Example 2:
 *
 * Input: nums = [2,4,8,3,9,12], threshold = 10
 * Output: 2
 * Explanation:
 * The two connected components are (2, 3, 4, 8, 9), and (12).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * All elements of nums are unique.
 * 1 <= nums[i] <= 10^9
 * 1 <= threshold <= 2 * 10^5
 *
 */
public class CountConnectedComponentsInLCMGraph {

    // V0
    // IDEA: UNION FIND OVER COMMON MULTIPLES (every divisor of L <= threshold is connected via L)
    //  - a value > threshold has lcm > threshold with anything, so it is its own component
    //  - if a and b both divide some L <= threshold, lcm(a, b) divides L, so they are connected;
    //    for each value walk its multiples v, 2v, ... <= threshold and union with whoever
    //    claimed that multiple first (harmonic sum: threshold * ln(threshold) steps)
    /**
     * time = O(threshold * log(threshold) * alpha)
     * space = O(threshold + n)
     */
    public int countComponents(int[] nums, int threshold) {
        int n = nums.length;
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int bigCount = 0;
        // owner[L] = index of a value dividing L, -1 = none yet
        int[] owner = new int[threshold + 1];
        Arrays.fill(owner, -1);
        for (int i = 0; i < n; i++) {
            int value = nums[i];
            if (value > threshold) {
                bigCount++;
                continue;
            }
            for (int multiple = value; multiple <= threshold; multiple += value) {
                if (owner[multiple] == -1) {
                    owner[multiple] = i;
                } else {
                    union(i, owner[multiple]);
                }
            }
        }

        // count the distinct roots among the small values
        int smallComponents = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] <= threshold && find(i) == i) {
                smallComponents++;
            }
        }
        return smallComponents + bigCount;
    }

    private int[] parent;

    /** Root of x, with path halving. */
    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    /** Merges the components of a and b. */
    private void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA != rootB) {
            parent[rootA] = rootB;
        }
    }
}
