package LeetCodeJava.Greedy;

// https://leetcode.com/problems/eliminate-maximum-number-of-monsters/description/

import java.util.*;

/**
 * 1921. Eliminate Maximum Number of Monsters
 * Medium
 *
 * You are playing a video game where you are defending your city from a group of
 * n monsters. You are given a 0-indexed integer array dist of size n, where
 * dist[i] is the initial distance in kilometers of the ith monster from the city.
 *
 * The monsters walk toward the city at a constant speed. The speed of each
 * monster is given to you in an integer array speed of size n, where speed[i] is
 * the speed of the ith monster in kilometers per minute.
 *
 * You have a weapon that, once fully charged, can eliminate a single monster.
 * However, the weapon takes one minute to charge. The weapon is fully charged at
 * the very start.
 *
 * You lose when any monster reaches your city. If a monster reaches the city at
 * the exact moment the weapon is fully charged, it counts as a loss, and the game
 * ends before you can use your weapon.
 *
 * Return the maximum number of monsters that you can eliminate before you lose,
 * or n if you can eliminate all the monsters before they reach the city.
 *
 *
 * Example 1:
 *
 * Input: dist = [1,3,4], speed = [1,1,1]
 * Output: 3
 * Explanation: All 3 monsters can be eliminated.
 *
 * Example 2:
 *
 * Input: dist = [1,1,2,3], speed = [1,1,1,1]
 * Output: 1
 * Explanation: After the first shot, the distances are [X,0,1,2], so you lose.
 * You can only eliminate 1 monster.
 *
 * Example 3:
 *
 * Input: dist = [3,2,4], speed = [5,3,2]
 * Output: 1
 * Explanation: After the first shot, the distances are [X,0,2], so you lose.
 * You can only eliminate 1 monster.
 *
 *
 * Constraints:
 *
 * n == dist.length == speed.length
 * 1 <= n <= 10^5
 * 1 <= dist[i], speed[i] <= 10^5
 *
 */
public class EliminateMaximumNumberOfMonsters {

    // V0
    // IDEA: GREEDY + SORT BY DEADLINE (shoot the most urgent monster first)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int eliminateMaximum(int[] dist, int[] speed) {
        int n = dist.length;
        // deadline[i] = last whole minute monster i can still be shot;
        // arriving exactly on a charge tick is already a loss, hence (dist - 1) / speed
        int[] deadline = new int[n];
        for (int i = 0; i < n; i++) {
            deadline[i] = (dist[i] - 1) / speed[i];
        }
        Arrays.sort(deadline);

        // shot number `minute` is fired at that minute; it must not be past the deadline
        for (int minute = 0; minute < n; minute++) {
            if (deadline[minute] < minute) {
                return minute;
            }
        }
        return n;
    }
}
