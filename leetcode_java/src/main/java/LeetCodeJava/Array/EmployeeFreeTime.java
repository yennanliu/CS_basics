package LeetCodeJava.Array;

// https://leetcode.com/problems/employee-free-time/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  759. Employee Free Time
 *  Hard
 *
 *  We are given a list schedule of employees, which represents the working time
 *  for each employee.
 *
 *  Each employee has a list of non-overlapping Intervals, and these intervals
 *  are in sorted order.
 *
 *  Return the list of finite intervals representing common, positive-length free
 *  time for all employees, also in sorted order.
 *
 *  Example 1:
 *    Input: schedule = [[[1,2],[5,6]],[[1,3]],[[4,10]]]
 *    Output: [[3,4]]
 *    Explanation: There are a total of three employees, and all common free time
 *    intervals would be [-inf, 1], [3, 4], [10, inf]. We discard any intervals
 *    that contain inf as they aren't finite.
 *
 *  Example 2:
 *    Input: schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]
 *    Output: [[5,6],[7,9]]
 *
 *  Constraints:
 *    1 <= schedule.length , schedule[i].length <= 50
 *    0 <= schedule[i].start < schedule[i].end <= 10^8
 */
public class EmployeeFreeTime {

    // LC provided definition for an Interval
    public static class Interval {
        public int start;
        public int end;

        public Interval() {
        }

        public Interval(int _start, int _end) {
            start = _start;
            end = _end;
        }
    }

    // V0
    // IDEA: flatten every employee's intervals into one list, sort by start,
    //       then sweep: whenever the next interval starts after the furthest end
    //       seen so far, the gap in between is common free time.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        List<Interval> res = new ArrayList<>();
        if (schedule == null || schedule.isEmpty()) {
            return res;
        }

        List<Interval> all = new ArrayList<>();
        for (List<Interval> emp : schedule) {
            if (emp != null) {
                all.addAll(emp);
            }
        }
        if (all.isEmpty()) {
            return res;
        }

        Collections.sort(all, new Comparator<Interval>() {
            @Override
            public int compare(Interval a, Interval b) {
                return Integer.compare(a.start, b.start);
            }
        });

        int prevEnd = all.get(0).end;
        for (int i = 1; i < all.size(); i++) {
            Interval cur = all.get(i);
            if (cur.start > prevEnd) {
                res.add(new Interval(prevEnd, cur.start));
            }
            prevEnd = Math.max(prevEnd, cur.end);
        }
        return res;
    }

    // V1
    // IDEA: K-WAY MERGE WITH A MIN-HEAP — every employee's list is already sorted,
    //       so pull intervals in global start order via a heap of (employee, index)
    //       and record the gap whenever the next interval starts after the max end.
    /**
     * time = O(n log k), k = number of employees
     * space = O(k)
     */
    public List<Interval> employeeFreeTime_1(final List<List<Interval>> schedule) {
        List<Interval> res = new ArrayList<>();
        if (schedule == null || schedule.isEmpty()) {
            return res;
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(schedule.get(a[0]).get(a[1]).start,
                        schedule.get(b[0]).get(b[1]).start);
            }
        });
        for (int e = 0; e < schedule.size(); e++) {
            List<Interval> emp = schedule.get(e);
            if (emp != null && !emp.isEmpty()) {
                pq.add(new int[]{e, 0});
            }
        }
        if (pq.isEmpty()) {
            return res;
        }

        int prevEnd = Integer.MIN_VALUE;
        boolean first = true;
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            Interval iv = schedule.get(cur[0]).get(cur[1]);
            if (first) {
                prevEnd = iv.end;
                first = false;
            } else {
                if (iv.start > prevEnd) {
                    res.add(new Interval(prevEnd, iv.start));
                }
                prevEnd = Math.max(prevEnd, iv.end);
            }
            if (cur[1] + 1 < schedule.get(cur[0]).size()) {
                pq.add(new int[]{cur[0], cur[1] + 1});
            }
        }
        return res;
    }

    // V2
    // IDEA: SWEEP LINE — turn every interval into two boundary events (+1 on start,
    //       -1 on end), sort them, and track how many employees are busy; a stretch
    //       between the moment the counter hits 0 and the next event is free time.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public List<Interval> employeeFreeTime_2(List<List<Interval>> schedule) {
        List<Interval> res = new ArrayList<>();
        if (schedule == null || schedule.isEmpty()) {
            return res;
        }

        List<int[]> events = new ArrayList<>();
        for (List<Interval> emp : schedule) {
            if (emp == null) {
                continue;
            }
            for (Interval iv : emp) {
                events.add(new int[]{iv.start, 1});
                events.add(new int[]{iv.end, -1});
            }
        }
        if (events.isEmpty()) {
            return res;
        }

        Collections.sort(events, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] != b[0]) {
                    return Integer.compare(a[0], b[0]);
                }
                // at the same x, process starts (+1) before ends (-1) so that
                // touching intervals produce no zero-length free slot
                return Integer.compare(b[1], a[1]);
            }
        });

        int active = 0;
        int gapStart = Integer.MIN_VALUE;
        boolean hasGapStart = false;
        for (int[] e : events) {
            if (active == 0 && hasGapStart && e[0] > gapStart) {
                res.add(new Interval(gapStart, e[0]));
            }
            active += e[1];
            if (active == 0) {
                gapStart = e[0];
                hasGapStart = true;
            }
        }
        return res;
    }
}
