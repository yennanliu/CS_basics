package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/longest-subarray-with-restricted-pair-sums/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 4067. Longest Subarray With Restricted Pair Sums
 * Medium
 *
 * You are given an integer array nums.
 *
 * A subarray nums[l..r] is valid if there are no three distinct indices i, j, and k
 * such that l <= i, j, k <= r and:
 *
 *     nums[i] + nums[j] == nums[k]
 *
 * Return the maximum length of a valid subarray of nums.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,5,3,2,1]
 * Output: 3
 * Explanation:
 * Consider the subarray [3, 5, 3]. The pairs of elements at distinct indices have the
 * following sums:
 *     3 + 5 = 8
 *     3 + 3 = 6, using the two different occurrences of 3
 *     5 + 3 = 8
 * None of these sums is an element at the remaining index, so the subarray is valid.
 * Every subarray of length 4 contains 2, 3, and 5 at distinct indices, where
 * 2 + 3 = 5. Therefore, no longer valid subarray exists, and the answer is 3.
 *
 * Example 2:
 *
 * Input: nums = [3,4,5,6]
 * Output: 4
 * Explanation:
 * The sums obtained from every pair of elements at distinct indices are 7, 8, 9, 9,
 * 10, and 11. None of these values appears at the remaining index, so the entire
 * array is valid.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 500
 *
 */
public class LongestSubarrayWithRestrictedPairSums {

    // V0
    // IDEA: SLIDING WINDOW OVER VALUE COUNTS, CHECK ONLY THE NEWCOMER (the one to use in an interview)
    // validity is monotone: dropping elements never creates a triple, so two pointers work.
    // the window before nums[right] joins is already valid, so any new triple must use nums[right]:
    //   newcomer as the sum:    a + b == val with a, b in the window (a == b needs two copies)
    //   newcomer as an addend:  a in the window and a + val in the window
    // values are >= 1, so a sum is strictly bigger than its addends -> the three indices are distinct for free.
    /**
     * time = O(N * V)   (V = max(nums) <= 500)
     * space = O(V)
     */
    public int maxSubarray(int[] nums) {
        int n = nums.length;
        // edge case: fewer than 3 elements cannot form a triple
        if (n <= 2) {
            return n;
        }

        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        // valCount[v] = how many elements of the current window equal v
        int[] valCount = new int[maxVal + 1];

        int maxLen = 0;
        int left = 0;
        for (int right = 0; right < n; right++) {
            int val = nums[right];

            // shrink from the left until nums[right] can join without forming a triple
            while (joinsTriple(valCount, val, maxVal)) {
                valCount[nums[left]] -= 1;
                left += 1;
            }

            valCount[val] += 1;
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    /** True if val would form nums[i] + nums[j] == nums[k] with the window counted in valCount. */
    private boolean joinsTriple(int[] valCount, int val, int maxVal) {
        // case 1: val is the sum -> a + b == val with a <= b
        for (int a = 1; a <= val / 2; a++) {
            int b = val - a;
            if (a == b) {
                // the same value twice needs two different indices
                if (valCount[a] >= 2) {
                    return true;
                }
            } else if (valCount[a] >= 1 && valCount[b] >= 1) {
                return true;
            }
        }

        // case 2: val is an addend -> a and a + val are both in the window
        for (int a = 1; a + val <= maxVal; a++) {
            if (valCount[a] >= 1 && valCount[a + val] >= 1) {
                return true;
            }
        }
        return false;
    }

    // V1
    // IDEA: SLIDING WINDOW + PAIR-SUM HASHMAP, UPDATED INCREMENTALLY
    // keep over the window:
    //   pairSumCount[s] : number of index pairs (i < j) in the window whose sum is s
    //   valCount[v]     : number of window elements equal to v
    //   conflicts       : number of (pair, k) matches nums[i] + nums[j] == nums[k]
    // the window is valid exactly when conflicts == 0. adding / removing one element only changes
    // the pairs it belongs to, so each step costs O(window).
    /**
     * time = O(N^2)
     * space = O(V)
     */
    public int maxSubarray_1(int[] nums) {
        int n = nums.length;
        if (n <= 2) {
            return n;
        }

        Map<Integer, Integer> pairSumCount = new HashMap<>();
        Map<Integer, Integer> valCount = new HashMap<>();
        int conflicts = 0;

        int maxLen = 0;
        int left = 0;
        for (int right = 0; right < n; right++) {
            int val = nums[right];

            // 1) val as the sum: every pair already summing to it is a new conflict
            conflicts += pairSumCount.getOrDefault(val, 0);
            // 2) val as an addend: a new pair with every window element, matched by elements equal to the sum
            for (int i = left; i < right; i++) {
                int pairSum = nums[i] + val;
                pairSumCount.put(pairSum, pairSumCount.getOrDefault(pairSum, 0) + 1);
                conflicts += valCount.getOrDefault(pairSum, 0);
            }
            valCount.put(val, valCount.getOrDefault(val, 0) + 1);

            // shrink from the left until no triple is left in the window
            while (conflicts > 0) {
                int out = nums[left];
                valCount.put(out, valCount.get(out) - 1);
                // mirror of the two steps above, for the element leaving
                conflicts -= pairSumCount.getOrDefault(out, 0);
                for (int i = left + 1; i <= right; i++) {
                    int pairSum = nums[i] + out;
                    pairSumCount.put(pairSum, pairSumCount.get(pairSum) - 1);
                    conflicts -= valCount.getOrDefault(pairSum, 0);
                }
                left += 1;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}
