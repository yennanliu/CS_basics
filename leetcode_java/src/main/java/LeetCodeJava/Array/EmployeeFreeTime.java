package LeetCodeJava.Array;

// https://leetcode.com/problems/employee-free-time/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
}
