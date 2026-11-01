package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/find-the-longest-equal-subarray/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2831. Find the Longest Equal Subarray
 * Medium
 *
 * You are given a 0-indexed integer array nums and an integer k.
 *
 * A subarray is called equal if all of its elements are equal. Note that the empty subarray is an
 * equal subarray.
 *
 * Return the length of the longest possible equal subarray after deleting at most k elements from
 * nums.
 *
 * A subarray is a contiguous, possibly empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,2,3,1,3], k = 3
 * Output: 3
 *
 * Example 2:
 *
 * Input: nums = [1,1,2,2,1,1], k = 2
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= nums.length
 * 0 <= k <= nums.length
 *
 */
public class FindTheLongestEqualSubarray {

    // V0
    // IDEA: SLIDING WINDOW OVER THE POSITION LIST OF EACH VALUE
    //  keeping positions[l..r] of value v means deleting every other element between
    //  positions[l] and positions[r]: positions[r] - positions[l] - (r - l) of them.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestEqualSubarray(List<Integer> nums, int k) {
        // value -> indices where it occurs, in increasing order
        Map<Integer, List<Integer>> positionsByValue = new HashMap<>();
        for (int i = 0; i < nums.size(); i++) {
            positionsByValue.computeIfAbsent(nums.get(i), key -> new ArrayList<>()).add(i);
        }

        int best = 0;
        for (List<Integer> positions : positionsByValue.values()) {
            int left = 0;
            for (int right = 0; right < positions.size(); right++) {
                // elements of another value sitting between positions[left] and positions[right]
                while (positions.get(right) - positions.get(left) - (right - left) > k) {
                    left++;
                }
                // the answer is the number of kept equal elements, not the span
                best = Math.max(best, right - left + 1);
            }
        }
        return best;
    }
}
