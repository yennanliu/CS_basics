package LeetCodeJava.Array;

// https://leetcode.com/problems/my-calendar-i/description/

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * 729. My Calendar I
 * Medium
 * Topics
 * Companies
 * Hint
 * You are implementing a program to use as your calendar. We can add a new event if adding the event will not cause a double booking.
 *
 * A double booking happens when two events have some non-empty intersection (i.e., some moment is common to both events.).
 *
 * The event can be represented as a pair of integers start and end that represents a booking on the half-open interval [start, end), the range of real numbers x such that start <= x < end.
 *
 * Implement the MyCalendar class:
 *
 * MyCalendar() Initializes the calendar object.
 * boolean book(int start, int end) Returns true if the event can be added to the calendar successfully without causing a double booking. Otherwise, return false and do not add the event to the calendar.
 *
 *
 * Example 1:
 *
 * Input
 * ["MyCalendar", "book", "book", "book"]
 * [[], [10, 20], [15, 25], [20, 30]]
 * Output
 * [null, true, false, true]
 *
 * Explanation
 * MyCalendar myCalendar = new MyCalendar();
 * myCalendar.book(10, 20); // return True
 * myCalendar.book(15, 25); // return False, It can not be booked because time 15 is already booked by another event.
 * myCalendar.book(20, 30); // return True, The event can be booked, as the first event takes every time less than 20, but not including 20.
 *
 *
 * Constraints:
 *
 * 0 <= start < end <= 109
 * At most 1000 calls will be made to book.
 *
 */
public class MyCalendar1 {

    // V0
    // IDEA : BRUTE FORCE (fixed by gpt)
    class MyCalendar {
        List<List<Integer>> dates;

        public MyCalendar() {
            this.dates = new ArrayList<>();
        }

        public boolean book(int start, int end) {
            for (List<Integer> date : dates) {
                // Check if the new booking overlaps with an existing booking
                /**
                 *
                 *
                 * 	•	The condition if (start < date.get(1) && end > date.get(0)) checks if the new event overlaps with any existing booking. Specifically, this checks:
                 * 	•	start < date.get(1) ensures that the new event starts before the current event ends.
                 * 	•	end > date.get(0) ensures that the new event ends after the current event starts.
                 * 	•	If there is an overlap, return false.
                 * 	•	If no overlap exists, the event is added to the list and returns true.
                 *
                 */
                if (start < date.get(1) && end > date.get(0)) {
                    return false;  // There's an overlap
                }
            }
            // If no overlap, add the new booking
            List<Integer> newBook = new ArrayList<>();
            newBook.add(start);
            newBook.add(end);
            this.dates.add(newBook);
            return true;
        }
    }

    // V1-1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/my-calendar-i/editorial/
    public class MyCalendar_1_1 {
        List<int[]> calendar;

        MyCalendar_1_1() {
            calendar = new ArrayList();
        }

        /**
         *    10     20      iv
         *       15     25   start, end
         *
         */
        public boolean book(int start, int end) {
            for (int[] iv: calendar) {
                if (iv[0] < end && start < iv[1]) {
                    return false;
                }
            }
            calendar.add(new int[]{start, end});
            return true;
        }
    }

    // V1-2
    // IDEA : Sorted List + Binary Search
    // https://leetcode.com/problems/my-calendar-i/editorial/
    public class MyCalendar_1_2 {
        TreeMap<Integer, Integer> calendar;

        MyCalendar_1_2() {
            calendar = new TreeMap();
        }

        public boolean book(int start, int end) {
            Integer prev = calendar.floorKey(start),
                    next = calendar.ceilingKey(start);
            if ((prev == null || calendar.get(prev) <= start) &&
                    (next == null || end <= next)) {
                calendar.put(start, end);
                return true;
            }
            return false;
        }
    }

    // V2
}
