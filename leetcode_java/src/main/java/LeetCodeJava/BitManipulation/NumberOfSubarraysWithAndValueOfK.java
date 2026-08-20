package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-subarrays-with-and-value-of-k/

import java.util.HashMap;
import java.util.Map;

/**
 *  3209. Number of Subarrays With AND Value of K
 *  Hard
 *
 *  Given an array of integers nums and an integer k, return the number of
 *  subarrays of nums where the bitwise AND of the elements of the subarray
 *  equals k.
 *
 *  Example 1:
 *    Input: nums = [1,1,1], k = 1
 *    Output: 6
 *    Explanation: All subarrays contain only 1's.
 *
 *  Example 2:
 *    Input: nums = [1,1,2], k = 1
 *    Output: 3
 *    Explanation: Subarrays having an AND value of 1 are: [1,1], [1], [1].
 *
 *  Example 3:
 *    Input: nums = [1,2,3], k = 2
 *    Output: 2
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i], k <= 10^9
 */
public class NumberOfSubarraysWithAndValueOfK {

    // V0
    // IDEA: THE ANDs OF ALL SUBARRAYS ENDING AT i FORM AT MOST ~30 DISTINCT VALUES
    //       extending a subarray leftwards can only CLEAR bits, so the ANDs of the
    //       subarrays ending at index i form a chain that loses bits and never gains
    //       any -> at most 31 distinct values for 30-bit numbers.
    //       carry a small map { AND value -> how many subarrays ending here produce it }.
    //       moving to nums[i] ANDs every key with nums[i] (merging collisions by adding
    //       counts) and adds the single-element subarray; accumulate the count under k.
    //       NOTE: the answer can exceed int (n = 10^5 all-equal -> ~5*10^9) -> long.
    /**
     * time = O(30 * N)
     * space = O(30)
     */
    public long countSubarrays(int[] nums, int k) {
        long res = 0L;
        Map<Integer, Long> prev = new HashMap<>();

        for (int x : nums) {
            Map<Integer, Long> cur = new HashMap<>();
            cur.put(x, 1L);
            for (Map.Entry<Integer, Long> e : prev.entrySet()) {
                int v = e.getKey() & x;
                Long old = cur.get(v);
                cur.put(v, (old == null ? 0L : old) + e.getValue());
            }
            Long hit = cur.get(k);
            if (hit != null) {
                res += hit;
            }
            prev = cur;
        }
        return res;
    }
}
