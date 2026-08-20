package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/sum-of-values-at-indices-with-k-set-bits/

import java.util.List;

/**
 *  2859. Sum of Values at Indices With K Set Bits
 *  Easy
 *
 *  You are given a 0-indexed integer array nums and an integer k.
 *
 *  Return an integer that denotes the sum of elements in nums whose corresponding
 *  indices have exactly k set bits in their binary representation.
 *
 *  The set bits in an integer are the 1's present when it is written in binary.
 *  For example, the binary representation of 21 is 10101, which has 3 set bits.
 *
 *  Example 1:
 *    Input: nums = [5,10,1,5,2], k = 1
 *    Output: 13
 *    Explanation: indices 1 (001), 2 (010) and 4 (100) have exactly 1 set bit,
 *                 so the answer is nums[1] + nums[2] + nums[4] = 13.
 *
 *  Example 2:
 *    Input: nums = [4,3,2,1], k = 2
 *    Output: 1
 *    Explanation: only index 3 (11) has 2 set bits.
 *
 *  Constraints:
 *    1 <= nums.length <= 1000
 *    1 <= nums[i] <= 10^5
 *    0 <= k <= 10
 */
public class SumOfValuesAtIndicesWithKSetBits {

    // V0
    // IDEA: POPCOUNT VIA `x &= x - 1`
    //       walk every index, count its set bits, and accumulate nums[i] when the
    //       count equals k. clearing the LOWEST set bit with x &= x - 1 runs once
    //       per 1-bit rather than once per bit position.
    //       NOTE: k == 0 is allowed and only index 0 has zero set bits — the loop
    //             handles that with no special case.
    //       (Integer.bitCount(i) is the idiomatic one-liner; the mask trick is kept
    //        to mirror the reference solution.)
    /**
     * time = O(N * log N)
     * space = O(1)
     */
    public int sumIndicesWithKSetBits(List<Integer> nums, int k) {
        int res = 0;
        for (int i = 0; i < nums.size(); i++) {
            int x = i;
            int bits = 0;
            while (x != 0) {
                x &= x - 1;
                bits++;
            }
            if (bits == k) {
                res += nums.get(i);
            }
        }
        return res;
    }
}
