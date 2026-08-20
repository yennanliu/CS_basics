package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/subarrays-with-xor-at-least-k/

import java.util.HashMap;
import java.util.Map;

/**
 *  3632. Subarrays with XOR at Least K
 *  Hard
 *
 *  Given an array of positive integers nums of length n and a non-negative
 *  integer k.
 *
 *  Return the number of contiguous subarrays whose bitwise XOR of all elements
 *  is greater than or equal to k.
 *
 *  Example 1:
 *    Input: nums = [3,1,2,3], k = 2
 *    Output: 6
 *    Explanation: the valid subarrays are [3], [3,1], [3,1,2,3], [1,2], [2] and
 *                 the trailing [3] — 6 in total.
 *
 *  Example 2:
 *    Input: nums = [0,0,0], k = 0
 *    Output: 6
 *    Explanation: every contiguous subarray yields XOR = 0, which meets k = 0.
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^9
 *    0 <= k <= 10^9
 */
public class SubarraysWithXorAtLeastK {

    // V0
    // IDEA: COUNT THE COMPLEMENT, ONE BIT OF K AT A TIME
    //       with prefix xors p[0..n] a subarray's xor is p[i] ^ p[j], so the task is
    //       counting prefix PAIRS whose xor is >= k. the usual answer is a binary
    //       trie; the sharper observation is that "z < k" has a UNIQUE witness bit —
    //       the highest bit where z and k differ, necessarily a bit b with k_b = 1
    //       and z_b = 0, everything above agreeing.
    //       that splits one messy inequality into a DISJOINT union over the set bits
    //       of k. for such a b, "z agrees with k above b and has 0 at b" is exactly
    //           (p_i >> b) ^ (p_j >> b) == (k >> b) ^ 1
    //       an equality — so bucket the prefixes by p >> b and add
    //       cnt[u] * cnt[u ^ c]. no trie, no descent, one frequency table per set bit.
    //       counting ORDERED pairs keeps the arithmetic clean; at the end subtract the
    //       i == j diagonal (xor 0, so it only qualifies when k == 0) and halve.
    //       NOTE: m*m with m = n+1 reaches ~10^10 -> everything must be long.
    /**
     * time = O(N * log C)
     * space = O(N)
     */
    public long countXorSubarrays(int[] nums, int k) {
        int n = nums.length;
        int[] pref = new int[n + 1];
        int cur = 0;
        for (int i = 0; i < n; i++) {
            cur ^= nums[i];
            pref[i + 1] = cur;
        }

        long below = 0L; // ordered pairs with xor < k
        int kBits = 32 - Integer.numberOfLeadingZeros(k); // 0 when k == 0
        for (int b = 0; b < kBits; b++) {
            if (((k >> b) & 1) == 0) {
                continue;
            }
            int c = (k >> b) ^ 1;
            Map<Integer, Long> cnt = new HashMap<>();
            for (int p : pref) {
                int key = p >> b;
                Long old = cnt.get(key);
                cnt.put(key, (old == null ? 0L : old) + 1L);
            }
            for (Map.Entry<Integer, Long> e : cnt.entrySet()) {
                Long other = cnt.get(e.getKey() ^ c);
                if (other != null) {
                    below += e.getValue() * other;
                }
            }
        }

        long m = n + 1;
        long atLeast = m * m - below; // ordered pairs with xor >= k
        if (k == 0) {
            atLeast -= m; // drop the i == j diagonal
        }
        return atLeast / 2;
    }
}
