package LeetCodeJava.Sort;

// https://leetcode.com/problems/the-number-of-the-smallest-unoccupied-chair/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 *  1942. The Number of the Smallest Unoccupied Chair
 *  Medium
 *
 *  There is a party where n friends numbered from 0 to n - 1 are attending. There
 *  is an infinite number of chairs in this party that are numbered from 0 to
 *  infinity. When a friend arrives at the party, they sit on the unoccupied chair
 *  with the smallest number.
 *
 *  For example, if chairs 0, 1, and 5 are occupied when a friend comes, they will
 *  sit on chair number 2.
 *
 *  When a friend leaves the party, their chair becomes unoccupied at the moment
 *  they leave. If another friend arrives at that same moment, they can sit in
 *  that chair.
 *
 *  You are given a 0-indexed 2D integer array times where
 *  times[i] = [arrival_i, leaving_i], and an integer targetFriend. All arrival
 *  times are distinct.
 *
 *  Return the chair number that the friend numbered targetFriend will sit on.
 *
 *  Example 1:
 *    Input: times = [[1,4],[2,3],[4,6]], targetFriend = 1
 *    Output: 1
 *
 *  Example 2:
 *    Input: times = [[3,10],[1,5],[2,6]], targetFriend = 0
 *    Output: 2
 *
 *  Constraints:
 *    n == times.length
 *    2 <= n <= 10^4
 *    times[i].length == 2
 *    1 <= arrival_i < leaving_i <= 10^5
 *    0 <= targetFriend <= n - 1
 *    Each arrival_i time is distinct.
 */
public class TheNumberOfTheSmallestUnoccupiedChair {

    // V0
    // IDEA: SORT BY ARRIVAL + TWO MIN-HEAPS (free chairs / occupied by leave time)
    //       process friends in arrival order (arrivals are distinct, so no
    //       tie-break is needed). two min-heaps:
    //         free : chair numbers currently empty -> poll gives the SMALLEST
    //         busy : (leaving, chair) of seated friends
    //       before seating someone arriving at t, release every busy chair whose
    //       leaving time is <= t (a chair freed exactly at t is reusable at t).
    //       NOTE: at most n friends overlap, so chairs 0..n-1 always suffice ->
    //             pre-seed `free` with 0..n-1 instead of tracking "next new".
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int smallestChair(int[][] times, int targetFriend) {
        int n = times.length;

        // sort the friend indices by arrival time (keeps the original index)
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(times[a][0], times[b][0]));

        PriorityQueue<Integer> free = new PriorityQueue<>();
        for (int c = 0; c < n; c++) {
            free.add(c);
        }
        // (leaving, chair), min by leaving
        PriorityQueue<int[]> busy =
                new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        for (int k = 0; k < n; k++) {
            int i = order[k];
            int arrival = times[i][0];
            int leaving = times[i][1];

            while (!busy.isEmpty() && busy.peek()[0] <= arrival) {
                free.add(busy.poll()[1]);
            }

            int chair = free.poll();
            if (i == targetFriend) {
                return chair;
            }
            busy.add(new int[]{leaving, chair});
        }
        return -1;
    }
}
