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
  // TODO: fix below
//  class MyCalendarTwo {
//
//      List<List<Integer>> booked;
//      Map<List<Integer>, Integer> overlapCnt;
//
//      public MyCalendarTwo() {
//          this.booked = new ArrayList<>();
//          this.overlapCnt = new HashMap<>();
//      }
//
//      public boolean book(int startTime, int endTime) {
//
//          List<Integer> tmp = new ArrayList<>();
//          tmp.add(startTime);
//          tmp.add(endTime);
//
//          // case 1) booked is empty
//          if(this.booked.isEmpty()){
//              this.booked.add(tmp);
//              return true;
//          }
//
//          boolean lessEqualsThreeOverlap = false;
//
//
//          for(List<Integer> x: this.booked){
//              /**
//               *   |----|
//               *     |------| (old)
//               *
//               *     or
//               *
//               *    |-----|
//               *  |----|  (old)
//               *
//               *    or
//               *
//               *    |---|
//               *  |----------|  (old)
//               *
//               *
//               */
//              int existingStart = x.get(0);
//              int existingEnd = x.get(1);
//
//              if (startTime < existingEnd && existingStart < endTime) {
//                  // case 2) has overlap, but `overlap count` <= 3
//                  List<Integer> tmpExisting = new ArrayList<>();
//                  tmpExisting.add(existingStart);
//                  tmpExisting.add(existingEnd);
//                  if(this.overlapCnt.get(tmpExisting) <= 3){
//                      // update existing start, end
//                      existingStart = Math.min(existingStart, startTime);
//                      existingEnd  = Math.max(existingEnd, endTime);
//                      List<Integer> tmp2 = new ArrayList<>();
//                      tmp2.add(existingStart);
//                      tmp2.add(existingEnd);
//                      this.overlapCnt.put(tmp2, this.overlapCnt.get(tmpExisting)+1); // update overlap cnt
//                      this.overlapCnt.remove(tmpExisting);
//                      return true;
//                  }else{
//                      // case 3) has overlap, and `overlap count` > 3
//                      return false;
//                  }
//              }
//
//          }
//
//          // case 4) no overlap
//          this.booked.add(tmp);
//          this.overlapCnt.put(tmp, 1); // update overlap cnt
//          return true;
//      }
//  }

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
