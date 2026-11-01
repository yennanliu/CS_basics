package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/subsequence-sum-after-capping-elements/description/

import java.util.*;

/**
 * 3685. Subsequence Sum After Capping Elements
 * Medium
 *
 * You are given an integer array nums of size n and a positive integer k.
 *
 * An array capped by value x is obtained by replacing every element nums[i]
 * with min(nums[i], x).
 *
 * For each integer x from 1 to n, determine whether it is possible to choose
 * a subsequence from the array capped by x such that the sum of the chosen
 * elements is exactly k.
 *
 * Return a 0-indexed boolean array answer of size n, where answer[i] is true
 * if it is possible when using x = i + 1, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,3,2,4], k = 5
 * Output: [false,false,true,true]
 * Explanation:
 * For x = 1, the capped array is [1, 1, 1, 1]. Possible sums are 1, 2, 3, 4,
 * so it is impossible to form a sum of 5.
 * For x = 2, the capped array is [2, 2, 2, 2]. Possible sums are 2, 4, 6, 8,
 * so it is impossible to form a sum of 5.
 * For x = 3, the capped array is [3, 3, 2, 3]. A subsequence [2, 3] sums to 5,
 * so it is possible.
 * For x = 4, the capped array is [4, 3, 2, 4]. A subsequence [3, 2] sums to 5,
 * so it is possible.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5], k = 3
 * Output: [true,true,true,true,true]
 * Explanation:
 * For every value of x, it is always possible to select a subsequence from the
 * capped array that sums exactly to 3.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 4000
 * 1 <= nums[i] <= n
 * 1 <= k <= 4000
 *
 */
public class SubsequenceSumAfterCappingElements {

    // V0
    // IDEA: SWEEP THE CAP UPWARD, ONE INCREMENTAL SUBSET-SUM DP FOR THE UNCAPPED ELEMENTS
    //   with cap x, elements <= x are unchanged and every element > x becomes x.
    //   The capped ones are interchangeable: taking t of them adds exactly t * x.
    //   reachable[s] = some subset of the elements <= x sums to s (0/1 knapsack, s <= k)
    //   x is answerable iff reachable[k - t * x] for some 0 <= t <= (count of elements > x).
    //   As x grows the uncapped pool only grows, so one DP serves every x.
    /**
     * time = O(N * K + K log N)
     * space = O(N + K)
     */
    public boolean[] subsequenceSumAfterCapping(int[] nums, int k) {
        int n = nums.length;
        // countOfValue[v] = how many elements equal v (values are in 1..n)
        int[] countOfValue = new int[n + 1];
        for (int num : nums) {
            countOfValue[num]++;
        }

        boolean[] reachable = new boolean[k + 1];
        reachable[0] = true;
        // elements still bigger than the cap
        int capped = n;
        boolean[] answer = new boolean[n];

        for (int cap = 1; cap <= n; cap++) {
            // elements equal to cap join the uncapped pool: 0/1 knapsack, downward
            for (int copy = 0; copy < countOfValue[cap]; copy++) {
                for (int sum = k; sum >= cap; sum--) {
                    if (reachable[sum - cap]) {
                        reachable[sum] = true;
                    }
                }
            }
            capped -= countOfValue[cap];

            // try using t capped elements, each worth exactly cap
            boolean ok = false;
            for (int t = 0; t <= capped && (long) t * cap <= k; t++) {
                if (reachable[k - t * cap]) {
                    ok = true;
                    break;
                }
            }
            answer[cap - 1] = ok;
        }
        return answer;
    }
}
