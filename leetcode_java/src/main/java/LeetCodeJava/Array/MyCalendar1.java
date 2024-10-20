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
    // detailed explain : https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/array_overlap_explaination.md
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

    // V0-1
    // IDEA : ARRAY + BOUNDARY HANDLING
    class MyCalendar_0_1 {

        List<List<Integer>> bookings;

        public MyCalendar_0_1() {
            this.bookings = new ArrayList<>();
        }

        public boolean book(int start, int end) {
            if (this.bookings.size()==0){
                List<Integer> newBooking = new ArrayList<>();
                newBooking.add(start);
                newBooking.add(end);
                this.bookings.add(newBooking);
                return true;
            }
            /**
             *  Overlap 3 cases
             *
             *  case 1)
             *
             *    |-----| old       new[1] > old[0] && new[0] < old[1]
             * |----|     new
             *
             *
             * case 2)
             *    |-----|      old     new[1] > old[0] && new[0] < old[1]
             *        |-----|  new
             *
             * case 3)
             *
             *    |------|       old    new[1] > old[0] && new[0] < old[1]
             *  |-------------|  new
             *
             *
             *  -> so, all overlap cases
             *  -> are with condition : "new[1] > old[0] && new[0] < old[1]"
             */
            for (List<Integer> booking: bookings){
                // NOTE !!! check if overlap happens
                if (booking.get(1) > start && booking.get(0) < end){
                    return false;
                }
            }
            List<Integer> newBooking = new ArrayList<>();
            newBooking.add(start);
            newBooking.add(end);
            this.bookings.add(newBooking);
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

    // V2-1
    // IDEA : LINKEDLIST
    // https://leetcode.com/problems/my-calendar-i/solutions/1262570/js-python-java-c-easy-sorted-tree-linked-list-solutions-w-explanation/
    class ListNode {
        public int start, end;
        public ListNode next;

        public ListNode(int s, int e, ListNode n) {
            start = s;
            end = e;
            next = n;
        }
    }

    class MyCalendar_2_1 {
        ListNode calendar;

        public MyCalendar_2_1() {
            ListNode tail = new ListNode(Integer.MAX_VALUE, Integer.MAX_VALUE, null);
            calendar = new ListNode(-1, -1, tail);
        }

        public boolean book(int start, int end) {
            ListNode curr = calendar, last = curr;
            while (start >= curr.end) {
                last = curr;
                curr = curr.next;
            }
            if (curr.start < end)
                return false;
            last.next = new ListNode(start, end, curr);
            return true;
        }
    }

    // V3
    // IDEA : LINKEDLIST
    // https://leetcode.com/problems/my-calendar-i/solutions/2372060/java-easy-solution-100-faster-code/
    class Node {
        int start, end;
        Node left;
        Node right;

        public Node(int start, int end) {
            this.start = start;
            this.end = end;
            left = null;
            right = null;
        }
    }

    class MyCalendar_3 {

        Node root;

        public MyCalendar_3() {
            this.root = null;

        }

        public boolean insert(Node parent, int s, int e) {
            if (parent.start >= e) {
                if (parent.left == null) {
                    parent.left = new Node(s, e);
                    return true;
                } else {
                    return insert(parent.left, s, e);
                }
            } else if (parent.end <= s) {
                if (parent.right == null) {
                    parent.right = new Node(s, e);
                    return true;
                } else {
                    return insert(parent.right, s, e);
                }
            }

            return false;
        }

        public boolean book(int start, int end) {
            if (root == null) {
                root = new Node(start, end);
                return true;
            } else {
                return insert(root, start, end);
            }
        }
    }

}
