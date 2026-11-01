package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-total-importance-of-roads/description/

import java.util.Arrays;

/**
 * 2285. Maximum Total Importance of Roads
 * Medium
 *
 * You are given an integer n denoting the number of cities in a country. The cities are numbered from 0 to
 * n - 1.
 *
 * You are also given a 2D integer array roads where roads[i] = [ai, bi] denotes that there exists a
 * bidirectional road connecting cities ai and bi.
 *
 * You need to assign each city with an integer value from 1 to n, where each value can only be used once.
 * The importance of a road is then defined as the sum of the values of the two cities it connects.
 *
 * Return the maximum total importance of all roads possible after assigning the values optimally.
 *
 *
 * Example 1:
 *
 * Input: n = 5, roads = [[0,1],[1,2],[2,3],[0,2],[1,3],[2,4]]
 * Output: 43
 *
 * Example 2:
 *
 * Input: n = 5, roads = [[0,3],[2,4],[1,3]]
 * Output: 20
 *
 *
 * Constraints:
 *
 * 2 <= n <= 5 * 10^4
 * 1 <= roads.length <= 5 * 10^4
 * roads[i].length == 2
 * 0 <= ai, bi <= n - 1
 * ai != bi
 * There are no duplicate roads.
 *
 */
public class MaximumTotalImportanceOfRoads {

    // V0
    // IDEA: TOTAL = SUM(value[city] * degree[city]) - SORT DEGREES, GIVE THE BIGGEST VALUE TO THE BIGGEST DEGREE
    /**
     * time = O(N log N + E)
     * space = O(N)
     */
    public long maximumImportance(int n, int[][] roads) {
        // a city's value is counted once per road touching it
        int[] degree = new int[n];
        for (int[] road : roads) {
            degree[road[0]]++;
            degree[road[1]]++;
        }

        // rearrangement inequality: pair the sorted degrees with values 1..n in the same order
        Arrays.sort(degree);
        long total = 0;
        for (int i = 0; i < n; i++) {
            long value = i + 1;
            total += value * degree[i];
        }
        return total;
    }
}
