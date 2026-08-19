package LeetCodeJava.HashTable;

// https://leetcode.com/problems/card-flipping-game/

import java.util.HashSet;
import java.util.Set;

/**
 *  822. Card Flipping Game
 *  Medium
 *
 *  You are given "n" cards. Each card has a number printed on the front (fronts[i])
 *  and a number printed on the back (backs[i]). Initially, every card is placed on
 *  a table with its front side facing up.
 *
 *  You may flip over any number of cards (possibly zero). After the flips, you pick
 *  one card, and the number "x" on its BACK (the hidden face) is "good" if x does NOT
 *  appear on the front (the visible face) of ANY card.
 *
 *  Return the minimum possible good integer after flipping the cards.
 *  If there is no good integer, return 0.
 *
 *  Example 1:
 *  Input: fronts = [1,2,4,4,7], backs = [1,3,4,1,3]
 *  Output: 2
 *  Explanation: flip the second card, then the fronts are [1,3,4,4,7] and the backs
 *  are [1,2,4,1,3]; 2 is on a back and never on a front, so 2 is good and minimal.
 *
 *  Example 2:
 *  Input: fronts = [1], backs = [1]
 *  Output: 0
 *
 *  Constraints:
 *  n == fronts.length == backs.length
 *  1 <= n <= 1000
 *  1 <= fronts[i], backs[i] <= 2000
 */
public class CardFlippingGame {

    // V0
    // IDEA: HASH SET (GREEDY) - a value is unusable iff some card has it on BOTH sides
    /**
     * time = O(n)
     * space = O(n)
     */
    public int flipgame(int[] fronts, int[] backs) {

        // edge
        if (fronts == null || backs == null || fronts.length == 0) {
            return 0;
        }

        // values that are "blocked": same number on front and back of one card,
        // so this number is always visible somewhere -> can never be good
        Set<Integer> blocked = new HashSet<>();
        for (int i = 0; i < fronts.length; i++) {
            if (fronts[i] == backs[i]) {
                blocked.add(fronts[i]);
            }
        }

        int res = Integer.MAX_VALUE;
        for (int i = 0; i < fronts.length; i++) {
            if (!blocked.contains(fronts[i])) {
                res = Math.min(res, fronts[i]);
            }
            if (!blocked.contains(backs[i])) {
                res = Math.min(res, backs[i]);
            }
        }

        return res == Integer.MAX_VALUE ? 0 : res;
    }
}
