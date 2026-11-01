package LeetCodeJava.BFS;

// https://leetcode.com/problems/stepping-numbers/description/

import java.util.*;

/**
 * 1215. Stepping Numbers
 * Medium
 *
 * A stepping number is an integer such that all of its adjacent digits have an absolute
 * difference of exactly 1.
 *
 * For example, 321 is a stepping number while 421 is not.
 *
 * Given two integers low and high, return a sorted list of all the stepping numbers in the
 * inclusive range [low, high].
 *
 * Example 1:
 *
 * Input: low = 0, high = 21
 * Output: [0,1,2,3,4,5,6,7,8,9,10,12,21]
 *
 * Example 2:
 *
 * Input: low = 10, high = 15
 * Output: [10,12]
 *
 *
 * Constraints:
 *
 * 0 <= low <= high <= 2 * 10^9
 *
 */
public class SteppingNumbers {

    // V0
    // IDEA: BFS (BUILD STEPPING NUMBERS DIGIT BY DIGIT)
    //  seed the queue with 1..9; from v with last digit d the only one-digit-longer
    //  stepping numbers are v*10 + (d-1) and v*10 + (d+1). The queue pops in
    //  increasing order, so the first value above high ends the search.
    //  0 is handled on its own (it cannot be extended without duplicating 1).
    /**
     * time = O(2^D), D = number of digits of high
     * space = O(2^D)
     */
    public List<Integer> countSteppingNumbers(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low == 0) {
            result.add(0);
        }

        // long: v * 10 can pass Integer.MAX_VALUE when high is close to 2 * 10^9
        Deque<Long> queue = new ArrayDeque<>();
        for (long digit = 1; digit <= 9; digit++) {
            queue.offer(digit);
        }

        while (!queue.isEmpty()) {
            long value = queue.poll();
            if (value > high) {
                break;
            }
            if (value >= low) {
                result.add((int) value);
            }
            long lastDigit = value % 10;
            if (lastDigit > 0) {
                queue.offer(value * 10 + lastDigit - 1);
            }
            if (lastDigit < 9) {
                queue.offer(value * 10 + lastDigit + 1);
            }
        }
        return result;
    }

    // V1
    // IDEA: DFS (SAME GENERATION RULE, RECURSIVE), THEN SORT
    /**
     * time = O(2^D * log(2^D))
     * space = O(2^D)
     */
    public List<Integer> countSteppingNumbers_1(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low == 0) {
            result.add(0);
        }
        for (long digit = 1; digit <= 9; digit++) {
            dfs(digit, low, high, result);
        }
        // DFS emits in depth-first order, not numeric order
        Collections.sort(result);
        return result;
    }

    /** Collects every stepping number in [low, high] that starts with the digits of value. */
    private void dfs(long value, int low, int high, List<Integer> result) {
        if (value > high) {
            return;
        }
        if (value >= low) {
            result.add((int) value);
        }
        long lastDigit = value % 10;
        if (lastDigit > 0) {
            dfs(value * 10 + lastDigit - 1, low, high, result);
        }
        if (lastDigit < 9) {
            dfs(value * 10 + lastDigit + 1, low, high, result);
        }
    }
}
