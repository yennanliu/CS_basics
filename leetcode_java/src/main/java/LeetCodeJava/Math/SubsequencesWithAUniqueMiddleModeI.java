package LeetCodeJava.Math;

// https://leetcode.com/problems/subsequences-with-a-unique-middle-mode-i/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3395. Subsequences with a Unique Middle Mode I
 * Hard
 *
 * Given an integer array nums, find the number of subsequences of size 5 of nums with a unique middle
 * mode.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * A mode of a sequence of numbers is defined as the element that appears the maximum number of times in
 * the sequence.
 *
 * A sequence of numbers contains a unique mode if it has only one mode.
 *
 * A sequence of numbers seq of size 5 contains a unique middle mode if the middle element (seq[2]) is a
 * unique mode.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1,1,1,1]
 * Output: 6
 * Explanation:
 * [1, 1, 1, 1, 1] is the only subsequence of size 5 that can be formed, and it has a unique middle mode
 * of 1. This subsequence can be formed in 6 different ways, so the output is 6.
 *
 * Example 2:
 *
 * Input: nums = [1,2,2,3,3,4]
 * Output: 4
 * Explanation:
 * [1, 2, 2, 3, 4] and [1, 2, 3, 3, 4] each have a unique middle mode because the number at index 2 has
 * the greatest frequency in the subsequence. [1, 2, 2, 3, 3] does not have a unique middle mode because
 * 2 and 3 appear twice.
 *
 * Example 3:
 *
 * Input: nums = [0,1,2,3,4,5,6,7,8]
 * Output: 0
 * Explanation:
 * There is no subsequence of length 5 with a unique middle mode.
 *
 *
 * Constraints:
 *
 * 5 <= nums.length <= 1000
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class SubsequencesWithAUniqueMiddleModeI {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COMBINATORICS — FIX THE MIDDLE INDEX i (VALUE x), PICK 2 ON THE LEFT AND 2 ON THE RIGHT.
    //   with c = copies of x among the 5 picks:
    //     c = 1  -> never a unique mode
    //     c = 2  -> valid iff the 3 non-x picks are pairwise distinct
    //     c >= 3 -> always valid
    //   count c >= 3 by subtraction, and for c = 2 subtract the picks where some value y repeats
    //   (two different y cannot both repeat inside 3 slots, so the per-y failures are disjoint).
    //   n <= 1000, so re-summing over every distinct value per middle index is fine.
    /**
     * time = O(N * D)
     * space = O(D)
     */
    public int subsequencesWithMiddleMode(int[] nums) {
        int n = nums.length;

        // compress values to ids 0 .. distinct-1
        Map<Integer, Integer> idOf = new HashMap<>();
        int[] ids = new int[n];
        for (int i = 0; i < n; i++) {
            if (!idOf.containsKey(nums[i])) {
                idOf.put(nums[i], idOf.size());
            }
            ids[i] = idOf.get(nums[i]);
        }
        int distinct = idOf.size();

        // leftCount[v] / rightCount[v] = copies of v strictly left / right of the middle index
        long[] leftCount = new long[distinct];
        long[] rightCount = new long[distinct];
        for (int id : ids) {
            rightCount[id]++;
        }
        rightCount[ids[0]]--;

        long answer = 0;
        for (int i = 0; i < n; i++) {
            int x = ids[i];
            long leftSize = i;
            long rightSize = n - 1 - i;
            long leftX = leftCount[x];
            long rightX = rightCount[x];
            long leftOther = leftSize - leftX;
            long rightOther = rightSize - rightX;

            long allPicks = choose2(leftSize) * choose2(rightSize);
            // no extra copy of x
            long zeroExtra = choose2(leftOther) * choose2(rightOther);
            // exactly one extra copy of x, taken from the left or from the right
            long oneExtra = leftX * leftOther * choose2(rightOther)
                    + choose2(leftOther) * rightX * rightOther;
            // two or more extra copies: always a unique mode
            long twoOrMoreExtra = allPicks - zeroExtra - oneExtra;

            // among the one-extra picks, the ones where some y != x appears twice (or three times)
            long repeatedOther = 0;
            if (oneExtra > 0) {
                for (int y = 0; y < distinct; y++) {
                    if (y == x) {
                        continue;
                    }
                    long leftY = leftCount[y];
                    long rightY = rightCount[y];
                    if (leftY == 0 && rightY == 0) {
                        continue;
                    }
                    // the extra x is on the left: left = {x, ?}, right = {?, ?}
                    repeatedOther += leftX * (leftY * rightY * (rightOther - rightY) + leftOther * choose2(rightY));
                    // the extra x is on the right: mirror image
                    repeatedOther += rightX * (rightY * leftY * (leftOther - leftY) + rightOther * choose2(leftY));
                }
            }

            answer = (answer + twoOrMoreExtra + oneExtra - repeatedOther) % MOD;

            // slide the middle one step right
            if (i + 1 < n) {
                leftCount[x]++;
                rightCount[ids[i + 1]]--;
            }
        }
        return (int) answer;
    }

    /** C(count, 2). */
    private long choose2(long count) {
        return count * (count - 1) / 2;
    }
}
