package LeetCodeJava.Math;

// https://leetcode.com/problems/random-pick-index/

import java.util.Random;

/**
 *  398. Random Pick Index
 *  Medium
 *
 *  Given an integer array nums with possible duplicates, randomly output the index of a
 *  given target number. You can assume that the given target number must exist in the array.
 *
 *  Implement the Solution class:
 *   - Solution(int[] nums) Initializes the object with the array nums.
 *   - int pick(int target) Picks a random index i from nums where nums[i] == target.
 *     If there are multiple valid i's, then each index should have an equal probability
 *     of returning.
 *
 *  Example 1:
 *
 *  Input
 *  ["Solution", "pick", "pick", "pick"]
 *  [[[1, 2, 3, 3, 3]], [3], [1], [3]]
 *  Output
 *  [null, 4, 0, 2]
 *
 *  Constraints:
 *
 *  1 <= nums.length <= 2 * 10^4
 *  -2^31 <= nums[i] <= 2^31 - 1
 *  target is an integer from nums.
 *  At most 10^4 calls will be made to pick.
 */
public class RandomPickIndex {

    private final int[] nums;
    private final Random rand;

    // V0
    // IDEA: reservoir sampling over the matching indices -> O(1) extra space
    /**
     * time = O(1)
     * space = O(1)  (keeps a reference to the input array)
     */
    public RandomPickIndex(int[] nums) {
        this.nums = nums;
        this.rand = new Random();
    }

    /**
     * time = O(n)
     * space = O(1)
     */
    public int pick(int target) {
        int result = -1;
        int count = 0;
        for (int i = 0; i < this.nums.length; i++) {
            if (this.nums[i] == target) {
                count++;
                if (this.rand.nextInt(count) == 0) {
                    result = i;
                }
            }
        }
        return result;
    }
}
