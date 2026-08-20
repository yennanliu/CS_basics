package LeetCodeJava.Design;

// https://leetcode.com/problems/count-integers-in-intervals/

import java.util.Map;
import java.util.TreeMap;

/**
 *  2276. Count Integers in Intervals
 *  Hard
 *
 *  Given an empty set of intervals, implement a data structure that can:
 *   - Add an interval to the set of intervals.
 *   - Count the number of integers that are present in at least one interval.
 *
 *  Implement the CountIntervals class:
 *
 *   - CountIntervals() Initializes the object with an empty set of intervals.
 *   - void add(int left, int right) Adds the interval [left, right] to the set of intervals.
 *   - int count() Returns the number of integers that are present in at least one interval.
 *
 *  Note that an interval [left, right] denotes all the integers x where left <= x <= right.
 *
 *  Example 1:
 *    Input
 *      ["CountIntervals", "add", "add", "count", "add", "count"]
 *      [[], [2, 3], [7, 10], [], [5, 8], []]
 *    Output
 *      [null, null, null, 6, null, 8]
 *    Explanation
 *      add(2, 3); add(7, 10);
 *      count();   // return 6  -> {2,3} and {7,8,9,10}
 *      add(5, 8);
 *      count();   // return 8  -> {2,3} and {5,...,10}
 *
 *  Constraints:
 *    1 <= left <= right <= 10^9
 *    At most 10^5 calls in total will be made to add and count.
 *    At least one call will be made to count.
 */
public class CountIntegersInIntervals {

    // V0
    // IDEA: KEEP THE INTERVALS DISJOINT AND MERGE ON INSERT
    //
    //   store the set as DISJOINT intervals in a TreeMap start -> end, plus a
    //   running `total` of covered integers. adding [left, right]:
    //
    //     1. locate the last interval whose start is <= right (floorEntry)
    //     2. while it OVERLAPS (its end >= left), absorb it: widen [left, right],
    //        subtract its length from `total`, drop it from the map
    //     3. insert the widened interval and add its length back
    //
    //   each interval is inserted once and deleted at most once, so the merging
    //   loop is amortised O(1) per add -- the log factor comes from the TreeMap.
    //
    //   count() is then just the maintained total.
    /**
     * time = O(log N) amortised per add, O(1) per count
     * space = O(N)
     */
    private final TreeMap<Integer, Integer> intervals = new TreeMap<>();
    private long total = 0L;

    public CountIntegersInIntervals() {
    }

    public void add(int left, int right) {
        while (true) {
            Map.Entry<Integer, Integer> e = intervals.floorEntry(right);
            if (e == null || e.getValue() < left) {
                break;   // no overlap, and none earlier either
            }
            left = Math.min(left, e.getKey());
            right = Math.max(right, e.getValue());
            this.total -= (long) e.getValue() - e.getKey() + 1L;
            intervals.remove(e.getKey());
        }
        intervals.put(left, right);
        this.total += (long) right - left + 1L;
    }

    public int count() {
        return (int) this.total;
    }
}
