package LeetCodeJava.Array;

// https://leetcode.com/problems/sort-array-by-parity/description/
/**
 *  905. Sort Array By Parity
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums, move all the even integers at the beginning of the array followed by all the odd integers.
 *
 * Return any array that satisfies this condition.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1,2,4]
 * Output: [2,4,3,1]
 * Explanation: The outputs [4,2,3,1], [2,4,1,3], and [4,2,1,3] would also be accepted.
 * Example 2:
 *
 * Input: nums = [0]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5000
 * 0 <= nums[i] <= 5000
 *
 *
 *
 */
public class SortArrayByParity {

    // V0
//    public int[] sortArrayByParity(int[] nums) {
//
//    }
//
    // V1
    // https://leetcode.com/problems/sort-array-by-parity/solutions/6280114/video-how-we-think-about-a-solution-one-0ks18/
    public int[] sortArrayByParity_1(int[] nums) {
        int left = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 0) {
                int temp = nums[left];
                nums[left] = nums[i];
                nums[i] = temp;
                left++;
            }
        }

        return nums;
    }

    // V2
    // https://leetcode.com/problems/sort-array-by-parity/solutions/170734/cjava-in-place-swap-by-lee215-yf1d/
    public int[] sortArrayByParity_2(int[] A) {
        for (int i = 0, j = 0; j < A.length; j++)
            if (A[j] % 2 == 0) {
                int tmp = A[i];
                A[i++] = A[j];
                A[j] = tmp;
                ;
            }
        return A;
    }


    // V3




}
