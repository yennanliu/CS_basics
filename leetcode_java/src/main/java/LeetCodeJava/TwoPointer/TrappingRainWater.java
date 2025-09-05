package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/trapping-rain-water/description/
// https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_42_1.png

import java.util.Arrays;
import java.util.Stack;

/**
 * 42. Trapping Rain Water
 * Solved
 * Hard
 * Topics
 * Companies
 * Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * Output: 6
 * Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.
 * Example 2:
 *
 * Input: height = [4,2,0,3,2,5]
 * Output: 9
 *
 *
 * Constraints:
 *
 * n == height.length
 * 1 <= n <= 2 * 104
 * 0 <= height[i] <= 105
 *
 */
public class TrappingRainWater {

    // V0
    // TODO : implement
//    public int trap(int[] height) {
//
//    }

    // V0-0-1
    // IDEA: STACK (fixed by gpt)
    public int trap_0_0_1(int[] height) {
        int n = height.length;
        if (n <= 2)
            return 0;

        int ans = 0;
        Stack<Integer> st = new Stack<>(); // store indices

        for (int i = 0; i < n; i++) {
            // Process while current height is greater than the stack top
            while (!st.isEmpty() && height[i] > height[st.peek()]) {
                int bottom = st.pop(); // the "valley"
                if (st.isEmpty())
                    break; // no left boundary

                int left = st.peek(); // left boundary index
                int width = i - left - 1; // distance between left and right walls
                int h = Math.min(height[left], height[i]) - height[bottom]; // bounded height

                ans += width * h;
            }
            st.push(i);
        }

        return ans;
    }

    // V0-1
    // IDEA: 2 POINTER + left_till_array + max_till_array
    /**
     *  NOTE !!!
     *
     *    amount_of_water =  min(L,R) - h[i]
     *
     *    so ALL we need to do is:
     *
     *     1) get `max_at_left` array that get `max till from LEFT direction`
     *     2) get `max_at_right` array that get `max till from RIGHT direction`
     *     3) get `min_left_right` array that get min of above 2 array per idx
     *     4) get `min_left_right_height_diff` till  `min_left_right - height[i]`
     *          - NOTE that if diff < 0, we use 0 instead
     *
     *    finally, we sum over val in min_left_right_height_diff as result
     */
    // https://youtu.be/ZI2z5pq0TqA?si=rAMqW6nXeqnoxNKo
    public int trap_0_1(int[] height) {
        // edge
        if (height == null || height.length <= 2) {
            return 0;
        }

        int[] max_at_left = new int[height.length];
        int[] max_at_right = new int[height.length];
        int[] min_left_right = new int[height.length];
        int[] min_left_right_height_diff = new int[height.length];

        // compute left max
        /** NOTE !!!
         *
         *  we init leftMaxTillNow as `first element` val
         */
        int leftMaxTillNow = height[0];
        for (int i = 1; i < height.length; i++) {
            leftMaxTillNow = Math.max(leftMaxTillNow, height[i]);
            max_at_left[i] = leftMaxTillNow;
        }

        // compute right max
        /** NOTE !!!
         *
         *  we init rightMaxTillNow as `last element` val
         */
        int rightMaxTillNow = height[height.length - 1];
        for (int i = height.length - 2; i >= 0; i--) {
            rightMaxTillNow = Math.max(rightMaxTillNow, height[i]);
            max_at_right[i] = rightMaxTillNow;
        }

        // compute min of left and right max arrays
        for (int i = 0; i < height.length; i++) {
            min_left_right[i] = Math.min(max_at_left[i], max_at_right[i]);
        }

        // water trapped at each index
        for (int i = 0; i < height.length; i++) {
            int diff = min_left_right[i] - height[i];
            if (diff > 0) {
                min_left_right_height_diff[i] = diff;
            }
        }

        // Optional debug
//        System.out.println("max_at_left = " + Arrays.toString(max_at_left));
//        System.out.println("max_at_right = " + Arrays.toString(max_at_right));
//        System.out.println("min_left_right = " + Arrays.toString(min_left_right));
//        System.out.println("min_left_right_height_diff = " + Arrays.toString(min_left_right_height_diff));

        // sum up all trapped water
        int res = 0;
        for (int val : min_left_right_height_diff) {
            res += val;
        }

        return res;
    }

    // V1-1
    // https://neetcode.io/problems/trapping-rain-water
    // IDEA: BRUTE FORCE
    public int trap_1_1(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int n = height.length;
        int res = 0;

        for (int i = 0; i < n; i++) {
            int leftMax = height[i];
            int rightMax = height[i];

            for (int j = 0; j < i; j++) {
                leftMax = Math.max(leftMax, height[j]);
            }
            for (int j = i + 1; j < n; j++) {
                rightMax = Math.max(rightMax, height[j]);
            }

            res += Math.min(leftMax, rightMax) - height[i];
        }
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/trapping-rain-water
    // IDEA: PREFIX & SUFFIX ARRAY
    public int trap_1_2(int[] height) {
        int n = height.length;
        if (n == 0) {
            return 0;
        }

        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }

        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            res += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return res;
    }

    // V1-3
    // https://neetcode.io/problems/trapping-rain-water
    // IDEA: STACK
    public int trap_1_3(int[] height) {
        if (height.length == 0) {
            return 0;
        }

        Stack<Integer> stack = new Stack<>();
        int res = 0;

        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[i] >= height[stack.peek()]) {
                int mid = height[stack.pop()];
                if (!stack.isEmpty()) {
                    int right = height[i];
                    int left = height[stack.peek()];
                    int h = Math.min(right, left) - mid;
                    int w = i - stack.peek() - 1;
                    res += h * w;
                }
            }
            stack.push(i);
        }
        return res;
    }

    // V1-4
    // https://neetcode.io/problems/trapping-rain-water
    // IDEA: 2 POINTERS
    public int trap_1_4(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }

        int l = 0, r = height.length - 1;
        int leftMax = height[l], rightMax = height[r];
        int res = 0;
        while (l < r) {
            if (leftMax < rightMax) {
                l++;
                leftMax = Math.max(leftMax, height[l]);
                res += leftMax - height[l];
            } else {
                r--;
                rightMax = Math.max(rightMax, height[r]);
                res += rightMax - height[r];
            }
        }
        return res;
    }


    // V2
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0042-trapping-rain-water.java
    public int trap_2(int[] heights) {
        int left[] = new int[heights.length], right[] = new int[heights.length], max =
                heights[0], c = 0;

        for (int i = 0; i < heights.length; i++) {
            left[i] = Math.max(heights[i], max);
            max = left[i];
        }

        max = heights[heights.length - 1];
        for (int i = heights.length - 1; i >= 0; i--) {
            right[i] = Math.max(heights[i], max);
            max = right[i];
        }

        for (int i = 0; i < heights.length; i++) {
            c = c + Math.min(left[i], right[i]) - heights[i];
        }
        return c;
    }

    // V3
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0042-trapping-rain-water.java
    public int trap_3(int[] heights) {
        if (heights.length == 0) return 0;

        int l = 0, r = heights.length - 1;
        int leftMax = heights[l], rightMax = heights[r];
        int res = 0;

        while (l < r) {
            if (leftMax < rightMax) {
                l++;
                leftMax = Math.max(leftMax, heights[l]);
                res += leftMax - heights[l];
            } else {
                r--;
                rightMax = Math.max(rightMax, heights[r]);
                res += rightMax - heights[r];
            }
        }

        return res;
    }


}
