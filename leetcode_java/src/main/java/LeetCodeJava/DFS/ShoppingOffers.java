package LeetCodeJava.DFS;

// https://leetcode.com/problems/shopping-offers/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  638. Shopping Offers
 *  Medium
 *
 *  In LeetCode Store, there are n items to sell. Each item has a price. However, there are some
 *  special offers, and a special offer consists of one or more different kinds of items with a
 *  sale price.
 *
 *  You are given an integer array price where price[i] is the price of the ith item, and an
 *  integer array needs where needs[i] is the number of pieces of the ith item you want to buy.
 *
 *  You are also given an array special where special[i] is of size n + 1 where special[i][j] is
 *  the number of pieces of the jth item in the ith offer and special[i][n] (i.e., the last
 *  integer in the array) is the price of the ith offer.
 *
 *  Return the lowest price you have to pay for exactly certain items as given, where you could
 *  make optimal use of the special offers. You are not allowed to buy more items than you want,
 *  even if that would lower the overall price. You could use any of the special offers as many
 *  times as you want.
 *
 *  Example 1:
 *  Input: price = [2,5], special = [[3,0,5],[1,2,10]], needs = [3,2]
 *  Output: 14
 *
 *  Example 2:
 *  Input: price = [2,3,4], special = [[1,1,0,4],[2,2,1,9]], needs = [1,2,1]
 *  Output: 11
 *
 *  Constraints:
 *  n == price.length == needs.length
 *  1 <= n <= 6
 *  0 <= price[i], needs[i] <= 10
 *  1 <= special.length <= 100
 *  special[i].length == n + 1
 *  0 <= special[i][j] <= 50
 */
public class ShoppingOffers {

    // V0
    // IDEA: DFS + MEMO on the remaining `needs` state; baseline = buy everything at unit price,
    //       then try each still-affordable offer and recurse on the leftover needs
    /**
     * time = O(m * k^n)   // m = #offers, k = max need count, n = #items
     * space = O(k^n)
     */
    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        return dfs(price, special, needs, new HashMap<String, Integer>());
    }

    private int dfs(List<Integer> price, List<List<Integer>> special, List<Integer> needs,
                    Map<String, Integer> memo) {
        String key = needs.toString();
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // baseline: no offer, pay unit price for everything left
        int best = 0;
        for (int i = 0; i < needs.size(); i++) {
            best += price.get(i) * needs.get(i);
        }

        for (List<Integer> offer : special) {
            List<Integer> remain = new ArrayList<>();
            boolean valid = true;
            int taken = 0;
            for (int j = 0; j < needs.size(); j++) {
                int left = needs.get(j) - offer.get(j);
                if (left < 0) {
                    valid = false;
                    break;
                }
                taken += offer.get(j);
                remain.add(left);
            }
            // NOTE: skip a "takes nothing" offer, it would loop on the same state forever
            if (valid && taken > 0) {
                int offerPrice = offer.get(offer.size() - 1);
                best = Math.min(best, offerPrice + dfs(price, special, remain, memo));
            }
        }

        memo.put(key, best);
        return best;
    }

    // V1
    // IDEA: BOTTOM-UP DP over the WHOLE `needs` state space, mixed-radix encoded.
    //       Every state (n0, n1, ..., nk) is packed into a single int index; a state is
    //       relaxed only from strictly smaller states, so a plain increasing sweep works
    //       and no recursion / memo map is needed.
    /**
     * time = O(m * n * S), S = product of (needs[i] + 1), m = #offers
     * space = O(S)
     */
    public int shoppingOffers_1(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {

        int n = needs.size();
        int[] radix = new int[n];  // needs[i] + 1 possible counts for item i
        int[] weight = new int[n]; // positional weight of item i in the encoding
        int total = 1;
        for (int i = 0; i < n; i++) {
            radix[i] = needs.get(i) + 1;
            weight[i] = total;
            total *= radix[i];
        }

        int[] dp = new int[total];
        int[] cur = new int[n];

        for (int state = 0; state < total; state++) {
            // decode the state + compute the "no offer" baseline
            int rest = state;
            int best = 0;
            for (int i = 0; i < n; i++) {
                cur[i] = rest % radix[i];
                rest /= radix[i];
                best += cur[i] * price.get(i);
            }

            for (List<Integer> offer : special) {
                boolean valid = true;
                int taken = 0;
                int prev = state;
                for (int i = 0; i < n; i++) {
                    int cnt = offer.get(i);
                    if (cnt > cur[i]) {
                        valid = false;
                        break;
                    }
                    taken += cnt;
                    prev -= cnt * weight[i];
                }
                // NOTE: an offer that takes nothing would point back at the same state
                if (valid && taken > 0) {
                    best = Math.min(best, offer.get(offer.size() - 1) + dp[prev]);
                }
            }
            dp[state] = best;
        }

        return dp[total - 1];
    }

    // V2
    // IDEA: brute force DFS with NO memoization - kept as a readable correctness reference.
    //       At each state, either pay the unit price for everything left, or apply one
    //       affordable offer and recurse on the leftover needs. Exponential, small inputs only.
    /**
     * time = O(m^(sum of needs))  // exponential, no state reuse
     * space = O(sum of needs)     // recursion depth
     */
    public int shoppingOffers_2(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        return dfs_2(price, special, needs);
    }

    private int dfs_2(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        int best = 0;
        for (int i = 0; i < needs.size(); i++) {
            best += price.get(i) * needs.get(i);
        }

        for (List<Integer> offer : special) {
            List<Integer> remain = new ArrayList<>();
            boolean valid = true;
            int taken = 0;
            for (int j = 0; j < needs.size(); j++) {
                int left = needs.get(j) - offer.get(j);
                if (left < 0) {
                    valid = false;
                    break;
                }
                taken += offer.get(j);
                remain.add(left);
            }
            if (valid && taken > 0) {
                best = Math.min(best, offer.get(offer.size() - 1) + dfs_2(price, special, remain));
            }
        }

        return best;
    }
}
