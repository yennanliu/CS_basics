package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-health-to-beat-game/description/
// https://leetcode.ca/2022-04-16-2214-Minimum-Health-to-Beat-Game/

/**
 * 2214. Minimum Health to Beat Game
 * Medium
 *
 * You are playing a game that has n levels numbered from 0 to n - 1. You are given a 0-indexed integer
 * array damage where damage[i] is the amount of health you will lose to complete the ith level.
 *
 * You are also given an integer armor. You may use your armor ability at most once during the game on any
 * level which will protect you from at most armor damage.
 *
 * You must complete the levels in order and your health must be greater than 0 at all times to beat the
 * game.
 *
 * Return the minimum health you need to start with to beat the game.
 *
 *
 * Example 1:
 *
 * Input: damage = [2,7,4,3], armor = 4
 * Output: 13
 *
 * Example 2:
 *
 * Input: damage = [2,5,3,4], armor = 7
 * Output: 10
 *
 * Example 3:
 *
 * Input: damage = [3,3,3], armor = 0
 * Output: 10
 *
 *
 * Constraints:
 *
 * n == damage.length
 * 1 <= n <= 10^5
 * 0 <= damage[i] <= 10^5
 * 0 <= armor <= 10^5
 *
 */
public class MinimumHealthToBeatGame {

    // V0
    // IDEA: GREEDY - SPEND THE ARMOR ON THE BIGGEST HIT; ONLY THE TOTAL DAMAGE MATTERS
    /**
     * time = O(N)
     * space = O(1)
     */
    public long minimumHealth(int[] damage, int armor) {
        // total can reach 10^5 * 10^5, so it needs a long
        long totalDamage = 0;
        int biggestHit = 0;
        for (int hit : damage) {
            totalDamage += hit;
            biggestHit = Math.max(biggestHit, hit);
        }

        // damage only ever accumulates, so the binding moment is the end of the game;
        // the armor absorbs at most min(armor, hit), largest on the biggest hit
        long absorbed = Math.min(armor, biggestHit);

        // health must stay strictly above 0, hence the + 1
        return totalDamage - absorbed + 1;
    }
}
