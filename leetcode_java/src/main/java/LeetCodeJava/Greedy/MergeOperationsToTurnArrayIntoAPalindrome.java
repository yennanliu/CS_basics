package LeetCodeJava.Greedy;

// https://leetcode.com/problems/merge-operations-to-turn-array-into-a-palindrome/description/

import java.util.*;

/**
 * 2422. Merge Operations to Turn Array Into a Palindrome
 * Medium
 *
 *
 * You are given an array nums consisting of positive integers.
 *
 * You can perform the following operation on the array any number of times:
 *
 * Choose any two adjacent elements and replace them with their sum.
 * For example, if nums = [1,2,3,1], you can apply one operation to make it [1,5,1].
 *
 * Return the minimum number of operations needed to turn the array into a palindrome.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,3,2,1,2,3,1]
 * Output: 2
 * Explanation: We can turn the array into a palindrome in 2 operations as follows:
 * - Apply the operation on the fourth and fifth element of the array, nums becomes equal to [4,3,2,3,3,1].
 * - Apply the operation on the fifth and sixth element of the array, nums becomes equal to [4,3,2,3,4].
 * The array [4,3,2,3,4] is a palindrome.
 * It can be shown that 2 is the minimum number of operations needed.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 * Output: 3
 * Explanation: We do the operation 3 times in any position, we obtain the array [10] at the end which is a palindrome.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 *
 */
public class MergeOperationsToTurnArrayIntoAPalindrome {

    // V0
    // IDEA: TWO POINTERS, ALWAYS MERGE ON THE SMALLER SIDE
    //       equal ends form the palindrome's outer layer and are kept;
    //       otherwise the smaller end can never be matched alone (merges only grow values),
    //       so it must absorb its neighbour - a forced move, hence optimal.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumOperations(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        // running (possibly merged) values at each end; long guards against overflow
        long leftValue = nums[left];
        long rightValue = nums[right];
        int operations = 0;

        while (left < right) {
            if (leftValue == rightValue) {
                // matched pair: move both inward
                left++;
                right--;
                leftValue = nums[left];
                rightValue = nums[right];
            } else if (leftValue < rightValue) {
                // merge the left end into its right neighbour
                left++;
                leftValue += nums[left];
                operations++;
            } else {
                // merge the right end into its left neighbour
                right--;
                rightValue += nums[right];
                operations++;
            }
        }
        return operations;
    }
}
