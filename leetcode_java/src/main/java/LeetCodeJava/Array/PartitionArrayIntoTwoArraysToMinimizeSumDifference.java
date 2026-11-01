package LeetCodeJava.Array;

// https://leetcode.com/problems/partition-array-into-two-arrays-to-minimize-sum-difference/description/

import java.util.*;

/**
 * 2035. Partition Array Into Two Arrays to Minimize Sum Difference
 * Hard
 *
 * You are given an integer array nums of 2 * n integers. You need to partition nums into two arrays of length n to minimize the absolute difference of the sums of the arrays. To partition nums, put each element of nums into one of the two arrays.
 *
 * Return the minimum possible absolute difference.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,9,7,3]
 * Output: 2
 * Explanation: One optimal partition is: [3,9] and [7,3].
 * The absolute difference between the sums of the arrays is abs((3 + 9) - (7 + 3)) = 2.
 *
 * Example 2:
 *
 * Input: nums = [-36,36]
 * Output: 72
 * Explanation: One optimal partition is: [-36] and [36].
 * The absolute difference between the sums of the arrays is abs((-36) - (36)) = 72.
 *
 * Example 3:
 *
 * Input: nums = [2,-1,0,4,-2,-9]
 * Output: 0
 * Explanation: One optimal partition is: [2,4,-9] and [-1,0,-2].
 * The absolute difference between the sums of the arrays is abs((2 + 4 + -9) - (-1 + 0 + -2)) = 0.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 15
 * nums.length == 2 * n
 * -10^7 <= nums[i] <= 10^7
 *
 */
public class PartitionArrayIntoTwoArraysToMinimizeSumDifference {

    // V0
    // IDEA: MEET IN THE MIDDLE + BINARY SEARCH (2^30 subsets is too many, 2 * 2^15 is not)
    /**
     *  Split nums into a left half and a right half (n elements each).
     *  A valid partition takes k elements from the left half and n - k from the right.
     *
     *  For each half, enumerate every subset and bucket its sum by subset SIZE:
     *      leftSums[k]  = sums of left-half subsets with exactly k elements
     *      rightSums[k] = same for the right half (sorted)
     *
     *  If the first array takes sum a from the left (k elements) and b from the
     *  right (n - k elements), the difference is |total - 2 * (a + b)|, so for each a
     *  binary-search rightSums[n - k] for the b closest to total / 2 - a.
     *
     *  Brute force over all C(2n, n) partitions is the stepping stone; it times out at n = 15.
     *
     * time = O(2^N * N)
     * space = O(2^N)
     */
    public int minimumDifference(int[] nums) {
        int n = nums.length / 2;
        long total = 0;
        for (int num : nums) {
            total += num;
        }

        List<List<Long>> leftSums = subsetSumsBySize(nums, 0, n);
        List<List<Long>> rightSums = subsetSumsBySize(nums, n, n);
        for (List<Long> group : rightSums) {
            Collections.sort(group);
        }

        long best = Long.MAX_VALUE;
        for (int k = 0; k <= n; k++) {
            List<Long> candidates = rightSums.get(n - k);
            for (long a : leftSums.get(k)) {
                // want a + b as close as possible to total / 2, i.e. 2b close to total - 2a
                int idx = lowerBound(candidates, total - 2 * a);
                // the closest b is either at the insertion point or just before it
                for (int j = idx - 1; j <= idx; j++) {
                    if (j >= 0 && j < candidates.size()) {
                        long diff = Math.abs(total - 2 * (a + candidates.get(j)));
                        best = Math.min(best, diff);
                    }
                }
            }
        }
        return (int) best;
    }

    /** Sums of every subset of nums[start, start + len), grouped by subset size. */
    private List<List<Long>> subsetSumsBySize(int[] nums, int start, int len) {
        List<List<Long>> groups = new ArrayList<>();
        for (int size = 0; size <= len; size++) {
            groups.add(new ArrayList<>());
        }
        // each mask picks a subset: bit i set -> nums[start + i] is in it
        for (int mask = 0; mask < (1 << len); mask++) {
            long sum = 0;
            int size = 0;
            for (int i = 0; i < len; i++) {
                if ((mask & (1 << i)) != 0) {
                    sum += nums[start + i];
                    size++;
                }
            }
            groups.get(size).add(sum);
        }
        return groups;
    }

    /** First index whose 2 * value is >= target (sorted list); size() if none. */
    private int lowerBound(List<Long> sorted, long target) {
        int left = 0;
        int right = sorted.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (2 * sorted.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
