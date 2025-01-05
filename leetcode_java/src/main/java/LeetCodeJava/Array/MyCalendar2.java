package LeetCodeJava.Array;

// https://leetcode.com/problems/my-calendar-ii/description/

import java.util.*;

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
  // IDEA: SCANNING LINE + CUSTOM SORTING (fixed by gpt)
  class MyCalendarTwo {

      // Attributes
      /**
       * NOTE !!!
       *
       *
       * statusList: {time, status, curBooked}
       * time: start time or end time
       * status: 1 for start, -1 for end
       */
      List<Integer[]> statusList;

      // Constructor
      public MyCalendarTwo() {
          this.statusList = new ArrayList<>();
      }

      public boolean book(int startTime, int endTime) {

          // Create intervals
          /**
           * NOTE !!!
           *
           * 1) we can init array at once as `new Inteter[] {a,b,c};
           * 2) we init curStart, curEnd array at first
           */
          Integer[] curStart = new Integer[] { startTime, 1, 0 }; // {time, status, placeholder}
          Integer[] curEnd = new Integer[] { endTime, -1, 0 }; // {time, status, placeholder}

          // Temporarily add them to the list for simulation
          /**
           * NOTE !!!
           *
           * -> we add curStart, curEnd to statusList directly
           * -> if new time is `over 2 times overlap`, we can REMOVE them
           *    from statusList and return `false` in this method,
           *    and we can keep this method running and validate the
           *    other input (startTime, endTime)
           */
          statusList.add(curStart);
          statusList.add(curEnd);

          // Sort by time, then by status (start before end)
          /**
           * NOTE !!!
           *
           *  custom sorting
           *
           *  -> so, sort time first,
           *  if time are equal, then sort on `status` (0 or 1),
           *  `open` goes first, `close` goes next
           */
          statusList.sort((x, y) -> {
              if (!x[0].equals(y[0])) {
                  return x[0] - y[0];
              }
              return x[1] - y[1]; // start (+1) comes before end (-1)
          });

          // Scanning line to check overlaps
          int curBooked = 0;
          for (Integer[] interval : statusList) {
              curBooked += interval[1];
              if (curBooked >= 3) {
                  // Remove the temporary intervals
                  /**
                   * NOTE !!!
                   *
                   *  if overlap > 2, we just remove the new added times,
                   *  and return false as method response
                   */
                  statusList.remove(curStart);
                  statusList.remove(curEnd);
                  return false; // Booking not allowed
              }
          }

          // Booking is valid, keep the intervals
          return true;
      }
  }

  // V1-1
  // https://leetcode.com/problems/my-calendar-ii/editorial/
  // IDEA:  Line Sweep (Scanning line)
  /**
   *  IDEA:
   *
   *
   *  1) Class `MyCalendarTwo` will have two data members,
   *     `maxOverlappedBooking` which is the maximum number of
   *     concurrent bookings possible at a time,
   *     and `bookingCount` which is a map from integer to integer
   *     with the time point as the key and number of bookings as the value.
   *
   *
   *  2) Initialize `maxOverlappedBooking` as 2, as we need to check for triple booking.
   *
   *  3) Define the function book(start, end) as:
   *
   *    - Increase the number of bookings for the time start and decrease
   *      the number of bookings for end by 1 in the map bookingCount.
   *
   *    - Iterate over each key-value pair in the map
   *      in ascending order of keys to find the prefix sum.
   *      Add the value in the map to the count overlappedBooking.
   *
   *    - If overlappedBooking is more than two, it implies that this
   *      is triple booking. Hence, we should return false.
   *      Also, we need to revert the changes in the map as this booking shouldn't be added.
   *
   *    - If we reach here, it implies no triple booking and hence returns true.
   *
   */
  class MyCalendarTwo_1_1 {



        private TreeMap<Integer, Integer> bookingCount;
        private int maxOverlappedBooking;

        public MyCalendarTwo_1_1() {
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

    // V1-2
    // https://leetcode.com/problems/my-calendar-ii/editorial/
    // IDEA:  Using Overlapped Intervals
    class MyCalendarTwo_1_2 {

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

        public MyCalendarTwo_1_2() {
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

    // V2
    // https://leetcode.com/problems/my-calendar-ii/solutions/5838400/understand-once-you-will-solve-on-your-o-kymy/
    class Event {
        int start;
        int end;

        public Event(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    class MyCalendarTwo_2 {
        List<Event> overLapBookings;
        List<Event> bookings;

        public MyCalendarTwo_2() {
            overLapBookings = new ArrayList<>();
            bookings = new ArrayList<>();
        }

        public boolean book(int start, int end) {

            for (Event e : overLapBookings) {
                if (isOverlap(e, start, end))
                    return false;
            }

            for (Event e : bookings) {
                if (isOverlap(e, start, end)) {
                    overLapBookings.add(getOverlapEvent(e, start, end));
                }
            }
            bookings.add(new Event(start, end));

            return true;
        }

        public boolean isOverlap(Event e, int start, int end) {
            return Math.max(e.start, start) < Math.min(e.end, end);
        }

        public Event getOverlapEvent(Event e, int start, int end) {
            return new Event(Math.max(e.start, start), Math.min(e.end, end));
        }
    }

    // V3
    // https://leetcode.com/problems/my-calendar-ii/solutions/5838424/full-sweep-line-concept-dry-run-illustra-xueh/
    class MyCalendarTwo_3 {
        // List to hold the booked intervals
        private List<int[]> bookings;

        public MyCalendarTwo_3() {
            bookings = new ArrayList<>();
        }

        public boolean book(int start, int end) {
            // Check for overlaps with existing bookings
            for (int[] interval : bookings) {
                int a = interval[0], b = interval[1];

                // Check if the new booking overlaps with the existing interval
                if (start < b && end > a) {
                    // Calculate the overlapping sub-interval
                    int newStart = Math.max(a, start);
                    int newEnd = Math.min(b, end);

                    // Check if the sub-interval overlaps more than once
                    if (check(newStart, newEnd)) {
                        return false; // Overlapping more than once, booking fails
                    }
                }
            }

            // If there are no conflicts, add the booking
            bookings.add(new int[] { start, end });
            return true; // Booking successful
        }

        // Check if the sub-interval overlaps more than once
        private boolean check(int start, int end) {
            int overlapCount = 0;

            for (int[] interval : bookings) {
                int a = interval[0], b = interval[1];

                // Check for strict overlap
                if (start < b && end > a) {
                    overlapCount++;
                    if (overlapCount == 2) {
                        return true; // Found more than one overlap
                    }
                }
            }

            return false; // No overlapping found
        }
    }

}
