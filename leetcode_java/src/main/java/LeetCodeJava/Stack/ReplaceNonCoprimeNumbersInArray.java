package LeetCodeJava.Stack;

// https://leetcode.com/problems/replace-non-coprime-numbers-in-array/

import java.util.ArrayList;
import java.util.List;

/**
 *  2197. Replace Non-Coprime Numbers in Array
 *  Hard
 *
 *  You are given an array of integers nums. Perform the following steps:
 *    - Find any two adjacent numbers in nums that are non-coprime.
 *    - If no such numbers are found, stop the process.
 *    - Otherwise, delete the two numbers and replace them with their LCM
 *      (Least Common Multiple).
 *    - Repeat as long as you keep finding two adjacent non-coprime numbers.
 *
 *  Return the final modified array. It can be shown that replacing adjacent
 *  non-coprime numbers in any arbitrary order will lead to the same result.
 *
 *  Two values x and y are non-coprime if GCD(x, y) > 1.
 *
 *  Example 1:
 *    Input: nums = [6,4,3,2,7,6,2]
 *    Output: [12,7,6]
 *
 *  Example 2:
 *    Input: nums = [2,2,1,1,3,3,3]
 *    Output: [2,1,1,3]
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^5
 *    The test cases are generated such that the values in the final array are
 *    less than or equal to 10^8.
 */
public class ReplaceNonCoprimeNumbersInArray {

    // V0
    // IDEA: STACK - MERGE BACKWARDS UNTIL COPRIME
    //       push each number, then repeatedly look at the top TWO entries: while
    //       they share a factor, pop both and push their LCM. merging can expose
    //       a new non-coprime pair further down the stack, which is exactly what
    //       the inner loop keeps handling.
    //       the statement guarantees the result is order-independent, so this
    //       left-to-right sweep is a valid schedule.
    //       lcm(a, b) = a / gcd(a, b) * b - dividing FIRST keeps the
    //       intermediate value small.
    /**
     * time = O(N * log(max value))   // amortised
     * space = O(N)
     */
    public List<Integer> replaceNonCoprimes(int[] nums) {
        List<Integer> stack = new ArrayList<>();
        for (int x : nums) {
            stack.add(x);
            while (stack.size() > 1) {
                int b = stack.get(stack.size() - 1);
                int a = stack.get(stack.size() - 2);
                int g = gcd(a, b);
                if (g == 1) {
                    break;
                }
                stack.remove(stack.size() - 1);
                stack.remove(stack.size() - 1);
                stack.add(a / g * b);
            }
        }
        return stack;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
