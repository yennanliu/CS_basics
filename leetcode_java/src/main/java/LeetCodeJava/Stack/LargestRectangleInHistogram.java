package LeetCodeJava.Stack;

// https://leetcode.com/problems/largest-rectangle-in-histogram/
/**
 * 84. Largest Rectangle in Histogram
 * Solved
 * Hard
 * Topics
 * Companies
 * Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: heights = [2,1,5,6,2,3]
 * Output: 10
 * Explanation: The above is a histogram where width of each bar is 1.
 * The largest rectangle is shown in the red area, which has an area = 10 units.
 * Example 2:
 *
 *
 * Input: heights = [2,4]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= heights.length <= 105
 * 0 <= heights[i] <= 104
 *
 *
 */
import java.util.ArrayDeque;
import java.util.Deque;

public class LargestRectangleInHistogram {

    // V0
    // IDEA : STACK
    // https://www.bilibili.com/list/525438321?sort_field=pubtime&spm_id_from=333.999.0.0&oid=992760704&bvid=BV1Ns4y1o7uB
//    public int largestRectangleArea(int[] heights) {
//
//        return 0;
//    }

    // V0-1
    // IDEA : STACK
    // https://leetcode.com/problems/largest-rectangle-in-histogram/editorial/
    public int largestRectangleArea_1(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1);
        int length = heights.length;
        int maxArea = 0;
        for (int i = 0; i < length; i++) {
            while ((stack.peek() != -1)
                    && (heights[stack.peek()] >= heights[i])) {
                int currentHeight = heights[stack.pop()];
                int currentWidth = i - stack.peek() - 1;
                maxArea = Math.max(maxArea, currentHeight * currentWidth);
            }
            stack.push(i);
        }
        while (stack.peek() != -1) {
            int currentHeight = heights[stack.pop()];
            int currentWidth = length - stack.peek() - 1;
            maxArea = Math.max(maxArea, currentHeight * currentWidth);
        }
        return maxArea;
    }

    // V2
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/largest-rectangle-in-histogram/editorial/
    public int largestRectangleArea_2(int[] heights) {
        int maxarea = 0;
        for (int i = 0; i < heights.length; i++) {
            for (int j = i; j < heights.length; j++) {
                int minheight = Integer.MAX_VALUE;
                for (int k = i; k <= j; k++)
                    minheight = Math.min(minheight, heights[k]);
                maxarea = Math.max(maxarea, minheight * (j - i + 1));
            }
        }
        return maxarea;
    }

    // V3
    // IDEA : better BRUTE FORCE
    // https://leetcode.com/problems/largest-rectangle-in-histogram/editorial/
    public int largestRectangleArea_3(int[] heights) {
        int maxArea = 0;
        int length = heights.length;
        for (int i = 0; i < length; i++) {
            int minHeight = Integer.MAX_VALUE;
            for (int j = i; j < length; j++) {
                minHeight = Math.min(minHeight, heights[j]);
                maxArea = Math.max(maxArea, minHeight * (j - i + 1));
            }
        }
        return maxArea;
    }

    // V4
    // IDEA : Divide and Conquer Approach
    // https://leetcode.com/problems/largest-rectangle-in-histogram/editorial/
    public int calculateArea(int[] heights, int start, int end) {
        if (start > end)
            return 0;
        int minindex = start;
        for (int i = start; i <= end; i++)
            if (heights[minindex] > heights[i])
                minindex = i;
        return Math.max(heights[minindex] * (end - start + 1),
                Math.max(calculateArea(heights, start, minindex - 1),
                        calculateArea(heights, minindex + 1, end))
        );
    }

    public int largestRectangleArea_4(int[] heights) {
        return calculateArea(heights, 0, heights.length - 1);
    }

    // V5
    // IDEA : Better Divide and Conquer
    // https://leetcode.com/problems/largest-rectangle-in-histogram/editorial/
    // https://leetcode.com/problems/largest-rectangle-in-histogram/solutions/28941/segment-tree-solution-just-another-idea-onlogn-solution/


    // V6
    // IDEA : STACK
    // https://leetcode.com/problems/largest-rectangle-in-histogram/editorial/
    public int largestRectangleArea_6(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1);
        int length = heights.length;
        int maxArea = 0;
        for (int i = 0; i < length; i++) {
            while ((stack.peek() != -1)
                    && (heights[stack.peek()] >= heights[i])) {
                int currentHeight = heights[stack.pop()];
                int currentWidth = i - stack.peek() - 1;
                maxArea = Math.max(maxArea, currentHeight * currentWidth);
            }
            stack.push(i);
        }
        while (stack.peek() != -1) {
            int currentHeight = heights[stack.pop()];
            int currentWidth = length - stack.peek() - 1;
            maxArea = Math.max(maxArea, currentHeight * currentWidth);
        }
        return maxArea;
    }

}
