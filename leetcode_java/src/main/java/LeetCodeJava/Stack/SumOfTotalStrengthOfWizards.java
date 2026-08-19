package LeetCodeJava.Stack;

// https://leetcode.com/problems/sum-of-total-strength-of-wizards/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 *  2281. Sum of Total Strength of Wizards
 *  Hard
 *
 *  As the ruler of a kingdom, you have an army of wizards at your command.
 *
 *  You are given a 0-indexed integer array strength, where strength[i] denotes
 *  the strength of the ith wizard. For a contiguous group of wizards (i.e. the
 *  wizards' strengths form a subarray of strength), the total strength is
 *  defined as the product of:
 *    - the strength of the weakest wizard in the group, and
 *    - the total of all the individual strengths of the wizards in the group.
 *
 *  Return the sum of the total strengths of all contiguous groups of wizards.
 *  Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: strength = [1,3,1,2]
 *    Output: 44
 *
 *  Example 2:
 *    Input: strength = [5,4,6]
 *    Output: 213
 *
 *  Constraints:
 *    1 <= strength.length <= 10^5
 *    1 <= strength[i] <= 10^9
 */
public class SumOfTotalStrengthOfWizards {

    // V0
    // IDEA: MONOTONIC STACK FOR THE "I AM THE MINIMUM" RANGE + DOUBLE PREFIX SUMS
    //       attribute every subarray to its MINIMUM element. for index i let
    //         l = last index on the left with a STRICTLY smaller value (else -1)
    //         r = first index on the right with a value <= strength[i] (else n)
    //       the strict/non-strict split makes ties attribute to exactly one index.
    //       i is then the minimum of every subarray [a, b] with l < a <= i <= b < r.
    //       its contribution is strength[i] * (SUM OF THOSE SUBARRAY SUMS), and
    //       with P[k] = sum of the first k elements, PP[k] = P[0] + ... + P[k-1]:
    //         sum of sums = (i - l) * (PP[r+1] - PP[i+1])
    //                     - (r - i) * (PP[i+1] - PP[l+1])
    //       the first term collects the right endpoints (each reachable by i - l
    //       choices of left endpoint), the second subtracts the left endpoints.
    //       NOTE: normalise the subtraction back to [0, MOD) - java's % can be
    //             negative.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int totalStrength(int[] strength) {
        final long MOD = 1_000_000_007L;
        int n = strength.length;

        // previous strictly-smaller
        int[] left = new int[n];
        Arrays.fill(left, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && strength[stack.peek()] >= strength[i]) {
                stack.pop();
            }
            left[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        // next smaller-or-equal
        int[] right = new int[n];
        Arrays.fill(right, n);
        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && strength[stack.peek()] > strength[i]) {
                stack.pop();
            }
            right[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        // P[k] = sum of the first k elements ; PP[k] = P[0] + ... + P[k-1]
        long[] P = new long[n + 1];
        for (int i = 0; i < n; i++) {
            P[i + 1] = (P[i] + strength[i]) % MOD;
        }
        long[] PP = new long[n + 2];
        for (int k = 0; k <= n; k++) {
            PP[k + 1] = (PP[k] + P[k]) % MOD;
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            int l = left[i];
            int r = right[i];
            long rightPart = ((PP[r + 1] - PP[i + 1]) % MOD + MOD) % MOD;
            long leftPart = ((PP[i + 1] - PP[l + 1]) % MOD + MOD) % MOD;
            long total = ((i - l) * rightPart % MOD - (r - i) * leftPart % MOD) % MOD;
            total = (total + MOD) % MOD;
            res = (res + strength[i] * total) % MOD;
        }
        return (int) res;
    }
}
