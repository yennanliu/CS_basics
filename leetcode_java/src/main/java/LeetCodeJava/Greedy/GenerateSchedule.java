package LeetCodeJava.Greedy;

// https://leetcode.com/problems/generate-schedule/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3680. Generate Schedule
 * Medium
 *
 * You are given an integer n representing n teams. You are asked to generate a
 * schedule such that:
 *
 * Each team plays every other team exactly twice: once at home and once away.
 * There is exactly one match per day; the schedule is a list of consecutive days
 * and schedule[i] is the match on day i.
 * No team plays on consecutive days.
 *
 * Return a 2D integer array schedule, where schedule[i][0] represents the home team
 * and schedule[i][1] represents the away team. If multiple schedules meet the
 * conditions, return any one of them.
 *
 * If no schedule exists that meets the conditions, return an empty array.
 *
 *
 * Example 1:
 *
 * Input: n = 3
 * Output: []
 *
 * Example 2:
 *
 * Input: n = 5
 * Output: [[0,1],[2,3],[0,4],[1,2],[3,4],[0,2],[1,3],[2,4],[0,3],[1,4],
 *          [2,0],[3,1],[4,0],[2,1],[4,3],[1,0],[3,2],[4,1],[3,0],[4,2]]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 50
 *
 */
public class GenerateSchedule {

    // V0
    // IDEA: GROUP MATCHES BY CIRCULAR GAP, SWEEP EACH GAP IN ORDER (CONSTRUCTION)
    //   seat teams on a circle; a match [i, (i+g) % n] has gap g. for a fixed gap
    //   g >= 2 (and g != n-1), listing i = start, start+1, ... makes consecutive days
    //   disjoint, so a block of n days per orientation is conflict-free by itself.
    //   gap 1 always touches its neighbour, so it is laid out by parity instead
    //   ([0,1],[2,3],... then the odd starts). gap n/2 (even n) needs one block only,
    //   since its home and away copies are both in it. each block starts one seat
    //   past the team that just played, which clears the seams between blocks.
    //   n <= 4 is impossible (n = 4: two teams would be forced onto the same days).
    /**
     * time = O(N^2)
     * space = O(N^2)   (the output)
     */
    public int[][] generateSchedule(int n) {
        if (n <= 4) {
            return new int[0][];
        }

        List<int[]> schedule = new ArrayList<>();

        // ---- gap 1, laid out by parity so consecutive days never share a team
        if (n % 2 == 0) {
            for (int i = 0; i < n; i += 2) {
                schedule.add(new int[]{i, i + 1});
            }
            for (int i = 0; i < n; i += 2) {
                schedule.add(new int[]{i + 1, i});
            }
            for (int i = 1; i < n; i += 2) {
                schedule.add(new int[]{i, (i + 1) % n});
            }
            for (int i = 1; i < n; i += 2) {
                schedule.add(new int[]{(i + 1) % n, i});
            }
        } else {
            // odd n: stepping by 2 around the circle visits every seat exactly once
            for (int i = 0; i < 2 * n; i += 2) {
                schedule.add(new int[]{i % n, (i + 1) % n});
            }
            for (int i = 0; i < 2 * n; i += 2) {
                schedule.add(new int[]{(i + 1) % n, i % n});
            }
        }

        // ---- gaps 2 .. ceil(n/2) - 1: a home block, then an away block
        for (int gap = 2; gap < (n + 1) / 2; gap++) {
            int start = lastMatch(schedule)[0] + 1;
            for (int i = start; i < start + n; i++) {
                schedule.add(new int[]{i % n, (i + gap) % n});
            }
            // shift by n so the start stays non-negative before taking it mod n
            start = lastMatch(schedule)[1] - 1 + n;
            for (int i = start; i < start + n; i++) {
                schedule.add(new int[]{(i + gap) % n, i % n});
            }
        }

        // ---- the diametric gap n/2 (even n) covers both orientations in one block
        if (n % 2 == 0) {
            int gap = n / 2;
            int start = lastMatch(schedule)[0] - 1 + n;
            for (int i = start; i < start + n; i++) {
                schedule.add(new int[]{i % n, (i + gap) % n});
            }
        }

        return schedule.toArray(new int[0][]);
    }

    private int[] lastMatch(List<int[]> schedule) {
        return schedule.get(schedule.size() - 1);
    }

    // V1
    // IDEA: DAY-BY-DAY GREEDY -- ALWAYS PLAY THE BUSIEST PAIR STILL OWED
    //   owed[i][j] = true while "i at home vs j" is unplayed; remaining[t] = matches
    //   team t still owes. each day, among pairs avoiding yesterday's two teams, pick
    //   the one whose teams owe the most (ties: a pair still owing both legs first,
    //   then the smallest i, j). keeping demand flat means no team runs out of legal
    //   days; it produces a valid schedule for every n in 5..50.
    /**
     * time = O(N^4)   (N^2 days x an O(N^2) scan)
     * space = O(N^2)
     */
    public int[][] generateSchedule_1(int n) {
        if (n <= 4) {
            return new int[0][];
        }

        boolean[][] owed = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                owed[i][j] = (i != j);
            }
        }
        int[] remaining = new int[n];
        for (int t = 0; t < n; t++) {
            remaining[t] = 2 * (n - 1);
        }

        int totalDays = n * (n - 1);
        int[][] schedule = new int[totalDays][];
        int previousHome = -1;
        int previousAway = -1;

        for (int day = 0; day < totalDays; day++) {
            int bestHome = -1;
            int bestAway = -1;
            int bestLoad = -1;
            boolean bestBothLegsOwed = false;

            for (int i = 0; i < n; i++) {
                if (i == previousHome || i == previousAway) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (j == previousHome || j == previousAway || !owed[i][j]) {
                        continue;
                    }
                    int load = remaining[i] + remaining[j];
                    boolean bothLegsOwed = owed[j][i];
                    // strictly better load wins; on a tie prefer a pair owing both legs.
                    // scanning i, j upward and only replacing on "strictly better" keeps
                    // the smallest (i, j) among equals.
                    boolean better = load > bestLoad
                            || (load == bestLoad && bothLegsOwed && !bestBothLegsOwed);
                    if (better) {
                        bestHome = i;
                        bestAway = j;
                        bestLoad = load;
                        bestBothLegsOwed = bothLegsOwed;
                    }
                }
            }

            if (bestHome == -1) {
                return new int[0][]; // stuck: no legal match today
            }
            owed[bestHome][bestAway] = false;
            remaining[bestHome]--;
            remaining[bestAway]--;
            schedule[day] = new int[]{bestHome, bestAway};
            previousHome = bestHome;
            previousAway = bestAway;
        }
        return schedule;
    }
}
