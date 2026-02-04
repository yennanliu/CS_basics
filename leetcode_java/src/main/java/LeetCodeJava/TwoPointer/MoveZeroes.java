package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/move-zeroes/
/**
 * 283. Move Zeroes
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements.
 *
 * Note that you must do this in-place without making a copy of the array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,0,3,12]
 * Output: [1,3,12,0,0]
 * Example 2:
 *
 * Input: nums = [0]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -231 <= nums[i] <= 231 - 1
 *
 *
 * Follow up: Could you minimize the total number of operations done?
 *
 */
public class MoveZeroes {

    // V0
    // IDEA: 2 POINTERS (GEMINI)
    public void moveZeroes(int[] nums) {
        if (nums == null || nums.length <= 1)
            return;

        // 'l' is the position where the next non-zero number should be placed
        int l = 0;


        /** NOTE !!!
         *
         *  BOTH l, r start from idx = 0
         */
        // Iterate through the array with 'r'
        for (int r = 0; r < nums.length; r++) {
            // If we find a non-zero element
            if (nums[r] != 0) {
                // Swap it with the element at position 'l'
                int tmp = nums[r];
                nums[r] = nums[l];
                nums[l] = tmp;

                /** NOTE !!!
                 *
                 *  Move 'l' forward if `we swap`
                 */
                // Move 'l' forward
                l++;
            }
        }
    }

    // V0-1
    // IDEA : 2 POINTER (gpt)
    /**
     * time = O(N)
     * space = O(1)
     */
    public void moveZeroes_0_1(int[] nums) {
        int y = 0;
        for (int x = 0; x < nums.length; x++) {
            if (nums[x] != 0) {
                // Swap nums[x] and nums[y]
                int temp = nums[x];
                nums[x] = nums[y];
                nums[y] = temp;
                y++;
            }
        }
    }


    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/move-zeroes/solutions/72000/1ms-java-solution/
    /**
     * time = O(N)
     * space = O(1)
     */
    public void moveZeroes_1(int[] nums) {

        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                int temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
                j++;
            }
        }
    }

    // V2
    // https://leetcode.com/problems/move-zeroes/solutions/72011/simple-o-n-java-solution-using-insert-index/
    /**
     * time = O(N)
     * space = O(1)
     */
    public void moveZeroes_2(int[] nums) {
        if (nums == null || nums.length == 0) return;

        int insertPos = 0;
        for (int num: nums) {
            if (num != 0) nums[insertPos++] = num;
        }

        while (insertPos < nums.length) {
            nums[insertPos++] = 0;
        }
    }



}
