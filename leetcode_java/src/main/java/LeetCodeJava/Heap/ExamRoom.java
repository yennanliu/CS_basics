package LeetCodeJava.Heap;

// https://leetcode.com/problems/exam-room/description/
/**
 * 855. Exam Room
 * Medium
 * Topics
 * Companies
 * There is an exam room with n seats in a single row labeled from 0 to n - 1.
 * <p>
 * When a student enters the room, they must sit in the seat that maximizes the distance to the closest person. If there are multiple such seats, they sit in the seat with the lowest number. If no one is in the room, then the student sits at seat number 0.
 * <p>
 * Design a class that simulates the mentioned exam room.
 * <p>
 * Implement the ExamRoom class:
 * <p>
 * ExamRoom(int n) Initializes the object of the exam room with the number of the seats n.
 * int seat() Returns the label of the seat at which the next student will set.
 * void leave(int p) Indicates that the student sitting at seat p will leave the room. It is guaranteed that there will be a student sitting at seat p.
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input
 * ["ExamRoom", "seat", "seat", "seat", "seat", "leave", "seat"]
 * [[10], [], [], [], [], [4], []]
 * Output
 * [null, 0, 9, 4, 2, null, 5]
 * <p>
 * Explanation
 * ExamRoom examRoom = new ExamRoom(10);
 * examRoom.seat(); // return 0, no one is in the room, then the student sits at seat number 0.
 * examRoom.seat(); // return 9, the student sits at the last seat number 9.
 * examRoom.seat(); // return 4, the student sits at the last seat number 4.
 * examRoom.seat(); // return 2, the student sits at the last seat number 2.
 * examRoom.leave(4);
 * examRoom.seat(); // return 5, the student sits at the last seat number 5.
 * <p>
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= n <= 109
 * It is guaranteed that there is a student sitting at seat p.
 * At most 104 calls will be made to seat and leave.
 */

import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Your ExamRoom object will be instantiated and called as such:
 * ExamRoom obj = new ExamRoom(n);
 * int param_1 = obj.seat();
 * obj.leave(p);
 */
public class ExamRoom {

    // V0
//    class ExamRoom {
//
//        public ExamRoom(int n) {
//
//        }
//
//        public int seat() {
//
//        }
//
//        public void leave(int p) {
//
//        }
//    }

    // V1

    // V2-1
    // IDEA : Linkedlist (offered by gpt)
    public class ExamRoom_2_1 {

        private int n;
        private ArrayList<Integer> seats;

        public ExamRoom_2_1(int n) {
            this.n = n;
            this.seats = new ArrayList<>();
        }

        public int seat() {
            // If no one is seated, the first student sits at seat 0
            if (seats.isEmpty()) {
                seats.add(0);
                return 0;
            }

            // Determine where to seat the next student
            int maxDist = seats.get(0); // Distance from seat 0 to the first occupied seat
            int seat = 0; // Initially, consider the first seat

            // Check gaps between seated students
            for (int i = 0; i < seats.size() - 1; i++) {
                // calculates the halfway distance between two students
                // who are already seated at positions seats.get(i) and seats.get(i + 1).
                int dist = (seats.get(i + 1) - seats.get(i)) / 2;
                if (dist > maxDist) {
                    maxDist = dist;
                    seat = seats.get(i) + dist; // The seat in the middle
                }
            }

            // Check the distance from the last seated student to the last seat
            if (n - 1 - seats.get(seats.size() - 1) > maxDist) {
                seat = n - 1;
            }

            // Add the student to the seat
            seats.add(seat);
            Collections.sort(seats); // Sort to maintain seat order
            return seat;
        }

        public void leave(int p) {
            seats.remove(Integer.valueOf(p)); // Remove the student from the seat
        }

    }

    // V2-2
    // IDEA : Treeset (offered by gpt)
    /**
     *  Idea:
     *
     *  You can solve the problem by maintaining a sorted list of occupied seats.
     *  Each time a student enters, you look for the largest available gap between two occupied seats,
     *  or between an occupied seat and the room boundaries (start or end).
     *  When a student leaves, you remove their seat from the list.
     */
    public class ExamRoom_2_2 {

        /**
         * TreeSet: A TreeSet is used to maintain the seats in sorted order.
         * This helps efficiently finding gaps between occupied seats
         * and determining where the next student should sit.
         */
        private TreeSet<Integer> seats;
        private int n;

        public ExamRoom_2_2(int n) {
            this.n = n;
            this.seats = new TreeSet<>();
        }

        /**
         * 	2.	seat():
         *
         * 	    •	If no seats are occupied, the student sits at seat 0.
         * 	    •	Otherwise, iterate through the list of occupied seats to find the largest gap between two seats. For each gap, calculate the middle seat and check if it maximizes the distance to the nearest student.
         * 	    •	Finally, also consider the distance from the last occupied seat to the end of the row, in case sitting at the last seat maximizes the distance.
         */
        public int seat() {
            int seat = 0;

            if (seats.size() > 0) {
                // Initialize the max distance and seat to sit at
                int maxDist = seats.first();
                seat = 0; // sitting at the first seat

                Integer prev = null;
                for (Integer s : seats) {
                    if (prev != null) {
                        int dist = (s - prev) / 2;
                        if (dist > maxDist) {
                            maxDist = dist;
                            seat = prev + dist;
                        }
                    }
                    prev = s;
                }

                // Consider sitting at the last seat
                if (n - 1 - seats.last() > maxDist) {
                    seat = n - 1;
                }
            }

            // Add the seat to the occupied set
            seats.add(seat);
            return seat;
        }

        /**
         * 3.	leave(int p): Simply remove seat p from the
         */
        public void leave(int p) {
            seats.remove(p);
        }

    }

    // V3
    // https://leetcode.com/problems/exam-room/solutions/1331530/easy-to-understand-java-solution-with-explanation-treeset/0
    class ExamRoom_3 {

        int capacity; // this is just to keep track of the max allowed size
        TreeSet<Integer> seats;
	/* why treeset, because seats will be sorted automatically
	   and get first()/last() element in log(n) time.
	*/

        public ExamRoom_3(int n) {
            this.capacity = n;
            this.seats = new TreeSet<>();
        }

        public int seat() {
            int seatNumber = 0;
		/*
		  Return 0 for first attempt ( as mentioned in question)
		  Otherwise, we need to calculate the max distance by checking the whole treeset : O(n) time.
		  Note that "distance" variable is initialized to first appearing seat,
		  why because the distance calculation is based on current seat and the seat before that.
		  Find the maximum distance and update the seat number accordingly.
		  distance calculation -> (current seat - previous seat )/ 2
		  Update the max distance at each step.
		  New seat number will be ->  previous seat number + max distance

		  Now, before returning the end result,  check for one more edge case:
		  That is, if the max distance calculated is less than ->  capacity-1-seats.last()

		  Why because -> if last seat number in treeset is far from last position,
		  then  the largest distance possible is the last position.

		*/
            if (seats.size() > 0) {
                Integer prev = null;
                int distance = seats.first();
                for (Integer seat : seats) {
                    if (prev != null) {
                        int d = (seat - prev) / 2;
                        if (distance < d) {
                            distance = d;
                            seatNumber = prev + distance;
                        }
                    }
                    prev = seat;
                }

                if (distance < capacity - 1 - seats.last()) {
                    seatNumber = capacity - 1;
                }
            }
            seats.add(seatNumber);
            return seatNumber;
        }

        public void leave(int p) {
            seats.remove(p);
		/* simply remove the seat number from treeset
		 and treeset will be automatically adjust its order in log(n) time.
		*/
        }
    }

    // V4
    //  https://leetcode.com/problems/exam-room/solutions/2824662/java-simple-one-treeset-solution-with-o-n-seat-and-o-logn-leave/
    class ExamRoom_4 {
        int N;
        TreeSet<Integer> seats;
        public ExamRoom_4(int n) {
            seats = new TreeSet<>(); // ordered set to keep track of students
            this.N = n;
        }

        public int seat() {
            int student = 0;

            if (seats.size() > 0) { // if at least one student, otherwise seat at 0
                int dist = seats.first(); // the student at lowest seat
                Integer prev = null;
                for (Integer seat: seats) {
                    if (prev != null) {
                        int d = (seat - prev)/2; // every time we see a new student we can seat them between 2 other students
                        if (d > dist) { // select the max range
                            dist = d;
                            student = prev + d;
                        }
                    }
                    prev = seat;
                }
                if (N - 1 - seats.last() > dist) {
                    student = N-1;
                }
            }
            seats.add(student);
            return student;
        }

        public void leave(int p) {
            seats.remove(p);
        }
    }

    // V5
    // https://leetcode.com/problems/exam-room/solutions/2085063/java-o-logn-seat-leave-treeset-solution/
    class ExamRoom_5 {

        private final int max;
        private final TreeSet<Interval> available;
        private final TreeSet<Integer> taken;

        public ExamRoom_5(int n) {
            this.max = n - 1;
            this.available = new TreeSet<>((a, b) -> {
                int distA = getMinDistance(a);
                int distB = getMinDistance(b);
                return distA == distB ? a.s - b.s : distB - distA;
            });
            this.available.add(new Interval(0, max));
            this.taken = new TreeSet<>();
        }

        public int seat() {
            Interval inter = available.pollFirst();
            int idx = getInsertPosition(inter);
            taken.add(idx);
            if ((idx - 1) - inter.s >= 0)
                available.add(new Interval(inter.s, idx - 1));
            if (inter.e - (idx + 1) >= 0)
                available.add(new Interval(idx + 1, inter.e));
            return idx;
        }

        public void leave(int p) {
            taken.remove(p);
            Integer lo = taken.lower(p);
            if (lo == null)
                lo = -1;
            Integer hi = taken.higher(p);
            if (hi == null)
                hi = max + 1;
            available.remove(new Interval(lo + 1, p - 1));
            available.remove(new Interval(p + 1, hi - 1));
            available.add(new Interval(lo + 1, hi - 1));
        }

        private int getInsertPosition(Interval inter) {
            if (inter.s == 0)
                return 0;
            else if (inter.e == max)
                return max;
            else
                return inter.s + (inter.e - inter.s) / 2;
        }

        private int getMinDistance(Interval in) {
            return in.s == 0 || in.e == max ? in.e - in.s : (in.e - in.s) / 2;
        }

        private final class Interval {
            private final int s;
            private final int e;

            Interval(int s, int e) {
                this.s = s;
                this.e = e;
            }

            @Override
            public String toString() {
                return "[" + s + "," + e + "]";
            }
        }
    }

}
