package LeetCodeJava.Array;

// https://leetcode.com/problems/shuffle-an-array/

import java.util.Random;

/**
 *  384. Shuffle an Array
 *  Medium
 *
 *  Given an integer array nums, design an algorithm to randomly shuffle
 *  the array. All permutations of the array should be equally likely as a
 *  result of the shuffling.
 *
 *  Implement the Solution class:
 *   - Solution(int[] nums) initializes the object with the integer array nums.
 *   - int[] reset() resets the array to its original configuration and returns it.
 *   - int[] shuffle() returns a random shuffling of the array.
 *
 *  Example 1:
 *   Input: ["Solution","shuffle","reset","shuffle"], [[[1,2,3]],[],[],[]]
 *   Output: [null,[3,1,2],[1,2,3],[1,3,2]]
 *
 *  Constraints:
 *   1 <= nums.length <= 50
 *   -10^6 <= nums[i] <= 10^6
 *   All the elements of nums are unique.
 *   At most 10^4 calls in total will be made to reset and shuffle.
 */
public class ShuffleAnArray {

    private final int[] original;
    private final int[] arr;
    private final Random rand;

    // V0
    // IDEA: FISHER-YATES SHUFFLE (keep a pristine copy for reset)
    /**
     * time = O(n)
     * space = O(n)
     */
    public ShuffleAnArray(int[] nums) {
        this.original = nums.clone();
        this.arr = nums.clone();
        this.rand = new Random();
    }

    /**
     * time = O(n)
     * space = O(1)
     */
    public int[] reset() {
        System.arraycopy(this.original, 0, this.arr, 0, this.original.length);
        return this.arr;
    }

    /**
     * time = O(n)
     * space = O(1)
     */
    public int[] shuffle() {
        // NOTE !!! swap arr[i] with a random idx in [i, n-1] -> uniform permutation
        for (int i = this.arr.length - 1; i > 0; i--) {
            int j = this.rand.nextInt(i + 1);
            int tmp = this.arr[i];
            this.arr[i] = this.arr[j];
            this.arr[j] = tmp;
        }
        return this.arr;
    }
}
