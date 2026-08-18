package AlgorithmJava;

import java.util.Arrays;

/**
 *  DIFFERENCE ARRAY -- O(1) range updates
 *
 *  The mirror image of a prefix sum. A prefix sum answers many range
 *  QUERIES after one pass; a difference array applies many range
 *  UPDATES after one pass.
 *
 *  THE IDEA: instead of adding `v` to every slot in [start, end], record
 *  only where the change BEGINS and where it ENDS:
 *
 *      diff[start] += v        the increase starts here
 *      diff[end + 1] -= v      and stops after here
 *
 *  Then one prefix-sum pass turns the deltas back into values. Each
 *  update becomes two writes instead of (end - start + 1) of them.
 *
 *      n = 5, bookings [1,2,10] and [2,3,20]
 *
 *      diff    +10  0   -10   0    0      after [1,2,10]
 *      diff    +10 +20  -30   0    0      after [2,3,20]
 *
 *      prefix   10  30   0    0    0      <- the answer
 *
 *  Naively that would be O(N) per update, so O(K*N) overall. This is
 *  O(K + N) -- the difference is what turns LC 1109 from too slow into
 *  linear.
 *
 *  THE OFF-BY-ONE: `end + 1` is what makes `end` itself inclusive.
 *  Using `end` there would stop the increase one slot early. The array
 *  is sized n+1 so that `end + 1 == n` has somewhere to land instead of
 *  needing a bounds check.
 *
 *  NOTE this file is 1-INDEXED to match LC 1109, where flights are
 *  numbered 1..n. Slot 0 is unused and trimmed off at the end.
 *
 *  Used by: LC 1109 Corporate Flight Bookings, LC 370 Range Addition,
 *           LC 1094 Car Pooling.
 *
 *  Time  : O(K + N) for K updates over N slots
 *  Space : O(N)
 *
 *  Reference: https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/difference_array.md
 */
public class DifferenceArray {

    /**
     *  LC 1109 Corporate Flight Bookings.
     *
     *  @param input each row is {startFlight, endFlight, seats}, 1-indexed
     *               and INCLUSIVE at both ends
     *  @param n     number of flights
     *  @return seats booked on each flight, as a 0-indexed array of length n
     */
    public int[] getDifferenceArray(int[][] input, int n) {
        // 1-indexed, so slot 0 goes unused and slot n is a valid
        // landing spot for `end + 1`
        int[] diff = new int[n + 1];

        for (int[] booking : input) {
            int start = booking[0];
            int end = booking[1];
            int seats = booking[2];

            diff[start] += seats;          // the increase starts here
            if (end + 1 <= n) {            // ...and stops after `end`
                diff[end + 1] -= seats;
            }
        }

        // prefix sum: turn the deltas back into values
        for (int i = 1; i < diff.length; i++) {
            diff[i] += diff[i - 1];
        }

        return Arrays.copyOfRange(diff, 1, n + 1);
    }

    public static void main(String[] args) {
        DifferenceArray solution = new DifferenceArray();

        // LC 1109 example 1
        int[][] bookings1 = {{1, 2, 10}, {2, 3, 20}, {2, 5, 25}};
        assertThat(Arrays.toString(solution.getDifferenceArray(bookings1, 5))
                        .equals("[10, 55, 45, 25, 25]"), "LC 1109 example 1");

        // LC 1109 example 2
        int[][] bookings2 = {{1, 2, 10}, {2, 2, 15}};
        assertThat(Arrays.toString(solution.getDifferenceArray(bookings2, 2))
                        .equals("[10, 25]"), "LC 1109 example 2");

        // no bookings at all
        assertThat(Arrays.toString(solution.getDifferenceArray(new int[0][], 3))
                        .equals("[0, 0, 0]"), "no bookings");

        // both ends are INCLUSIVE -- this is what `end + 1` buys
        assertThat(Arrays.toString(solution.getDifferenceArray(new int[][] {{2, 2, 7}}, 4))
                        .equals("[0, 7, 0, 0]"), "a single-flight range");

        // a range covering everything: `end + 1 > n`, so no closing delta
        assertThat(Arrays.toString(solution.getDifferenceArray(new int[][] {{1, 4, 5}}, 4))
                        .equals("[5, 5, 5, 5]"), "the whole range");

        // overlapping ranges accumulate
        assertThat(Arrays.toString(solution.getDifferenceArray(new int[][] {{1, 3, 1}, {2, 4, 1}}, 4))
                        .equals("[1, 2, 2, 1]"), "overlaps add up");

        System.out.println(Arrays.toString(solution.getDifferenceArray(bookings1, 5)));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
