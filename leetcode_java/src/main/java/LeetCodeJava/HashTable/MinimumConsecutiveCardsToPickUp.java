package LeetCodeJava.HashTable;

// https://leetcode.com/problems/minimum-consecutive-cards-to-pick-up/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2260. Minimum Consecutive Cards to Pick Up
 * Medium
 *
 * You are given an integer array cards where cards[i] represents the value of the ith card. A pair of cards are matching if the cards have the same value.
 *
 * Return the minimum number of consecutive cards you have to pick up to have a pair of matching cards among the picked cards. If it is impossible to have matching cards, return -1.
 *
 *
 * Example 1:
 *
 * Input: cards = [3,4,2,3,4,7]
 * Output: 4
 * Explanation: We can pick up the cards [3,4,2,3] which contain a matching pair of cards with value 3. Note that picking up the cards [4,2,3,4] is also optimal.
 *
 * Example 2:
 *
 * Input: cards = [1,0,5,3]
 * Output: -1
 * Explanation: There is no way to pick up a set of consecutive cards that contain a pair of matching cards.
 *
 *
 * Constraints:
 *
 * 1 <= cards.length <= 10^5
 * 0 <= cards[i] <= 10^6
 *
 */
public class MinimumConsecutiveCardsToPickUp {

    // V0
    // IDEA: HASH MAP OF THE MOST RECENT INDEX OF EACH VALUE
    //   the shortest window holding a matching pair is bounded by two ADJACENT equal
    //   values, so only the latest previous index of each value matters.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumCardPickup(int[] cards) {
        Map<Integer, Integer> lastIndex = new HashMap<>();
        int best = -1;

        for (int i = 0; i < cards.length; i++) {
            int card = cards[i];
            if (lastIndex.containsKey(card)) {
                // cards lastIndex..i inclusive contain a matching pair
                int span = i - lastIndex.get(card) + 1;
                if (best == -1 || span < best) {
                    best = span;
                }
            }
            lastIndex.put(card, i);
        }
        return best;
    }
}
