package LeetCodeJava.Array;

// https://leetcode.com/problems/my-calendar-iii/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 732. My Calendar III
 * Hard
 *
 * A k-booking happens when k events have some non-empty intersection
 * (i.e., there is some time that is common to all k events.)
 *
 * You are given some events [startTime, endTime), after each given event, return an
 * integer k representing the maximum k-booking between all the previous events.
 *
 * Implement the MyCalendarThree class:
 *
 *   - MyCalendarThree() Initializes the object.
 *   - int book(int startTime, int endTime) Returns an integer k representing the largest
 *     integer such that there exists a k-booking in the calendar.
 *
 *
 * Example 1:
 *
 * Input
 * ["MyCalendarThree", "book", "book", "book", "book", "book", "book"]
 * [[], [10, 20], [50, 60], [10, 40], [5, 15], [5, 10], [25, 55]]
 * Output
 * [null, 1, 1, 2, 3, 3, 3]
 *
 * Explanation
 * MyCalendarThree myCalendarThree = new MyCalendarThree();
 * myCalendarThree.book(10, 20); // return 1
 * myCalendarThree.book(50, 60); // return 1
 * myCalendarThree.book(10, 40); // return 2
 * myCalendarThree.book(5, 15); // return 3
 * myCalendarThree.book(5, 10); // return 3
 * myCalendarThree.book(25, 55); // return 3
 *
 *
 * Constraints:
 *
 * 0 <= startTime < endTime <= 10^9
 * At most 400 calls will be made to book.
 *
 */
public class MyCalendar3 {

    /**
     * Your MyCalendarThree object will be instantiated and called as such:
     * MyCalendarThree obj = new MyCalendarThree();
     * int param_1 = obj.book(startTime,endTime);
     */

    // V0
    // IDEA: SWEEP LINE (BOUNDARY DELTA COUNTING)
    /**
     *   Store only the event BOUNDARIES as +1 / -1 deltas keyed by time.
     *   Sweeping the keys in ascending order and accumulating the deltas gives the
     *   number of simultaneously active events at each boundary; the max of that
     *   running sum is the answer.
     *
     *   Since intervals are HALF-OPEN [start, end), the -1 at `end` is applied at the
     *   same time point as any +1 starting there, so touching intervals never overlap.
     *
     *   time  = O(n log n) per book call, n = number of bookings so far
     *   space = O(n)
     */
    class MyCalendarThree {

        /** NOTE !!!
         *
         *  TreeMap keeps the boundaries SORTED by time,
         *  which is exactly what the sweep needs.
         *
         *  time -> net change in number of active events
         */
        private TreeMap<Integer, Integer> delta;

        public MyCalendarThree() {
            this.delta = new TreeMap<>();
        }

        public int book(int startTime, int endTime) {
            /** NOTE !!!
             *
             *  +1 at start (an event begins),
             *  -1 at end   (an event ends, exclusive)
             */
            delta.put(startTime, delta.getOrDefault(startTime, 0) + 1);
            delta.put(endTime, delta.getOrDefault(endTime, 0) - 1);

            int active = 0;
            int best = 0;
            for (Map.Entry<Integer, Integer> entry : delta.entrySet()) {
                active += entry.getValue();
                best = Math.max(best, active);
            }

            return best;
        }
    }


    // V1
    // IDEA: RE-SCAN ALL BOOKINGS PER CALL (no shared delta map)
    /**
     *  Keep the raw list of intervals and, on every book(), rebuild the boundary
     *  deltas from scratch.
     *
     *  Strictly worse than V0, but it keeps NO derived state, so it is the easiest
     *  version to convince yourself is correct -- and with only 400 calls it is
     *  still fast enough.
     *
     *  time  = O(n^2 log n) per book call
     *  space = O(n)
     */
    class MyCalendarThree_1 {

        private List<int[]> events;

        public MyCalendarThree_1() {
            this.events = new ArrayList<>();
        }

        public int book(int startTime, int endTime) {
            events.add(new int[] { startTime, endTime });

            TreeMap<Integer, Integer> delta = new TreeMap<>();
            for (int[] e : events) {
                delta.merge(e[0], 1, Integer::sum);
                delta.merge(e[1], -1, Integer::sum);
            }

            int active = 0;
            int best = 0;
            for (int d : delta.values()) {
                active += d;
                best = Math.max(best, active);
            }
            return best;
        }
    }

    // V2
    // IDEA: SORTED START / END ARRAYS + TWO POINTER SWEEP
    /**
     *  Store the starts and the ends in two separately sorted lists. Walking them
     *  with two pointers is the classic `meeting rooms II` sweep: advance whichever
     *  boundary is smaller, +1 on a start, -1 on an end.
     *
     *  NOTE !!! on a tie the END must be consumed first, because the intervals are
     *           half-open -- [1,5) and [5,9) do NOT overlap.
     *
     *  time  = O(n log n) per book call
     *  space = O(n)
     */
    class MyCalendarThree_2 {

        private List<Integer> starts;
        private List<Integer> ends;

        public MyCalendarThree_2() {
            this.starts = new ArrayList<>();
            this.ends = new ArrayList<>();
        }

        public int book(int startTime, int endTime) {
            starts.add(startTime);
            ends.add(endTime);
            Collections.sort(starts);
            Collections.sort(ends);

            int best = 0;
            int active = 0;
            int i = 0;
            int j = 0;
            while (i < starts.size()) {
                if (starts.get(i) < ends.get(j)) {
                    active += 1;
                    best = Math.max(best, active);
                    i += 1;
                } else {
                    active -= 1;
                    j += 1;
                }
            }
            return best;
        }
    }

    // V3
    // IDEA: SEGMENT TREE WITH LAZY PROPAGATION over the coordinate range
    /**
     *  The `proper` data-structure answer: a dynamically indexed segment tree over
     *  [0, 10^9) storing, per node, the max booking count in its range plus a lazy
     *  `+1 to the whole range` tag.
     *
     *  -> book() becomes O(log C) instead of O(n log n), so this is the only
     *     version that stays fast when the call count grows past a few thousand.
     *
     *  time  = O(log C) per book call, C = 10^9
     *  space = O(n log C)
     */
    class MyCalendarThree_3 {

        private Map<Integer, Integer> best;  // node -> max count in its range
        private Map<Integer, Integer> lazy;  // node -> pending `+x to whole range`

        public MyCalendarThree_3() {
            this.best = new HashMap<>();
            this.lazy = new HashMap<>();
        }

        public int book(int startTime, int endTime) {
            update(1, 0, 1_000_000_000, startTime, endTime - 1);
            return best.getOrDefault(1, 0);
        }

        private void update(int node, int lo, int hi, int ql, int qr) {
            if (qr < lo || hi < ql) {
                return;
            }
            if (ql <= lo && hi <= qr) {
                best.merge(node, 1, Integer::sum);
                lazy.merge(node, 1, Integer::sum);
                return;
            }
            int mid = lo + (hi - lo) / 2;
            update(node * 2, lo, mid, ql, qr);
            update(node * 2 + 1, mid + 1, hi, ql, qr);

            /** NOTE !!!
             *
             *  the lazy tag applies to the WHOLE node range, so it is ADDED on top
             *  of the children's max rather than pushed down
             */
            int childMax = Math.max(best.getOrDefault(node * 2, 0),
                    best.getOrDefault(node * 2 + 1, 0));
            best.put(node, childMax + lazy.getOrDefault(node, 0));
        }
    }

}
