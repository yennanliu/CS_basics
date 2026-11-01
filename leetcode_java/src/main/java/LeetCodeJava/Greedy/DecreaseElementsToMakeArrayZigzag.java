package LeetCodeJava.Greedy;

// https://leetcode.com/problems/decrease-elements-to-make-array-zigzag/description/

/**
 * 1144. Decrease Elements To Make Array Zigzag
 * Medium
 *
 * Given an array nums of integers, a move consists of choosing any element
 * and decreasing it by 1.
 *
 * An array A is a zigzag array if either:
 *
 * Every even-indexed element is greater than adjacent elements,
 * ie. A[0] > A[1] < A[2] > A[3] < A[4] > ...
 * OR, every odd-indexed element is greater than adjacent elements,
 * ie. A[0] < A[1] > A[2] < A[3] > A[4] < ...
 *
 * Return the minimum number of moves to transform the given array nums into a zigzag array.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: 2
 * Explanation: We can decrease 2 to 0 or 3 to 1.
 *
 * Example 2:
 *
 * Input: nums = [9,6,1,6,2]
 * Output: 4
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 *
 */
public class DecreaseElementsToMakeArrayZigzag {

    // V0
    // IDEA: GREEDY + TRY BOTH PARITIES
    //   we may only DECREASE, so touching a "peak" never helps. Pick which parity
    //   becomes the valleys, and push every valley down to min(neighbours) - 1.
    //   Valleys are never adjacent, so their costs are independent.
    //   answer = min(cost with even-index valleys, cost with odd-index valleys)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int movesToMakeZigzag(int[] nums) {
        int n = nums.length;
        // cost[p] = moves needed when indices of parity p are the valleys
        int[] cost = new int[2];

        for (int parity = 0; parity < 2; parity++) {
            for (int j = parity; j < n; j += 2) {
                int smallestNeighbour = Integer.MAX_VALUE;
                if (j - 1 >= 0) {
                    smallestNeighbour = Math.min(smallestNeighbour, nums[j - 1]);
                }
                if (j + 1 < n) {
                    smallestNeighbour = Math.min(smallestNeighbour, nums[j + 1]);
                }
                // a valley must be strictly below both neighbours
                if (smallestNeighbour != Integer.MAX_VALUE && nums[j] >= smallestNeighbour) {
                    cost[parity] += nums[j] - smallestNeighbour + 1;
                }
            }
        }
        return Math.min(cost[0], cost[1]);
    }
}
