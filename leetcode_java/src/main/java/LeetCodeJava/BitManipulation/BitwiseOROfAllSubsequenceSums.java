package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/bitwise-or-of-all-subsequence-sums/

/**
 *  2505. Bitwise OR of All Subsequence Sums
 *  Medium
 *
 *  Given an integer array nums, return the value of the bitwise OR of the sum
 *  of all possible subsequences in the array.
 *
 *  A subsequence is a sequence that can be derived from another sequence by
 *  removing zero or more elements without changing the order of the remaining
 *  elements.
 *
 *  Example 1:
 *    Input: nums = [2,1,0,3]
 *    Output: 7
 *    Explanation: All possible subsequence sums that we can have are:
 *                 0, 1, 2, 3, 4, 5, 6.
 *                 0 OR 1 OR 2 OR 3 OR 4 OR 5 OR 6 = 7, so we return 7.
 *
 *  Example 2:
 *    Input: nums = [0,0,0]
 *    Output: 0
 *    Explanation: 0 is the only possible subsequence sum, so we return 0.
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^9
 */
public class BitwiseOROfAllSubsequenceSums {

    // V0
    // IDEA: BIT COUNTING + CARRY PROPAGATION
    //       claim: bit i shows up in SOME subsequence sum
    //              <=> T(i) >= 2^i, where T(i) = sum over nums of (v mod 2^(i+1)),
    //              i.e. all the weight living in bits 0..i.
    //       (=>)  any subset's low part is at most T(i); if T(i) < 2^i no subset
    //             can ever reach bit i.
    //       (<=)  add elements one by one and watch the running low part cross
    //             2^i: either it lands inside [2^i, 2^(i+1)) -> that prefix has
    //             bit i, or the single element that made it jump is itself in
    //             that window -> that one element alone has bit i.
    //       T(i) >= 2^i is computed with a carry sweep: cnt[i] = #nums with bit i
    //       set, then cnt[i+1] += cnt[i] / 2 turns cnt[i] into floor(T(i) / 2^i),
    //       so the test is just "cnt[i] != 0".
    //       NOTE: the sweep must run WELL above the input width - carries from
    //             10^5 values keep climbing (total sum < 10^14 < 2^47), so use
    //             ~64 bits and a long result.
    /**
     * time = O(N * 32 + 64)
     * space = O(64)
     */
    public long subsequenceSumOr(int[] nums) {
        final int B = 64;
        long[] cnt = new long[B + 1];
        for (int x : nums) {
            long v = x;
            int i = 0;
            while (v != 0) {
                if ((v & 1L) == 1L) {
                    cnt[i]++;
                }
                v >>>= 1;
                i++;
            }
        }

        long res = 0L;
        for (int i = 0; i < B; i++) {
            if (cnt[i] != 0) {
                res |= (1L << i);
            }
            cnt[i + 1] += cnt[i] / 2;
        }
        return res;
    }
}
