package LeetCodeJava.Heap;

// https://leetcode.com/problems/construct-target-array-with-multiple-sums/description/

import java.util.*;

/**
 * 1354. Construct Target Array With Multiple Sums
 * Hard
 *
 * You are given an array target of n integers. From a starting array arr consisting of n 1's,
 * you may perform the following procedure :
 *
 * let x be the sum of all elements currently in your array.
 * choose index i, such that 0 <= i < n and set the value of arr at index i to x.
 * You may repeat this procedure as many times as needed.
 *
 * Return true if it is possible to construct the target array from arr, otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: target = [9,3,5]
 * Output: true
 * Explanation: Start with arr = [1, 1, 1]
 * [1, 1, 1], sum = 3 choose index 1
 * [1, 3, 1], sum = 5 choose index 2
 * [1, 3, 5], sum = 9 choose index 0
 * [9, 3, 5] Done
 *
 * Example 2:
 *
 * Input: target = [1,1,1,2]
 * Output: false
 * Explanation: Impossible to create target array from [1,1,1,1].
 *
 * Example 3:
 *
 * Input: target = [8,5]
 * Output: true
 *
 *
 * Constraints:
 *
 * n == target.length
 * 1 <= n <= 5 * 10^4
 * 1 <= target[i] <= 10^9
 *
 */
public class ConstructTargetArrayWithMultipleSums {

    // V0
    // IDEA: REVERSE SIMULATION + MAX HEAP (the max was written last; undo it with a modulo jump)
    /**
     * time = O(N log N log M)   M = max(target)
     * space = O(N)
     */
    public boolean isPossible(int[] target) {
        // the sum can reach 5 * 10^4 * 10^9, so it needs a long
        long total = 0;
        PriorityQueue<Long> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int value : target) {
            total += value;
            maxHeap.add((long) value);
        }

        // going backwards is forced: the largest element equals the sum of the array
        // at the moment it was written, so it must be the one written last
        while (maxHeap.peek() > 1) {
            long largest = maxHeap.poll();
            long restSum = total - largest;

            // a single element above 1 can never be built,
            // and the previous value would be <= 0 if largest <= restSum
            if (restSum == 0 || largest - restSum < 1) {
                return false;
            }

            // subtracting restSum one step at a time is too slow ([1, 10^9]),
            // so jump straight to the last value still >= 1
            long previous = largest % restSum;
            if (previous == 0) {
                // only possible when restSum == 1: the value can drop all the way to 1
                previous = restSum;
            }

            maxHeap.add(previous);
            total = restSum + previous;
        }
        return true;
    }
}
