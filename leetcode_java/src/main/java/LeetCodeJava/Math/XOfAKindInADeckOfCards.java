package LeetCodeJava.Math;

// https://leetcode.com/problems/x-of-a-kind-in-a-deck-of-cards/

import java.util.HashMap;
import java.util.Map;

/**
 *  914. X of a Kind in a Deck of Cards
 *  Easy
 *
 *  You are given an integer array deck where deck[i] represents the number
 *  written on the i-th card.
 *
 *  Partition the cards into one or more groups such that:
 *   - Each group has exactly x cards where x > 1, and
 *   - All the cards in one group have the same integer written on them.
 *
 *  Return true if such partition is possible, or false otherwise.
 *
 *  Example 1:
 *   Input: deck = [1,2,3,4,4,3,2,1]
 *   Output: true
 *   Explanation: possible partition [1,1],[2,2],[3,3],[4,4].
 *
 *  Example 2:
 *   Input: deck = [1,1,1,2,2,2,3,3]
 *   Output: false
 *   Explanation: no possible partition.
 *
 *  Constraints:
 *   - 1 <= deck.length <= 10^4
 *   - 0 <= deck[i] < 10^4
 */
public class XOfAKindInADeckOfCards {

    // V0
    // IDEA: MATH (GCD) - a valid group size x must divide every card count,
    //       so such an x > 1 exists iff gcd(all counts) >= 2.
    /**
     * time = O(n + k * log(maxCount))   n = deck.length, k = # distinct values
     * space = O(k)
     */
    public boolean hasGroupsSizeX(int[] deck) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int c : deck) {
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);
        }
        int g = 0;
        for (int v : cnt.values()) {
            g = gcd(g, v);
        }
        return g >= 2;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
