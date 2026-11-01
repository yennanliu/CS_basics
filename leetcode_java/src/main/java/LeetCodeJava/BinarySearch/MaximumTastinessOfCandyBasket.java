package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-tastiness-of-candy-basket/description/

import java.util.Arrays;

/**
 * 2517. Maximum Tastiness of Candy Basket
 * Medium
 *
 * You are given an array of positive integers price where price[i] denotes the price of the ith candy and a positive integer k.
 *
 * The store sells baskets of k distinct candies. The tastiness of a candy basket is the smallest absolute difference of the prices of any two candies in the basket.
 *
 * Return the maximum tastiness of a candy basket.
 *
 *
 * Example 1:
 *
 * Input: price = [13,5,1,8,21,2], k = 3
 * Output: 8
 * Explanation: Choose the candies with the prices [13,5,21].
 * The tastiness of the candy basket is: min(|13 - 5|, |13 - 21|, |5 - 21|) = min(8, 8, 16) = 8.
 * It can be proven that 8 is the maximum tastiness that can be achieved.
 *
 * Example 2:
 *
 * Input: price = [1,3,1], k = 2
 * Output: 2
 * Explanation: Choose the candies with the prices [1,3].
 * The tastiness of the candy basket is: min(|1 - 3|) = min(2) = 2.
 * It can be proven that 2 is the maximum tastiness that can be achieved.
 *
 * Example 3:
 *
 * Input: price = [7,7,7,7], k = 2
 * Output: 0
 * Explanation: Choosing any two distinct candies from the candies we have will result in a tastiness of 0.
 *
 *
 * Constraints:
 *
 * 2 <= k <= price.length <= 10^5
 * 1 <= price[i] <= 10^9
 *
 */
public class MaximumTastinessOfCandyBasket {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + GREEDY FEASIBILITY CHECK ("aggressive cows")
    // "can we pick k candies whose adjacent (sorted) gaps are all >= x?" is monotone in x,
    // so binary search the largest feasible x. The check greedily takes the earliest candy
    // that is at least x above the last one taken - that leaves the most room for the rest.
    /**
     * time = O(N log N + N log(maxPrice))
     * space = O(1) beyond the sort
     */
    public int maximumTastiness(int[] price, int k) {
        int[] sorted = price.clone();
        Arrays.sort(sorted);

        int left = 0;
        int right = sorted[sorted.length - 1] - sorted[0];
        while (left < right) {
            // upper mid, because the feasible branch sets left = mid
            int mid = left + (right - left + 1) / 2;
            if (canPick(sorted, k, mid)) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /** Greedy: can k candies be chosen with every adjacent gap >= minGap? */
    private boolean canPick(int[] sorted, int k, int minGap) {
        int count = 1;
        int lastTaken = sorted[0];
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] - lastTaken >= minGap) {
                lastTaken = sorted[i];
                count++;
                if (count >= k) {
                    return true;
                }
            }
        }
        return count >= k;
    }
}
