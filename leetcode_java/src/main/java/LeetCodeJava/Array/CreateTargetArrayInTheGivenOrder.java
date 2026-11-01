package LeetCodeJava.Array;

// https://leetcode.com/problems/create-target-array-in-the-given-order/description/

import java.util.*;

/**
 * 1389. Create Target Array in the Given Order
 * Easy
 *
 * Given two arrays of integers nums and index. Your task is to create target array under the
 * following rules:
 *
 * Initially target array is empty.
 * From left to right read nums[i] and index[i], insert at index index[i] the value nums[i]
 * in target array.
 * Repeat the previous step until there are no elements to read in nums and index.
 *
 * Return the target array.
 *
 * It is guaranteed that the insertion operations will be valid.
 *
 * Example 1:
 *
 * Input: nums = [0,1,2,3,4], index = [0,1,2,2,1]
 * Output: [0,4,1,3,2]
 * Explanation:
 * nums       index     target
 * 0            0        [0]
 * 1            1        [0,1]
 * 2            2        [0,1,2]
 * 3            2        [0,1,3,2]
 * 4            1        [0,4,1,3,2]
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,0], index = [0,1,2,3,0]
 * Output: [0,1,2,3,4]
 * Explanation:
 * nums       index     target
 * 1            0        [1]
 * 2            1        [1,2]
 * 3            2        [1,2,3]
 * 4            3        [1,2,3,4]
 * 0            0        [0,1,2,3,4]
 *
 * Example 3:
 *
 * Input: nums = [1], index = [0]
 * Output: [1]
 *
 * Constraints:
 *
 * 1 <= nums.length, index.length <= 100
 * nums.length == index.length
 * 0 <= nums[i] <= 100
 * 0 <= index[i] <= i
 *
 */
public class CreateTargetArrayInTheGivenOrder {

    // V0
    // IDEA: DIRECT SIMULATION (List.add(index, value) does the shifting)
    //   index[i] <= i guarantees the position always exists; n <= 100, so the
    //   O(n) cost of each insert does not matter
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int[] createTargetArray(int[] nums, int[] index) {
        List<Integer> target = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            // shifts everything from index[i] onward one slot right
            target.add(index[i], nums[i]);
        }

        int[] result = new int[target.size()];
        for (int i = 0; i < target.size(); i++) {
            result[i] = target.get(i);
        }
        return result;
    }
}
