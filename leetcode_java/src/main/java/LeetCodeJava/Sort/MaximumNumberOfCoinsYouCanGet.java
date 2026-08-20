package LeetCodeJava.Sort;

// https://leetcode.com/problems/maximum-number-of-coins-you-can-get/

import java.util.Arrays;

/**
 *  1561. Maximum Number of Coins You Can Get
 *  Medium
 *
 *  There are 3n piles of coins of varying size, you and your friends will take
 *  piles of coins as follows:
 *    - In each step, you will choose any 3 piles of coins (not necessarily consecutive).
 *    - Of your choice, Alice will pick the pile with the maximum number of coins.
 *    - You will pick the next pile with the maximum number of coins.
 *    - Your friend Bob will pick the last pile.
 *    - Repeat until there are no more piles of coins.
 *
 *  Given an array of integers piles where piles[i] is the number of coins in the
 *  ith pile, return the maximum number of coins that you can have.
 *
 *  Example 1:
 *    Input: piles = [2,4,1,2,7,8]
 *    Output: 9
 *    Explanation: Choose the triplet (2, 7, 8): Alice picks 8, you pick 7, Bob picks 2.
 *                 Choose the triplet (1, 2, 4): Alice picks 4, you pick 2, Bob picks 1.
 *                 7 + 2 = 9.
 *
 *  Example 2:
 *    Input: piles = [2,4,5]
 *    Output: 4
 *
 *  Constraints:
 *    3 <= piles.length <= 10^5
 *    piles.length % 3 == 0
 *    1 <= piles[i] <= 10^4
 */
public class MaximumNumberOfCoinsYouCanGet {

    // V0
    // IDEA: SORT + GREEDY (sacrifice the n/3 smallest piles to Bob)
    //       Bob always gets the minimum of the chosen triple, so the n/3 smallest
    //       piles can never come to us. pair each of them with the two biggest
    //       remaining piles: Alice takes the largest, we take the second largest.
    //       -> after sorting ascending we collect every OTHER pile starting at n/3.
    /**
     * time = O(N log N)
     * space = O(1)   // ignoring the sort
     */
    public int maxCoins(int[] piles) {
        Arrays.sort(piles);
        int n = piles.length;
        int res = 0;
        for (int i = n / 3; i < n; i += 2) {
            res += piles[i];
        }
        return res;
    }
}
