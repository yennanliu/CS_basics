package LeetCodeJava.Sort;

// https://leetcode.com/problems/largest-perimeter-triangle/

import java.util.Arrays;

/**
 * 976. Largest Perimeter Triangle
 * Easy
 *
 * Given an integer array nums, return the largest perimeter of a triangle with a non-zero area, formed from three of these lengths. If it is impossible to form any triangle of a non-zero area, return 0.
 *
 * Example 1:
 *
 * Input: nums = [2,1,2]
 * Output: 5
 * Explanation: You can form a triangle with three side lengths: 1, 2, and 2.
 *
 * Example 2:
 *
 * Input: nums = [1,2,1,10]
 * Output: 0
 * Explanation:
 * You cannot use the side lengths 1, 1, and 2 to form a triangle.
 * You cannot use the side lengths 1, 1, and 10 to form a triangle.
 * You cannot use the side lengths 1, 2, and 10 to form a triangle.
 * As we cannot use any three side lengths to form a triangle of non-zero area, we return 0.
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^4
 * 1 <= nums[i] <= 10^6
 *
 */
public class LargestPerimeterTriangle {

    // V0
    // IDEA : SORTING
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
    public int largestPerimeter_2(int[] A) {
        Arrays.sort(A);
        for (int i = A.length - 3; i >= 0; --i)
            if (A[i] + A[i+1] > A[i+2])
                return A[i] + A[i+1] + A[i+2];
        return 0;
    }

}
