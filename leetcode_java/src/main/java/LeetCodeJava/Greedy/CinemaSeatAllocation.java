package LeetCodeJava.Greedy;

// https://leetcode.com/problems/cinema-seat-allocation/description/

import java.util.*;

/**
 * 1386. Cinema Seat Allocation
 * Medium
 *
 * A cinema has n rows of seats, numbered from 1 to n. Each row has 10 seats,
 * numbered from 1 to 10.
 *
 * You are given a 2D integer array reservedSeats, where
 * reservedSeats[i] = [rowi, seati] means that seat seati in row rowi is already
 * reserved.
 *
 * A four-person group must be assigned to four seats in the same row. The group
 * can be seated in one of the following seat blocks:
 *
 * seats 2, 3, 4, 5
 * seats 4, 5, 6, 7
 * seats 6, 7, 8, 9
 *
 * A block can be used only if none of its seats are reserved. Each seat can be
 * assigned to at most one group.
 *
 * Return an integer denoting the maximum number of four-person groups that can be
 * assigned.
 *
 *
 * Example 1:
 *
 * Input: n = 3, reservedSeats = [[1,2],[1,3],[1,8],[2,6],[3,1],[3,10]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: n = 2, reservedSeats = [[2,1],[1,8],[2,6]]
 * Output: 2
 *
 * Example 3:
 *
 * Input: n = 4, reservedSeats = [[4,3],[1,4],[4,6],[1,7]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 * 1 <= reservedSeats.length <= min(10 * n, 10^4)
 * reservedSeats[i] == [rowi, seati]
 * 1 <= rowi <= n
 * 1 <= seati <= 10
 * All reservedSeats[i] are distinct.
 *
 */
public class CinemaSeatAllocation {

    // a row's reservations are kept as a bitmask: bit `seat` is set when that seat is taken.
    // each block below is the mask of its four seats
    private static final int BLOCK_2345 = (1 << 2) | (1 << 3) | (1 << 4) | (1 << 5);
    private static final int BLOCK_6789 = (1 << 6) | (1 << 7) | (1 << 8) | (1 << 9);
    private static final int BLOCK_4567 = (1 << 4) | (1 << 5) | (1 << 6) | (1 << 7);

    // V0
    // IDEA: HASH MAP (row -> reserved-seat bitmask) + GREEDY (outer blocks first, middle only if neither fits)
    /**
     * time = O(M)   M = reservedSeats.length
     * space = O(M)
     */
    public int maxNumberOfFamilies(int n, int[][] reservedSeats) {
        // only rows that have a reservation need inspecting (n can be 10^9)
        Map<Integer, Integer> reservedMask = new HashMap<>();
        for (int[] reserved : reservedSeats) {
            int row = reserved[0];
            int seat = reserved[1];
            int mask = reservedMask.getOrDefault(row, 0);
            reservedMask.put(row, mask | (1 << seat));
        }

        // an untouched row always fits two groups: 2345 and 6789
        long groups = 2L * (n - reservedMask.size());

        for (int taken : reservedMask.values()) {
            boolean leftFree = (taken & BLOCK_2345) == 0;
            boolean rightFree = (taken & BLOCK_6789) == 0;

            if (leftFree && rightFree) {
                groups += 2;
            } else if (leftFree || rightFree) {
                groups += 1;
            } else if ((taken & BLOCK_4567) == 0) {
                // the middle block overlaps both outer ones, so it only helps
                // when neither outer block is available
                groups += 1;
            }
        }
        return (int) groups;
    }
}
