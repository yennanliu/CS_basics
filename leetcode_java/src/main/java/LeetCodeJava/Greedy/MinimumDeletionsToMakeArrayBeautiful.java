package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-deletions-to-make-array-beautiful/description/

/**
 * 2216. Minimum Deletions to Make Array Beautiful
 * Medium
 *
 * You are given a 0-indexed integer array nums. The array nums is beautiful if:
 *
 * nums.length is even.
 * nums[i] != nums[i + 1] for all i % 2 == 0.
 *
 * Note that an empty array is considered beautiful.
 *
 * You can delete any number of elements from nums. When you delete an element, all the elements to the
 * right of the deleted element will be shifted one unit to the left to fill the gap created and all the
 * elements to the left of the deleted element will remain unchanged.
 *
 * Return the minimum number of elements to delete from nums to make it beautiful.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2,3,5]
 * Output: 1
 *
 * Example 2:
 *
 * Input: nums = [1,1,2,2,3,3]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^5
 *
 */
public class MinimumDeletionsToMakeArrayBeautiful {

    // V0
    // IDEA: GREEDY LEFT-TO-RIGHT PAIRING, THEN FIX THE PARITY AT THE END
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minDeletion(int[] nums) {
        int n = nums.length;
        int deletions = 0;
        // i is always the first element of the pair being formed
        int i = 0;

        while (i < n - 1) {
            if (nums[i] == nums[i + 1]) {
                // an equal pair: delete nums[i]; the survivor nums[i + 1] now starts the pair
                deletions++;
                i++;
            } else {
                // a valid pair: move on to the next one
                i += 2;
            }
        }

        // the kept array must have even length; drop the last element if it is odd
        if ((n - deletions) % 2 == 1) {
            deletions++;
        }
        return deletions;
    }
}
