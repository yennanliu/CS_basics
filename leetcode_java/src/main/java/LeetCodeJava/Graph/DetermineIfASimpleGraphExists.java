package LeetCodeJava.Graph;

// https://leetcode.com/problems/determine-if-a-simple-graph-exists/description/

import java.util.*;

/**
 * 3656. Determine if a Simple Graph Exists
 * Medium
 *
 * You are given an integer array degrees, where degrees[i] represents the
 * desired degree of the ith vertex.
 *
 * Your task is to determine if there exists an undirected simple graph with
 * exactly these vertex degrees.
 *
 * A simple graph has no self-loops or parallel edges between the same pair of
 * vertices.
 *
 * Return true if such a graph exists, otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: degrees = [3,1,2,2]
 * Output: true
 * Explanation:
 * One possible undirected simple graph is:
 * Edges: (0, 1), (0, 2), (0, 3), (2, 3)
 * Degrees: deg(0) = 3, deg(1) = 1, deg(2) = 2, deg(3) = 2.
 *
 * Example 2:
 *
 * Input: degrees = [1,3,3,1]
 * Output: false
 * Explanation:
 * degrees[1] = 3 and degrees[2] = 3 means they must be connected to all other
 * vertices.
 * This requires degrees[0] and degrees[3] to be at least 2, but both are equal
 * to 1, which contradicts the requirement.
 * Thus, the answer is false.
 *
 *
 * Constraints:
 *
 * 1 <= n == degrees.length <= 10^5
 * 0 <= degrees[i] <= n - 1
 *
 */
public class DetermineIfASimpleGraphExists {

    // V0
    // IDEA: ERDOS-GALLAI THEOREM
    //  - a non-increasing sequence d1 >= ... >= dn is graphical iff the sum is even and for
    //    every k: d1 + ... + dk <= k(k-1) + sum_{i>k} min(d_i, k)
    //  - with the degrees sorted, the tail elements >= k form a prefix of the tail (ending at j,
    //    each contributing k) and the rest contribute themselves (a prefix-sum lookup);
    //    j comes from a "how many degrees are >= k" counting table
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public boolean simpleGraphExists(int[] degrees) {
        int n = degrees.length;

        // sort descending
        int[] sorted = degrees.clone();
        Arrays.sort(sorted);
        for (int left = 0, right = n - 1; left < right; left++, right--) {
            int swap = sorted[left];
            sorted[left] = sorted[right];
            sorted[right] = swap;
        }

        // prefix[i] = sum of the i largest degrees
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + sorted[i];
        }
        // the total number of edge endpoints must be even
        if (prefix[n] % 2 != 0) {
            return false;
        }

        // atLeast[v] = how many degrees are >= v (degrees lie in [0, n - 1])
        int[] count = new int[n + 2];
        for (int degree : sorted) {
            count[degree]++;
        }
        int[] atLeast = new int[n + 2];
        for (int v = n; v >= 0; v--) {
            atLeast[v] = atLeast[v + 1] + count[v];
        }

        for (int k = 1; k <= n; k++) {
            // tail positions k .. j-1 hold degrees >= k (each absorbs k endpoints)
            int j = Math.max(atLeast[k], k);
            long rightSide = (long) k * (k - 1) + (long) (j - k) * k + (prefix[n] - prefix[j]);
            if (prefix[k] > rightSide) {
                return false;
            }
        }
        return true;
    }
}
