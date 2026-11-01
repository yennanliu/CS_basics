package LeetCodeJava.Math;

// https://leetcode.com/problems/water-bottles-ii/description/

/**
 * 3100. Water Bottles II
 * Medium
 *
 * You are given two integers numBottles and numExchange.
 *
 * numBottles represents the number of full water bottles that you initially have. In one operation,
 * you can perform one of the following operations:
 *
 * Drink any number of full water bottles turning them into empty bottles.
 * Exchange numExchange empty bottles with one full water bottle. Then, increase numExchange by one.
 *
 * Note that you cannot exchange multiple batches of empty bottles for the same value of numExchange.
 * For example, if numBottles == 3 and numExchange == 1, you cannot exchange 3 empty water bottles for
 * 3 full bottles.
 *
 * Return the maximum number of water bottles you can drink.
 *
 *
 * Example 1:
 *
 * Input: numBottles = 13, numExchange = 6
 * Output: 15
 *
 * Example 2:
 *
 * Input: numBottles = 10, numExchange = 3
 * Output: 13
 *
 *
 * Constraints:
 *
 * 1 <= numBottles <= 100
 * 1 <= numExchange <= 100
 *
 */
public class WaterBottlesII {

    // V0
    // IDEA: SIMULATION — DRINK EVERYTHING, THEN EXCHANGE WHILE THE EMPTIES COVER THE (RISING) PRICE
    /**
     * time = O(sqrt(numBottles))
     * space = O(1)
     */
    public int maxBottlesDrunk(int numBottles, int numExchange) {
        // drinking never hurts, so drink every full bottle right away
        int drunk = numBottles;
        int empty = numBottles;
        int price = numExchange;

        while (empty >= price) {
            // pay `price` empties for one full bottle ...
            empty -= price;
            // ... and the next exchange costs one more
            price++;
            // drink the new bottle immediately: one more drunk, one more empty
            drunk++;
            empty++;
        }
        return drunk;
    }
}
