package LeetCodeJava.Sort;

// https://leetcode.com/problems/largest-perimeter-triangle/

import java.util.Arrays;

public class LargestPerimeterTriangle {

    // V0
    // IDEA : SORTING
    public int largestPerimeter(int[] nums) {

        if (nums.length == 0 || nums == null || nums.length < 3){
            return 0;
        }

        if (nums.length == 3){
            if (isTriangle(nums)){
                return getPerimeter(nums);
            }
            return 0;
        }

        // sort
        Arrays.sort(nums);
        System.out.println("nums = " + nums.toString());

        int ans = 0;
        for (int i = 0; i < nums.length-2; i++){
            // https://stackoverflow.com/questions/4439595/how-to-create-a-sub-array-from-another-array-in-java
            int[] tmp = Arrays.copyOfRange( nums, i, i+3);
            System.out.println("tmp[0] "  + tmp[0] + " tmp[1] "  + tmp[1] + " tmp[2] "  + tmp[2]);

            if (isTriangle(tmp)){
                ans = Math.max(ans, getPerimeter(tmp));
            }
        }

        return ans;
    }

    private Boolean isTriangle(int[] nums) {

        int a = nums[0];
        int b = nums[1];
        int c = nums[2];

        if (a + b < c){
            return false;
        }
        if (c - b >= a || c - a >= b){
            return false;
        }
        return true;
    }

    private int getPerimeter(int[] nums){

        int a = nums[0];
        int b = nums[1];
        int c = nums[2];

        return a + b + c;
    }

    // V1
    // https://leetcode.com/problems/largest-perimeter-triangle/editorial/
    // IDEA : SORTING
    public int largestPerimeter_2(int[] A) {
        Arrays.sort(A);
        for (int i = A.length - 3; i >= 0; --i)
            if (A[i] + A[i+1] > A[i+2])
                return A[i] + A[i+1] + A[i+2];
        return 0;
    }

}
