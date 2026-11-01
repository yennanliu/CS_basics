package LeetCodeJava.Greedy;

// https://leetcode.com/problems/rearranging-fruits/description/

import java.util.*;

/**
 * 2561. Rearranging Fruits
 * Hard
 *
 * You have two fruit baskets containing n fruits each. You are given two 0-indexed integer arrays basket1 and basket2 representing the cost of fruit in each basket. You want to make both baskets equal. To do so, you can use the following operation as many times as you want:
 *
 * - Choose two indices i and j, and swap the ith fruit of basket1 with the jth fruit of basket2.
 * - The cost of the swap is min(basket1[i], basket2[j]).
 *
 * Two baskets are considered equal if sorting them according to the fruit cost makes them exactly the same baskets.
 *
 * Return the minimum cost to make both the baskets equal or -1 if impossible.
 *
 *
 * Example 1:
 *
 * Input: basket1 = [4,2,2,2], basket2 = [1,4,1,2]
 * Output: 1
 * Explanation: Swap index 1 of basket1 with index 0 of basket2, which has cost 1. Now basket1 = [4,1,2,2] and basket2 = [2,4,1,2]. Rearranging both the arrays makes them equal.
 *
 * Example 2:
 *
 * Input: basket1 = [2,3,4,1], basket2 = [3,2,5,1]
 * Output: -1
 * Explanation: It can be shown that it is impossible to make both the baskets equal.
 *
 *
 * Constraints:
 *
 * basket1.length == basket2.length
 * 1 <= basket1.length <= 10^5
 * 1 <= basket1[i], basket2[i] <= 10^9
 *
 */
public class RearrangingFruits {

    // V0
    // IDEA: GREEDY + COUNTING (with the cheapest fruit as a middleman)
    //       count +1 for basket1, -1 for basket2. an odd net count can never balance -> -1.
    //       |net| / 2 copies of each value must move; collect them, sort, and pay for the
    //       smaller half. each swap costs min(x, 2 * cheapest), because routing the swap
    //       through the globally cheapest fruit costs two swaps of that fruit.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long minCost(int[] basket1, int[] basket2) {
        // net count per fruit cost: +1 when in basket1, -1 when in basket2
        Map<Integer, Integer> netCount = new HashMap<>();
        int cheapest = Integer.MAX_VALUE;
        for (int i = 0; i < basket1.length; i++) {
            netCount.put(basket1[i], netCount.getOrDefault(basket1[i], 0) + 1);
            netCount.put(basket2[i], netCount.getOrDefault(basket2[i], 0) - 1);
            cheapest = Math.min(cheapest, Math.min(basket1[i], basket2[i]));
        }

        // every fruit that has to change baskets (half of each surplus)
        List<Integer> toMove = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : netCount.entrySet()) {
            int net = entry.getValue();
            if (net % 2 != 0) {
                return -1;
            }
            int copies = Math.abs(net) / 2;
            for (int c = 0; c < copies; c++) {
                toMove.add(entry.getKey());
            }
        }
        Collections.sort(toMove);

        // half the movers come from each side; each swap pays for its cheaper fruit,
        // so the smaller half of the sorted list is what gets paid
        long cost = 0;
        int swaps = toMove.size() / 2;
        for (int i = 0; i < swaps; i++) {
            long direct = toMove.get(i);
            long viaCheapest = 2L * cheapest;
            cost += Math.min(direct, viaCheapest);
        }
        return cost;
    }
}
