package LeetCodeJava.Greedy;

// https://leetcode.com/problems/partition-array-such-that-maximum-difference-is-k/description/

import java.util.*;

/**
 * 2294. Partition Array Such That Maximum Difference Is K
 * Medium
 *
 * You are given an integer array nums and an integer k. You may partition nums into one or more subsequences such that each element in nums appears in exactly one of the subsequences.
 *
 * Return the minimum number of subsequences needed such that the difference between the maximum and minimum values in each subsequence is at most k.
 *
 * A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,6,1,2,5], k = 2
 * Output: 2
 * Explanation:
 * We can partition nums into the two subsequences [3,1,2] and [6,5].
 * The difference between the maximum and minimum value in the first subsequence is 3 - 1 = 2.
 * The difference between the maximum and minimum value in the second subsequence is 6 - 5 = 1.
 * Since two subsequences were created, we return 2. It can be shown that 2 is the minimum number of subsequences needed.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3], k = 1
 * Output: 2
 * Explanation:
 * We can partition nums into the two subsequences [1,2] and [3].
 * The difference between the maximum and minimum value in the first subsequence is 2 - 1 = 1.
 * The difference between the maximum and minimum value in the second subsequence is 3 - 3 = 0.
 * Since two subsequences were created, we return 2. It can be shown that 2 is the minimum number of subsequences needed.
 *
 * Example 3:
 *
 * Input: nums = [2,2,4,5], k = 0
 * Output: 3
 * Explanation:
 * We can partition nums into the three subsequences [2,2], [4], and [5].
 * The difference between the maximum and minimum value in the first subsequences is 2 - 2 = 0.
 * The difference between the maximum and minimum value in the second subsequences is 4 - 4 = 0.
 * The difference between the maximum and minimum value in the third subsequences is 5 - 5 = 0.
 * Since three subsequences were created, we return 3. It can be shown that 3 is the minimum number of subsequences needed.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^5
 * 0 <= k <= 10^5
 *
 */
public class PartitionArraySuchThatMaximumDifferenceIsK {

    // V0
    // IDEA: SORT + GREEDY (ORDER IS IRRELEVANT, EXTEND EACH GROUP AS FAR AS anchor + k)
    //       the smallest remaining value must start some group, and letting that
    //       group swallow everything up to anchor + k never hurts.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int partitionArray(int[] nums, int k) {
        // a subsequence may pick any indices, so only the values matter
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int groups = 1;
        // the smallest value of the group being built
        int anchor = sorted[0];
        for (int i = 1; i < sorted.length; i++) {
            // this value cannot join the current group -> it opens a new one
            if (sorted[i] - anchor > k) {
                groups++;
                anchor = sorted[i];
            }
        }
        return groups;
    }
}
