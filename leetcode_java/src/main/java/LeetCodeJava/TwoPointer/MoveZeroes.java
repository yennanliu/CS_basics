package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/move-zeroes/

public class MoveZeroes {

    // V0
    // IDEA : 2 POINTER (gpt)
    public void moveZeroes(int[] nums) {
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
