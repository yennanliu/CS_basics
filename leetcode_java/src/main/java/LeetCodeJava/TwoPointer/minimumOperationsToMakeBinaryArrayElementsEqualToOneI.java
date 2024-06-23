package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/description/

import java.util.Arrays;

public class minimumOperationsToMakeBinaryArrayElementsEqualToOneI {

    // V0
    // IDEA : BRUTE FORCE
    // TODO : fix, implement
//    public int minOperations(int[] nums) {
//
//        int res = 0;
//
//        if (nums.length == 3){
//            if (Arrays.stream(nums).sum() == 0){
//                return 1;
//            }
//            if (Arrays.stream(nums).sum() == 3){
//                return 0;
//            }
//            return -1;
//        }
//
//        int idx = 0;
//
//        // get 1st 0 element
//        for (int j = 0; j < nums.length; j++){
//            if (nums[j] == 0){
//                idx = j;
//                break;
//            }
//        }
//
//        System.out.println("idx = " + idx);
//
//        while (idx <= nums.length-3){
//            idx = flip(nums, idx);
//            res += 1;
//            if (idx==-1){
//                break;
//            }
//        }
//
//        System.out.println("---> nums =  " + Arrays.toString(nums) + ", idx = " + idx);
//        //return idx == nums.length ? res : -1;
//        return nums.length == Arrays.stream(nums).sum() ? res : -1;
//    }
//
//    public int flip(int[] input, int idx){
//        System.out.println("input =  " + Arrays.toString(input) + ", idx = " + idx);
//        int zeroIdx = -1;
//        for (int i = idx; i < idx+3; i++){
//            if (input[i] == 0){
//                input[i] = 1;
//            }else{
//                input[i] = 0;
//                if (zeroIdx==-1){
//                    zeroIdx = i;
//                }
//            }
//        }
//        if (zeroIdx == input.length){
//            return input.length;
//        }
//        System.out.println("input =  " + Arrays.toString(input) + ", idx = " + idx + ", zeroIdx = " + zeroIdx);
//        return zeroIdx;
//    }

    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/submissions/1297671097/
    public int minOperations_1(int[] nums) {
        int n = nums.length;
        int i = 0, j = 2, ans = 0;

        while (j < n) {
            if (nums[i] == 0) {
                ans++;
                for (int k = i; k < i + 3; k++) {
                    nums[k] = (nums[k] == 0) ? 1 : 0;
                }
            }
            i++;
            j++;
        }

        for (int m = 0; m < n; m++) {
            if (nums[m] == 0) {
                return -1;
            }
        }

        return ans;
    }

    // V2
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/solutions/5352828/easy-approach/
    public int minOperations_2(int[] nums) {
        int n = nums.length;
        int cnt = 0;

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0 && i + 2 < n) {
                nums[i] = 1;
                if (nums[i + 1] == 1) {
                    nums[i + 1] = 0;
                } else {
                    nums[i + 1] = 1;
                }
                if (nums[i + 2] == 1) {
                    nums[i + 2] = 0;
                } else {
                    nums[i + 2] = 1;
                }
                cnt++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                return -1;
            }
        }

        return cnt;
    }

    // V3
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/solutions/5353478/basic-ternary-operator-easy-beginner/
    public int minOperations_3(int[] nums) {
        int ans = 0;

        // Iterate through the array up to the third-last element
        for (int i = 0; i < nums.length - 2; i++) {
            // If the current element is 0, perform an operation
            if (nums[i] == 0) {
                ans++;
                // Flip the current element and the next two elements
                nums[i] = 1;
                nums[i + 1] = nums[i + 1] == 0 ? 1 : 0;
                nums[i + 2] = nums[i + 2] == 0 ? 1 : 0;
            }
        }

        // Check the last two elements if they are 0, return -1 as they cannot be flipped
        for (int i = nums.length - 2; i < nums.length; i++) {
            if (nums[i] == 0) {
                return -1;
            }
        }

        return ans;
    }
    
}
