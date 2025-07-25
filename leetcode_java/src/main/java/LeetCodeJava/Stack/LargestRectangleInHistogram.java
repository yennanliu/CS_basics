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
import java.util.Stack;

public class LargestRectangleInHistogram {

    // V0
    // IDEA : STACK
    // https://www.bilibili.com/list/525438321?sort_field=pubtime&spm_id_from=333.999.0.0&oid=992760704&bvid=BV1Ns4y1o7uB
//    public int largestRectangleArea(int[] heights) {
//
//        return 0;
//    }

    // V0-0-1
    // IDEA: BRUTE FORCE (TLE)
    public int largestRectangleArea_0_0_1(int[] heights) {
        // edge
        if (heights == null || heights.length == 0) {
            return 0;
        }
        if (heights.length == 1) {
            return heights[0];
        }
        int max_area = 0;

        for (int i = 0; i < heights.length; i++) {
            int min_height = heights[i];
            for (int j = i; j < heights.length; j++) {

                min_height = Math.min(min_height, heights[j]);

                max_area = Math.max(max_area, Math.max(
                        heights[j], (j - i + 1) * min_height));
            }
        }

        return max_area;
    }

    // V0-1
    // IDEA : STACK (monotonic stack)
    // https://leetcode.com/problems/largest-rectangle-in-histogram/editorial/
    /**
     *
     * 1) Example Walkthrough:
     *  Let’s say heights = [2, 1, 5, 6, 2, 3]:
     *
     * - Start with an empty stack and iterate over the bars.
     * - At each step, check if the current bar is shorter than the one at the top of the stack. If so, pop from the stack, calculate the area of the rectangle that can be formed, and update maxArea.
     * - Once the entire array is processed, clean up the stack by popping any remaining bars and calculating the area they form.
     *
     *
     * 2) Time Complexity:
     * Time Complexity: O(n), where n is the number of bars in the histogram.
     *
     * Each index is pushed and popped from the stack once, so the overall time complexity is linear.
     *
     * 3) Space Complexity: O(n), where n is the number of bars in the histogram.
     *
     * The stack stores at most n indices during the iteration.
     *
     *
     * 4) Key Concepts:
     * Monotonic Stack:
     *    - The stack is maintained in a way that the heights of
     *      the bars indexed in the stack are in increasing order.
     *
     *
     * Area Calculation:
     *    - Every time we pop an element from the stack,
     *     we calculate the area of the rectangle that the popped bar forms.
     */
    public int largestRectangleArea_1(int[] heights) {

        /**
         *
         *  NOTE:
         *
         *
         *  - We initialize a stack to keep track of the
         *    `indices` of the bars in the histogram.
         *
         * - The stack will help us maintain a `monotonic increasing order`of bar heights.
         *   This means, from the bottom to the top of the stack,
         *   the bars will have heights in increasing order.
         *
         * - We push -1 to the stack initially, which acts as a sentinel value
         *   (a marker for when the stack becomes empty or at the base of the histogram).
         */
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1);

        /**
         *  - We iterate over the array heights using the index i.
         *  - maxArea keeps track of the maximum area encountered so far.
         */
        int length = heights.length;
        int maxArea = 0;

        for (int i = 0; i < length; i++) {

            /**
             *  NOTE:
             *
             *  - For each i, we check the height of the bar at the index i (heights[i])
             *    against the height of the bar at the index stored at the top of the stack
             *    (heights[stack.peek()]).
             *
             * - If the current bar is shorter than the bar at the top of the stack,
             *    we start popping from the stack.
             *     - Each time we pop, the bar at the top of the stack represents the
             *       height of a potential rectangle.
             *     - The width of the rectangle is the difference between the current
             *        index i and the index of the new top of the stack (after popping), minus 1
             *        (since the stack contains the index of the last bar that was taller).
             *
             * - After calculating the area of the rectangle formed by the popped bar,
             *   we update maxArea to store the maximum area found.
             *
             *
             * - Once we've processed all the bars that are taller than the current one,
             *   we push the current index i onto the stack.
             *
             */
            while ((stack.peek() != -1)
                    && (heights[stack.peek()] >= heights[i])) {
                int currentHeight = heights[stack.pop()];
                int currentWidth = i - stack.peek() - 1;
                maxArea = Math.max(maxArea, currentHeight * currentWidth);
            }
            stack.push(i);
        }


        /**
         *  - After iterating through the entire array,
         *    there may still be some indices left in the stack.
         *
         * - These indices represent bars that could form rectangles extending
         *   to the end of the histogram.
         *
         * - We continue popping from the stack and calculating the area as we did earlier,
         *   but now the width extends from the popped index
         *   to the very end of the histogram (index length - 1).
         *
         */
        while (stack.peek() != -1) {
            int currentHeight = heights[stack.pop()];
            int currentWidth = length - stack.peek() - 1;
            maxArea = Math.max(maxArea, currentHeight * currentWidth);
        }


        return maxArea;
    }

    // V0-2
    // IDEA: MONO STACK (GPT)
    /**
     *  IDEA:
     *
     *  🔑 Takeaways:
     * 	•	Stack stores indices, not heights — needed to compute width.
     * 	•	Height comes from the popped index.
     * 	•	Width is computed from current index minus previous index.
     * 	•	The dummy 0 is a useful trick to flush the stack at the end.
     */
    /**
     *
     🧠 Why This Works:
     •	As long as the current bar is taller or equal, we push its index — since it may be part of a future larger rectangle.
     •	When the current bar is shorter, we know:
     •	All previous taller bars can’t extend past this point,
     •	So we pop them, calculate area, and move on.
     *
     */
    public int largestRectangleArea_0_2(int[] heights) {
        int n = heights.length;

        /**
         *  NOTE !!!!
         *
         *  - A monotonic increasing stack to store the `indices` of bars.
         * 	- We’ll use this to track the left boundaries of rectangles.
         *
         *
         * 	NOTE !!!
         *
         * 	 the stack stores the `index` of bars (small -> big)
         *
         * 	 -> The stack stores indices of elements in non-decreasing height order
         * 	 (i.e., from shortest to tallest) as we move left to right.
         */
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;

        // Append a 0 to flush out remaining stack at end
        /**
         *
         * 	- We’re looping from i = 0 to i = n inclusive.
         *
         * 	- At i = n, we use a dummy bar of height 0 to
         * 	  flush the stack — this ensures we process
         * 	  all remaining bars.
         */
        for (int i = 0; i <= n; i++) {

            /**
             * NOTE !!!!
             *
             * 	- If we’re at i == n, we use height 0 (virtual bar).
             * 	   - Otherwise, we get the real bar height.
             *
             * 	- This trick helps to pop all remaining bars in the
             *   	stack by triggering the while condition.
             *
             */
            int currHeight = (i == n) ? 0 : heights[i];

            /**
             * 	- While the current height is `less` than the height at
             * 	  the `top` of the stack:

             * 	   - We’ve found the right boundary for a rectangle with
             * 	     `height heights[stack.peek()]`.
             *
             * 	   - Now compute area using that height.
             *
             */
            while (!stack.isEmpty() && currHeight < heights[stack.peek()]) {

                /**
                 *  NOTE !!!
                 *
                 * 	- Pop the top index from the stack, and get its height.
                 * 	- This is the `height` of the rectangle we’re evaluating.
                 */
                int height = heights[stack.pop()]; // NOTE !!! we pop the element from stack

                /**
                 *  NOTE !!!
                 *
                 *  - Compute the width of the rectangle:
                 *
                 * 	   - If the stack is empty after popping,
                 * 	       it means the popped bar is the smallest so far,
                 * 	       so its width spans from index 0 to i - 1 → width = i.
                 *
                 * 	   - Otherwise, the left boundary is the new stack.peek(),
                 * 	      so width is from stack.peek() + 1 to i - 1.
                 *
                 */
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;

                // Compute area and update maxArea if it’s larger.
                maxArea = Math.max(maxArea, height * width);
            }

            /**
             *
             * 	- Push the current index onto the stack.
             * 	- This keeps the indices of bars in `non-decreasing` height order.
             */
            stack.push(i);
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
