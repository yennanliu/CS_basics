package LeetCodeJava.Greedy;

// https://leetcode.com/problems/two-city-scheduling/description/

import java.util.*;

/**
 * 1029. Two City Scheduling
 * Medium
 *
 * A company is planning to interview 2n people. Given the array costs where
 * costs[i] = [aCost_i, bCost_i], the cost of flying the ith person to city a is aCost_i,
 * and the cost of flying the ith person to city b is bCost_i.
 *
 * Return the minimum cost to fly every person to a city such that exactly n people
 * arrive in each city.
 *
 *
 * Example 1:
 *
 * Input: costs = [[10,20],[30,200],[400,50],[30,20]]
 * Output: 110
 * Explanation:
 * The first person goes to city A for a cost of 10.
 * The second person goes to city A for a cost of 30.
 * The third person goes to city B for a cost of 50.
 * The fourth person goes to city B for a cost of 20.
 *
 * The total minimum cost is 10 + 30 + 50 + 20 = 110 to have half the people
 * interviewing in each city.
 *
 * Example 2:
 *
 * Input: costs = [[259,770],[448,54],[926,667],[184,139],[840,118],[577,469]]
 * Output: 1859
 *
 * Example 3:
 *
 * Input: costs = [[515,563],[451,713],[537,709],[343,819],[855,779],[457,60],[650,359],[631,42]]
 * Output: 3086
 *
 *
 * Constraints:
 *
 * 2 * n == costs.length
 * 2 <= costs.length <= 100
 * costs.length is even.
 * 1 <= aCost_i, bCost_i <= 1000
 *
 */
public class TwoCityScheduling {

    // V0
    // IDEA: GREEDY + SORT ON (aCost - bCost): the n people with the smallest delta go to A
    /**
     * time = O(N log N)
     * space = O(1)
     */
    public int twoCitySchedCost(int[][] costs) {
        // sending person i to A instead of B costs an extra (a - b);
        // sort so the people who "save the most" by going to A come first
        Arrays.sort(costs, (x, y) -> Integer.compare(x[0] - x[1], y[0] - y[1]));

        int n = costs.length / 2;
        int total = 0;
        for (int i = 0; i < n; i++) {
            // first half flies to A, second half flies to B
            total += costs[i][0];
            total += costs[i + n][1];
        }
        return total;
    }

    // V1
    // IDEA: BASE SUM + SORTED DELTAS (everyone to B, then move the n cheapest deltas to A)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int twoCitySchedCost_1(int[][] costs) {
        int n = costs.length / 2;

        // start with everyone flying to B
        int base = 0;
        int[] deltas = new int[costs.length];
        for (int i = 0; i < costs.length; i++) {
            base += costs[i][1];
            deltas[i] = costs[i][0] - costs[i][1];
        }

        // switching to A changes the cost by delta; take the n smallest
        Arrays.sort(deltas);
        for (int i = 0; i < n; i++) {
            base += deltas[i];
        }
        return base;
    }
}
