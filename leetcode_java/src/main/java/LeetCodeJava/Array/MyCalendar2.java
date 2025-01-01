package LeetCodeJava.Array;

// https://leetcode.com/problems/my-calendar-ii/description/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 731. My Calendar II
 * Medium
 * Topics
 * Companies
 * Hint
 * You are implementing a program to use as your calendar. We can add a new event if adding the event will not cause a triple booking.
 *
 * A triple booking happens when three events have some non-empty intersection (i.e., some moment is common to all the three events.).
 *
 * The event can be represented as a pair of integers startTime and endTime that represents a booking on the half-open interval [startTime, endTime), the range of real numbers x such that startTime <= x < endTime.
 *
 * Implement the MyCalendarTwo class:
 *
 * MyCalendarTwo() Initializes the calendar object.
 * boolean book(int startTime, int endTime) Returns true if the event can be added to the calendar successfully without causing a triple booking. Otherwise, return false and do not add the event to the calendar.
 *
 *
 * Example 1:
 *
 * Input
 * ["MyCalendarTwo", "book", "book", "book", "book", "book", "book"]
 * [[], [10, 20], [50, 60], [10, 40], [5, 15], [5, 10], [25, 55]]
 * Output
 * [null, true, true, true, false, true, true]
 *
 * Explanation
 * MyCalendarTwo myCalendarTwo = new MyCalendarTwo();
 * myCalendarTwo.book(10, 20); // return True, The event can be booked.
 * myCalendarTwo.book(50, 60); // return True, The event can be booked.
 * myCalendarTwo.book(10, 40); // return True, The event can be double booked.
 * myCalendarTwo.book(5, 15);  // return False, The event cannot be booked, because it would result in a triple booking.
 * myCalendarTwo.book(5, 10); // return True, The event can be booked, as it does not use time 10 which is already double booked.
 * myCalendarTwo.book(25, 55); // return True, The event can be booked, as the time in [25, 40) will be double booked with the third event, the time [40, 50) will be single booked, and the time [50, 55) will be double booked with the second event.
 *
 *
 * Constraints:
 *
 * 0 <= start < end <= 109
 * At most 1000 calls will be made to book.
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 187.4K
 * Submissions
 * 298.6K
 * Acceptance Rate
 * 62.8%
 */
public class MyCalendar2 {

    /**
     * Your MyCalendarTwo object will be instantiated and called as such:
     * MyCalendarTwo obj = new MyCalendarTwo();
     * boolean param_1 = obj.book(startTime,endTime);
     */

    // V0
//    class MyCalendarTwo {
//
//        public MyCalendarTwo() {
//
//        }
//
//        public boolean book(int startTime, int endTime) {
//
//        }
//    }

    // V1-1
    // https://leetcode.com/problems/my-calendar-ii/editorial/
    // IDEA:  Using Overlapped Intervals
    class MyCalendarTwo_1_1 {

        private List<int[]> bookings;
        private List<int[]> overlapBookings;

        // Return true if the booking [start1, end1) & [start2, end2) overlaps.
        private boolean doesOverlap(int start1, int end1, int start2, int end2) {
            return Math.max(start1, start2) < Math.min(end1, end2);
        }

        // Return overlapping booking between [start1, end1) & [start2, end2).
        private int[] getOverlapped(int start1, int end1, int start2, int end2) {
            return new int[] { Math.max(start1, start2), Math.min(end1, end2) };
        }

        public MyCalendarTwo_1_1() {
            bookings = new ArrayList<>();
            overlapBookings = new ArrayList<>();
        }

        public boolean book(int start, int end) {
            // Returns false if the new booking has an overlap
            // with the existing double-booked bookings.
            for (int[] booking : overlapBookings) {
                if (doesOverlap(booking[0], booking[1], start, end)) {
                    return false;
                }
            }

            // Add the double overlapping bookings if any with the new booking.
            for (int[] booking : bookings) {
                if (doesOverlap(booking[0], booking[1], start, end)) {
                    overlapBookings.add(
                            getOverlapped(booking[0], booking[1], start, end));
                }
            }

            // Add the new booking to the list of bookings.
            bookings.add(new int[] { start, end });
            return true;
        }
    }


    // V1-2
    // https://leetcode.com/problems/my-calendar-ii/editorial/
    // IDEA:  Line Sweep
    class MyCalendarTwo_1_2 {

        private TreeMap<Integer, Integer> bookingCount;
        private int maxOverlappedBooking;

        public MyCalendarTwo_1_2() {
            bookingCount = new TreeMap<>();
            maxOverlappedBooking = 2;
        }

        public boolean book(int start, int end) {
            // Increase the booking count at 'start' and decrease at 'end'.
            bookingCount.put(start, bookingCount.getOrDefault(start, 0) + 1);
            bookingCount.put(end, bookingCount.getOrDefault(end, 0) - 1);

            int overlappedBooking = 0;

            // Calculate the prefix sum of bookings.
            for (Map.Entry<Integer, Integer> entry : bookingCount.entrySet()) {
                overlappedBooking += entry.getValue();

                // If the number of overlaps exceeds the allowed limit, rollback and
                // return false.
                if (overlappedBooking > maxOverlappedBooking) {
                    // Rollback changes.
                    bookingCount.put(start, bookingCount.get(start) - 1);
                    bookingCount.put(end, bookingCount.get(end) + 1);

                    // Clean up if the count becomes zero to maintain the map clean.
                    if (bookingCount.get(start) == 0) {
                        bookingCount.remove(start);
                    }

                    return false;
                }
            }

            return true;
        }
    }


    // V2
}
