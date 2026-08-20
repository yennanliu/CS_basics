package LeetCodeJava.Sort;

// https://leetcode.com/problems/minimum-swaps-to-sort-by-digit-sum/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  3551. Minimum Swaps to Sort by Digit Sum
 *  Medium
 *
 *  You are given an array nums of distinct positive integers. You need to sort the
 *  array in increasing order based on the sum of the digits of each number. If two
 *  numbers have the same digit sum, the smaller number appears first in the sorted
 *  order.
 *
 *  Return the minimum number of swaps required to rearrange nums into this sorted
 *  order. A swap is defined as exchanging the values at two distinct positions.
 *
 *  Example 1:
 *    Input: nums = [37,100]
 *    Output: 1
 *    Explanation: digit sums [10, 1] -> sorted order is [100, 37], so 1 swap.
 *
 *  Example 2:
 *    Input: nums = [22,14,33,7]
 *    Output: 0
 *    Explanation: digit sums [4,5,6,7], already sorted.
 *
 *  Example 3:
 *    Input: nums = [18,43,34,16]
 *    Output: 2
 *    Explanation: digit sums [9,7,7,7] -> sorted order is [16,34,43,18], 2 swaps.
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^9
 *    nums consists of distinct positive integers.
 */
public class MinimumSwapsToSortByDigitSum {

    // V0
    // IDEA: PERMUTATION CYCLE DECOMPOSITION
    //       the target arrangement is a fixed permutation of the current one: the
    //       values are distinct, so the key (digitSum, value) is a total order and
    //       every element has a unique target position.
    //
    //       a permutation that decomposes into c cycles needs exactly n - c swaps:
    //       a cycle of length L costs L - 1 swaps, and no scheme does better since
    //       one swap merges at most two cycles into one.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minSwaps(int[] nums) {
        int n = nums.length;

        final int[] arr = nums;
        final int[] dsum = new int[n];
        for (int i = 0; i < n; i++) {
            dsum[i] = digitSum(nums[i]);
        }

        // order[p] = index in nums of the element that must end up at position p
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                if (dsum[a] != dsum[b]) {
                    return Integer.compare(dsum[a], dsum[b]);
                }
                return Integer.compare(arr[a], arr[b]);
            }
        });

        boolean[] seen = new boolean[n];
        int cycles = 0;
        for (int start = 0; start < n; start++) {
            if (seen[start]) {
                continue;
            }
            cycles++;
            int j = start;
            while (!seen[j]) {
                seen[j] = true;
                j = order[j];
            }
        }
        return n - cycles;
    }

    private int digitSum(int x) {
        int s = 0;
        while (x > 0) {
            s += x % 10;
            x /= 10;
        }
        return s;
    }
}
