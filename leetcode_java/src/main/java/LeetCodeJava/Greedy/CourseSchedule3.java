package LeetCodeJava.Greedy;

// https://leetcode.com/problems/course-schedule-iii/description/

import java.util.TreeMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * 630. Course Schedule III
 * Hard
 *
 * There are n different online courses numbered from 1 to n.
 * You are given an array courses where courses[i] = [duration_i, lastDay_i] indicate that
 * the ith course should be taken continuously for duration_i days and must be finished
 * before or on lastDay_i.
 *
 * You will start on the 1st day and you cannot take two or more courses simultaneously.
 *
 * Return the maximum number of courses that you can take.
 *
 * Example 1:
 *
 * Input: courses = [[100,200],[200,1300],[1000,1250],[2000,3200]]
 * Output: 3
 * Explanation:
 * There are totally 4 courses, but you can take 3 courses at most:
 * First, take the 1st course, it costs 100 days so you will finish it on the 100th day,
 * and ready to take the next course on the 101st day.
 * Second, take the 3rd course, it costs 1000 days so you will finish it on the 1100th day,
 * and ready to take the next course on the 1101st day.
 * Third, take the 2nd course, it costs 200 days so you will finish it on the 1300th day.
 * The 4th course cannot be taken now, since you will finish it on the 3300th day,
 * which exceeds the closed date.
 *
 * Example 2:
 *
 * Input: courses = [[1,2]]
 * Output: 1
 *
 * Example 3:
 *
 * Input: courses = [[3,2],[4,3]]
 * Output: 0
 *
 * Constraints:
 *
 * 1 <= courses.length <= 10^4
 * 1 <= duration_i, lastDay_i <= 10^4
 *
 */
public class CourseSchedule3 {

    // V0
    // IDEA: GREEDY + MAX HEAP (exchange argument)
    /**
     *   SORT courses by DEADLINE ascending, then take them one by one.
     *   Keep a running total of the time spent on the courses taken so far.
     *
     *   If adding the current course pushes the total PAST its deadline, DROP the
     *   single LONGEST course taken so far (it may be the current one).
     *
     *   NOTE !!! dropping the longest keeps the course COUNT the same while freeing
     *            the MOST time, so it can never hurt a later course
     *            -> the count stays optimal (exchange argument).
     *
     *   time  = O(n * log(n))
     *   space = O(n)
     */
    public int scheduleCourse(int[][] courses) {
        // take the TIGHTEST deadlines first
        int[][] sorted = courses.clone();
        Arrays.sort(sorted, (a, b) -> a[1] - b[1]);

        // MAX heap of the durations currently taken
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int total = 0;

        for (int[] c : sorted) {
            int duration = c[0];
            int lastDay = c[1];

            pq.add(duration);
            total += duration;

            if (total > lastDay) {
                // over the deadline -> give up the LONGEST course so far
                total -= pq.poll();
            }
        }

        return pq.size();
    }


    // V1
    // IDEA: 0/1 KNAPSACK OVER TIME
    /**
     *  dp[t] = the maximum number of courses finishable by exactly time t.
     *
     *  Sort by deadline, then for each course walk t DOWNWARD (so the course is
     *  used at most once) and relax dp[t] from dp[t - duration].
     *
     *  Pseudo-polynomial O(n * maxDeadline) rather than O(n log n), but it needs no
     *  exchange argument at all -- it is a plain knapsack.
     *
     *  time  = O(n * maxDeadline)
     *  space = O(maxDeadline)
     */
    public int scheduleCourse_1(int[][] courses) {
        int[][] sorted = courses.clone();
        Arrays.sort(sorted, (a, b) -> a[1] - b[1]);

        int maxDay = 0;
        for (int[] c : sorted) {
            maxDay = Math.max(maxDay, c[1]);
        }

        int[] dp = new int[maxDay + 1]; // dp[t] = max courses done by day t
        for (int[] c : sorted) {
            int dur = c[0];
            int last = c[1];
            for (int t = last; t >= dur; t--) {
                dp[t] = Math.max(dp[t], dp[t - dur] + 1);
            }
        }

        int best = 0;
        for (int v : dp) {
            best = Math.max(best, v);
        }
        return best;
    }

    // V2
    // IDEA: SORT BY DEADLINE + TreeMap MULTISET OF DURATIONS
    /**
     *  Same exchange-argument greedy as V0, but the taken set is a
     *  TreeMap<duration, count>, so besides `drop the longest` it can also report
     *  the median / distribution of the chosen courses.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int scheduleCourse_2(int[][] courses) {
        int[][] sorted = courses.clone();
        Arrays.sort(sorted, (a, b) -> a[1] - b[1]);

        TreeMap<Integer, Integer> taken = new TreeMap<>();
        int count = 0;
        long total = 0;

        for (int[] c : sorted) {
            taken.merge(c[0], 1, Integer::sum);
            count += 1;
            total += c[0];

            if (total > c[1]) {
                int worst = taken.lastKey();
                if (taken.merge(worst, -1, Integer::sum) == 0) {
                    taken.remove(worst);
                }
                count -= 1;
                total -= worst;
            }
        }
        return count;
    }

    // V3
    // IDEA: BRUTE FORCE -- try every subset (bitmask), keep the largest feasible
    /**
     *  Enumerate all 2^n subsets; a subset is feasible iff, taken in deadline
     *  order, no prefix sum of durations passes its deadline.
     *
     *  Exponential and only usable for n <= ~20, but it makes no greedy claim, so
     *  it is the oracle that shows `drop the longest` really is optimal.
     *
     *  time  = O(2^n * n)
     *  space = O(n)
     */
    public int scheduleCourse_3(int[][] courses) {
        int n = courses.length;
        int[][] sorted = courses.clone();
        Arrays.sort(sorted, (a, b) -> a[1] - b[1]);

        int best = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            long time = 0;
            boolean ok = true;
            int cnt = 0;
            for (int i = 0; i < n && ok; i++) {
                if (((mask >> i) & 1) == 0) {
                    continue;
                }
                time += sorted[i][0];
                cnt += 1;
                if (time > sorted[i][1]) {
                    ok = false;
                }
            }
            if (ok) {
                best = Math.max(best, cnt);
            }
        }
        return best;
    }

}
