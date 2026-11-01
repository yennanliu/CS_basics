package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-the-number-of-good-subarrays/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2537. Count the Number of Good Subarrays
 * Medium
 *
 * Given an integer array nums and an integer k, return the number of good subarrays of nums.
 *
 * A subarray arr is good if there are at least k pairs of indices (i, j) such that i < j and arr[i] == arr[j].
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1,1,1], k = 10
 * Output: 1
 * Explanation: The only good subarray is the array nums itself.
 *
 * Example 2:
 *
 * Input: nums = [3,1,4,3,2,2,4], k = 2
 * Output: 4
 * Explanation: There are 4 different good subarrays:
 * - [3,1,4,3,2,2] that has 2 pairs.
 * - [3,1,4,3,2,2,4] that has 3 pairs.
 * - [1,4,3,2,2,4] that has 2 pairs.
 * - [4,3,2,2,4] that has 2 pairs.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i], k <= 10^9
 *
 */
public class CountTheNumberOfGoodSubarrays {

    // V0
    // IDEA: SLIDING WINDOW + COUNT MAP (the pair count only grows as the window grows)
    //  adding x on the right creates count[x] new equal pairs; dropping y on the left removes
    //  count[y] - 1 of them. for each right end, shrink from the left while the window still
    //  has >= k pairs; when that stops, `left` is the first start that FAILS, so every start
    //  0 .. left - 1 gives a good subarray ending here -> add left.
    //  NOTE: pairs += count[x] BEFORE incrementing it, pairs -= count[y] AFTER decrementing it.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long countGood(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        long result = 0;
        long pairs = 0;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            int x = nums[right];
            int seen = count.getOrDefault(x, 0);
            // x pairs with each copy already in the window
            pairs += seen;
            count.put(x, seen + 1);

            while (pairs >= k) {
                int y = nums[left];
                int remaining = count.get(y) - 1;
                count.put(y, remaining);
                // y no longer pairs with the remaining copies
                pairs -= remaining;
                left++;
            }
            result += left;
        }
        return result;
    }
}
