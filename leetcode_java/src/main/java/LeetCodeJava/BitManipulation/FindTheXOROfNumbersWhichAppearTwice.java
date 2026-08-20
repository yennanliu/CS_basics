package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-the-xor-of-numbers-which-appear-twice/

import java.util.HashSet;
import java.util.Set;

/**
 *  3158. Find the XOR of Numbers Which Appear Twice
 *  Easy
 *
 *  You are given an array nums, where each number in the array appears either
 *  once or twice.
 *
 *  Return the bitwise XOR of all the numbers that appear twice in the array, or
 *  0 if no number appears twice.
 *
 *  Example 1:
 *    Input: nums = [1,2,1,3]
 *    Output: 1
 *    Explanation: the only number that appears twice is 1.
 *
 *  Example 2:
 *    Input: nums = [1,2,3]
 *    Output: 0
 *    Explanation: no number appears twice.
 *
 *  Example 3:
 *    Input: nums = [1,2,2,1]
 *    Output: 3
 *    Explanation: 1 and 2 appear twice, and 1 XOR 2 == 3.
 *
 *  Constraints:
 *    1 <= nums.length <= 50
 *    1 <= nums[i] <= 50
 *    Each number in nums appears either once or twice.
 */
public class FindTheXOROfNumbersWhichAppearTwice {

    // V0
    // IDEA: A SET REMEMBERS WHAT WAS SEEN - XOR ON THE SECOND SIGHTING
    //       since nothing appears more than twice, the moment a value shows up
    //       again it is a "twice" value, so fold it into the running XOR right
    //       then. an empty result is 0, exactly the required fallback.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int duplicateNumbersXOR(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        int res = 0;
        for (int x : nums) {
            if (!seen.add(x)) {
                res ^= x;
            }
        }
        return res;
    }
}
