package LeetCodeJava.Stack;

// https://leetcode.com/problems/number-of-visible-people-in-a-queue/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1944. Number of Visible People in a Queue
 *  Hard
 *
 *  There are n people standing in a queue, numbered from 0 to n - 1 in left to
 *  right order. You are given an array heights of distinct integers where
 *  heights[i] represents the height of the ith person.
 *
 *  A person can see another person to their right in the queue if everybody in
 *  between is shorter than both of them. More formally, the ith person can see
 *  the jth person if i < j and
 *  min(heights[i], heights[j]) > max(heights[i+1], ..., heights[j-1]).
 *
 *  Return an array answer of length n where answer[i] is the number of people
 *  the ith person can see to their right in the queue.
 *
 *  Example 1:
 *    Input: heights = [10,6,8,5,11,9]
 *    Output: [3,1,2,1,1,0]
 *
 *  Example 2:
 *    Input: heights = [5,1,2,3,10]
 *    Output: [4,1,1,1,0]
 *
 *  Constraints:
 *    n == heights.length
 *    1 <= n <= 10^5
 *    1 <= heights[i] <= 10^5
 *    All the values of heights are unique.
 */
public class NumberOfVisiblePeopleInAQueue {

    // V0
    // IDEA: MONOTONIC (DECREASING) STACK, SCANNING RIGHT -> LEFT
    //       the people person i can see form a strictly INCREASING chain of
    //       heights to his right, ending at the first person taller than him
    //       (that one blocks everybody behind).
    //       keep a stack decreasing from bottom to top holding the heights to
    //       the right of i. for person i:
    //         - pop every shorter person: each of them is directly visible
    //         - if the stack is still non-empty, the remaining top is the first
    //           TALLER person -> he is visible too, and he blocks the rest
    //         - push heights[i]
    //       every height is pushed and popped once -> linear overall.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] canSeePersonsCount(int[] heights) {
        int n = heights.length;
        int[] res = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = n - 1; i >= 0; i--) {
            int h = heights[i];
            while (!stack.isEmpty() && stack.peek() < h) {
                stack.pop();
                res[i]++;
            }
            if (!stack.isEmpty()) {
                res[i]++;   // the first taller person is visible, then blocks
            }
            stack.push(h);
        }
        return res;
    }
}
