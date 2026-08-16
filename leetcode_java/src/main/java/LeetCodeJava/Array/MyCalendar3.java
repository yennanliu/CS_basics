package LeetCodeJava.Array;

// https://leetcode.com/problems/my-calendar-iii/description/

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

}
