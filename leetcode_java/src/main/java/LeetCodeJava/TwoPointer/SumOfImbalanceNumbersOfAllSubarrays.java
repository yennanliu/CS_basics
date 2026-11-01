package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/sum-of-imbalance-numbers-of-all-subarrays/description/

/**
 * 2763. Sum of Imbalance Numbers of All Subarrays
 * Hard
 *
 * The imbalance number of a 0-indexed integer array arr of length n is defined as the number of
 * indices in sarr = sorted(arr) such that:
 *
 * 0 <= i < n - 1, and
 * sarr[i+1] - sarr[i] > 1
 *
 * Here, sorted(arr) is the function that returns the sorted version of arr.
 *
 * Given a 0-indexed integer array nums, return the sum of imbalance numbers of all its subarrays.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,1,4]
 * Output: 3
 *
 * Example 2:
 *
 * Input: nums = [1,3,3,3,5]
 * Output: 8
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= nums.length
 *
 */
public class SumOfImbalanceNumbersOfAllSubarrays {

    // V0
    // IDEA: FIX THE LEFT END, GROW THE RIGHT END, COUNT IMBALANCE OVER THE DISTINCT-VALUE SET
    //  with S the distinct values (d = |S|), sorted S has d - 1 adjacent pairs and a pair is
    //  NOT a gap exactly when the two values are consecutive integers, so
    //      imbalance = (d - 1) - #{ v in S : v + 1 in S }
    //  which updates in O(1) when one new value is inserted.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int sumImbalanceNumbers(int[] nums) {
        int n = nums.length;
        // values are in [1, n]; one extra slot each side so v - 1 and v + 1 are always valid
        boolean[] seen = new boolean[n + 2];
        int total = 0;

        for (int i = 0; i < n; i++) {
            int distinct = 0;  // d
            int joins = 0;     // pairs of consecutive integers both present
            for (int j = i; j < n; j++) {
                int v = nums[j];
                // a repeated value changes neither d nor the joins
                if (!seen[v]) {
                    seen[v] = true;
                    distinct++;
                    if (seen[v + 1]) {
                        joins++;
                    }
                    if (seen[v - 1]) {
                        joins++;
                    }
                }
                total += distinct - 1 - joins;
            }
            // undo only the values this left end inserted
            for (int j = i; j < n; j++) {
                seen[nums[j]] = false;
            }
        }
        return total;
    }
}
