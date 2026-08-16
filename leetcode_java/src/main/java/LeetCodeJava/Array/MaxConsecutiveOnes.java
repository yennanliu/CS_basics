package LeetCodeJava.Array;

// https://leetcode.com/problems/max-consecutive-ones/description/

import java.util.ArrayList;
import java.util.List;
/**
 * 485. Max Consecutive Ones
 * Easy
 *
 * Given a binary array nums, return the maximum number of consecutive 1's in the
 * array.
 *
 * Example 1:
 *
 * Input: nums = [1,1,0,1,1,1]
 * Output: 3
 * Explanation: The first two digits or the last three digits are consecutive 1s.
 * The maximum number of consecutive 1s is 3.
 *
 * Example 2:
 *
 * Input: nums = [1,0,1,1,0,1]
 * Output: 2
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums[i] is either 0 or 1.
 *
 */
public class MaxConsecutiveOnes {

    // V0
    // IDEA: ONE PASS RUNNING COUNTER
    /**
     *  Keep a counter of the current streak of 1's; a 0 RESETS it to 0.
     *  Track the best streak seen so far.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int best = 0;
        int cur = 0;

        for (int x : nums) {
            if (x == 1) {
                cur += 1;
                best = Math.max(best, cur);
            } else {
                cur = 0; // streak broken
            }
        }

        return best;
    }


    // V1
    // IDEA: TWO POINTERS (window that only ever holds 1s)
    /**
     *  Walk a `right` pointer and keep `left` anchored at the start of the current
     *  run of 1s; a 0 re-anchors left to right + 1.
     *
     *  Same linear scan as V0, but it yields the run BOUNDARIES for free, which
     *  the plain counter does not.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findMaxConsecutiveOnes_1(int[] nums) {
        int best = 0;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                left = right + 1;
            } else {
                best = Math.max(best, right - left + 1);
            }
        }

        return best;
    }

    // V2
    // IDEA: SPLIT ON THE ZEROS (string / token style)
    /**
     *  Treat the array as a string of 0/1 and split it on '0'; the answer is the
     *  length of the longest surviving chunk.
     *
     *  A declarative one-liner style -- no manual counter at all.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int findMaxConsecutiveOnes_2(int[] nums) {
        StringBuilder sb = new StringBuilder();
        for (int x : nums) {
            sb.append(x);
        }

        int best = 0;
        for (String chunk : sb.toString().split("0")) {
            best = Math.max(best, chunk.length());
        }
        return best;
    }

    // V3
    // IDEA: PREFIX OF ZERO POSITIONS
    /**
     *  Record the index of every 0 (plus virtual sentinels at -1 and n).
     *  The longest run of 1s is the largest gap between two consecutive zeros.
     *
     *  This generalises directly to `at most k zeros` (LC 487 / LC 1004) by
     *  comparing zeros[i + k + 1] against zeros[i] instead of adjacent entries.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int findMaxConsecutiveOnes_3(int[] nums) {
        List<Integer> zeros = new ArrayList<>();
        zeros.add(-1);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                zeros.add(i);
            }
        }
        zeros.add(nums.length);

        int best = 0;
        for (int i = 1; i < zeros.size(); i++) {
            best = Math.max(best, zeros.get(i) - zeros.get(i - 1) - 1);
        }
        return best;
    }

}
