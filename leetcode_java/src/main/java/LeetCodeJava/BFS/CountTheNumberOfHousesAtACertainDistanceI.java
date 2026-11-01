package LeetCodeJava.BFS;

// https://leetcode.com/problems/count-the-number-of-houses-at-a-certain-distance-i/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 3015. Count the Number of Houses at a Certain Distance I
 * Medium
 *
 * You are given three positive integers n, x, and y.
 *
 * In a city, there exist houses numbered 1 to n connected by n streets. There is a street connecting the house numbered i with the house numbered i + 1 for all 1 <= i <= n - 1 . An additional street connects the house numbered x with the house numbered y.
 *
 * For each k, such that 1 <= k <= n, you need to find the number of pairs of houses (house1, house2) such that the minimum number of streets that need to be traveled to reach house2 from house1 is k.
 *
 * Return a 1-indexed array result of length n where result[k] represents the total number of pairs of houses such that the minimum streets required to reach one house from the other is k.
 *
 * Note that x and y can be equal.
 *
 *
 * Example 1:
 *
 * Input: n = 3, x = 1, y = 3
 * Output: [6,0,0]
 * Explanation: Let's look at each pair of houses:
 * - For the pair (1, 2), we can go from house 1 to house 2 directly.
 * - For the pair (2, 1), we can go from house 2 to house 1 directly.
 * - For the pair (1, 3), we can go from house 1 to house 3 directly.
 * - For the pair (3, 1), we can go from house 3 to house 1 directly.
 * - For the pair (2, 3), we can go from house 2 to house 3 directly.
 * - For the pair (3, 2), we can go from house 3 to house 2 directly.
 *
 * Example 2:
 *
 * Input: n = 5, x = 2, y = 4
 * Output: [10,8,2,0,0]
 * Explanation: For each distance k the pairs are:
 * - For k == 1, the pairs are (1, 2), (2, 1), (2, 3), (3, 2), (2, 4), (4, 2), (3, 4), (4, 3), (4, 5), and (5, 4).
 * - For k == 2, the pairs are (1, 3), (3, 1), (1, 4), (4, 1), (2, 5), (5, 2), (3, 5), and (5, 3).
 * - For k == 3, the pairs are (1, 5) and (5, 1).
 * - For k == 4 and k == 5, there are no pairs.
 *
 * Example 3:
 *
 * Input: n = 4, x = 1, y = 1
 * Output: [6,4,2,0]
 * Explanation: For each distance k the pairs are:
 * - For k == 1, the pairs are (1, 2), (2, 1), (2, 3), (3, 2), (3, 4), and (4, 3).
 * - For k == 2, the pairs are (1, 3), (3, 1), (2, 4), and (4, 2).
 * - For k == 3, the pairs are (1, 4), and (4, 1).
 * - For k == 4, there are no pairs.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 100
 * 1 <= x, y <= n
 *
 */
public class CountTheNumberOfHousesAtACertainDistanceI {

    // V0
    // IDEA: n <= 100, SO BFS FROM EVERY HOUSE
    //   the graph is a path plus one extra street, every edge costs 1, so one BFS
    //   per source gives all its shortest distances. Pairs are ORDERED, so each
    //   (source, target) found by a BFS is counted once as it is.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int[] countOfPairs(int n, int x, int y) {
        // houses are 1-indexed: street i connects i and i + 1
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 1; i < n; i++) {
            graph.get(i).add(i + 1);
            graph.get(i + 1).add(i);
        }
        // the extra street; a self-loop (x == y) adds nothing
        if (x != y) {
            graph.get(x).add(y);
            graph.get(y).add(x);
        }

        // pairsAtDistance[d] = ordered pairs at shortest distance d
        int[] pairsAtDistance = new int[n + 1];
        for (int source = 1; source <= n; source++) {
            int[] distance = new int[n + 1];
            Arrays.fill(distance, -1);
            distance[source] = 0;
            Queue<Integer> queue = new ArrayDeque<>();
            queue.offer(source);
            while (!queue.isEmpty()) {
                int house = queue.poll();
                for (int next : graph.get(house)) {
                    if (distance[next] == -1) {
                        distance[next] = distance[house] + 1;
                        pairsAtDistance[distance[next]]++;
                        queue.offer(next);
                    }
                }
            }
        }

        // result[k - 1] = pairs at distance k, for k = 1..n
        int[] result = new int[n];
        for (int k = 1; k <= n; k++) {
            result[k - 1] = pairsAtDistance[k];
        }
        return result;
    }
}
