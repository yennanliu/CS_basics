package LeetCodeJava.Greedy;

// https://leetcode.com/problems/course-schedule-iii/description/

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

}
