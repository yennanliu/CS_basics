package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-moves-to-equal-array-elements/

/**
 *  453. Minimum Moves to Equal Array Elements
 *  Medium
 *
 *  Given an integer array nums of size n, return the minimum number of moves
 *  required to make all array elements equal.
 *
 *  In one move, you can increment n - 1 elements of the array by 1.
 *
 *  Example 1:
 *    Input: nums = [1,2,3]
 *    Output: 3
 *    Explanation: Only three moves are needed (remember each move increments two elements):
 *    [1,2,3]  =>  [2,3,3]  =>  [3,4,3]  =>  [4,4,4]
 *
 *  Example 2:
 *    Input: nums = [1,1,1]
 *    Output: 0
 *
 *  Constraints:
 *    n == nums.length
 *    1 <= nums.length <= 10^5
 *    -10^9 <= nums[i] <= 10^9
 *    The answer is guaranteed to fit in a 32-bit integer.
 */
public class MinimumMovesToEqualArrayElements {

    // V0
    // IDEA: incrementing n-1 elements == decrementing 1 element,
    //       so answer = sum(nums) - n * min(nums)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int minMoves(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        long sum = 0;
        int min = Integer.MAX_VALUE;
        for (int x : nums) {
            sum += x;
            min = java.lang.Math.min(min, x);
        }
        return (int) (sum - (long) nums.length * min);
    }
}
