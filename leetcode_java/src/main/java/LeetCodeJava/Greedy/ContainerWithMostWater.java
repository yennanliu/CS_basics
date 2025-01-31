package LeetCodeJava.Greedy;

// https://leetcode.com/problems/container-with-most-water/
/**
 *  11. Container With Most Water
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).
 *
 * Find two lines that together with the x-axis form a container, such that the container contains the most water.
 *
 * Return the maximum amount of water a container can store.
 *
 * Notice that you may not slant the container.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: height = [1,8,6,2,5,4,8,3,7]
 * Output: 49
 * Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case, the max area of water (blue section) the container can contain is 49.
 * Example 2:
 *
 * Input: height = [1,1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * n == height.length
 * 2 <= n <= 105
 * 0 <= height[i] <= 104
 *
 */
public class ContainerWithMostWater {

    // V0
    // IDEA : 2 POINTERS
    /**
     * IDEA: 2 POINTERS
     *
     * Step 0) init left, right pointer
     * Step 1) get cur area, then move the `smaller val` pointer
     * Step 2) repeat above steps, and maintain the `biggest` area
     * Step 3) return the biggest area as result
     *
     */
    public int maxArea(int[] height) {
        // edge
        if (height == null || height.length == 0) {
            return 0;
        }
        if (height.length == 1) {
            return Math.abs(height[1] - height[0]);
        }

        // 2 pointers
        int res = 0;
        int l = 0;
        int r = height.length - 1;
        while (r > l) {
            int h = Math.min(height[l], height[r]);
            int w = r - l;
            res = Math.max(res, h * w);
            if (height[l] < height[r]) {
                l += 1;
            } else {
                r -= 1;
            }
        }

        return res;
    }

    // V0-1
    // IDEA : 2 POINTERS
    public int maxArea_0_1(int[] height) {

        if (height.length == 0 || height.equals(null)){
            return 0;
        }

        int ans = 0;
        int left = 0;
        // NOTE : right as height.length - 1
        int right = height.length - 1;

        // either ">=" or ">" is OK for this problem, but for logic alignment, we use ">=" here
        while (right >= left){
            int leftVal = height[left];
            int rightVal = height[right];
            // NOTE !!! right - left, we get distance between left, right pointer
            int amount = (right - left) * Math.min(leftVal, rightVal);
            ans = Math.max(amount, ans);
            if (rightVal > leftVal){
                left += 1;
            }else{
                right -= 1;
            }
        }
        return ans;
    }

    // V0-2
    // IDEA : BRUTE FORCE
    public int maxArea_0_2(int[] height) {

        if (height.length == 0 || height.equals(null)){
            return 0;
        }

        int ans = 0;
        for (int i = 0; i < height.length-1; i++){
            for (int j = 1; j < height.length; j++){
                int amount = (j - i) * Math.min(height[i], height[j]);
                ans = Math.max(amount, ans);
            }
        }

        return ans;
    }

    // V1
    // IDEA : 2 POINTERS
    public int maxArea_1(int[] height) {

        if (height.length == 0 || height.equals(null)){
            return 0;
        }

        int ans = 0;
        int left = 0;
        int right = height.length - 1;
        while (right >= left){
            int leftVal = height[left];
            int rightVal = height[right];
            int amount = (right - left) * Math.min(leftVal, rightVal);
            ans = Math.max(amount, ans);
            if (rightVal > leftVal){
                left += 1;
            }else{
                right -= 1;
            }
        }
        return ans;
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/container-with-most-water/editorial/
    public int maxArea_2(int[] height) {
        int maxarea = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            int width = right - left;
            maxarea = Math.max(maxarea, Math.min(height[left], height[right]) * width);
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxarea;
    }

}
