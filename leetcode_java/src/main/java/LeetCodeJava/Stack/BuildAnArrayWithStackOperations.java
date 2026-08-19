package LeetCodeJava.Stack;

// https://leetcode.com/problems/build-an-array-with-stack-operations/

import java.util.ArrayList;
import java.util.List;

/**
 *  1441. Build an Array With Stack Operations
 *  Medium
 *
 *  You are given an integer array target and an integer n.
 *
 *  You have an empty stack with the two following operations:
 *
 *   - "Push": pushes an integer to the top of the stack.
 *   - "Pop": removes the integer on the top of the stack.
 *
 *  You also have a stream of the integers in the range [1, n].
 *
 *  Use the two stack operations to make the numbers in the stack (from the
 *  bottom to the top) equal to target. You should follow the following rules:
 *
 *   - If the stream of the integers is not empty, pick the next integer from
 *     the stream and push it to the top of the stack.
 *   - If the stack is not empty, pop the integer at the top of the stack.
 *   - If, at any moment, the elements in the stack (from the bottom to the top)
 *     are equal to target, do not read new integers from the stream and do not
 *     do more operations on the stack.
 *
 *  Return the stack operations needed to build target following the mentioned
 *  rules. If there are multiple valid answers, return any of them.
 *
 *  Example 1:
 *    Input: target = [1,3], n = 3
 *    Output: ["Push","Push","Pop","Push"]
 *
 *  Example 2:
 *    Input: target = [1,2,3], n = 3
 *    Output: ["Push","Push","Push"]
 *
 *  Constraints:
 *    1 <= target.length <= 100
 *    1 <= n <= 100
 *    1 <= target[i] <= n
 *    target is strictly increasing.
 */
public class BuildAnArrayWithStackOperations {

    // V0
    // IDEA: SIMULATION
    //       the stream is 1, 2, 3, ... so we must READ every number up to
    //       target[last], but KEEP only those in target.
    //         - a number in target        -> "Push"
    //         - a number skipped by target -> "Push" then "Pop"
    //       stop right after the last target value (never read beyond it).
    /**
     * time = O(n)
     * space = O(1)   // ignoring the output list
     */
    public List<String> buildArray(int[] target, int n) {
        List<String> res = new ArrayList<>();
        int cur = 1; // next number in the stream

        for (int x : target) {
            // NOTE !!! burn through the stream numbers that target skipped
            while (cur < x) {
                res.add("Push");
                res.add("Pop");
                cur++;
            }
            res.add("Push");
            cur++;
        }
        return res;
    }
}
