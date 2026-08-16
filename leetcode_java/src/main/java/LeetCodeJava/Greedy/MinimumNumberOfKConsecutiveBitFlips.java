package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-k-consecutive-bit-flips/description/

import java.util.ArrayDeque;
import java.util.Deque;
/**
 * 995. Minimum Number of K Consecutive Bit Flips
 * Hard
 *
 * You are given a binary array nums and an integer k.
 *
 * A k-bit flip is choosing a subarray of length k from nums and simultaneously changing
 * every 0 in the subarray to 1, and every 1 in the subarray to 0.
 *
 * Return the minimum number of k-bit flips required so that there is no 0 in the array.
 * If it is not possible, return -1.
 *
 * A subarray is a contiguous part of an array.
 *
 * Example 1:
 *
 * Input: nums = [0,1,0], k = 1
 * Output: 2
 * Explanation: Flip nums[0], then flip nums[2].
 *
 * Example 2:
 *
 * Input: nums = [1,1,0], k = 2
 * Output: -1
 * Explanation: No matter how we flip subarrays of size 2, we cannot make the array
 * become [1,1,1].
 *
 * Example 3:
 *
 * Input: nums = [0,0,0,1,0,1,1,0], k = 3
 * Output: 3
 * Explanation:
 * Flip nums[0],nums[1],nums[2]: nums becomes [1,1,1,1,0,1,1,0]
 * Flip nums[4],nums[5],nums[6]: nums becomes [1,1,1,1,1,0,0,0]
 * Flip nums[5],nums[6],nums[7]: nums becomes [1,1,1,1,1,1,1,1]
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= k <= nums.length
 *
 */
public class MinimumNumberOfKConsecutiveBitFlips {

    // V0
    // IDEA: GREEDY + DIFFERENCE ARRAY (sliding flip count)
    /**
     *  - Scan LEFT TO RIGHT. Index 0 can only ever be fixed by a flip STARTING
     *    at 0, so the leftmost still-zero position FORCES a flip starting there.
     *    That makes the greedy choice UNIQUE, hence optimal.
     *
     *  - Flipping is expensive to apply literally (O(n*k)). Instead track how many
     *    flips currently COVER position i: a flip started at i stops covering at
     *    i + k, so record that EXPIRY in a difference array.
     *
     *  - Position i is effectively 1 iff (nums[i] + coveringFlips) is ODD.
     *
     *  - If a flip MUST start at i but i + k > n, the tail can never be fixed -> -1.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int minKBitFlips(int[] nums, int k) {
        int n = nums.length;

        int[] expire = new int[n + 1]; // expire[i] = -(flips that stop covering at i)
        int covering = 0;              // how many flips currently cover position i
        int res = 0;

        for (int i = 0; i < n; i++) {
            covering += expire[i];

            if ((nums[i] + covering) % 2 == 0) {
                // still a 0 here -> we are FORCED to start a flip at i
                if (i + k > n) {
                    return -1;
                }
                res += 1;
                covering += 1;
                expire[i + k] -= 1;
            }
        }

        return res;
    }


    // V1
    // IDEA: QUEUE OF ACTIVE FLIP POSITIONS
    /**
     *  Instead of a difference array, keep a QUEUE of the start positions of the
     *  flips still covering the cursor, and evict the head once it expires.
     *
     *  The queue size IS the number of covering flips, so the parity test is just
     *  `queue.size() % 2` -- and the queue can be printed to show exactly which
     *  flips are live.
     *
     *  time  = O(n)
     *  space = O(k)
     */
    public int minKBitFlips_1(int[] nums, int k) {
        int n = nums.length;
        Deque<Integer> flips = new ArrayDeque<>();
        int res = 0;

        for (int i = 0; i < n; i++) {
            // retire the flips that no longer reach i
            while (!flips.isEmpty() && flips.peekFirst() + k <= i) {
                flips.pollFirst();
            }
            if ((nums[i] + flips.size()) % 2 == 0) {
                if (i + k > n) {
                    return -1;
                }
                flips.offerLast(i);
                res += 1;
            }
        }
        return res;
    }

    // V2
    // IDEA: IN-PLACE MARKING (encode `a flip starts here` in the array itself)
    /**
     *  Write the value 2 into nums[i] to record that a flip starts at i. That
     *  costs no extra memory: 2 is distinguishable from 0/1, and `nums[i] % 2`
     *  still recovers the original bit.
     *
     *  -> O(1) auxiliary space, versus O(n) for the difference array.
     *
     *  NOTE !!! this MUTATES the input, which is why the other variants clone it
     *           before calling this one in tests.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int minKBitFlips_2(int[] nums, int k) {
        int n = nums.length;
        int covering = 0;
        int res = 0;

        for (int i = 0; i < n; i++) {
            if (i >= k && nums[i - k] == 2) {
                covering -= 1;   // that flip has expired
            }
            if ((nums[i] + covering) % 2 == 0) {
                if (i + k > n) {
                    return -1;
                }
                nums[i] = 2;     // mark: a flip starts here
                covering += 1;
                res += 1;
            }
        }
        return res;
    }

    // V3
    // IDEA: BRUTE FORCE -- actually flip the subarray
    /**
     *  Scan left to right and, on every remaining 0, physically invert the next k
     *  entries.
     *
     *  O(n * k), too slow at n = 10^5, but it performs the operation the statement
     *  describes rather than simulating it -- the oracle for the two O(n) versions.
     *
     *  time  = O(n * k)
     *  space = O(1)
     */
    public int minKBitFlips_3(int[] nums, int k) {
        int n = nums.length;
        int[] arr = nums.clone();
        int res = 0;

        for (int i = 0; i < n; i++) {
            if (arr[i] == 1) {
                continue;
            }
            if (i + k > n) {
                return -1;
            }
            for (int j = i; j < i + k; j++) {
                arr[j] ^= 1;
            }
            res += 1;
        }
        return res;
    }

}
