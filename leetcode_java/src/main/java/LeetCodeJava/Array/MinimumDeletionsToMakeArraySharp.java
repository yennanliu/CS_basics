package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-deletions-to-make-array-sharp/description/

/**
 * 4083. Minimum Deletions to Make Array Sharp
 * Easy
 *
 * You are given an integer array nums.
 *
 * You may delete any number of elements from nums. The remaining elements
 * keep their original order.
 *
 * The resulting array a is sharp if the bitwise XOR of every pair of
 * adjacent elements is non-zero. That is, a[i] XOR a[i + 1] != 0 for every
 * index i such that 0 <= i < a.length - 1. An array with at most one element
 * is sharp.
 *
 * Return the minimum number of deletions needed to make nums sharp.
 *
 * Bitwise XOR compares the corresponding bits of two integers. A bit in the
 * result is 1 if the corresponding bits differ, and 0 otherwise.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2,2,2,3]
 * Output: 3
 * Explanation:
 * The pairs at indices (0, 1), (2, 3), and (3, 4) each have an XOR of 0.
 * Deleting one 1 and two 2s leaves [1, 2, 3], where 1 XOR 2 == 3 and
 * 2 XOR 3 == 1 are both non-zero. Therefore, the answer is 3.
 *
 * Example 2:
 *
 * Input: nums = [4,4,9,4]
 * Output: 1
 * Explanation:
 * The pair at indices (0, 1) has an XOR of 0.
 * Deleting either of these two 4s leaves [4, 9, 4], where 4 XOR 9 == 13 and
 * 9 XOR 4 == 13 are both non-zero.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 *
 */
public class MinimumDeletionsToMakeArraySharp {

    // V0
    // IDEA: XOR == 0 <=> EQUAL VALUES -> COUNT ADJACENT EQUAL PAIRS
    //       a run of k equal values must shrink to 1 -> k - 1 deletions, and every extra
    //       member of a run is exactly one nums[i] == nums[i - 1] pair. deleting inside a run
    //       never makes two different runs touch with equal values, so runs are independent.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minDeletions(int[] nums) {
        // edge case: 0 or 1 element is already sharp
        if (nums == null || nums.length <= 1) {
            return 0;
        }

        int deletions = 0;
        for (int i = 1; i < nums.length; i++) {
            // count the pair right away, so a run at the very end is counted too
            if (nums[i] == nums[i - 1]) {
                deletions++;
            }
        }
        return deletions;
    }
}
