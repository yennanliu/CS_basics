package LeetCodeJava.Array;

// https://leetcode.com/problems/binary-searchable-numbers-in-an-unsorted-array/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1966. Binary Searchable Numbers in an Unsorted Array
 * Medium
 *
 * Consider a function that implements an algorithm similar to Binary Search. The function has two
 * input parameters: sequence is a sequence of integers, and target is an integer value. The purpose
 * of the function is to find if the target exists in the sequence.
 *
 * The pseudocode of the function is as follows:
 *
 * func(sequence, target)
 *   while sequence is not empty
 *     randomly choose an element from sequence as the pivot
 *     if pivot = target, return true
 *     else if pivot < target, remove pivot and all elements to its left from the sequence
 *     else, remove pivot and all elements to its right from the sequence
 *   end while
 *   return false
 *
 * When the sequence is sorted, the function works correctly for all values. When the sequence is
 * not sorted, the function does not work for all values, but may still work for some values.
 *
 * Given an integer array nums, representing the sequence, that contains unique numbers and may or
 * may not be sorted, return the number of values that are guaranteed to be found using the
 * function, for every possible pivot selection.
 *
 *
 * Example 1:
 *
 * Input: nums = [7]
 * Output: 1
 * Explanation: Searching for value 7 is guaranteed to be found.
 *
 * Example 2:
 *
 * Input: nums = [-1,5,2]
 * Output: 1
 * Explanation: Searching for value -1 is guaranteed to be found. Searching for 5 or 2 is not:
 * a pivot of 2 (or 5) can remove the target. Because only -1 is guaranteed to be found, return 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^5 <= nums[i] <= 10^5
 * All the values of nums are unique.
 *
 *
 * Follow-up: If nums has duplicates, would you modify your algorithm? If so, how?
 *
 */
public class BinarySearchableNumbersInAnUnsortedArray {

    // V0
    // IDEA: PREFIX MAX + SUFFIX MIN
    // nums[i] survives every pivot choice iff everything on its left is smaller and everything on its
    // right is larger. A bigger left element chosen as pivot would discard nums[i] with the right
    // side (and symmetrically), so: nums[i] > max(left part) and nums[i] < min(right part).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int binarySearchableNumbers(int[] nums) {
        int n = nums.length;
        // searchable[i] stays true only if nums[i] passes both tests
        boolean[] searchable = new boolean[n];

        int maxSoFar = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            searchable[i] = nums[i] > maxSoFar;
            maxSoFar = Math.max(maxSoFar, nums[i]);
        }

        int minSoFar = Integer.MAX_VALUE;
        int count = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (searchable[i] && nums[i] < minSoFar) {
                count++;
            }
            minSoFar = Math.min(minSoFar, nums[i]);
        }
        return count;
    }

    // V1
    // IDEA: MONOTONIC INCREASING STACK, ONE PASS
    // the stack holds candidates that are larger than everything before them. A new value pops every
    // candidate bigger than it (they now have a smaller element on their right); it becomes a
    // candidate itself only if it is larger than every value seen so far.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int binarySearchableNumbers_1(int[] nums) {
        Deque<Integer> candidates = new ArrayDeque<>();
        int maxSoFar = Integer.MIN_VALUE;
        for (int value : nums) {
            while (!candidates.isEmpty() && candidates.peek() > value) {
                candidates.pop();
            }
            if (value > maxSoFar) {
                candidates.push(value);
                maxSoFar = value;
            }
        }
        return candidates.size();
    }
}
