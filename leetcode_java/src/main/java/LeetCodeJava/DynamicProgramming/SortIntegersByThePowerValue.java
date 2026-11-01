package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/sort-integers-by-the-power-value/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1387. Sort Integers by The Power Value
 * Medium
 *
 * The power of an integer x is defined as the number of steps needed to transform
 * x into 1 using the following steps:
 *
 * if x is even then x = x / 2
 * if x is odd then x = 3 * x + 1
 *
 * For example, the power of x = 3 is 7 because 3 needs 7 steps to become 1
 * (3 --> 10 --> 5 --> 16 --> 8 --> 4 --> 2 --> 1).
 *
 * Given three integers lo, hi and k. The task is to sort all integers in the
 * interval [lo, hi] by the power value in ascending order, if two or more integers
 * have the same power value sort them by ascending order.
 *
 * Return the kth integer in the range [lo, hi] sorted by the power value.
 *
 * Notice that for any integer x (lo <= x <= hi) it is guaranteed that x will
 * transform into 1 using these steps and that the power of x is will fit in a
 * 32-bit signed integer.
 *
 *
 * Example 1:
 *
 * Input: lo = 12, hi = 15, k = 2
 * Output: 13
 * Explanation: The power of 12 is 9 (12 --> 6 --> 3 --> 10 --> 5 --> 16 --> 8 --> 4 --> 2 --> 1)
 * The power of 13 is 9
 * The power of 14 is 17
 * The power of 15 is 17
 * The interval sorted by the power value [12,13,14,15]. For k = 2 answer is the
 * second element which is 13.
 *
 * Example 2:
 *
 * Input: lo = 7, hi = 11, k = 4
 * Output: 7
 * Explanation: The power array corresponding to the interval [7, 8, 9, 10, 11] is
 * [16, 3, 19, 6, 14].
 * The interval sorted by power is [8, 10, 11, 7, 9].
 * The fourth number in the sorted array is 7.
 *
 *
 * Constraints:
 *
 * 1 <= lo <= hi <= 1000
 * 1 <= k <= hi - lo + 1
 *
 */
public class SortIntegersByThePowerValue {

    // V0
    // IDEA: MEMOIZED COLLATZ "POWER" (TOP-DOWN DP) + SORT BY (POWER, VALUE)
    /**
     * power(1) = 0
     * power(x) = 1 + power(x / 2)       if x is even
     * power(x) = 1 + power(3 * x + 1)   if x is odd
     *
     * the Collatz chains of numbers in [lo, hi] overlap heavily, so memoising
     * them is what makes this a DP problem.
     */
    /**
     * time = O(N log N + N * M), N = hi - lo + 1, M = max chain length
     * space = O(N + M)
     */
    public int getKth(int lo, int hi, int k) {
        Map<Long, Integer> memo = new HashMap<>();
        memo.put(1L, 0);

        int size = hi - lo + 1;
        Integer[] numbers = new Integer[size];
        int[] powerOf = new int[size];
        for (int x = lo; x <= hi; x++) {
            numbers[x - lo] = x;
            powerOf[x - lo] = power(x, memo);
        }

        // ascending power, ties broken by ascending value
        Arrays.sort(numbers, (a, b) -> {
            int byPower = Integer.compare(powerOf[a - lo], powerOf[b - lo]);
            if (byPower != 0) {
                return byPower;
            }
            return Integer.compare(a, b);
        });
        return numbers[k - 1];
    }

    /** Collatz steps from x down to 1, caching every value on the chain. */
    private int power(long x, Map<Long, Integer> memo) {
        // walk down until a cached value is hit
        List<Long> path = new ArrayList<>();
        long current = x;
        while (!memo.containsKey(current)) {
            path.add(current);
            if (current % 2 == 0) {
                current = current / 2;
            } else {
                current = 3 * current + 1;
            }
        }

        // fill the cache back up: each value is one step further than the next
        int steps = memo.get(current);
        for (int i = path.size() - 1; i >= 0; i--) {
            steps++;
            memo.put(path.get(i), steps);
        }
        return memo.get(x);
    }
}
