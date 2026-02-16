package LeetCodeJava.Array;

// https://leetcode.com/problems/shuffle-the-array/description/
/**
 * 1470. Shuffle the Array
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given the array nums consisting of 2n elements in the form [x1,x2,...,xn,y1,y2,...,yn].
 *
 * Return the array in the form [x1,y1,x2,y2,...,xn,yn].
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,5,1,3,4,7], n = 3
 * Output: [2,3,5,4,1,7]
 * Explanation: Since x1=2, x2=5, x3=1, y1=3, y2=4, y3=7 then the answer is [2,3,5,4,1,7].
 * Example 2:
 *
 * Input: nums = [1,2,3,4,4,3,2,1], n = 4
 * Output: [1,4,2,3,3,2,4,1]
 * Example 3:
 *
 * Input: nums = [1,1,2,2], n = 2
 * Output: [1,2,1,2]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 500
 * nums.length == 2n
 * 1 <= nums[i] <= 10^3
 *
 *
 */
public class ShuffleTheArray {

    // V0
//    public int[] shuffle(int[] nums, int n) {
//
//    }

    // V0-1
    // IDEA: ARRAY OP (gemini)
    public int[] shuffle_0_1(int[] nums, int n) {
        // 1. The result array must accommodate all 2n elements
        int[] res = new int[2 * n];

        // 2. Iterate through the first half (n elements)
        for (int i = 0; i < n; i++) {
            // Place x_i at even indices: 0, 2, 4...
            res[2 * i] = nums[i];

            // Place y_i at odd indices: 1, 3, 5...
            // y_i is found at index (i + n) in the original array
            res[2 * i + 1] = nums[i + n];
        }

        return res;
    }

    // V0-2
    // IDEA: ARRAY OP (GPT)
    public int[] shuffle_0_2(int[] nums, int n) {

        int[] res = new int[2 * n];

        for (int i = 0; i < n; i++) {
            res[2 * i] = nums[i]; // x_i
            res[2 * i + 1] = nums[i + n]; // y_i
        }

        return res;
    }

    // V1-1
    // https://leetcode.com/problems/shuffle-the-array/solutions/1314638/java-c-simple-o1-space-in-place-100-fast-9gr1/
    public int[] shuffle_1_1(int[] nums, int n) {

        int len = nums.length;

        // to store the pair of numbers in right half of the original array
        for (int i = n; i < len; i++) {
            nums[i] = (nums[i] * 1024) + nums[i - n];
        }

        int index = 0;
        // to retrive values from the pair of numbers and placing those retrieved value at their desired position
        for (int i = n; i < len; i++, index += 2) {
            nums[index] = nums[i] % 1024;
            nums[index + 1] = nums[i] / 1024;
        }

        return nums;
    }

    // V1-2
    // IDEA: BIT OP
    // https://leetcode.com/problems/shuffle-the-array/solutions/1314638/java-c-simple-o1-space-in-place-100-fast-9gr1/
    public int[] shuffle_1_2(int[] nums, int n) {

        int len = nums.length;

        for (int i = n; i < len; i++) {
            nums[i] = (nums[i] << 10) | nums[i - n];
        }

        int index = 0;
        for (int i = n; i < len; i++, index += 2) {
            nums[index] = nums[i] & 1023;
            nums[index + 1] = nums[i] >>> 10;
        }

        return nums;
    }


    // V2
    public int[] shuffle_2(int[] nums, int n) {
        int[] result = new int[2 * n];
        for (int i = 0; i < n; i++) {
            result[2 * i] = nums[i];
            result[2 * i + 1] = nums[n + i];
        }
        return result;
    }



}
