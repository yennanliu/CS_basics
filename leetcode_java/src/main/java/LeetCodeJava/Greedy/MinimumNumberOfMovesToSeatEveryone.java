package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-moves-to-seat-everyone/description/

import java.util.Arrays;

/**
 * 2037. Minimum Number of Moves to Seat Everyone
 * Easy
 *
 * There are n availabe seats and n students standing in a room. You are given an array seats of length n,
 * where seats[i] is the position of the ith seat. You are also given the array students of length n, where
 * students[j] is the position of the jth student.
 *
 * You may perform the following move any number of times:
 *
 * Increase or decrease the position of the ith student by 1 (i.e., moving the ith student from position x
 * to x + 1 or x - 1)
 *
 * Return the minimum number of moves required to move each student to a seat such that no two students are
 * in the same seat.
 *
 * Note that there may be multiple seats or students in the same position at the beginning.
 *
 *
 * Example 1:
 *
 * Input: seats = [3,1,5], students = [2,7,4]
 * Output: 4
 *
 * Example 2:
 *
 * Input: seats = [4,1,5,9], students = [1,3,2,6]
 * Output: 7
 *
 * Example 3:
 *
 * Input: seats = [2,2,6,6], students = [1,3,2,6]
 * Output: 4
 *
 *
 * Constraints:
 *
 * n == seats.length == students.length
 * 1 <= n <= 100
 * 1 <= seats[i], students[j] <= 100
 *
 */
public class MinimumNumberOfMovesToSeatEveryone {

    // V0
    // IDEA: SORT BOTH, MATCH IN ORDER (EXCHANGE ARGUMENT - CROSSED PAIRS NEVER HELP)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minMovesToSeat(int[] seats, int[] students) {
        // sort copies so the caller's arrays are not mutated
        int[] sortedSeats = seats.clone();
        int[] sortedStudents = students.clone();
        Arrays.sort(sortedSeats);
        Arrays.sort(sortedStudents);

        // the i-th smallest student takes the i-th smallest seat
        int moves = 0;
        for (int i = 0; i < sortedSeats.length; i++) {
            moves += Math.abs(sortedSeats[i] - sortedStudents[i]);
        }
        return moves;
    }

    // V1
    // IDEA: COUNTING SORT (POSITIONS ARE <= 100) + RUNNING BALANCE OF UNMATCHED STUDENTS
    /**
     * time = O(N + P), P = 100 positions
     * space = O(P)
     */
    public int minMovesToSeat_1(int[] seats, int[] students) {
        final int MAX_POSITION = 100;
        // balance[p] = (#seats at p) - (#students at p)
        int[] balance = new int[MAX_POSITION + 2];
        for (int seat : seats) {
            balance[seat]++;
        }
        for (int student : students) {
            balance[student]--;
        }

        // sweep left to right: `unmatched` is how many seats (or students, if negative) are waiting
        // for a partner; each of them pays one move to cross the gap from p to p + 1
        int moves = 0;
        int unmatched = 0;
        for (int p = 0; p <= MAX_POSITION; p++) {
            unmatched += balance[p];
            moves += Math.abs(unmatched);
        }
        return moves;
    }
}
