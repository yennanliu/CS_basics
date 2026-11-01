package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/first-day-where-you-have-been-in-all-the-rooms/description/
/**
 * 1997. First Day Where You Have Been in All the Rooms
 * Medium
 *
 * There are n rooms you need to visit, labeled from 0 to n - 1. Each day is labeled,
 * starting from 0. You will go in and visit one room a day.
 *
 * Initially on day 0, you visit room 0. The order you visit the rooms for the coming days
 * is determined by the following rules and a given 0-indexed array nextVisit of length n:
 *
 * Assuming that on a day, you visit room i,
 * if you have been in room i an odd number of times (including the current visit), on the
 * next day you will visit a room with a lower or equal room number specified by
 * nextVisit[i] where 0 <= nextVisit[i] <= i;
 * if you have been in room i an even number of times (including the current visit), on
 * the next day you will visit room (i + 1) mod n.
 *
 * Return the label of the first day where you have been in all the rooms. It can be shown
 * that such a day exists. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nextVisit = [0,0]
 * Output: 2
 * Explanation:
 * - On day 0, you visit room 0. The total times you have been in room 0 is 1, which is odd.
 *   On the next day you will visit room nextVisit[0] = 0
 * - On day 1, you visit room 0, The total times you have been in room 0 is 2, which is even.
 *   On the next day you will visit room (0 + 1) mod 2 = 1
 * - On day 2, you visit room 1. This is the first day where you have been in all the rooms.
 *
 * Example 2:
 *
 * Input: nextVisit = [0,0,2]
 * Output: 6
 * Explanation:
 * Your room visiting order for each day is: [0,0,1,0,0,1,2,...].
 * Day 6 is the first day where you have been in all the rooms.
 *
 * Example 3:
 *
 * Input: nextVisit = [0,1,2,0]
 * Output: 6
 * Explanation:
 * Your room visiting order for each day is: [0,0,1,1,2,2,3,...].
 * Day 6 is the first day where you have been in all the rooms.
 *
 *
 * Constraints:
 *
 * n == nextVisit.length
 * 2 <= n <= 10^5
 * 0 <= nextVisit[i] <= i
 *
 */
public class FirstDayWhereYouHaveBeenInAllTheRooms {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: DP ON "FIRST ARRIVAL DAY" (PREFIX INVARIANT)
    /**
     *  DP def:
     *     - firstDay[i] = the day on which room i is entered for the FIRST time
     *
     *  Key invariant:
     *     - when room i is first reached, every room < i has been visited an EVEN
     *       number of times, so climbing back up from any room j to i costs exactly
     *       what it did the first time (firstDay[i] - firstDay[j]).
     *
     *  DP eq:
     *     firstDay[i] = firstDay[i-1]                                  // first reach i-1
     *                 + 1                                              // bounce to nextVisit[i-1]
     *                 + (firstDay[i-1] - firstDay[nextVisit[i-1]])     // re-climb to i-1
     *                 + 1                                              // step to i
     *
     *  Init: firstDay[0] = 0.  Answer: firstDay[n-1].
     *
     *  time = O(N)
     *  space = O(N)
     */
    public int firstDayBeenInAllRooms(int[] nextVisit) {
        int n = nextVisit.length;
        long[] firstDay = new long[n];

        for (int i = 1; i < n; i++) {
            long reClimb = firstDay[i - 1] - firstDay[nextVisit[i - 1]];
            // add MOD before % so the subtraction can never leave a negative value
            firstDay[i] = (firstDay[i - 1] + reClimb + 2 + MOD) % MOD;
        }
        return (int) firstDay[n - 1];
    }
}
