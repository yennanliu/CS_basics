package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-profit-of-operating-a-centennial-wheel/description/

/**
 * 1599. Maximum Profit of Operating a Centennial Wheel
 * Medium
 *
 * You are the operator of a Centennial Wheel that has four gondolas, and each
 * gondola has room for up to four people. You have the ability to rotate the
 * gondolas counterclockwise, which costs you runningCost dollars.
 *
 * You are given an array customers of length n where customers[i] is the number of
 * new customers arriving just before the ith rotation (0-indexed). This means you
 * must rotate the wheel i times before the customers[i] customers arrive. You
 * cannot make customers wait if there is room in the gondola. Each customer pays
 * boardingCost dollars when they board on the gondola closest to the ground and
 * will exit once that gondola reaches the ground again.
 *
 * You can stop the wheel at any time, including before serving all customers. If
 * you decide to stop serving customers, all subsequent rotations are free in order
 * to get all the customers down safely. Note that if there are currently more than
 * four customers waiting at the wheel, only four will board the gondola, and the
 * rest will wait for the next rotation.
 *
 * Return the minimum number of rotations you need to perform to maximize your
 * profit. If there is no scenario where the profit is positive, return -1.
 *
 *
 * Example 1:
 *
 * Input: customers = [8,3], boardingCost = 5, runningCost = 6
 * Output: 3
 * Explanation: The highest profit was $37 after rotating the wheel 3 times.
 *
 * Example 2:
 *
 * Input: customers = [10,9,6], boardingCost = 6, runningCost = 4
 * Output: 7
 * Explanation: The highest profit was $122 after rotating the wheel 7 times.
 *
 * Example 3:
 *
 * Input: customers = [3,4,0,5,1], boardingCost = 1, runningCost = 92
 * Output: -1
 * Explanation: The profit was never positive, so return -1.
 *
 *
 * Constraints:
 *
 * n == customers.length
 * 1 <= n <= 10^5
 * 0 <= customers[i] <= 50
 * 1 <= boardingCost, runningCost <= 100
 *
 */
public class MaximumProfitOfOperatingACentennialWheel {

    private static final int GONDOLA_CAPACITY = 4;

    // V0
    // IDEA: SIMULATION (PROFIT IS ONLY CHECKED RIGHT AFTER EACH ROTATION)
    //       each rotation: new arrivals join the queue, min(4, waiting) board.
    //       profit(r) = boarded * boardingCost - r * runningCost; keep the first
    //       rotation count reaching the best positive profit. Keep rotating while
    //       anyone is still waiting.
    /**
     * time = O(N + totalCustomers / 4)
     * space = O(1)
     */
    public int minOperationsMaxProfit(int[] customers, int boardingCost, int runningCost) {
        int n = customers.length;
        int waiting = 0;
        long boarded = 0;
        int rotations = 0;
        long bestProfit = 0;
        int bestRotations = -1;
        int i = 0;

        while (i < n || waiting > 0) {
            // customers[i] arrive just before rotation i
            if (i < n) {
                waiting += customers[i];
                i++;
            }
            int boarding = Math.min(GONDOLA_CAPACITY, waiting);
            waiting -= boarding;
            boarded += boarding;
            rotations++;

            long profit = boarded * boardingCost - (long) rotations * runningCost;
            // strictly greater keeps the MINIMUM rotation count for the best profit
            if (profit > bestProfit) {
                bestProfit = profit;
                bestRotations = rotations;
            }
        }
        return bestRotations;
    }
}
