package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-the-number-of-good-partitions/description/

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 2963. Count the Number of Good Partitions
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed array nums consisting of positive integers.
 *
 * A partition of an array into one or more contiguous subarrays is called good if no two subarrays contain the same number.
 *
 * Return the total number of good partitions of nums.
 *
 * Since the answer may be large, return it modulo 109 + 7.
 *
 *   Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 8
 * Explanation: The 8 possible good partitions are: ([1], [2], [3], [4]), ([1], [2], [3,4]), ([1], [2,3], [4]), ([1], [2,3,4]), ([1,2], [3], [4]), ([1,2], [3,4]), ([1,2,3], [4]), and ([1,2,3,4]).
 * Example 2:
 *
 * Input: nums = [1,1,1,1]
 * Output: 1
 * Explanation: The only possible good partition is: ([1,1,1,1]).
 * Example 3:
 *
 * Input: nums = [1,2,1,3]
 * Output: 2
 * Explanation: The 2 possible good partitions are: ([1,2,1], [3]) and ([1,2,1,3]).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 109
 *
 *
 */
public class CountTheNumberOfGoodPartitions {

    // V0
//    public int numberOfGoodPartitions(int[] nums) {
//
//    }

    // V0-1
    // IDEA: PREFIX SUM (gpt)
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/63954646
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numberOfGoodPartitions_0_1(int[] nums) {
        final int MOD = 1_000_000_007;
        int n = nums.length;

        Map<Integer, Integer> first = new HashMap<>();
        Map<Integer, Integer> last = new HashMap<>();

        // 記錄 first/last 出現位置
        for (int i = 0; i < n; i++) {
            last.put(nums[i], i);
            if (!first.containsKey(nums[i])) {
                first.put(nums[i], i);
            }
        }

        // 差分陣列
        int[] diff = new int[n + 1];

        // 建立覆蓋區間
        for (int v : first.keySet()) {
            int L = first.get(v);
            int R = last.get(v);

            diff[L]++; // 區間開始
            diff[R]--; // 區間結束（不包含 R 位置）
        }

        // prefix sum 計算覆蓋數
        int cover = 0;
        int segments = 0;

        for (int i = 0; i < n; i++) {
            cover += diff[i];
            if (cover == 0) {
                segments++;
            }
        }

        // 共有 segments 個段落 ⇒ 回傳 2^(segments - 1)
        long ans = 1;
        for (int i = 1; i < segments; i++) {
            ans = (ans * 2) % MOD;
        }

        return (int) ans;
    }


    // V0-2
    // IDEA: (gemini)
    /**
     * Counts the total number of good partitions.
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numberOfGoodPartitions_0_2(int[] nums) {
        final int MOD = 1_000_000_007;

        // --- Step 1: Build the Last Occurrence Map (Dependency Table) ---
        // lastSeen[num] := the index of the last time 'num' appeared.
        Map<Integer, Integer> lastSeen = new HashMap<>();
        for (int i = 0; i < nums.length; ++i) {
            lastSeen.put(nums[i], i);
        }

        // --- Step 2: O(N) Sweep to Count Mandatory Segments ---

        // 'ans' will store 2^(number of free cuts)
        int ans = 1;

        // maxRight: The furthest index the current partition MUST reach to cover all
        // occurrences of numbers seen so far in this segment.
        int maxRight = 0;

        for (int i = 0; i < nums.length; ++i) {

            // Core Logic:
            // If the current index 'i' has moved past the max required boundary 'maxRight',
            // it means the previous mandatory segment is complete, and a new, independent
            // segment must start at 'i'.
            if (i > maxRight) {
                // We've found a new mandatory segment boundary. This boundary is a
                // "free cut" that we can choose to use or merge with the next segment.
                // Each new free cut doubles the total number of good partitions.
                ans = (int) ((ans * 2L) % MOD);
            }

            // Update the max right boundary required by the current number.
            // This is the "prefix maximum" update for the current segment's dependency.
            maxRight = Math.max(maxRight, lastSeen.get(nums[i]));
        }

        return ans;
    }


    // V1
    // https://leetcode.com/problems/count-the-number-of-good-partitions/solutions/4384415/javacpython-two-passes-by-lee215-8cju/
    /** IDEA:
     *
     * 1. All same integers should be in the same subarray
     *
     * 2. We can concat two subarray in good partition,
     * and it's still good.
     *
     * 3. If we can split into atmost k subarray,
     * then we have k - 1 spaces we can concat two subarrays.
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numberOfGoodPartitions_1(int[] A) {
        int res = 1, n = A.length, mod = 1000000007;
        Map<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            last.put(A[i], i);
        }
        for (int i = 0, j = 0; i < n; ++i) {
            res = i > j ? (res * 2) % mod : res;
            j = Math.max(j, last.get(A[i]));
        }
        return res;
    }

    // V2
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public long power(long b, long p, long m) {
        if (p == 0)
            return 1;
        long t = power(b, p / 2, m);
        return (p % 2 == 1) ? ((((t * t) % m) * b) % m) : (t * t) % m;
    }
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numberOfGoodPartitions_2(int[] nums) {
        long count = 0, m = 1000_000_007;
        HashMap<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < nums.length; ++i)
            last.put(nums[i], i);
        for (int i = 0, next = -1; i < nums.length; ++i) {
            if (i > next)
                ++count;
            next = Math.max(next, last.get(nums[i]));
        }
        return (int) power(2, count - 1, m);
    }


    // V3


}
