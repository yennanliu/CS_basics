package LeetCodeJava.Heap;

// https://leetcode.com/problems/k-th-nearest-obstacle-queries/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  3275. K-th Nearest Obstacle Queries
 *  Medium
 *
 *  There is an infinite 2D plane. You are given a positive integer k and a 2D array
 *  queries, where queries[i] = [x, y] builds an obstacle at coordinate (x, y). It is
 *  guaranteed that there is no obstacle at this coordinate when the query is made.
 *
 *  After each query, find the distance of the kth nearest obstacle from the origin.
 *  Return an integer array results where results[i] is that distance after query i,
 *  or -1 if there are fewer than k obstacles. Initially there are no obstacles.
 *
 *  The distance of an obstacle at (x, y) from the origin is |x| + |y|.
 *
 *  Example 1:
 *    Input: k = 2, queries = [[1,2],[3,4],[2,3],[-3,0]]
 *    Output: [-1,7,5,3]
 *    Explanation: the distances build up as {3}, {3,7}, {3,5,7}, {3,3,5,7}.
 *
 *  Example 2:
 *    Input: k = 1, queries = [[5,5],[4,4],[3,3]]
 *    Output: [10,8,6]
 *
 *  Constraints:
 *    1 <= queries.length <= 2 * 10^5
 *    All queries[i] are unique.
 *    -10^9 <= queries[i][0], queries[i][1] <= 10^9
 *    0 <= k <= 10^5
 */
public class KthNearestObstacleQueries {

    // V0
    // IDEA: A BOUNDED MAX-HEAP HOLDING THE k CLOSEST DISTANCES
    //
    //   only the k-th smallest distance is ever asked for, so keeping the k smallest
    //   distances is enough - and a MAX-heap of exactly those has the answer sitting
    //   on top. Push each new distance, then pop whenever the heap exceeds k, which
    //   discards the largest of the k+1. Before the heap fills up there are fewer
    //   than k obstacles, hence -1.
    /**
     * time = O(q log k)
     * space = O(k)
     */
    public int[] resultsArray(int[][] queries, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>(Collections.reverseOrder());
        int[] res = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int d = Math.abs(queries[i][0]) + Math.abs(queries[i][1]);
            heap.add(d);
            if (heap.size() > k) {
                heap.poll();
            }
            res[i] = (k > 0 && heap.size() == k) ? heap.peek() : -1;
        }
        return res;
    }
}
