package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-skips-to-arrive-at-meeting-on-time/description/

import java.util.*;

/**
 * 1883. Minimum Skips to Arrive at Meeting On Time
 * Hard
 *
 * You are given an integer hoursBefore, the number of hours you have to travel to your meeting. To
 * arrive at your meeting, you have to travel through n roads. The road lengths are given as an
 * integer array dist of length n, where dist[i] describes the length of the ith road in kilometers.
 * In addition, you are given an integer speed, which is the speed (in km/h) you will travel at.
 *
 * After you travel road i, you must rest and wait for the next integer hour before you can begin
 * traveling on the next road. Note that you do not have to rest after traveling the last road
 * because you are already at the meeting.
 *
 * For example, if traveling a road takes 1.4 hours, you must wait until the 2 hour mark before
 * traveling the next road. If traveling a road takes exactly 2 hours, you do not need to wait.
 *
 * However, you are allowed to skip some rests to be able to arrive on time, meaning you do not need
 * to wait for the next integer hour. Note that this means you may finish traveling future roads at
 * different hour marks.
 *
 * For example, suppose traveling the first road takes 1.4 hours and traveling the second road takes
 * 0.6 hours. Skipping the rest after the first road will mean you finish traveling the second road
 * right at the 2 hour mark, letting you start traveling the third road immediately.
 *
 * Return the minimum number of skips required to arrive at the meeting on time, or -1 if it is impossible.
 *
 *
 * Example 1:
 *
 * Input: dist = [1,3,2], speed = 4, hoursBefore = 2
 * Output: 1
 *
 * Example 2:
 *
 * Input: dist = [7,3,5,5], speed = 2, hoursBefore = 10
 * Output: 2
 *
 * Example 3:
 *
 * Input: dist = [7,3,5,5], speed = 1, hoursBefore = 10
 * Output: -1
 *
 *
 * Constraints:
 *
 * n == dist.length
 * 1 <= n <= 1000
 * 1 <= dist[i] <= 10^5
 * 1 <= speed <= 10^6
 * 1 <= hoursBefore <= 10^7
 *
 */
public class MinimumSkipsToArriveAtMeetingOnTime {

    // V0
    // IDEA: DP ON (ROADS DONE, SKIPS USED), TIME SCALED BY SPEED TO STAY IN INTEGERS
    //
    //   measure time in units of 1/speed hour, so a road costs exactly dist[i] units
    //   and "wait for the next whole hour" = round up to a multiple of speed.
    //   dp[i][j] = earliest arrival (scaled) after i roads using j skips.
    //     rest after road i : ceil((dp[i-1][j] + d) / speed) * speed   (no rest after the last road)
    //     skip after road i : dp[i-1][j-1] + d
    //   answer = smallest j with dp[n][j] <= hoursBefore * speed.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minSkips(int[] dist, int speed, int hoursBefore) {
        int n = dist.length;
        final long INF = Long.MAX_VALUE;

        long[] prev = new long[n + 1];
        Arrays.fill(prev, INF);
        prev[0] = 0;

        for (int i = 1; i <= n; i++) {
            long road = dist[i - 1];
            long[] cur = new long[n + 1];
            Arrays.fill(cur, INF);

            for (int j = 0; j <= i; j++) {
                long best = INF;

                // option 1: take the rest after this road (j skips so far)
                if (j <= i - 1 && prev[j] != INF) {
                    long time = prev[j] + road;
                    if (i < n) {
                        // round up to the next whole hour (a multiple of speed)
                        time = ((time + speed - 1) / speed) * speed;
                    }
                    best = time;
                }

                // option 2: skip the rest after this road (one more skip)
                if (j >= 1 && prev[j - 1] != INF) {
                    best = Math.min(best, prev[j - 1] + road);
                }
                cur[j] = best;
            }
            prev = cur;
        }

        long limit = (long) hoursBefore * speed;
        for (int j = 0; j <= n; j++) {
            if (prev[j] <= limit) {
                return j;
            }
        }
        return -1;
    }
}
