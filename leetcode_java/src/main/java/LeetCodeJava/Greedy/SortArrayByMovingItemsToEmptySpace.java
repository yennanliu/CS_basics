package LeetCodeJava.Greedy;

// https://leetcode.com/problems/sort-array-by-moving-items-to-empty-space/description/

import java.util.*;

/**
 * 2459. Sort Array by Moving Items to Empty Space
 * Hard
 *
 *
 * You are given an integer array nums of size n containing each element from 0 to n - 1 (inclusive). Each of the elements from 1 to n - 1 represents an item, and the element 0 represents an empty space.
 *
 * In one operation, you can move any item to the empty space. nums is considered to be sorted if the numbers of all the items are in ascending order and the empty space is either at the beginning or at the end of the array.
 *
 * For example, if n = 4, nums is sorted if:
 *
 * nums = [0,1,2,3] or
 * nums = [1,2,3,0]
 *
 * ...and considered to be unsorted otherwise.
 *
 * Return the minimum number of operations needed to sort nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,2,0,3,1]
 * Output: 3
 * Explanation:
 * - Move item 2 to the empty space. Now, nums = [4,0,2,3,1].
 * - Move item 1 to the empty space. Now, nums = [4,1,2,3,0].
 * - Move item 4 to the empty space. Now, nums = [0,1,2,3,4].
 * It can be proven that 3 is the minimum number of operations needed.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,0]
 * Output: 0
 * Explanation: nums is already sorted so return 0.
 *
 * Example 3:
 *
 * Input: nums = [1,0,2,4,3]
 * Output: 2
 * Explanation:
 * - Move item 2 to the empty space. Now, nums = [1,2,0,4,3].
 * - Move item 3 to the empty space. Now, nums = [1,2,3,4,0].
 * It can be proven that 2 is the minimum number of operations needed.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 2 <= n <= 10^5
 * 0 <= nums[i] < n
 * All the values of nums are unique.
 *
 */
public class SortArrayByMovingItemsToEmptySpace {

    // V0
    // IDEA: PERMUTATION CYCLES, TRY BOTH TARGETS (EMPTY SPACE FIRST OR LAST)
    //       "where does the value at index i belong" is a permutation; per cycle:
    //       length 1 costs 0, the cycle holding 0 costs L - 1 (the hole rotates through it),
    //       any other cycle costs L + 1 (park one item in the hole first, bring it back last).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int sortArray(int[] nums) {
        return Math.min(cost(nums, false), cost(nums, true));
    }

    /** Total moves to reach the target with 0 at the front (zeroLast = false) or the back. */
    private int cost(int[] nums, boolean zeroLast) {
        int n = nums.length;
        boolean[] visited = new boolean[n];
        int total = 0;

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }
            // walk the cycle that starts at this index
            int length = 0;
            boolean hasHole = false;
            int index = start;
            while (!visited[index]) {
                visited[index] = true;
                if (nums[index] == 0) {
                    hasHole = true;
                }
                length++;
                index = home(nums[index], n, zeroLast);
            }
            if (length > 1) {
                if (hasHole) {
                    total += length - 1;
                } else {
                    total += length + 1;
                }
            }
        }
        return total;
    }

    /** Index where value belongs in the target arrangement. */
    private int home(int value, int n, boolean zeroLast) {
        if (zeroLast) {
            // [1, 2, ..., n - 1, 0]: value v sits at v - 1, and 0 sits at n - 1
            return (value - 1 + n) % n;
        }
        // [0, 1, ..., n - 1]: value v sits at v
        return value;
    }
}
