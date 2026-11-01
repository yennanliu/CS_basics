package LeetCodeJava.Design;

// https://leetcode.com/problems/design-bus-seat-booking-system/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 4080. Design Bus Seat Booking System
 * Medium
 *
 * You are asked to design a seat booking system for a bus.
 *
 * The bus has n rows. Each row has four seats labeled "A", "B", "C", and "D".
 *
 * Seats "A" and "D" are window seats; seats "B" and "C" are middle seats. In
 * each row, seat "A" is adjacent to seat "B", and seat "D" is adjacent to
 * seat "C".
 *
 * Booking rules:
 *
 * - Booking a middle seat ("B" or "C") always takes 1 second.
 * - Booking a window seat ("A" or "D") takes 1 second if its adjacent middle
 *   seat is not booked, or 3 seconds (an extra delay of 2 seconds) if its
 *   adjacent middle seat is already booked.
 *
 * A seat is identified by a string such as "A1" or "D20" (one letter from A to
 * D followed by its row number).
 *
 * Implement the BusBooking class:
 *
 * - BusBooking(int n): Initializes the system with n empty rows.
 * - void toggle(string seat): Books seat if it is free, or frees it if it is
 *   already booked.
 * - int getTotalTime(): Returns the total time (in seconds) taken to book all
 *   currently booked seats, where the seats are booked in the order their
 *   toggle calls were made. If no seats are currently booked, return 0.
 * - int getMinTime(): Returns the smallest possible total time (in seconds) to
 *   book all currently booked seats, over every possible order of booking them.
 *   If no seats are currently booked, return 0.
 *
 * Note: The time cost of a seat is fixed at the moment it is booked, based on
 * the seats booked at that time. Booking or freeing other seats afterward does
 * not change the cost of a seat that is already booked.
 *
 *
 * Example 1:
 *
 * Input:
 * ["BusBooking", "toggle", "toggle", "getTotalTime", "getMinTime", "toggle", "toggle", "getTotalTime", "getMinTime"]
 * [[20], ["B3"], ["A3"], [], [], ["C3"], ["D3"], [], []]
 *
 * Output:
 * [null, null, null, 4, 2, null, null, 8, 4]
 *
 * Explanation:
 *
 * BusBooking busBooking = new BusBooking(20); // start with 20 empty rows
 * busBooking.toggle("B3");   // middle seat, costs 1. Total time is now 1.
 * busBooking.toggle("A3");   // window seat, adjacent "B3" is booked, costs 3. Total 4.
 * busBooking.getTotalTime(); // return 4
 * busBooking.getMinTime();   // return 2. Booking "A3" before "B3" avoids the delay.
 * busBooking.toggle("C3");   // middle seat, costs 1. Total 5.
 * busBooking.toggle("D3");   // window seat, adjacent "C3" is booked, costs 3. Total 8.
 * busBooking.getTotalTime(); // return 8
 * busBooking.getMinTime();   // return 4. Best order adds no delay : 4 * 1 = 4.
 *
 * Example 2:
 *
 * Input:
 * ["BusBooking", "toggle", "toggle", "getTotalTime", "toggle", "toggle", "toggle", "getTotalTime", "getMinTime"]
 * [[10], ["C7"], ["D7"], [], ["D7"], ["C7"], ["D7"], [], []]
 *
 * Output:
 * [null, null, null, 4, null, null, null, 1, 1]
 *
 * Explanation:
 *
 * BusBooking busBooking = new BusBooking(10); // start with 10 empty rows
 * busBooking.toggle("C7");   // middle seat, costs 1. Total 1.
 * busBooking.toggle("D7");   // window seat, adjacent "C7" is booked, costs 3. Total 4.
 * busBooking.getTotalTime(); // return 4
 * busBooking.toggle("D7");   // already booked, free it : removes its cost of 3. Total 1.
 * busBooking.toggle("C7");   // already booked, free it : removes its cost of 1. Total 0.
 * busBooking.toggle("D7");   // book "D7" again, "C7" is free now, costs 1. Total 1.
 * busBooking.getTotalTime(); // return 1
 * busBooking.getMinTime();   // return 1
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * seat[0] is one of 'A', 'B', 'C', 'D'.
 * The remaining characters of seat form an integer row with 1 <= row <= n.
 * The total number of calls to toggle, getTotalTime, and getMinTime does not exceed 10^5.
 *
 */
public class DesignBusSeatBookingSystem {

    // V0
    // IDEA: HASHMAP (SEAT -> COST PAID WHEN IT WAS BOOKED) + RUNNING TOTAL
    //       getTotalTime : a seat's cost is frozen at booking time, so remember it per seat.
    //       booking adds that cost to a running total, freeing subtracts exactly the cost
    //       that seat paid -> O(1) per call, no booking history to replay.
    //
    //       getMinTime : the only delay is "window seat booked AFTER its adjacent middle
    //       seat". in the best order every window seat (A, D) is booked first, then every
    //       middle seat (B, C, which always cost 1) -> no seat pays the delay, so the
    //       minimum is simply 1 second per booked seat.
    //
    //       e.g. B3, A3 booked in that order -> total = 1 + 3 = 4, min = 2 seats = 2
    /**
     * time = O(1) per call (a seat string is O(log n) long)
     * space = O(K)   // K = seats currently booked
     */
    public static class BusBooking {

        private static final int BASE_COST = 1;
        private static final int WINDOW_DELAY_COST = 3;

        // seat (e.g. "D20") -> the cost it paid when it was booked
        private final Map<String, Integer> seatCost;
        // sum of every value in seatCost, kept up to date on each toggle
        private int totalTime;

        public BusBooking(int n) {
            this.seatCost = new HashMap<>();
            this.totalTime = 0;
        }

        public void toggle(String seat) {
            // case 1) already booked -> free it and refund exactly what it paid
            if (seatCost.containsKey(seat)) {
                totalTime -= seatCost.get(seat);
                seatCost.remove(seat);
                return;
            }

            // case 2) free -> book it, and freeze its cost at this moment
            char column = seat.charAt(0);
            String row = seat.substring(1);

            int cost = BASE_COST;
            String adjacentMiddle = adjacentMiddleSeat(column, row);
            // only a window seat booked AFTER its middle seat pays the delay
            if (adjacentMiddle != null && seatCost.containsKey(adjacentMiddle)) {
                cost = WINDOW_DELAY_COST;
            }

            seatCost.put(seat, cost);
            totalTime += cost;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public int getMinTime() {
            // windows first, then middles -> every booked seat costs the base 1 second
            return seatCost.size() * BASE_COST;
        }

        /** The middle seat that delays a window seat, or null for a middle seat. */
        private String adjacentMiddleSeat(char column, String row) {
            if (column == 'A') {
                return "B" + row;
            }
            if (column == 'D') {
                return "C" + row;
            }
            return null;
        }
    }

    // V0-1
    // IDEA: 2D ARRAY [row][column] HOLDING THE COST EACH SEAT PAID (0 = FREE)
    //       same idea as V0, but the seat is decoded to (row, column) and the frozen cost
    //       lives in an n x 4 grid instead of a map. a count of booked seats answers
    //       getMinTime. allocates O(n) up front even when few seats are booked, which is
    //       why V0 is the one to say in an interview.
    /**
     * time = O(1) per call, O(n) to build
     * space = O(n)
     */
    public static class BusBooking_0_1 {

        private static final int SEATS_PER_ROW = 4;
        private static final int BASE_COST = 1;
        private static final int WINDOW_DELAY_COST = 3;
        private static final int COL_A = 0;
        private static final int COL_B = 1;
        private static final int COL_C = 2;
        private static final int COL_D = 3;

        // cost[row][column] = the cost the seat paid when booked; 0 means the seat is free
        private final int[][] cost;
        private int totalTime;
        private int bookedCount;

        public BusBooking_0_1(int n) {
            this.cost = new int[n][SEATS_PER_ROW];
            this.totalTime = 0;
            this.bookedCount = 0;
        }

        public void toggle(String seat) {
            int column = seat.charAt(0) - 'A';
            // rows are 1-indexed in the seat label, 0-indexed in storage
            int row = Integer.parseInt(seat.substring(1)) - 1;

            // case 1) already booked -> free it and refund its frozen cost
            if (cost[row][column] > 0) {
                totalTime -= cost[row][column];
                bookedCount--;
                cost[row][column] = 0;
                return;
            }

            // case 2) free -> book it; A is delayed by a booked B, D by a booked C
            int seatCost = BASE_COST;
            if (column == COL_A && cost[row][COL_B] > 0) {
                seatCost = WINDOW_DELAY_COST;
            } else if (column == COL_D && cost[row][COL_C] > 0) {
                seatCost = WINDOW_DELAY_COST;
            }

            cost[row][column] = seatCost;
            totalTime += seatCost;
            bookedCount++;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public int getMinTime() {
            // best order books windows before middles -> each seat costs 1
            return bookedCount;
        }
    }
}
