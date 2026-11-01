package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-stability-factor-of-array/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3605. Minimum Stability Factor of Array
 * Hard
 *
 * You are given an integer array nums and an integer maxC.
 *
 * A subarray is called stable if the highest common factor (HCF) of all its
 * elements is greater than or equal to 2.
 *
 * The stability factor of an array is defined as the length of its longest
 * stable subarray.
 *
 * You may modify at most maxC elements of the array to any integer.
 *
 * Return the minimum possible stability factor of the array after at most
 * maxC modifications. If no stable subarray remains, return 0.
 *
 * Note:
 *
 * The highest common factor (HCF) of an array is the largest integer that
 * evenly divides all the array elements.
 * A subarray of length 1 is stable if its only element is greater than or
 * equal to 2, since HCF([x]) = x.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,5,10], maxC = 1
 * Output: 1
 * Explanation:
 * The stable subarray [5, 10] has HCF = 5, which has a stability factor of 2.
 * Since maxC = 1, one optimal strategy is to change nums[1] to 7, resulting in
 * nums = [3, 7, 10].
 * Now, no subarray of length greater than 1 has HCF >= 2. Thus, the minimum
 * possible stability factor is 1.
 *
 * Example 2:
 *
 * Input: nums = [2,6,8], maxC = 2
 * Output: 1
 * Explanation:
 * The subarray [2, 6, 8] has HCF = 2, which has a stability factor of 3.
 * Since maxC = 2, one optimal strategy is to change nums[1] to 3 and nums[2]
 * to 5, resulting in nums = [2, 3, 5].
 * Now, no subarray of length greater than 1 has HCF >= 2. Thus, the minimum
 * possible stability factor is 1.
 *
 * Example 3:
 *
 * Input: nums = [2,4,9,6], maxC = 1
 * Output: 2
 * Explanation:
 * The stable subarrays are:
 * [2, 4] with HCF = 2 and stability factor of 2.
 * [9, 6] with HCF = 3 and stability factor of 2.
 * Since maxC = 1, the stability factor of 2 cannot be reduced due to two
 * separate stable subarrays. Thus, the minimum possible stability factor is 2.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 0 <= maxC <= n
 *
 */
public class MinimumStabilityFactorOfArray {

    // V0
    // IDEA: BINARY SEARCH THE ANSWER + GREEDY INTERVAL STABBING WITH GCD RUNS
    // a modification is only worth spending on writing a 1: it becomes a "wall" no stable
    // subarray (gcd >= 2) can pass through, and it never creates a new stable subarray.
    // "answer <= L" means every stable window of length L + 1 contains a wall; that needs fewer
    // walls as L grows -> binary search L in [0, n]. For a fixed L the cheapest placement is
    // the stabbing greedy: sweep right, and for the first stable window [i - L, i] no wall
    // pierces, put a wall at i (its rightmost cell, reaching furthest right).
    // reach[i] = length - 1 of the longest stable subarray ending at i: the distinct gcds of
    // subarrays ending at i form a short list (each distinct value at least halves), so it is
    // carried along with the earliest start giving each gcd.
    /**
     * time = O(N log(max(nums)) + N log N)
     * space = O(N)
     */
    public int minStable(int[] nums, int maxC) {
        int n = nums.length;

        int[] reach = new int[n];
        // {gcd, earliest start giving that gcd}, gcd >= 2, ordered from the earliest start
        List<int[]> gcdRuns = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<int[]> next = new ArrayList<>();
            for (int[] run : gcdRuns) {
                int newGcd = gcd(run[0], nums[i]);
                boolean isNew = next.isEmpty() || next.get(next.size() - 1)[0] != newGcd;
                if (newGcd >= 2 && isNew) {
                    next.add(new int[] {newGcd, run[1]});
                }
            }
            boolean isNew = next.isEmpty() || next.get(next.size() - 1)[0] != nums[i];
            if (nums[i] >= 2 && isNew) {
                next.add(new int[] {nums[i], i});
            }
            gcdRuns = next;
            // -1 means no stable subarray ends at i (nums[i] == 1 or gcd drops to 1)
            reach[i] = gcdRuns.isEmpty() ? -1 : i - gcdRuns.get(0)[1];
        }

        int left = 0;
        int right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canLimit(reach, maxC, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Whether at most maxC walls can stab every stable window of length maxLength + 1. */
    private boolean canLimit(int[] reach, int maxC, int maxLength) {
        int walls = 0;
        int lastWall = -1;
        for (int i = maxLength; i < reach.length; i++) {
            int windowStart = i - maxLength;
            // stable window [windowStart, i] that no wall pierces yet
            if (reach[i] >= maxLength && lastWall < windowStart) {
                walls++;
                if (walls > maxC) {
                    return false;
                }
                lastWall = i;
            }
        }
        return true;
    }

    /** Euclid's gcd. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
