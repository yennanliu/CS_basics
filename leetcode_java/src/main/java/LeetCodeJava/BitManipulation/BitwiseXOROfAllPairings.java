package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/bitwise-xor-of-all-pairings/

/**
 *  2425. Bitwise XOR of All Pairings
 *  Medium
 *
 *  You are given two 0-indexed arrays, nums1 and nums2, consisting of
 *  non-negative integers. There exists another array, nums3, which contains
 *  the bitwise XOR of all pairings of integers between nums1 and nums2 (every
 *  integer in nums1 is paired with every integer in nums2 exactly once).
 *
 *  Return the bitwise XOR of all integers in nums3.
 *
 *  Example 1:
 *    Input: nums1 = [2,1,3], nums2 = [10,2,5,0]
 *    Output: 13
 *    Explanation: A possible nums3 array is [8,0,7,2,11,3,4,1,9,1,6,3].
 *                 The bitwise XOR of all these numbers is 13.
 *
 *  Example 2:
 *    Input: nums1 = [1,2], nums2 = [3,4]
 *    Output: 0
 *    Explanation: nums3 = [2,5,1,6] and 2 ^ 5 ^ 1 ^ 6 = 0.
 *
 *  Constraints:
 *    1 <= nums1.length, nums2.length <= 10^5
 *    0 <= nums1[i], nums2[j] <= 10^9
 */
public class BitwiseXOROfAllPairings {

    // V0
    // IDEA: PARITY OF THE OPPOSITE ARRAY'S LENGTH DECIDES WHAT SURVIVES
    //       in the full pairing nums1[i] appears exactly nums2.length times and
    //       nums2[j] exactly nums1.length times. XOR cancels in pairs, so a value
    //       repeated an EVEN number of times vanishes. therefore:
    //         nums2.length odd -> the XOR of all of nums1 survives
    //         nums1.length odd -> the XOR of all of nums2 survives
    //       and the answer is the XOR of whatever survives (0 if neither).
    //       this never materialises the n * m pairings.
    /**
     * time = O(N + M)
     * space = O(1)
     */
    public int xorAllNums(int[] nums1, int[] nums2) {
        int res = 0;
        if (nums2.length % 2 == 1) {
            for (int x : nums1) {
                res ^= x;
            }
        }
        if (nums1.length % 2 == 1) {
            for (int x : nums2) {
                res ^= x;
            }
        }
        return res;
    }
}
