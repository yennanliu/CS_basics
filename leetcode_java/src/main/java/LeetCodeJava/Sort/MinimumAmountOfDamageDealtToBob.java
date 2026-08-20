package LeetCodeJava.Sort;

// https://leetcode.com/problems/minimum-amount-of-damage-dealt-to-bob/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  3273. Minimum Amount of Damage Dealt to Bob
 *  Hard
 *
 *  You are given an integer power and two integer arrays damage and health, both
 *  having length n.
 *
 *  Bob has n enemies, where enemy i will deal Bob damage[i] points of damage per
 *  second while they are alive (i.e. health[i] > 0).
 *
 *  Every second, after the enemies deal damage to Bob, he chooses one of the
 *  enemies that is still alive and deals power points of damage to them.
 *
 *  Determine the minimum total amount of damage points that will be dealt to Bob
 *  before all n enemies are dead.
 *
 *  Example 1:
 *    Input: power = 4, damage = [1,2,3,4], health = [4,5,6,8]
 *    Output: 39
 *
 *  Example 2:
 *    Input: power = 1, damage = [1,1,1,1], health = [1,2,3,4]
 *    Output: 20
 *
 *  Example 3:
 *    Input: power = 8, damage = [40], health = [59]
 *    Output: 320
 *
 *  Constraints:
 *    1 <= power <= 10^4
 *    1 <= n == damage.length == health.length <= 10^5
 *    1 <= damage[i], health[i] <= 10^4
 */
public class MinimumAmountOfDamageDealtToBob {

    // V0
    // IDEA: EXCHANGE ARGUMENT - ORDER THE KILLS BY damage / time
    //       splitting Bob's attacks never pays: once an enemy is targeted it is
    //       best to finish it, since leaving it alive keeps costing its damage.
    //       so the whole problem is the ORDER of the kills.
    //
    //       enemy i needs t[i] = ceil(health[i] / power) seconds. comparing the
    //       two orders "i then j" vs "j then i", the totals differ by
    //           t[i] * damage[j]   vs   t[j] * damage[i]
    //       so i comes first when damage[i] * t[j] > damage[j] * t[i], i.e. sort
    //       by the ratio damage / t DESCENDING.
    //
    //       then sweep the sorted order accumulating elapsed time: each enemy pays
    //       for all the time up to and including its own death.
    //
    //       NOTE: compare via cross multiplication in long (never a - b, and never
    //             floating point) to stay exact.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long minDamage(int power, int[] damage, int[] health) {
        int n = damage.length;

        final int[] turns = new int[n];
        for (int i = 0; i < n; i++) {
            turns[i] = (health[i] + power - 1) / power;   // ceil(health / power)
        }

        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        final int[] dmg = damage;
        Arrays.sort(order, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                // damage[a]/turns[a] DESC  <=>  damage[a]*turns[b] DESC
                long left = (long) dmg[a] * turns[b];
                long right = (long) dmg[b] * turns[a];
                return Long.compare(right, left);
            }
        });

        long total = 0L;
        long elapsed = 0L;
        for (int idx = 0; idx < n; idx++) {
            int i = order[idx];
            elapsed += turns[i];
            total += elapsed * damage[i];
        }
        return total;
    }
}
