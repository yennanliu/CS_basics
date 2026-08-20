package LeetCodeJava.Sort;

// https://leetcode.com/problems/minimum-unlocked-indices-to-sort-nums/

/**
 *  3431. Minimum Unlocked Indices to Sort Nums
 *  Medium
 *  (premium / locked problem)
 *
 *  You are given an array nums consisting of integers between 1 and 3, and a
 *  binary array locked of the same size.
 *
 *  We consider nums sortable if it can be sorted using adjacent swaps, where a
 *  swap between two indices i and i + 1 is allowed if nums[i] - nums[i + 1] == 1
 *  and locked[i] == 0.
 *
 *  In one operation, you can unlock any index i by setting locked[i] to 0.
 *
 *  Return the minimum number of operations needed to make nums sortable. If it is
 *  not possible to make nums sortable, return -1.
 *
 *  Example 1:
 *    Input: nums = [1,2,1,2,3,2], locked = [1,0,1,1,0,1]
 *    Output: 0
 *
 *  Example 2:
 *    Input: nums = [1,2,1,1,3,2,2], locked = [1,0,1,1,0,1,0]
 *    Output: 2
 *
 *  Example 3:
 *    Input: nums = [1,2,1,2,3,2,1], locked = [0,0,0,0,0,0,0]
 *    Output: -1
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 3
 *    locked.length == nums.length
 *    0 <= locked[i] <= 1
 */
public class MinimumUnlockedIndicesToSortNums {

    // V0
    // IDEA: A BOUNDARY NEEDS UNLOCKING EXACTLY WHEN ITS PREFIX IS WRONG
    //       a swap is only ever legal between values differing by 1, so a 1 can
    //       slip past a 2 and a 2 past a 3, but a 1 can NEVER get past a 3 - an
    //       adjacent transposition is the only way two elements change relative
    //       order and 3 - 1 = 2 is not an allowed difference. so if any 3 stands
    //       left of any 1 the array is unsortable, full stop.
    //
    //       otherwise every adjacent inversion that can ever appear is a (2,1) or
    //       a (3,2), both legal, so the only question is WHERE swaps must happen.
    //       look at the boundary between i and i+1: if the multiset of nums[0..i]
    //       already equals the multiset of the sorted array's first i+1 entries,
    //       nothing has to cross that boundary and both sides sort on their own.
    //       if it differs, something must cross, so that boundary must be unlocked.
    //
    //       the answer is therefore the count of LOCKED boundaries whose prefix
    //       counts are off. comparing the counts of 1s and 2s suffices (3s follow).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minUnlockedIndices(int[] nums, int[] locked) {
        int n = nums.length;

        // a 3 appearing before a 1 makes the array unsortable
        boolean seenThree = false;
        for (int v : nums) {
            if (v == 3) {
                seenThree = true;
            } else if (v == 1 && seenThree) {
                return -1;
            }
        }

        int c1 = 0;
        int c2 = 0;
        for (int v : nums) {
            if (v == 1) {
                c1++;
            } else if (v == 2) {
                c2++;
            }
        }

        int p1 = 0;
        int p2 = 0;
        int res = 0;
        for (int i = 0; i < n - 1; i++) {
            if (nums[i] == 1) {
                p1++;
            } else if (nums[i] == 2) {
                p2++;
            }
            int take = i + 1;
            int want1 = Math.min(take, c1);
            int want2 = Math.min(take - want1, c2);
            if ((p1 != want1 || p2 != want2) && locked[i] == 1) {
                res++;
            }
        }
        return res;
    }
}
