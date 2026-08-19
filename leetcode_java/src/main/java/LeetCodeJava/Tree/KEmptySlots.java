package LeetCodeJava.Tree;

// https://leetcode.com/problems/k-empty-slots/

/**
 *  683. K Empty Slots
 *  Hard
 *
 *  You have n bulbs in a row numbered from 1 to n. Initially, all the bulbs are
 *  turned off. We turn on exactly one bulb every day until all bulbs are on
 *  after n days.
 *
 *  You are given an array bulbs of length n where bulbs[i] = x means that on
 *  the (i+1)-th day, we will turn on the bulb at position x.
 *
 *  Given an integer k, return the minimum day number such that there exists two
 *  turned on bulbs that have exactly k bulbs between them that are all turned
 *  off. If there isn't such day, return -1.
 *
 *  Example 1:
 *
 *  Input: bulbs = [1,3,2], k = 1
 *  Output: 2
 *
 *  Example 2:
 *
 *  Input: bulbs = [1,2,3], k = 1
 *  Output: -1
 *
 *  Constraints:
 *
 *  n == bulbs.length
 *  1 <= n <= 2 * 10^4
 *  1 <= bulbs[i] <= n
 *  bulbs is a permutation of numbers from 1 to n.
 *  0 <= k <= 2 * 10^4
 */
public class KEmptySlots {

    // V0
    // IDEA: invert to days[pos] = the day bulb at pos turns on, then slide a
    //       window [left, right] of width k+1. The window is valid on day
    //       max(days[left], days[right]) iff every bulb strictly inside turns
    //       on later. When an inner bulb breaks the window, restart from it.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int kEmptySlots(int[] bulbs, int k) {
        if (bulbs == null || bulbs.length == 0) {
            return -1;
        }
        int n = bulbs.length;

        // days[i] = day (1-based) the bulb at position (i+1) turns on
        int[] days = new int[n];
        for (int day = 0; day < n; day++) {
            days[bulbs[day] - 1] = day + 1;
        }

        int res = Integer.MAX_VALUE;
        int left = 0;
        int right = k + 1;
        while (right < n) {
            boolean ok = true;
            for (int i = left + 1; i < right; i++) {
                if (days[i] < days[left] || days[i] < days[right]) {
                    // window broken -> restart the window at this bulb
                    left = i;
                    right = i + k + 1;
                    ok = false;
                    break;
                }
            }
            if (ok) {
                res = Math.min(res, Math.max(days[left], days[right]));
                left = right;
                right = left + k + 1;
            }
        }

        return (res == Integer.MAX_VALUE) ? -1 : res;
    }
}
