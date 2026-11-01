package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-elegance-of-a-k-length-subsequence/description/

import java.util.*;

/**
 * 2813. Maximum Elegance of a K-Length Subsequence
 * Hard
 *
 * You are given a 0-indexed 2D integer array items of length n and an integer k.
 *
 * items[i] = [profit_i, category_i], where profit_i and category_i denote the profit and category of the ith item respectively.
 *
 * Let's define the elegance of a subsequence of items as total_profit + distinct_categories^2, where total_profit is the sum of all profits in the subsequence, and distinct_categories is the number of distinct categories from all the categories in the selected subsequence.
 *
 * Your task is to find the maximum elegance from all subsequences of size k in items.
 *
 * Return an integer denoting the maximum elegance of a subsequence of items with size exactly k.
 *
 * Note: A subsequence of an array is a new array generated from the original array by deleting some elements (possibly none) without changing the remaining elements' relative order.
 *
 *
 * Example 1:
 *
 * Input: items = [[3,2],[5,1],[10,1]], k = 2
 * Output: 17
 * Explanation: In this example, we have to select a subsequence of size 2.
 * We can select items[0] = [3,2] and items[2] = [10,1].
 * The total profit in this subsequence is 3 + 10 = 13, and the subsequence contains 2 distinct categories [2,1].
 * Hence, the elegance is 13 + 2^2 = 17, and we can show that it is the maximum achievable elegance.
 *
 * Example 2:
 *
 * Input: items = [[3,1],[3,1],[2,2],[5,3]], k = 3
 * Output: 19
 * Explanation: In this example, we have to select a subsequence of size 3.
 * We can select items[0] = [3,1], items[2] = [2,2], and items[3] = [5,3].
 * The total profit in this subsequence is 3 + 2 + 5 = 10, and the subsequence contains 3 distinct categories [1,2,3].
 * Hence, the elegance is 10 + 3^2 = 19, and we can show that it is the maximum achievable elegance.
 *
 * Example 3:
 *
 * Input: items = [[1,1],[2,1],[3,1]], k = 3
 * Output: 7
 * Explanation: In this example, we have to select a subsequence of size 3.
 * We should select all the items.
 * The total profit will be 1 + 2 + 3 = 6, and the subsequence contains 1 distinct category [1].
 * Hence, the maximum elegance is 6 + 1^2 = 7.
 *
 *
 * Constraints:
 *
 * 1 <= items.length == n <= 10^5
 * items[i].length == 2
 * items[i][0] == profit_i
 * items[i][1] == category_i
 * 1 <= profit_i <= 10^9
 * 1 <= category_i <= n
 * 1 <= k <= n
 *
 */
public class MaximumEleganceOfAKLengthSubsequence {

    // V0
    // IDEA: GREEDY (SORT BY PROFIT DESC) + STACK OF DROPPABLE DUPLICATES
    /**
     *  Take the k most profitable items first: that maximises total profit.
     *  The only useful move afterwards is to trade profit for one more distinct
     *  category: bring in an item of a NEW category and drop the cheapest
     *  selected item whose category is duplicated (dropping a unique one would
     *  lose a category). Scanning the sorted prefix, duplicates are seen in
     *  decreasing profit order, so a stack's top is always the cheapest one.
     *  The answer is checked after every swap, since the best can be mid-way.
     *
     *  time = O(N log N)
     *  space = O(N)
     */
    public long findMaximumElegance(int[][] items, int k) {
        // sort a copy by profit, descending
        int[][] sorted = items.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(b[0], a[0]));

        long totalProfit = 0;
        Set<Integer> categories = new HashSet<>();
        Deque<Integer> duplicates = new ArrayDeque<>(); // profits we may drop

        // start with the k most profitable items
        for (int i = 0; i < k; i++) {
            int profit = sorted[i][0];
            int category = sorted[i][1];
            totalProfit += profit;
            if (categories.contains(category)) {
                duplicates.push(profit);
            } else {
                categories.add(category);
            }
        }

        long distinct = categories.size();
        long best = totalProfit + distinct * distinct;

        // try to swap in each remaining item of a brand new category
        for (int i = k; i < sorted.length; i++) {
            int profit = sorted[i][0];
            int category = sorted[i][1];
            if (categories.contains(category)) {
                continue; // no new category -> pure profit loss
            }
            if (duplicates.isEmpty()) {
                break; // nothing left that we are allowed to drop
            }
            categories.add(category);
            totalProfit += profit - duplicates.pop();

            distinct = categories.size();
            best = Math.max(best, totalProfit + distinct * distinct);
        }
        return best;
    }
}
