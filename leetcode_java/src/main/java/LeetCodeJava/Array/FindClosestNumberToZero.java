package LeetCodeJava.Array;

// https://leetcode.com/problems/find-closest-number-to-zero/description/

/**
 * 2239. Find Closest Number to Zero
 * Easy
 *
 * Given an integer array nums of size n, return the number with the value closest to 0 in nums. If there are multiple answers, return the number with the largest value.
 *
 *
 * Example 1:
 *
 * Input: nums = [-4,-2,1,4,8]
 * Output: 1
 * Explanation:
 * The distance from -4 to 0 is |-4| = 4.
 * The distance from -2 to 0 is |-2| = 2.
 * The distance from 1 to 0 is |1| = 1.
 * The distance from 4 to 0 is |4| = 4.
 * The distance from 8 to 0 is |8| = 8.
 * Thus, the closest number to 0 in the array is 1.
 *
 * Example 2:
 *
 * Input: nums = [2,-1,1]
 * Output: 1
 * Explanation: 1 and -1 are both the closest numbers to 0, so 1 being larger is returned.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 * -10^5 <= nums[i] <= 10^5
 *
 */
public class FindClosestNumberToZero {

    // V0
    // IDEA: ONE PASS -- SMALLER ABSOLUTE VALUE WINS, TIES GO TO THE LARGER VALUE
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findClosestNumber(int[] nums) {
        int closest = nums[0];
        for (int num : nums) {
            int distance = Math.abs(num);
            int bestDistance = Math.abs(closest);
            if (distance < bestDistance) {
                closest = num;
            } else if (distance == bestDistance && num > closest) {
                // tie: -x vs x -> keep the positive one
                closest = num;
            }
        }
        return closest;
    }
}
