package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/contains-duplicate-iii/

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *  220. Contains Duplicate III
 *  Hard
 *
 *  You are given an integer array nums and two integers indexDiff and valueDiff.
 *
 *  Find a pair of indices (i, j) such that:
 *   - i != j,
 *   - abs(i - j) <= indexDiff,
 *   - abs(nums[i] - nums[j]) <= valueDiff.
 *
 *  Return true if such pair exists, false otherwise.
 *
 *  Example 1:
 *   Input: nums = [1,2,3,1], indexDiff = 3, valueDiff = 0
 *   Output: true
 *
 *  Example 2:
 *   Input: nums = [1,5,9,1,5,9], indexDiff = 2, valueDiff = 3
 *   Output: false
 *
 *  Constraints:
 *   2 <= nums.length <= 10^5
 *   -10^9 <= nums[i] <= 10^9
 *   1 <= indexDiff <= nums.length
 *   0 <= valueDiff <= 10^9
 */
public class ContainsDuplicateIII {

    // V0
    // IDEA: bucketing. Put every value into a bucket of width (valueDiff + 1),
    //       so two values in the SAME bucket are always a hit; otherwise only the
    //       two neighbouring buckets can hold a match. Keep buckets for the last
    //       `indexDiff` elements only (sliding window).
    /**
     * time = O(n)
     * space = O(min(n, indexDiff))
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        if (nums == null || nums.length < 2 || indexDiff <= 0 || valueDiff < 0) {
            return false;
        }

        long width = (long) valueDiff + 1;
        Map<Long, Long> buckets = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            long val = nums[i];
            long id = bucketId(val, width);

            if (buckets.containsKey(id)) {
                return true;
            }
            Long prev = buckets.get(id - 1);
            if (prev != null && val - prev <= valueDiff) {
                return true;
            }
            Long next = buckets.get(id + 1);
            if (next != null && next - val <= valueDiff) {
                return true;
            }

            buckets.put(id, val);
            if (i >= indexDiff) {
                buckets.remove(bucketId(nums[i - indexDiff], width));
            }
        }
        return false;
    }

    // floor division so negatives land in the right bucket
    private long bucketId(long val, long width) {
        return val < 0 ? (val + 1) / width - 1 : val / width;
    }

    // V1
    // IDEA: sliding window of size indexDiff kept in a TreeSet; for each value
    //       probe ceiling(val - valueDiff) and check it is within valueDiff.
    /**
     * time = O(n log(min(n, indexDiff)))
     * space = O(min(n, indexDiff))
     */
    public boolean containsNearbyAlmostDuplicate_1(int[] nums, int indexDiff, int valueDiff) {
        if (nums == null || nums.length < 2 || indexDiff <= 0 || valueDiff < 0) {
            return false;
        }

        TreeSet<Long> window = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            long val = nums[i];
            Long ceil = window.ceiling(val - valueDiff);
            if (ceil != null && ceil <= val + valueDiff) {
                return true;
            }
            window.add(val);
            if (i >= indexDiff) {
                window.remove((long) nums[i - indexDiff]);
            }
        }
        return false;
    }
}
