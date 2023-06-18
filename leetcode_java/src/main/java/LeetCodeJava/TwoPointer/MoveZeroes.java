package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/move-zeroes/

public class MoveZeroes {

    // V0
//    public void moveZeroes(int[] nums) {
//
//        if (nums == null || nums.length == 0 || nums.length == 1) {
//            return;
//        }
//
//        int left = 0;
//        int right = 1;
//
//        while (right < nums.length) {
//
//            int leftVal = nums[left];
//            int rightVal = nums[right];
//
//            if (leftVal == 0) {
//                while (rightVal == 0) {
//                    right += 1;
//                }
//                nums[left] = rightVal;
//                nums[right] = leftVal;
//
//                left += 1;
//                right += 1;
//            }
//
//            left += 1;
//        }
//
//    }

    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/move-zeroes/solutions/72000/1ms-java-solution/
    public void moveZeroes(int[] nums) {

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
