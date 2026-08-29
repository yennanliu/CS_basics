package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-initial-strength-to-defeat-all-monsters/

/**
 *  4008. Minimum Initial Strength to Defeat All Monsters
 *  Medium
 *
 *  You are given an integer array monsters, where monsters[i] represents the
 *  strength of the ith monster.
 *
 *  You are also given a 2D integer array boosts, where boosts[i] = [li, ri, vi]
 *  indicates that vi is added to your temporary bonus while fighting any monster
 *  whose index lies in [li, ri]. Boost ranges may overlap, and the values of all
 *  applicable boosts are added together.
 *
 *  You start with a non-negative initial strength and fight the monsters from
 *  left to right. For each monster at index i:
 *   - Let bonus be the sum of the values of all boosts that apply to monster i.
 *   - You can defeat the monster only if current strength + bonus >= monsters[i].
 *   - After defeating it, only your current strength decreases by monsters[i];
 *     if it becomes negative it is set to 0.
 *
 *  Return the minimum initial strength required to defeat all monsters.
 *
 *  Example 1:
 *   Input: monsters = [5,10,15], boosts = [[1,1,10]]
 *   Output: 30
 *
 *  Example 2:
 *   Input: monsters = [5,10,15], boosts = [[1,2,10],[1,2,5]]
 *   Output: 5
 *
 *  Constraints:
 *   1 <= monsters.length <= 5 * 10^4
 *   1 <= monsters[i] <= 10^9
 *   0 <= boosts.length <= 5 * 10^4
 *   boosts[i] == [li, ri, vi], 0 <= li <= ri < monsters.length, 1 <= vi <= 10^9
 */
public class MinimumInitialStrengthToDefeatAllMonsters {

    // V0
    // IDEA: difference array to get the per-index bonus, then binary search the
    //       answer: feasibility is monotonic (a bigger start never hurts, since
    //       cur -> max(0, cur - m) is non-decreasing in cur).
    /**
     * time = O((n + m) + n * log(sum(monsters)))
     * space = O(n)
     */
    public long minInitialStrength(int[] monsters, int[][] boosts) {
        int n = monsters.length;

        long[] bonus = new long[n + 1];
        if (boosts != null) {
            for (int[] b : boosts) {
                bonus[b[0]] += b[2];
                bonus[b[1] + 1] -= b[2];
            }
        }
        for (int i = 1; i < n; i++) {
            bonus[i] += bonus[i - 1];
        }

        long lo = 0;
        long hi = 0;
        for (int m : monsters) {
            hi += m; // always enough: strength never drops below the remaining sum
        }

        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (canDefeat(mid, monsters, bonus)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    private boolean canDefeat(long start, int[] monsters, long[] bonus) {
        long cur = start;
        for (int i = 0; i < monsters.length; i++) {
            if (cur + bonus[i] < monsters[i]) {
                return false;
            }
            cur = Math.max(0L, cur - monsters[i]);
        }
        return true;
    }

    // V1
    // IDEA: backward greedy (no search at all) - walking right to left, the
    //       minimum strength needed BEFORE monster i is
    //         need(i) = max(0, monsters[i] - bonus[i],
    //                       need(i+1) > 0 ? monsters[i] + need(i+1) : 0)
    //       because cur -> max(0, cur - monsters[i]) after the fight.
    /**
     * time = O(n + m)
     * space = O(n)
     */
    public long minInitialStrength_1(int[] monsters, int[][] boosts) {
        int n = monsters.length;

        long[] bonus = new long[n + 1];
        if (boosts != null) {
            for (int[] b : boosts) {
                bonus[b[0]] += b[2];
                bonus[b[1] + 1] -= b[2];
            }
        }
        for (int i = 1; i < n; i++) {
            bonus[i] += bonus[i - 1];
        }

        long need = 0; // strength required entering the (i+1)-th fight
        for (int i = n - 1; i >= 0; i--) {
            long req = monsters[i] - bonus[i]; // enough to beat monster i itself
            if (need > 0) {
                // must still hold `need` AFTER paying monsters[i]
                req = Math.max(req, monsters[i] + need);
            }
            need = Math.max(0L, req);
        }
        return need;
    }

    // V2
    // IDEA: brute force - bonus per monster by scanning every boost, then try
    //       every candidate initial strength from 0 upward and simulate.
    //       Kept as a readable correctness reference (only viable on tiny input).
    /**
     * time = O(n * m + answer * n)
     * space = O(n)
     */
    public long minInitialStrength_2(int[] monsters, int[][] boosts) {
        int n = monsters.length;
        long[] bonus = new long[n];
        if (boosts != null) {
            for (int i = 0; i < n; i++) {
                for (int[] b : boosts) {
                    if (b[0] <= i && i <= b[1]) {
                        bonus[i] += b[2];
                    }
                }
            }
        }

        long limit = 0;
        for (int m : monsters) {
            limit += m; // this start is always enough
        }
        for (long start = 0; start <= limit; start++) {
            long cur = start;
            boolean ok = true;
            for (int i = 0; i < n; i++) {
                if (cur + bonus[i] < monsters[i]) {
                    ok = false;
                    break;
                }
                cur = Math.max(0L, cur - monsters[i]);
            }
            if (ok) {
                return start;
            }
        }
        return limit;
    }
}
